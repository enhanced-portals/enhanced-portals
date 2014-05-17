package enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
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
import enhancedportals.client.gui.GuiProgrammableInterface;
import enhancedportals.client.gui.GuiProgrammableInterfaceErrorLog;
import enhancedportals.client.gui.GuiRedstoneInterface;
import enhancedportals.client.gui.GuiTextureDialFrame;
import enhancedportals.client.gui.GuiTextureDialParticle;
import enhancedportals.client.gui.GuiTextureDialPortal;
import enhancedportals.client.gui.GuiTextureFrame;
import enhancedportals.client.gui.GuiTextureParticle;
import enhancedportals.client.gui.GuiTexturePortal;
import enhancedportals.client.gui.GuiTransferEnergy;
import enhancedportals.client.gui.GuiTransferFluid;
import enhancedportals.client.gui.GuiTransferItem;
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
import enhancedportals.inventory.ContainerProgrammableInterface;
import enhancedportals.inventory.ContainerProgrammableInterfaceErrorLog;
import enhancedportals.inventory.ContainerRedstoneInterface;
import enhancedportals.inventory.ContainerTextureDialFrame;
import enhancedportals.inventory.ContainerTextureDialParticle;
import enhancedportals.inventory.ContainerTextureDialPortal;
import enhancedportals.inventory.ContainerTextureFrame;
import enhancedportals.inventory.ContainerTextureParticle;
import enhancedportals.inventory.ContainerTexturePortal;
import enhancedportals.inventory.ContainerTransferEnergy;
import enhancedportals.inventory.ContainerTransferFluid;
import enhancedportals.inventory.ContainerTransferItem;
import enhancedportals.tileentity.TileController;
import enhancedportals.tileentity.TileDiallingDevice;
import enhancedportals.tileentity.TileEP;
import enhancedportals.tileentity.TileModuleManipulator;
import enhancedportals.tileentity.TileProgrammableInterface;
import enhancedportals.tileentity.TileRedstoneInterface;
import enhancedportals.tileentity.TileStabilizerMain;
import enhancedportals.tileentity.TileTransferEnergy;
import enhancedportals.tileentity.TileTransferFluid;
import enhancedportals.tileentity.TileTransferItem;

public class GuiHandler implements IGuiHandler
{
    public static final int PORTAL_CONTROLLER_A = 0;
    public static final int PORTAL_CONTROLLER_B = 1;
    public static final int NETWORK_INTERFACE_A = 2;
    public static final int NETWORK_INTERFACE_B = 3;
    public static final int DIALLING_DEVICE_A = 4;
    public static final int DIALLING_DEVICE_B = 5;
    public static final int DIALLING_DEVICE_C = 6;
    public static final int DIALLING_DEVICE_D = 7;
    public static final int TEXTURE_A = 8;
    public static final int TEXTURE_B = 9;
    public static final int TEXTURE_C = 10;
    public static final int TEXTURE_DIALLING_EDIT_A = 11;
    public static final int TEXTURE_DIALLING_EDIT_B = 12;
    public static final int TEXTURE_DIALLING_EDIT_C = 13;
    public static final int TEXTURE_DIALLING_SAVE_A = 14;
    public static final int TEXTURE_DIALLING_SAVE_B = 15;
    public static final int TEXTURE_DIALLING_SAVE_C = 16;
    public static final int REDSTONE_INTERFACE = 17;
    public static final int PROGRAMMABLE_INTERFACE = 18;
    public static final int PROGRAMMABLE_INTERFACE_ERRORS = 19;
    public static final int MODULE_MANIPULATOR = 20;
    public static final int TRANSFER_FLUID = 21;
    public static final int TRANSFER_ENERGY = 22;
    public static final int TRANSFER_ITEM = 23;
    public static final int DIMENSIONAL_BRIDGE_STABILIZER = 24;
    public static final int GUIDE = 25;

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
        else if (ID == TEXTURE_DIALLING_SAVE_A)
        {
            return new GuiTextureDialFrame((TileDiallingDevice) tile, player, false);
        }
        else if (ID == TEXTURE_DIALLING_SAVE_B)
        {
            return new GuiTextureDialPortal((TileDiallingDevice) tile, player, false);
        }
        else if (ID == TEXTURE_DIALLING_SAVE_C)
        {
            return new GuiTextureDialParticle((TileDiallingDevice) tile, player, false);
        }
        else if (ID == TEXTURE_DIALLING_EDIT_A)
        {
            return new GuiTextureDialFrame((TileDiallingDevice) tile, player, true);
        }
        else if (ID == TEXTURE_DIALLING_EDIT_B)
        {
            return new GuiTextureDialPortal((TileDiallingDevice) tile, player, true);
        }
        else if (ID == TEXTURE_DIALLING_EDIT_C)
        {
            return new GuiTextureDialParticle((TileDiallingDevice) tile, player, true);
        }
        else if (ID == PROGRAMMABLE_INTERFACE)
        {
            return new GuiProgrammableInterface((TileProgrammableInterface) tile, player);
        }
        else if (ID == PROGRAMMABLE_INTERFACE_ERRORS)
        {
            return new GuiProgrammableInterfaceErrorLog((TileProgrammableInterface) tile, player);
        }
        //else if (ID == GUIDE) // TODO
        //{
        //     return new GuiGuide();
        //}
        else if (ID == TRANSFER_FLUID)
        {
             return new GuiTransferFluid((TileTransferFluid) tile, player);
        }
        else if (ID == TRANSFER_ENERGY)
        {
             return new GuiTransferEnergy((TileTransferEnergy) tile, player);
        }
        else if (ID == TRANSFER_ITEM)
        {
             return new GuiTransferItem((TileTransferItem) tile, player);
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
            return new ContainerPortalController((TileController) tile, player.inventory);
        }
        else if (ID == PORTAL_CONTROLLER_B)
        {
            return new ContainerPortalControllerGlyphs((TileController) tile, player.inventory);
        }
        else if (ID == REDSTONE_INTERFACE)
        {
            return new ContainerRedstoneInterface((TileRedstoneInterface) tile, player.inventory);
        }
        else if (ID == NETWORK_INTERFACE_A)
        {
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
        else if (ID == TEXTURE_DIALLING_EDIT_A || ID == TEXTURE_DIALLING_SAVE_A)
        {
            return new ContainerTextureDialFrame((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == TEXTURE_DIALLING_EDIT_B || ID == TEXTURE_DIALLING_SAVE_B)
        {
            return new ContainerTextureDialPortal((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == TEXTURE_DIALLING_EDIT_C || ID == TEXTURE_DIALLING_SAVE_C)
        {
            return new ContainerTextureDialParticle((TileDiallingDevice) tile, player.inventory);
        }
        else if (ID == PROGRAMMABLE_INTERFACE)
        {
            return new ContainerProgrammableInterface((TileProgrammableInterface) tile, player.inventory);
        }
        else if (ID == PROGRAMMABLE_INTERFACE_ERRORS)
        {
            return new ContainerProgrammableInterfaceErrorLog((TileProgrammableInterface) tile, player.inventory);
        }
        //else if (ID == GUIDE) // TODO
        //{
        //    return null;
        //}
        else if (ID == TRANSFER_FLUID)
        {
            return new ContainerTransferFluid((TileTransferFluid) tile, player.inventory);
        }
        else if (ID == TRANSFER_ENERGY)
        {
            return new ContainerTransferEnergy((TileTransferEnergy) tile, player.inventory);
        }
        else if (ID == TRANSFER_ITEM)
        {
            return new ContainerTransferItem((TileTransferItem) tile, player.inventory);
        }

        return null;
    }
}
