package enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.portal.Portal;

public class TileEntityDialDeviceBasic extends TileEntityEnhancedPortals
{
    String         oldModifierNetwork;
    public boolean active = false;
    WorldLocation  modifierLocation;

    private void findPortalFrame()
    {
        if (modifierLocation != null)
        {
            return;
        }

        ForgeDirection direction = ForgeDirection.getOrientation(getBlockMetadata());
        WorldLocation currentLocation = new WorldLocation(xCoord, yCoord, zCoord, worldObj);

        if (currentLocation.getOffset(direction.getOpposite()).getBlockId() == BlockIds.Obsidian)
        {
            modifierLocation = currentLocation.getOffset(direction.getOpposite());
        }
        else if (currentLocation.getOffset(ForgeDirection.DOWN).getBlockId() == BlockIds.Obsidian)
        {
            modifierLocation = currentLocation.getOffset(ForgeDirection.DOWN);
        }
        else
        {
            if (direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH) // Facing SOUTH / NORTH
            {
                if (currentLocation.getOffset(ForgeDirection.EAST).getBlockId() == BlockIds.Obsidian)
                {
                    modifierLocation = currentLocation.getOffset(ForgeDirection.EAST);
                }
                else if (currentLocation.getOffset(ForgeDirection.WEST).getBlockId() == BlockIds.Obsidian)
                {
                    modifierLocation = currentLocation.getOffset(ForgeDirection.WEST);
                }
            }
            else if (direction == ForgeDirection.WEST || direction == ForgeDirection.EAST) // Facing EAST / WEST (Opposite)
            {
                if (currentLocation.getOffset(ForgeDirection.NORTH).getBlockId() == BlockIds.Obsidian)
                {
                    modifierLocation = currentLocation.getOffset(ForgeDirection.NORTH);
                }
                else if (currentLocation.getOffset(ForgeDirection.SOUTH).getBlockId() == BlockIds.Obsidian)
                {
                    modifierLocation = currentLocation.getOffset(ForgeDirection.SOUTH);
                }
            }
        }
    }

    public void processDiallingRequest(String network, EntityPlayer player)
    {
        if (worldObj.isRemote || active)
        {
            return;
        }

        findPortalFrame();

        if (modifierLocation == null)
        {
            if (player != null)
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noModifier"));
            }

            return;
        }

        if (EnhancedPortals.proxy.ModifierNetwork.hasNetwork(network))
        {
            boolean createdPortal = false;

            for (int i = 0; i < 6; i++)
            {
                if (new Portal(modifierLocation.getOffset(ForgeDirection.getOrientation(i)), "", true, true, (byte) 0).createPortal())
                {
                    createdPortal = true;
                    break;
                }
            }

            if (!createdPortal)
            {
                player.sendChatToPlayer("Can't make portal.");
            }

            /*TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();

            oldModifierNetwork = modifier.network;
            EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(modifierLocation);
            EnhancedPortals.proxy.ModifierNetwork.addToNetwork(network, modifierLocation);
            modifier.network = network;

            if (modifier.isAnyActive())
            {
                modifier.removePortal();
            }

            if (modifier.createPortal())
            {
                if (player != null)
                {
                    player.sendChatToPlayer(EnumChatFormatting.GREEN + Localization.localizeString("chat.dialSuccess"));
                }

                worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDeviceBasic, 760);
                active = true;
                PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
            }
            else
            {
                modifier.network = oldModifierNetwork;

                if (player != null)
                {
                    player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noPortal"));
                }
            }*/
        }
        else if (player != null)
        {
            player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        active = tagCompound.getBoolean("Active");

        if (active)
        {
            modifierLocation = new WorldLocation(tagCompound.getInteger("ModifierX"), tagCompound.getInteger("ModifierY"), tagCompound.getInteger("ModifierZ"), tagCompound.getInteger("ModifierD"));
            oldModifierNetwork = tagCompound.getString("ModifierNetwork");
        }
    }

    public void scheduledBlockUpdate()
    {
        if (modifierLocation == null)
        {
            active = false;
            // TODO PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
            return;
        }

        if (modifierLocation.getBlockId() == BlockIds.PortalModifier)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();

            EnhancedPortals.proxy.ModifierNetwork.removeFromNetwork(modifier.network, modifierLocation);

            if (oldModifierNetwork != null && !oldModifierNetwork.equals(""))
            {
                EnhancedPortals.proxy.ModifierNetwork.addToNetwork(oldModifierNetwork, modifierLocation);
            }

            modifier.removePortal();
            modifier.network = oldModifierNetwork;

            oldModifierNetwork = "";
            modifierLocation = null;
            active = false;
            // TODO PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
        }
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            // TODO PacketDispatcher.sendPacketToServer(new PacketRequestSync(this).getPacket());
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setBoolean("Active", active);

        if (active)
        {
            tagCompound.setInteger("ModifierX", modifierLocation.xCoord);
            tagCompound.setInteger("ModifierY", modifierLocation.yCoord);
            tagCompound.setInteger("ModifierZ", modifierLocation.zCoord);
            tagCompound.setInteger("ModifierD", modifierLocation.dimension);
            tagCompound.setString("ModifierNetwork", oldModifierNetwork);
        }
    }
}
