package enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.GuiDiallingDevice;
import enhancedportals.client.gui.GuiDiallingDeviceEdit;
import enhancedportals.client.gui.GuiDiallingDeviceManual;
import enhancedportals.client.gui.GuiDiallingDeviceSave;
import enhancedportals.client.gui.GuiDimensionalBridgeStabilizer;
import enhancedportals.client.gui.GuiModuleManipulator;
import enhancedportals.client.gui.GuiNetworkInterface;
import enhancedportals.client.gui.GuiNetworkInterfaceGlyphs;
import enhancedportals.client.gui.GuiPortalController;
import enhancedportals.client.gui.GuiPortalControllerGlyphs;
import enhancedportals.client.gui.GuiRedstoneInterface;
import enhancedportals.client.gui.GuiTextureDialFrame;
import enhancedportals.client.gui.GuiTextureDialParticle;
import enhancedportals.client.gui.GuiTextureDialPortal;
import enhancedportals.client.gui.GuiTextureFrame;
import enhancedportals.client.gui.GuiTextureParticle;
import enhancedportals.client.gui.GuiTexturePortal;
import enhancedportals.inventory.ContainerDiallingDevice;
import enhancedportals.inventory.ContainerDiallingDeviceEdit;
import enhancedportals.inventory.ContainerDiallingDeviceManual;
import enhancedportals.inventory.ContainerDiallingDeviceSave;
import enhancedportals.inventory.ContainerDimensionalBridgeStabilizer;
import enhancedportals.inventory.ContainerModuleManipulator;
import enhancedportals.inventory.ContainerNetworkInterface;
import enhancedportals.inventory.ContainerNetworkInterfaceGlyphs;
import enhancedportals.inventory.ContainerPortalController;
import enhancedportals.inventory.ContainerPortalControllerGlyphs;
import enhancedportals.inventory.ContainerRedstoneInterface;
import enhancedportals.inventory.ContainerTextureDialFrame;
import enhancedportals.inventory.ContainerTextureDialParticle;
import enhancedportals.inventory.ContainerTextureDialPortal;
import enhancedportals.inventory.ContainerTextureFrame;
import enhancedportals.inventory.ContainerTextureParticle;
import enhancedportals.inventory.ContainerTexturePortal;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.tileentity.TileEP;
import enhancedportals.tileentity.TileStabilizerMain;
import enhancedportals.tileentity.portal.TileController;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import enhancedportals.tileentity.portal.TileModuleManipulator;
import enhancedportals.tileentity.portal.TileRedstoneInterface;

public class GuiHandler implements IGuiHandler
{
    public static final int PORTAL_CONTROLLER_A = 100;
    public static final int PORTAL_CONTROLLER_B = 101;
    public static final int NETWORK_INTERFACE_A = 102;
    public static final int NETWORK_INTERFACE_B = 103;
    public static final int DIALLING_DEVICE_A = 104;
    public static final int DIALLING_DEVICE_B = 105;
    public static final int DIALLING_DEVICE_C = 106;
    public static final int DIALLING_DEVICE_D = 107;
    public static final int TEXTURE_A = 108;
    public static final int TEXTURE_B = 109;
    public static final int TEXTURE_C = 110;
    public static final int TEXTURE_DIALLING_A = 111;
    public static final int TEXTURE_DIALLING_B = 112;
    public static final int TEXTURE_DIALLING_C = 113;

    public static final int REDSTONE_INTERFACE = 2;
    public static final int PROGRAMMABLE_INTERFACE = 5;
    public static final int MODULE_MANIPULATOR = 6;
    public static final int TRANSFER_FLUID = 7;
    public static final int TRANSFER_ENERGY = 8;
    public static final int TRANSFER_ITEM = 9;

    public static final int DIMENSIONAL_BRIDGE_STABILIZER = 14;
    public static final int GUIDE = 16;

    public static void openGui(EntityPlayer player, TileEntity tile, int gui)
    {
        player.openGui(EnhancedPortals.instance, gui, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity t = world.getTileEntity(x, y, z);
        
        if (!(t instanceof TileEP))
        {
            return null;
        }
        
        TileEP tile = (TileEP) t;

        if (ID == PORTAL_CONTROLLER_A)
        {
            return new GuiPortalController((TileController) tile, player);
        }
        else if (ID == PORTAL_CONTROLLER_B)
        {
            return new GuiPortalControllerGlyphs((TileController) tile, player);
        }
        else if (ID == REDSTONE_INTERFACE)
        {
            return new GuiRedstoneInterface((TileRedstoneInterface) tile, player);
        }
        else if (ID == NETWORK_INTERFACE_A)
        {
            return new GuiNetworkInterface((TileController) tile, player);
        }
        else if (ID == NETWORK_INTERFACE_B)
        {
            return new GuiNetworkInterfaceGlyphs((TileController) tile, player);
        }
        else if (ID == MODULE_MANIPULATOR)
        {
            return new GuiModuleManipulator((TileModuleManipulator) tile, player);
        }
        else if (ID == DIMENSIONAL_BRIDGE_STABILIZER)
        {
            return new GuiDimensionalBridgeStabilizer((TileStabilizerMain) tile, player);
        }
        else if (ID == DIALLING_DEVICE_A)
        {
            return new GuiDiallingDevice((TileDiallingDevice) tile, player);
        }
        else if (ID == DIALLING_DEVICE_B)
        {
            return new GuiDiallingDeviceManual((TileDiallingDevice) tile, player);
        }
        else if (ID == DIALLING_DEVICE_C)
        {
            return new GuiDiallingDeviceSave((TileDiallingDevice) tile, player);
        }
        else if (ID == DIALLING_DEVICE_D)
        {
            return new GuiDiallingDeviceEdit((TileDiallingDevice) tile, player);
        }
        else if (ID == TEXTURE_A)
        {
            return new GuiTextureFrame((TileController) tile, player);
        }
        else if (ID == TEXTURE_B)
        {
            return new GuiTexturePortal((TileController) tile, player);
        }
        else if (ID == TEXTURE_C)
        {
            return new GuiTextureParticle((TileController) tile, player);
        }
        else if (ID == TEXTURE_DIALLING_A)
        {
            return new GuiTextureDialFrame((TileDiallingDevice) tile, player, true);
        }
        else if (ID == TEXTURE_DIALLING_B)
        {
            return new GuiTextureDialPortal((TileDiallingDevice) tile, player, true);
        }
        else if (ID == TEXTURE_DIALLING_C)
        {
            return new GuiTextureDialParticle((TileDiallingDevice) tile, player, true);
        }
        // else if (ID == BIOMETRIC_IDENTIFIER) // TODO
        // {
        // return new GuiBiometricIdentifier((TileBiometricIdentifier) tile, player);
        // }
        else if (ID == GUIDE)
        {
            // return new GuiGuide();
        }
        else if (ID == TRANSFER_FLUID)
        {
            // return new GuiTransferFluid((TileTransferFluid) tile);
        }
        else if (ID == TRANSFER_ENERGY)
        {
            // return new GuiTransferEnergy((TileTransferEnergy) tile);
        }
        else if (ID == TRANSFER_ITEM)
        {
            // return new GuiTransferItem((TileTransferItem) tile);
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity t = world.getTileEntity(x, y, z);
        
        if (!(t instanceof TileEP))
        {
            return null;
        }
        
        TileEP tile = (TileEP) t;


        if (ID == PORTAL_CONTROLLER_A)
        {
            EnhancedPortals.packetPipeline.sendTo(new PacketGui(tile), (EntityPlayerMP) player);
            return new ContainerPortalController((TileController) tile, player.inventory);
        }
        else if (ID == PORTAL_CONTROLLER_B)
        {
            return new ContainerPortalControllerGlyphs((TileController) tile, player.inventory);
        }
        else if (ID == REDSTONE_INTERFACE)
        {
            EnhancedPortals.packetPipeline.sendTo(new PacketGui(tile), (EntityPlayerMP) player);
            return new ContainerRedstoneInterface((TileRedstoneInterface) tile, player.inventory);
        }
        else if (ID == NETWORK_INTERFACE_A)
        {
            EnhancedPortals.packetPipeline.sendTo(new PacketGui(tile), (EntityPlayerMP) player);
            return new ContainerNetworkInterface((TileController) tile, player.inventory);
        }
        else if (ID == NETWORK_INTERFACE_B)
        {
            return new ContainerNetworkInterfaceGlyphs((TileController) tile, player.inventory);
        }
        else if (ID == MODULE_MANIPULATOR)
        {
            return new ContainerModuleManipulator((TileModuleManipulator) tile, player.inventory);
        }
        else if (ID == DIMENSIONAL_BRIDGE_STABILIZER)
        {
            return new ContainerDimensionalBridgeStabilizer((TileStabilizerMain) tile, player.inventory);
        }
        else if (ID == DIALLING_DEVICE_A)
        {
            EnhancedPortals.packetPipeline.sendTo(new PacketGui(tile), (EntityPlayerMP) player);
            return new ContainerDiallingDevice((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == DIALLING_DEVICE_B)
        {
            return new ContainerDiallingDeviceManual((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == DIALLING_DEVICE_C)
        {
            return new ContainerDiallingDeviceSave((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == DIALLING_DEVICE_D)
        {
            return new ContainerDiallingDeviceEdit((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == TEXTURE_A)
        {
            return new ContainerTextureFrame((TileController) tile, player.inventory);
        }
        else if (ID == TEXTURE_B)
        {
            return new ContainerTexturePortal((TileController) tile, player.inventory);
        }
        else if (ID == TEXTURE_C)
        {
            return new ContainerTextureParticle((TileController) tile, player.inventory);
        }
        else if (ID == TEXTURE_DIALLING_A)
        {
            return new ContainerTextureDialFrame((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == TEXTURE_DIALLING_B)
        {
            return new ContainerTextureDialPortal((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == TEXTURE_DIALLING_C)
        {
            return new ContainerTextureDialParticle((TileDiallingDevice) tile, player.inventory);
        }
        // else if (ID == BIOMETRIC_IDENTIFIER) // TODO
        // {
        // return new ContainerBiometricIdentifier((TileBiometricIdentifier) tile, player);
        // }
        else if (ID == GUIDE)
        {
            return null;
        }
        else if (ID == TRANSFER_FLUID)
        {
            // return new ContainerTransferFluid((TileTransferFluid) tile);
        }
        else if (ID == TRANSFER_ENERGY)
        {
            // return new ContainerTransferEnergy((TileTransferEnergy) tile);
        }
        else if (ID == TRANSFER_ITEM)
        {
            // return new ContainerTransferItem((TileTransferItem) tile);
        }

        return null;
    }
}
