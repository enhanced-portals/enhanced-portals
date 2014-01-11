package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice.GlyphElement;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class TileRedstoneInterface extends TilePortalPart
{
    public boolean isOutput;
    public byte state, previousRedstoneState;
    byte timeUntilOff;
    static int TPS = 20;
    static byte MAX_INPUT_STATE = 8, MAX_OUTPUT_STATE = 8;

    public TileRedstoneInterface()
    {
        isOutput = false;
        state = timeUntilOff = previousRedstoneState = 0;
    }

    public void entityTeleport(Entity entity)
    {
        if (isOutput)
        {
            if (state == 4 || state == 5 && entity instanceof EntityPlayer || state == 6 && entity instanceof EntityAnimal || state == 7 && entity instanceof EntityMob)
            {
                timeUntilOff = (byte) TPS;
            }

            notifyNeighbors();
        }
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);
        stream.writeBoolean(isOutput);
        stream.writeByte(state);
    }

    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);

        if (payload.data.hasKey("id"))
        {
            if (payload.data.getInteger("id") == 0)
            {
                isOutput = !isOutput;
                setState((byte) 0);

                CommonProxy.sendUpdatePacketToPlayer(this, player);
            }
            else if (payload.data.getInteger("id") == 1)
            {
                if (isOutput)
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

    @Override
    public int isProvidingStrongPower(int side)
    {
        if (timeUntilOff != 0)
        {
            return 15;
        }

        return super.isProvidingStrongPower(side);
    }

    @Override
    public int isProvidingWeakPower(int side)
    {
        if (timeUntilOff != 0)
        {
            return 15;
        }

        return super.isProvidingWeakPower(side);
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

    @Override
    public void onNeighborBlockChange(int id)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        if (!isOutput) // Only do this if set to input
        {
            TilePortalController controller = getPortalController();

            if (controller == null)
            {
                return;
            }

            boolean hasDialler = controller.blockManager.getHasDialDevice();
            byte redstoneInputState = getHighestPowerState();

            if (state == 1) // Remove portal on signal
            {
                if (redstoneInputState > 0 && controller.isPortalActive)
                {
                    controller.removePortal();
                }
                else if (redstoneInputState == 0 && !controller.isPortalActive)
                {
                    controller.createPortal();
                }
            }
            else if (state == 3 && redstoneInputState > 0 && previousRedstoneState == 0 && controller.isPortalActive) // Remove portal on pulse
            {
                controller.removePortal();
            }
            else if (!hasDialler) // These require no dialler
            {
                if (state == 0) // Create portal on signal
                {
                    if (redstoneInputState > 0 && !controller.isPortalActive)
                    {
                        controller.createPortal();
                    }
                    else if (redstoneInputState == 0 && controller.isPortalActive)
                    {
                        controller.removePortal();
                    }
                }
                else if (state == 2 && redstoneInputState > 0 && previousRedstoneState == 0 && !controller.isPortalActive) // Create portal on pulse
                {
                    controller.createPortal();
                }
            }
            else
            // These require a dialler
            {
                TileDiallingDevice dialler = controller.blockManager.getDialDevice(worldObj);

                if (dialler == null)
                {
                    return;
                }

                int glyphCount = dialler.glyphList.size();

                if (glyphCount == 0)
                {
                    return;
                }

                if (state == 4 && redstoneInputState > 0 && !controller.isPortalActive) // Dial specific identifier
                {
                    if (redstoneInputState - 1 < glyphCount)
                    {
                        GlyphElement e = dialler.glyphList.get(redstoneInputState - 1);
                        controller.dialRequest(e.identifier, e.texture);
                    }
                }
                else if (state == 5) // Dial specific identifier II
                {
                    if (redstoneInputState > 0 && !controller.isPortalActive)
                    {
                        if (redstoneInputState - 1 < glyphCount)
                        {
                            GlyphElement e = dialler.glyphList.get(redstoneInputState - 1);
                            controller.dialRequest(e.identifier, e.texture);
                        }
                    }
                    else if (redstoneInputState == 0 && controller.isPortalActive)
                    {
                        controller.removePortal();
                    }
                }
                else if (state == 6 && redstoneInputState > 0 && !controller.isPortalActive) // Dial random identifier
                {
                    GlyphElement e = dialler.glyphList.get(new Random().nextInt(glyphCount));
                    controller.dialRequest(e.identifier, e.texture);
                }
                else if (state == 7) // Dial random identifier II
                {
                    if (redstoneInputState > 0 && !controller.isPortalActive)
                    {
                        GlyphElement e = dialler.glyphList.get(new Random().nextInt(glyphCount));
                        controller.dialRequest(e.identifier, e.texture);
                    }
                    else if (redstoneInputState == 0 && controller.isPortalActive)
                    {
                        controller.removePortal();
                    }
                }
            }
        }
    }

    public void portalCreated()
    {
        if (isOutput)
        {
            if (state == 0)
            {
                timeUntilOff = (byte) TPS;
            }
            else if (state == 2)
            {
                timeUntilOff = -1;
            }
            else if (state == 3)
            {
                timeUntilOff = 0;
            }

            notifyNeighbors();
        }
    }

    public void portalRemoved()
    {
        if (isOutput)
        {
            if (state == 1)
            {
                timeUntilOff = (byte) TPS;
            }
            else if (state == 2)
            {
                timeUntilOff = 0;
            }
            else if (state == 3)
            {
                timeUntilOff = -1;
            }

            notifyNeighbors();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        isOutput = tagCompound.getBoolean("output");
        state = tagCompound.getByte("state");
        previousRedstoneState = tagCompound.getByte("previousRedstoneState");
        timeUntilOff = tagCompound.getByte("timeUntilOff");
    }

    public void setState(byte newState)
    {
        state = newState;

        if (timeUntilOff != 0)
        {
            timeUntilOff = 0;
            notifyNeighbors();
        }
    }

    @Override
    public void updateEntity()
    {
        if (isOutput)
        {
            if (timeUntilOff > 1)
            {
                timeUntilOff--;
            }
            else if (timeUntilOff == 1)
            {
                timeUntilOff--;
                notifyNeighbors(); // Make sure we update our neighbors
            }
        }
    }

    @Override
    public void usePacket(java.io.DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        isOutput = stream.readBoolean();
        setState(stream.readByte());
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("output", isOutput);
        tagCompound.setByte("state", state);
        tagCompound.setByte("previousRedstoneState", previousRedstoneState);
        tagCompound.setByte("timeUntilOff", timeUntilOff);
    }
}
