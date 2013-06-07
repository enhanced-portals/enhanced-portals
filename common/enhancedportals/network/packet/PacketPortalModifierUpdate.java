package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketPortalModifierUpdate extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    byte thickness, redstoneSetting;
    String texture, modifierNetwork, dialDeviceNetwork;

    public PacketPortalModifierUpdate()
    {

    }

    public PacketPortalModifierUpdate(TileEntityPortalModifier modifier)
    {
        xCoord = modifier.xCoord;
        yCoord = modifier.yCoord;
        zCoord = modifier.zCoord;
        thickness = modifier.thickness;
        redstoneSetting = modifier.redstoneSetting;
        dimension = modifier.worldObj.provider.dimensionId;
        texture = modifier.texture;
        modifierNetwork = modifier.modifierNetwork;
        dialDeviceNetwork = modifier.dialDeviceNetwork;

    }

    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "b", "b", "S", "S", "S");

            if (objArray != null)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                thickness = (byte) objArray[4];
                redstoneSetting = (byte) objArray[5];
                texture = (String) objArray[6];
                modifierNetwork = (String) objArray[7];
                dialDeviceNetwork = (String) objArray[8];

                return this;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);
        WorldLocation loc = new WorldLocation(xCoord, yCoord, zCoord, world);

        if (loc.getBlockId() == BlockIds.PortalModifier)
        {
            if (loc.getTileEntity() instanceof TileEntityPortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) loc.getTileEntity();

                modifier.texture = texture;
                modifier.redstoneSetting = redstoneSetting;
                modifier.thickness = thickness;

                if (!world.isRemote)
                {
                    if (modifier.isRemotelyControlled())
                    {
                        EnhancedPortals.proxy.DialDeviceNetwork.removeFromAllNetworks(loc);
                        EnhancedPortals.proxy.DialDeviceNetwork.addToNetwork(dialDeviceNetwork, loc);

                        if (EnhancedPortals.proxy.DialDeviceNetwork.isInNetwork(dialDeviceNetwork, loc))
                        {
                            modifier.dialDeviceNetwork = dialDeviceNetwork;
                        }
                        else
                        {
                            modifier.dialDeviceNetwork = "";
                        }
                    }
                    else
                    {
                        EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(loc);
                        EnhancedPortals.proxy.ModifierNetwork.addToNetwork(modifierNetwork, loc);

                        modifier.modifierNetwork = modifierNetwork;
                    }
                }
                else
                {
                    modifier.modifierNetwork = modifierNetwork;
                    modifier.dialDeviceNetwork = dialDeviceNetwork;
                }

                world.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, thickness, redstoneSetting, texture, modifierNetwork, dialDeviceNetwork);
    }
}
