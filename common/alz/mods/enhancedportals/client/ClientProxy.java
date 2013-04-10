package alz.mods.enhancedportals.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alz.mods.enhancedportals.EnhancedPortals;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketAddPortalData;
import alz.mods.enhancedportals.networking.PacketAllPortalData;
import alz.mods.enhancedportals.networking.PacketDataRequest;
import alz.mods.enhancedportals.networking.PacketGuiRequest;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import alz.mods.enhancedportals.tileentity.TileEntityNetherPortal;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxy extends CommonProxy
{
    public static void onAllPacketData(PacketAllPortalData packetPortal)
    {
        World world = FMLClientHandler.instance().getClient().theWorld;

        if (world.provider.dimensionId != packetPortal.Dimension)
        {
            return;
        }

        if (world.getBlockId(packetPortal.xCoord, packetPortal.yCoord, packetPortal.zCoord) == Reference.BlockIDs.DialDevice)
        {
            TileEntityDialDevice dialDevice = (TileEntityDialDevice) world.getBlockTileEntity(packetPortal.xCoord, packetPortal.yCoord, packetPortal.zCoord);

            dialDevice.PortalDataList = null;
            dialDevice.PortalDataList = packetPortal.portalDataList;
        }
    }

    public static void OnTileUpdate(PacketTileUpdate packet, byte type)
    {
        if (type == 0)
        {
            return;
        }

        World world = FMLClientHandler.instance().getClient().theWorld;

        if (world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
        {
            TileEntity tileEntity = world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);

            if (tileEntity instanceof TileEntityPortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) tileEntity;

                modifier.parseUpdatePacket(packet);
            }
            else if (tileEntity instanceof TileEntityNetherPortal)
            {
                TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) tileEntity;

                netherPortal.parseUpdatePacket(packet);
            }
        }
    }

    public static void OpenGuiFromLocal(EntityPlayer player, TileEntity tileEntity, int guiID)
    {
        PacketDispatcher.sendPacketToServer(new PacketGuiRequest(guiID, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord).getClientPacket());
        player.openGui(EnhancedPortals.instance, guiID, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    public static void RequestTileData(TileEntity tileEntity)
    {
        PacketDispatcher.sendPacketToServer(new PacketDataRequest(tileEntity).getClientPacket());
    }

    public static void SendBlockUpdate(ITileEntityNetworking tileEntity)
    {
        PacketDispatcher.sendPacketToServer(tileEntity.getUpdatePacket().getClientPacket());
    }

    public static void SendNewPortalData(TileEntityDialDevice dialDevice, String text)
    {
        PacketAddPortalData packet = new PacketAddPortalData();
        packet.portalData = new PortalData();
        packet.portalData.DisplayName = text;
        packet.Dimension = dialDevice.worldObj.provider.dimensionId;
        packet.xCoord = dialDevice.xCoord;
        packet.yCoord = dialDevice.yCoord;
        packet.zCoord = dialDevice.zCoord;

        PacketDispatcher.sendPacketToServer(packet.getClientPacket());
    }
    
    @Override
    public void SetupConfiguration()
    {
        Settings.AllowTeleporting = Settings.GetFromConfig("AllowTeleporting", Settings.AllowTeleporting_Default);
        Settings.CanDyePortals = Settings.GetFromConfig("CanDyePortals", Settings.CanDyePortals_Default);
        Settings.DoesDyingCost = Settings.GetFromConfig("DoesDyingCost", Settings.DoesDyingCost_Default);
        Settings.CanDyeByThrowing = Settings.GetFromConfig("CanDyeByThrowing", Settings.CanDyeByThrowing_Default);
        Settings.AllowModifiers = Settings.GetFromConfig("AllowModifiers", Settings.AllowModifiers_Default);
        Settings.AllowObsidianStairs = Settings.GetFromConfig("AllowObsidianStairs", Settings.AllowObsidianStairs_Default);
        Settings.AllowDialDevice = Settings.GetFromConfig("AllowDialDevice", Settings.AllowDialDevice_Default);
        Settings.RenderPortalEffects = Settings.GetFromConfig("RenderPortalEffects", Settings.RenderPortalEffects_Default);
        Settings.MaximumPortalSize = Settings.GetFromConfig("MaximumPortalSize", Settings.MaximumPortalSize_Default);

        Settings.PigmenSpawnChance = Settings.GetFromConfig("PigmenSpawnChance", 100, 0, 100);
        Settings.SoundLevel = Settings.GetFromConfig("SoundLevel", 100, 0, 100);
        Settings.ParticleLevel = Settings.GetFromConfig("ParticleLevel", 100, 0, 100);

        Reference.BlockIDs.ObsidianStairs = Settings.GetFromConfig("ObsidianStairsID", Reference.BlockIDs.ObsidianStairs_Default, true);
        Reference.BlockIDs.PortalModifier = Settings.GetFromConfig("PortalModifierID", Reference.BlockIDs.PortalModifier_Default, true);
        Reference.BlockIDs.DialDevice = Settings.GetFromConfig("DialDeviceID", Reference.BlockIDs.DialDevice_Default, true);

        Reference.ItemIDs.PortalModifierUpgrade = Settings.GetFromConfig("PortalModifierUpgradeID", Reference.ItemIDs.PortalModifierUpgrade_Default, false);
        Reference.ItemIDs.MiscItems = Settings.GetFromConfig("MiscItemsID", Reference.ItemIDs.MiscItems_Default, false);
        Reference.ItemIDs.ItemScroll = Settings.GetFromConfig("ItemScrollID", Reference.ItemIDs.ItemScroll_Default, false);

        Settings.AddToBorderBlocks(Settings.GetFromConfig("CustomBorderBlocks", ""));
        Settings.AddToDestroyBlocks(Settings.GetFromConfig("CustomDestroyBlocks", "8,9,10,11,6,18,30,31,32,37,38,39,40,78,104,105,106,111,115"));

        Settings.ConfigFile.save();
    }
}
