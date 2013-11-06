package uk.co.shadeddimensions.ep3.lib;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.client.gui.GuiBiometricIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.GuiDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.client.gui.GuiModuleManipulator;
import uk.co.shadeddimensions.ep3.client.gui.GuiNetworkInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalController;
import uk.co.shadeddimensions.ep3.client.gui.GuiRedstoneInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiTexture;
import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.container.ContainerBiometricIdentifier;
import uk.co.shadeddimensions.ep3.container.ContainerDiallingDevice;
import uk.co.shadeddimensions.ep3.container.ContainerDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.container.ContainerEnhancedPortals;
import uk.co.shadeddimensions.ep3.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.ep3.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.ep3.container.ContainerPortalController;
import uk.co.shadeddimensions.ep3.container.ContainerRedstoneInterface;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;

import com.google.common.base.Throwables;

public enum GUIs
{
    PortalController(GuiPortalController.class, ContainerPortalController.class, true), //
    RedstoneInterface(GuiRedstoneInterface.class, ContainerRedstoneInterface.class, true), //
    NetworkInterface(GuiNetworkInterface.class, ContainerNetworkInterface.class, false), //
    ModuleManipulator(GuiModuleManipulator.class, ContainerModuleManipulator.class, false), //
    DimensionalBridgeStabilizer(GuiDimensionalBridgeStabilizer.class, ContainerDimensionalBridgeStabilizer.class, true), //
    DiallingDevice(GuiDiallingDevice.class, ContainerDiallingDevice.class, true), //
    TexturesFrame(GuiTexture.class, ContainerTexture.class, true, 0, true), //
    TexturesPortal(GuiTexture.class, ContainerTexture.class, true, 1, true), //
    TexturesParticle(GuiTexture.class, ContainerTexture.class, true, 2, true), //
    TexturesDiallingDevice(GuiTexture.class, ContainerTexture.class, true, 0, false), //
    BiometricIdentifier(GuiBiometricIdentifier.class, ContainerBiometricIdentifier.class, true);

    Class<? extends GuiEnhancedPortals> clientGUI;
    Class<? extends ContainerEnhancedPortals> serverGUI;
    boolean sendPacketFirst;
    int guiType = -1;
    boolean guiSetting;

    private GUIs(Class<? extends GuiEnhancedPortals> gui, Class<? extends ContainerEnhancedPortals> container, boolean sendPacket)
    {
        clientGUI = gui;
        serverGUI = container;
        sendPacketFirst = sendPacket;
    }

    private GUIs(Class<? extends GuiEnhancedPortals> gui, Class<? extends ContainerEnhancedPortals> container, boolean sendPacket, int guitype, boolean guisetting)
    {
        clientGUI = gui;
        serverGUI = container;
        sendPacketFirst = sendPacket;
        guiType = guitype;
        guiSetting = guisetting;
    }

    public GuiEnhancedPortals getClientGui(TileEnhancedPortals tile, EntityPlayer player)
    {
        try
        {
            Class<?> tileClass = clientGUI.getConstructors()[0].getParameterTypes()[0];

            if (tileClass.isInstance(tile))
            {
                if (guiType != -1)
                {
                    return clientGUI.getConstructor(tileClass, EntityPlayer.class, int.class, boolean.class).newInstance(tile, player, guiType, guiSetting);
                }
                else
                {                
                    return clientGUI.getConstructor(tileClass, EntityPlayer.class).newInstance(tile, player);
                }
            }
        }
        catch (Exception e)
        {
            Throwables.propagateIfPossible(e);
            CommonProxy.logger.severe("Failed to construct client GUI element");
            e.printStackTrace();
        }

        return null;
    }

    public ContainerEnhancedPortals getServerGui(TileEnhancedPortals tile, EntityPlayer player)
    {
        try
        {
            Class<?> tileClass = serverGUI.getConstructors()[0].getParameterTypes()[0];

            if (tileClass.isInstance(tile))
            {
                if (sendPacketFirst)
                {
                    CommonProxy.sendUpdatePacketToPlayer(tile, player);
                }

                return serverGUI.getConstructor(tileClass, EntityPlayer.class).newInstance(tile, player);
            }
        }
        catch (Exception e)
        {
            Throwables.propagateIfPossible(e);
            CommonProxy.logger.severe("Failed to construct server GUI element");
            e.printStackTrace();
        }

        return null;
    }

    public static GUIs getGUI(int id)
    {
        return values()[id];
    }
}
