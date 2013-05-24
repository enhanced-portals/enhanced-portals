package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import alz.core.lib.Misc;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketPortalModifierUpdate extends PacketEnhancedPortals
{
    int    xCoord, yCoord, zCoord, dimension;
    byte   thickness, redstoneSetting;
    byte[] upgrades;
    String texture, network;

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
        upgrades = modifier.upgradeHandler.getInstalledUpgrades();
        texture = modifier.texture;
        network = modifier.network;
    }

    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = Misc.getObjects(data, "I", "I", "I", "I", "b", "b", "b[]", "S", "S");

            if (objArray != null && objArray.length == 9)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                thickness = (byte) objArray[4];
                redstoneSetting = (byte) objArray[5];
                upgrades = (byte[]) objArray[6];
                texture = (String) objArray[7];
                network = (String) objArray[8];
            
                return this;
            }
            else
            {
                return null;
            }
            
            /*xCoord = Misc.byteArrayToInteger(Arrays.copyOfRange(data, 0, 4));
            yCoord = Misc.byteArrayToInteger(Arrays.copyOfRange(data, 4, 8));
            zCoord = Misc.byteArrayToInteger(Arrays.copyOfRange(data, 8, 12));
            dimension = data[12];
            thickness = data[13];
            redstoneSetting = data[14];
            upgrades = Arrays.copyOfRange(data, 16, 16 + data[15]);

            int pos = 17 + upgrades.length;
            texture = new String(Arrays.copyOfRange(data, pos, pos + data[pos - 1]), "UTF-8");

            pos += data[pos - 1] + 1;
            network = new String(Arrays.copyOfRange(data, pos, pos + data[pos - 1]), "UTF-8");

            return this;*/
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.PortalModifier)
        {
            if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityPortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(xCoord, yCoord, zCoord);

                modifier.network = this.network;
                modifier.texture = texture;
                modifier.redstoneSetting = redstoneSetting;
                modifier.thickness = thickness;
                modifier.upgradeHandler.addUpgradesFromByteArray(upgrades, modifier);

                if (world.isRemote)
                {
                    world.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
                }
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        /*byte[] packetData;
        byte[] textureData = texture.getBytes();
        byte[] networkData = network.getBytes();

        packetData = Misc.integerToByteArray(xCoord);
        packetData = Misc.concatByteArray(packetData, Misc.integerToByteArray(yCoord));
        packetData = Misc.concatByteArray(packetData, Misc.integerToByteArray(zCoord));
        packetData = Misc.concatByteArray(packetData, new byte[] { (byte) dimension, thickness, redstoneSetting, (byte) upgrades.length });
        packetData = Misc.concatByteArray(packetData, upgrades);
        packetData = Misc.concatByteArray(packetData, new byte[] { (byte) textureData.length });
        packetData = Misc.concatByteArray(packetData, textureData);
        packetData = Misc.concatByteArray(packetData, new byte[] { (byte) networkData.length });
        packetData = Misc.concatByteArray(packetData, networkData);

        return packetData;*/
        
        return Misc.getByteArray(xCoord, yCoord, zCoord, dimension, thickness, redstoneSetting, upgrades, texture, network);
    }
}
