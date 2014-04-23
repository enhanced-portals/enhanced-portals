package uk.co.shadeddimensions.ep3.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.client.gui.GuiBiometricIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.GuiGuide;
import uk.co.shadeddimensions.ep3.client.gui.GuiScanner;
import uk.co.shadeddimensions.ep3.client.gui.GuiTextureDialler;
import uk.co.shadeddimensions.ep3.client.gui.GuiTransferEnergy;
import uk.co.shadeddimensions.ep3.client.gui.GuiTransferFluid;
import uk.co.shadeddimensions.ep3.client.gui.GuiTransferItem;
import uk.co.shadeddimensions.ep3.container.ContainerBiometricIdentifier;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.container.ContainerTransferEnergy;
import uk.co.shadeddimensions.ep3.container.ContainerTransferFluid;
import uk.co.shadeddimensions.ep3.container.ContainerTransferItem;
import uk.co.shadeddimensions.ep3.item.ItemHandheldScanner;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferEnergy;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferFluid;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferItem;
import uk.co.shadeddimensions.library.container.ContainerBase;
import cpw.mods.fml.common.network.IGuiHandler;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.GuiDimensionalBridgeStabilizer;
import enhancedportals.client.gui.GuiModuleManipulator;
import enhancedportals.client.gui.GuiNetworkInterface;
import enhancedportals.client.gui.GuiNetworkInterfaceGlyphs;
import enhancedportals.client.gui.GuiPortalController;
import enhancedportals.client.gui.GuiPortalControllerGlyphs;
import enhancedportals.client.gui.GuiRedstoneInterface;
import enhancedportals.client.gui.GuiTextureFrame;
import enhancedportals.client.gui.GuiTextureParticle;
import enhancedportals.client.gui.GuiTexturePortal;
import enhancedportals.inventory.ContainerDimensionalBridgeStabilizer;
import enhancedportals.inventory.ContainerModuleManipulator;
import enhancedportals.inventory.ContainerNetworkInterface;
import enhancedportals.inventory.ContainerNetworkInterfaceGlyphs;
import enhancedportals.inventory.ContainerPortalController;
import enhancedportals.inventory.ContainerPortalControllerGlyphs;
import enhancedportals.inventory.ContainerRedstoneInterface;
import enhancedportals.inventory.ContainerTextureFrame;
import enhancedportals.inventory.ContainerTextureParticle;
import enhancedportals.inventory.ContainerTexturePortal;

public class GuiHandler implements IGuiHandler
{
    public static final int PORTAL_CONTROLLER_A = 100;
    public static final int PORTAL_CONTROLLER_B = 101;
    public static final int NETWORK_INTERFACE_A = 102;
    public static final int NETWORK_INTERFACE_B = 103;
    
    public static final int REDSTONE_INTERFACE = 2;
    public static final int DIALLING_DEVICE = 4;
    public static final int BIOMETRIC_IDENTIFIER = 5;
    public static final int MODULE_MANIPULATOR = 6;
    public static final int TRANSFER_FLUID = 7;
    public static final int TRANSFER_ENERGY = 8;
    public static final int TRANSFER_ITEM = 9;
    
    public static final int TEXTURE_FRAME = 10;
    public static final int TEXTURE_PORTAL = 11;
    public static final int TEXTURE_PARTICLE = 12;
    public static final int TEXTURE_DIALLER = 13;
    
    public static final int DIMENSIONAL_BRIDGE_STABILIZER = 14;
    public static final int SCANNER = 15;
    public static final int GUIDE = 16;
    
    public static void openGui(EntityPlayer player, TileEntity tile, int gui)
    {
        player.openGui(EnhancedPortals.instance, gui, tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == SCANNER)
        {
            ItemStack stack = player.inventory.getCurrentItem();
            return new GuiScanner(ItemHandheldScanner.getInventory(stack), player, stack);
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

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
            else if (ID == DIALLING_DEVICE)
            {
                return new GuiDiallingDevice((TileDiallingDevice) tile);
            }
            else if (ID == TEXTURE_FRAME)
            {
                return new GuiTextureFrame((TileController) tile, player);
            }
            else if (ID == TEXTURE_PORTAL)
            {
                return new GuiTexturePortal((TileController) tile, player);
            }
            else if (ID == TEXTURE_PARTICLE)
            {
                return new GuiTextureParticle((TileController) tile, player);
            }
            else if (ID == TEXTURE_DIALLER)
            {
                return new GuiTextureDialler((TileDiallingDevice) tile, player);
            }
            else if (ID == BIOMETRIC_IDENTIFIER)
            {
                return new GuiBiometricIdentifier((TileBiometricIdentifier) tile, player);
            }
            else if (ID == GUIDE)
            {
                return new GuiGuide();
            }
            else if (ID == TRANSFER_FLUID)
            {
                return new GuiTransferFluid((TileTransferFluid) tile);
            }
            else if (ID == TRANSFER_ENERGY)
            {
                return new GuiTransferEnergy((TileTransferEnergy) tile);
            }
            else if (ID == TRANSFER_ITEM)
            {
                return new GuiTransferItem((TileTransferItem) tile);
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == SCANNER)
        {
            ItemStack stack = player.inventory.getCurrentItem();
            return new ContainerScanner(ItemHandheldScanner.getInventory(stack), player, stack);
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (ID == PORTAL_CONTROLLER_A)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileController) tile, player);
                return new ContainerPortalController((TileController) tile, player.inventory);
            }
            else if (ID == PORTAL_CONTROLLER_B)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileController) tile, player);
                return new ContainerPortalControllerGlyphs((TileController) tile, player.inventory);
            }
            else if (ID == REDSTONE_INTERFACE)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileRedstoneInterface) tile, player);
                return new ContainerRedstoneInterface((TileRedstoneInterface) tile, player.inventory);
            }
            else if (ID == NETWORK_INTERFACE_A)
            {
            	PacketHandlerServer.sendGuiPacketToPlayer((TileController) tile, player);
                return new ContainerNetworkInterface((TileController) tile, player.inventory);
            }
            else if (ID == NETWORK_INTERFACE_B)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileController) tile, player);
                return new ContainerNetworkInterfaceGlyphs((TileController) tile, player.inventory);
            }
            else if (ID == MODULE_MANIPULATOR)
            {
                return new ContainerModuleManipulator((TileModuleManipulator) tile, player.inventory);
            }
            else if (ID == DIMENSIONAL_BRIDGE_STABILIZER)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileStabilizerMain) tile, player);
                return new ContainerDimensionalBridgeStabilizer((TileStabilizerMain) tile, player.inventory);
            }
            else if (ID == DIALLING_DEVICE)
            {
            	PacketHandlerServer.sendGuiPacketToPlayer((TileDiallingDevice) tile, player);
                return new ContainerBase(tile);
            }
            else if (ID == TEXTURE_FRAME)
            {
                return new ContainerTextureFrame((TileController) tile, player.inventory);
            }
            else if (ID == TEXTURE_PORTAL)
            {
                return new ContainerTexturePortal((TileController) tile, player.inventory);
            }
            else if (ID == TEXTURE_PARTICLE)
            {
                return new ContainerTextureParticle((TileController) tile, player.inventory);
            }
            else if (ID == TEXTURE_DIALLER)
            {
                return new ContainerTexture((TileFrame) tile, player);
            }
            else if (ID == BIOMETRIC_IDENTIFIER)
            {
                PacketHandlerServer.sendGuiPacketToPlayer((TileBiometricIdentifier) tile, player);
                return new ContainerBiometricIdentifier((TileBiometricIdentifier) tile, player);
            }
            else if (ID == GUIDE)
            {
                return null;
            }
            else if (ID == TRANSFER_FLUID)
            {
                return new ContainerTransferFluid((TileTransferFluid) tile);
            }
            else if (ID == TRANSFER_ENERGY)
            {
                return new ContainerTransferEnergy((TileTransferEnergy) tile);
            }
            else if (ID == TRANSFER_ITEM)
            {
                return new ContainerTransferItem((TileTransferItem) tile);
            }
        }

        return null;
    }
}
