package enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.GuiDialingAdd;
import enhancedportals.client.gui.GuiDialingDevice;
import enhancedportals.client.gui.GuiDialingEdit;
import enhancedportals.client.gui.GuiDialingEditFrame;
import enhancedportals.client.gui.GuiDialingEditIdentifier;
import enhancedportals.client.gui.GuiDialingEditParticle;
import enhancedportals.client.gui.GuiDialingEditPortal;
import enhancedportals.client.gui.GuiDialingManual;
import enhancedportals.client.gui.GuiDimensionalBridgeStabilizer;
import enhancedportals.client.gui.GuiManual;
import enhancedportals.client.gui.GuiModuleManipulator;
import enhancedportals.client.gui.GuiNetworkInterface;
import enhancedportals.client.gui.GuiNetworkInterfaceGlyphs;
import enhancedportals.client.gui.GuiPortalController;
import enhancedportals.client.gui.GuiPortalControllerGlyphs;
import enhancedportals.client.gui.GuiRedstoneInterface;
import enhancedportals.client.gui.GuiTextureFrame;
import enhancedportals.client.gui.GuiTextureParticle;
import enhancedportals.client.gui.GuiTexturePortal;
import enhancedportals.client.gui.GuiTransferEnergy;
import enhancedportals.client.gui.GuiTransferFluid;
import enhancedportals.client.gui.GuiTransferItem;
import enhancedportals.inventory.ContainerDialingAdd;
import enhancedportals.inventory.ContainerDialingDevice;
import enhancedportals.inventory.ContainerDialingEdit;
import enhancedportals.inventory.ContainerDialingEditIdentifier;
import enhancedportals.inventory.ContainerDialingEditParticle;
import enhancedportals.inventory.ContainerDialingEditPortal;
import enhancedportals.inventory.ContainerDialingEditTexture;
import enhancedportals.inventory.ContainerDialingManual;
import enhancedportals.inventory.ContainerDimensionalBridgeStabilizer;
import enhancedportals.inventory.ContainerManual;
import enhancedportals.inventory.ContainerModuleManipulator;
import enhancedportals.inventory.ContainerNetworkInterface;
import enhancedportals.inventory.ContainerNetworkInterfaceGlyphs;
import enhancedportals.inventory.ContainerPortalController;
import enhancedportals.inventory.ContainerPortalControllerGlyphs;
import enhancedportals.inventory.ContainerRedstoneInterface;
import enhancedportals.inventory.ContainerTextureFrame;
import enhancedportals.inventory.ContainerTextureParticle;
import enhancedportals.inventory.ContainerTexturePortal;
import enhancedportals.inventory.ContainerTransferEnergy;
import enhancedportals.inventory.ContainerTransferFluid;
import enhancedportals.inventory.ContainerTransferItem;
import enhancedportals.tile.TileController;
import enhancedportals.tile.TileDialingDevice;
import enhancedportals.tile.TileEP;
import enhancedportals.tile.TilePortalManipulator;
import enhancedportals.tile.TileRedstoneInterface;
import enhancedportals.tile.TileStabilizerMain;
import enhancedportals.tile.TileTransferEnergy;
import enhancedportals.tile.TileTransferFluid;
import enhancedportals.tile.TileTransferItem;

public class GuiHandler implements IGuiHandler
{
    public static final int PORTAL_CONTROLLER_A = 0;
    public static final int PORTAL_CONTROLLER_B = 1;
    public static final int NETWORK_INTERFACE_A = 2;
    public static final int NETWORK_INTERFACE_B = 3;
    public static final int DIALING_DEVICE_A = 4;
    public static final int DIALING_DEVICE_B = 5;
    public static final int DIALING_DEVICE_C = 6;
    public static final int DIALING_DEVICE_D = 7;
    public static final int DIALING_DEVICE_E = 26;
    public static final int TEXTURE_A = 8;
    public static final int TEXTURE_B = 9;
    public static final int TEXTURE_C = 10;
    public static final int TEXTURE_DIALING_EDIT_A = 11;
    public static final int TEXTURE_DIALING_EDIT_B = 12;
    public static final int TEXTURE_DIALING_EDIT_C = 13;
    public static final int TEXTURE_DIALING_SAVE_A = 14;
    public static final int TEXTURE_DIALING_SAVE_B = 15;
    public static final int TEXTURE_DIALING_SAVE_C = 16;
    public static final int REDSTONE_INTERFACE = 17;
    public static final int MODULE_MANIPULATOR = 20;
    public static final int TRANSFER_FLUID = 21;
    public static final int TRANSFER_ENERGY = 22;
    public static final int TRANSFER_ITEM = 23;
    public static final int DIMENSIONAL_BRIDGE_STABILIZER = 24;
    public static final int MANUAL = 25;

    public static void openGui(EntityPlayer player, TileEntity tile, int gui)
    {
        player.openGui(EnhancedPortals.instance, gui, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == MANUAL)
        {
            return new GuiManual(player);
        }

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
            return new GuiModuleManipulator((TilePortalManipulator) tile, player);
        }
        else if (ID == DIMENSIONAL_BRIDGE_STABILIZER)
        {
            return new GuiDimensionalBridgeStabilizer((TileStabilizerMain) tile, player);
        }
        else if (ID == DIALING_DEVICE_A)
        {
            return new GuiDialingDevice((TileDialingDevice) tile, player);
        }
        else if (ID == DIALING_DEVICE_B)
        {
            return new GuiDialingManual((TileDialingDevice) tile, player);
        }
        else if (ID == DIALING_DEVICE_C)
        {
            return new GuiDialingAdd((TileDialingDevice) tile, player);
        }
        else if (ID == DIALING_DEVICE_D)
        {
            return new GuiDialingEdit((TileDialingDevice) tile, player);
        }
        else if (ID == DIALING_DEVICE_E)
        {
            return new GuiDialingEditIdentifier((TileDialingDevice) tile, player);
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
        else if (ID == TEXTURE_DIALING_SAVE_A)
        {
            return new GuiDialingEditFrame((TileDialingDevice) tile, player, false);
        }
        else if (ID == TEXTURE_DIALING_SAVE_B)
        {
            return new GuiDialingEditPortal((TileDialingDevice) tile, player, false);
        }
        else if (ID == TEXTURE_DIALING_SAVE_C)
        {
            return new GuiDialingEditParticle((TileDialingDevice) tile, player, false);
        }
        else if (ID == TEXTURE_DIALING_EDIT_A)
        {
            return new GuiDialingEditFrame((TileDialingDevice) tile, player, true);
        }
        else if (ID == TEXTURE_DIALING_EDIT_B)
        {
            return new GuiDialingEditPortal((TileDialingDevice) tile, player, true);
        }
        else if (ID == TEXTURE_DIALING_EDIT_C)
        {
            return new GuiDialingEditParticle((TileDialingDevice) tile, player, true);
        }
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
        if (ID == MANUAL)
        {
            return new ContainerManual(player.inventory);
        }

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
            return new ContainerModuleManipulator((TilePortalManipulator) tile, player.inventory);
        }
        else if (ID == DIMENSIONAL_BRIDGE_STABILIZER)
        {
            return new ContainerDimensionalBridgeStabilizer((TileStabilizerMain) tile, player.inventory);
        }
        else if (ID == DIALING_DEVICE_A)
        {
            return new ContainerDialingDevice((TileDialingDevice) tile, player.inventory);
        }
        else if (ID == DIALING_DEVICE_B)
        {
            return new ContainerDialingManual((TileDialingDevice) tile, player.inventory);
        }
        else if (ID == DIALING_DEVICE_C)
        {
            return new ContainerDialingAdd((TileDialingDevice) tile, player.inventory);
        }
        else if (ID == DIALING_DEVICE_D)
        {
            return new ContainerDialingEdit((TileDialingDevice) tile, player.inventory);
        }
        else if (ID == DIALING_DEVICE_E)
        {
            return new ContainerDialingEditIdentifier((TileDialingDevice) tile, player.inventory);
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
        else if (ID == TEXTURE_DIALING_EDIT_A || ID == TEXTURE_DIALING_SAVE_A)
        {
            return new ContainerDialingEditTexture((TileDialingDevice) tile, player.inventory);
        }
        else if (ID == TEXTURE_DIALING_EDIT_B || ID == TEXTURE_DIALING_SAVE_B)
        {
            return new ContainerDialingEditPortal((TileDialingDevice) tile, player.inventory);
        }
        else if (ID == TEXTURE_DIALING_EDIT_C || ID == TEXTURE_DIALING_SAVE_C)
        {
            return new ContainerDialingEditParticle((TileDialingDevice) tile, player.inventory);
        }
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
