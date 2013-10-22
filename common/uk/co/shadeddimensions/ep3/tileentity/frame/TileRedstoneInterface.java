package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class TileRedstoneInterface extends TilePortalPart
{
    public boolean output;

    byte previousRedstoneInputState, state, timer;
    boolean emitting, pulse;

    static byte MAX_INPUT_STATE = 4, MAX_OUTPUT_STATE = 5, MAX_TIMER = 5;

    public TileRedstoneInterface()
    {
        previousRedstoneInputState = 0;
        state = 0;
        emitting = pulse = false;
    }
    
    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);
        
        if (payload.data.hasKey("id"))
        {
            if (payload.data.getInteger("id") == 0)
            {
                output = !output;
                setState((byte) 0);
    
                CommonProxy.sendUpdatePacketToPlayer(this, player);
            }
            else if (payload.data.getInteger("id") == 1)
            {
                if (output)
                {
                    if (state + 1 < MAX_OUTPUT_STATE)
                    {
                        setState((byte) (state + 1));
                    }
                    else
                    {
                        setState((byte) 0);
                    }
                }
                else
                {
                    if (state + 1 < MAX_INPUT_STATE)
                    {
                        setState((byte) (state + 1));
                    }
                    else
                    {
                        setState((byte) 0);
                    }
                }
    
                CommonProxy.sendUpdatePacketToPlayer(this, player);
            }
        }
    }
    
    private byte getHighestPowerState()
    {
        byte current = 0;

        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            byte c = (byte) worldObj.getIndirectPowerLevelTo(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ, i);

            if (c > current)
            {
                current = c;
            }
        }

        return current;
    }

    public byte getState()
    {
        return state;
    }
    
    @Override
    public int isProvidingStrongPower(int side)
    {
        if (emitting || pulse)
        {
            return 15;
        }

        return super.isProvidingStrongPower(side);
    }

    @Override
    public int isProvidingWeakPower(int side)
    {
        if (emitting || pulse)
        {
            return 15;
        }

        return super.isProvidingWeakPower(side);
    }
    
    @Override
    public void onNeighborBlockChange(int id)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        if (!output) // Only do this if set to input
        {
            byte redstoneInputState = getHighestPowerState();

            if (state == 0 || state == 1)
            {
                if (redstoneInputState > 0 && previousRedstoneInputState == 0)
                {
                    TilePortalController c = getPortalController();

                    if (c != null)
                    {
                        if (state == 0)
                        {
                            c.createPortal();
                        }
                        else if (state == 1)
                        {
                            c.removePortal();
                        }
                    }
                }
                else if (redstoneInputState == 0 && previousRedstoneInputState > 0)
                {
                    TilePortalController c = getPortalController();

                    if (c != null)
                    {
                        if (state == 0)
                        {
                            c.removePortal();
                        }
                        else if (state == 1)
                        {
                            c.createPortal();
                        }
                    }

                }
            }
            else if (state == 2 || state == 3)
            {
                if (previousRedstoneInputState > 0 && redstoneInputState == 0)
                {
                    TilePortalController c = getPortalController();

                    if (state == 2)
                    {
                        c.createPortal();
                    }
                    else if (state == 3)
                    {
                        c.removePortal();
                    }
                }
            }

            previousRedstoneInputState = redstoneInputState;
        }
    }

    private void notifyNeighbors()
    {
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, CommonProxy.blockFrame.blockID);

        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            worldObj.notifyBlocksOfNeighborChange(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ, CommonProxy.blockFrame.blockID);
        }
    }

    public void portalCreated()
    {
        if (output)
        {
            if (state == 0)
            {
                pulseRedstone();
            }
            else if (state == 2)
            {
                emitting = true;
            }
            else if (state == 3)
            {
                emitting = false;
            }

            notifyNeighbors();
        }
    }

    public void portalRemoved()
    {
        if (output)
        {
            if (state == 1)
            {
                pulseRedstone();
            }
            else if (state == 2)
            {
                emitting = false;
            }
            else if (state == 3)
            {
                emitting = true;
            }

            notifyNeighbors();
        }
    }

    private void pulseRedstone()
    {
        if (pulse)
        {
            return;
        }

        pulse = true;
        notifyNeighbors();
        worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, CommonProxy.blockFrame.blockID, 20);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        previousRedstoneInputState = tagCompound.getByte("redstoneInputState");
        output = tagCompound.getBoolean("output");
        state = tagCompound.getByte("state");
        emitting = tagCompound.getBoolean("emitting");
        pulse = tagCompound.getBoolean("pulse");
    }

    @Override
    public void updateTick(Random random)
    {
        if (pulse)
        {
            pulse = false;
            notifyNeighbors();
        }
    }

    public void setState(byte newState)
    {
        state = newState;

        if (emitting || pulse)
        {
            emitting = pulse = false;
            notifyNeighbors();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setByte("redstoneInputState", previousRedstoneInputState);
        tagCompound.setBoolean("output", output);
        tagCompound.setByte("state", state);
        tagCompound.setBoolean("emitting", emitting);
        tagCompound.setBoolean("pulse", pulse);
    }
            
    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);
        
        stream.writeBoolean(output);
        stream.writeByte(state);
    }
    
    public void usePacket(java.io.DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        
        output = stream.readBoolean();
        setState(stream.readByte());
    }
}
