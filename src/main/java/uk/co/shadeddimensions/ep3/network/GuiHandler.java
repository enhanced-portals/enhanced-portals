package uk.co.shadeddimensions.ep3.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.client.gui.GuiBiometricIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.GuiDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.client.gui.GuiGuide;
import uk.co.shadeddimensions.ep3.client.gui.GuiModuleManipulator;
import uk.co.shadeddimensions.ep3.client.gui.GuiNetworkInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalController;
import uk.co.shadeddimensions.ep3.client.gui.GuiRedstoneInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiScanner;
import uk.co.shadeddimensions.ep3.client.gui.GuiTexture;
import uk.co.shadeddimensions.ep3.client.gui.GuiTextureDialler;
import uk.co.shadeddimensions.ep3.container.ContainerBiometricIdentifier;
import uk.co.shadeddimensions.ep3.container.ContainerDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.item.ItemHandheldScanner;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import uk.co.shadeddimensions.library.container.ContainerBase;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == GUIs.Scanner.ordinal())
        {
            ItemStack stack = player.inventory.getCurrentItem();
            return new GuiScanner(ItemHandheldScanner.getInventory(stack), player, stack);
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (ID == GUIs.PortalController.ordinal())
            {
                return new GuiPortalController((TilePortalController) tile);
            }
            else if (ID == GUIs.RedstoneInterface.ordinal())
            {
                return new GuiRedstoneInterface((TileRedstoneInterface) tile);
            }
            else if (ID == GUIs.NetworkInterface.ordinal())
            {
                return new GuiNetworkInterface((TilePortalController) tile);
            }
            else if (ID == GUIs.ModuleManipulator.ordinal())
            {
                return new GuiModuleManipulator((TileModuleManipulator) tile, player);
            }
            else if (ID == GUIs.DimensionalBridgeStabilizer.ordinal())
            {
                return new GuiDimensionalBridgeStabilizer((TileStabilizerMain) tile, player);
            }
            else if (ID == GUIs.DiallingDevice.ordinal())
            {
                return new GuiDiallingDevice((TileDiallingDevice) tile);
            }
            else if (ID == GUIs.TexturesFrame.ordinal())
            {
                return new GuiTexture((TilePortalController) tile, player, 0);
            }
            else if (ID == GUIs.TexturesPortal.ordinal())
            {
                return new GuiTexture((TilePortalController) tile, player, 1);
            }
            else if (ID == GUIs.TexturesParticle.ordinal())
            {
                return new GuiTexture((TilePortalController) tile, player, 2);
            }
            else if (ID == GUIs.TexturesDiallingDevice.ordinal())
            {
                return new GuiTextureDialler((TileDiallingDevice) tile, player);
            }
            else if (ID == GUIs.BiometricIdentifier.ordinal())
            {
                return new GuiBiometricIdentifier((TileBiometricIdentifier) tile, player);
            }
            else if (ID == GUIs.Guide.ordinal())
            {
                return new GuiGuide();
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == GUIs.Scanner.ordinal())
        {
            ItemStack stack = player.inventory.getCurrentItem();
            return new ContainerScanner(ItemHandheldScanner.getInventory(stack), player, stack);
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (ID == GUIs.PortalController.ordinal())
            {
                CommonProxy.sendUpdatePacketToPlayer((TilePortalController) tile, player);
                return new ContainerBase(tile);
            }
            else if (ID == GUIs.RedstoneInterface.ordinal())
            {
                CommonProxy.sendUpdatePacketToPlayer((TileRedstoneInterface) tile, player);
                return new ContainerBase(tile);
            }
            else if (ID == GUIs.NetworkInterface.ordinal())
            {
                return new ContainerBase(tile);
            }
            else if (ID == GUIs.ModuleManipulator.ordinal())
            {
                return new ContainerModuleManipulator((TileModuleManipulator) tile, player);
            }
            else if (ID == GUIs.DimensionalBridgeStabilizer.ordinal())
            {
                CommonProxy.sendUpdatePacketToPlayer((TileStabilizerMain) tile, player);
                return new ContainerDimensionalBridgeStabilizer((TileStabilizerMain) tile, player);
            }
            else if (ID == GUIs.DiallingDevice.ordinal())
            {
                return new ContainerBase(tile);
            }
            else if (ID == GUIs.TexturesFrame.ordinal())
            {
                return new ContainerTexture((TilePortalController) tile, player);
            }
            else if (ID == GUIs.TexturesPortal.ordinal())
            {
                return new ContainerTexture((TilePortalController) tile, player);
            }
            else if (ID == GUIs.TexturesParticle.ordinal())
            {
                return new ContainerTexture((TilePortalController) tile, player);
            }
            else if (ID == GUIs.TexturesDiallingDevice.ordinal())
            {
                return new ContainerTexture((TilePortalController) tile, player);
            }
            else if (ID == GUIs.BiometricIdentifier.ordinal())
            {
                CommonProxy.sendUpdatePacketToPlayer((TileBiometricIdentifier) tile, player);
                return new ContainerBiometricIdentifier((TileBiometricIdentifier) tile, player);
            }
            else if (ID == GUIs.Guide.ordinal())
            {
                
            }
        }

        return null;
    }
}
