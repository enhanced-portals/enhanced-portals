package com.alz.enhancedportals.core.tileentity;

import net.minecraft.tileentity.TileEntity;

import com.alz.enhancedportals.core.networking.packets.PacketRequestSync;
import com.alz.enhancedportals.core.networking.packets.PacketTileEntityUpdate;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityEnhancedPortals extends TileEntity
{
    /***
     * Sets the required metadata for this to sync on validation. N -1 for sync
     * regardless of metadata 0-15 for specific metadata -2 for don't sync on
     * validation
     */
    public byte requiredMetadataForUpdate = -1;

    public PacketTileEntityUpdate getUpdateData()
    {
        return null; // Supposed to be overriden by other classes.
    }

    public void parseTileData(PacketTileEntityUpdate packetUpdate)
    {
        // Supposed to be overridden by other classes.
    }

    @SideOnly(Side.CLIENT)
    public void requestTileData()
    {
        PacketDispatcher.sendPacketToServer(new PacketRequestSync(this).getPacket());
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote && requiredMetadataForUpdate != -2 && (requiredMetadataForUpdate == -1 || requiredMetadataForUpdate >= 0 && getBlockMetadata() == requiredMetadataForUpdate))
        {
            requestTileData();
        }
    }
}
