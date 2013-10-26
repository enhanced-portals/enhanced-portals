package uk.co.shadeddimensions.ep3.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.client.gui.GuiDBS;
import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.GuiModuleManipulator;
import uk.co.shadeddimensions.ep3.client.gui.GuiParticleTexture;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalFrameController;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalFrameNetworkInterface;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalFrameRedstone;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalFrameTexture;
import uk.co.shadeddimensions.ep3.client.gui.GuiPortalTexture;
import uk.co.shadeddimensions.ep3.container.ContainerDBS;
import uk.co.shadeddimensions.ep3.container.ContainerDiallingDevice;
import uk.co.shadeddimensions.ep3.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.ep3.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.ep3.container.ContainerParticleTexture;
import uk.co.shadeddimensions.ep3.container.ContainerPortalFrameController;
import uk.co.shadeddimensions.ep3.container.ContainerPortalFrameRedstone;
import uk.co.shadeddimensions.ep3.container.ContainerPortalFrameTexture;
import uk.co.shadeddimensions.ep3.container.ContainerPortalTexture;
import uk.co.shadeddimensions.ep3.lib.GuiIds;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PORTAL_CONTROLLER && tile instanceof TilePortalController)
        {
            return new GuiPortalFrameController(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.PORTAL_REDSTONE && tile instanceof TileRedstoneInterface)
        {
            return new GuiPortalFrameRedstone(player, (TileRedstoneInterface) tile);
        }
        else if (ID == GuiIds.PORTAL_FRAME_TEXTURE && tile instanceof TilePortalController)
        {
            return new GuiPortalFrameTexture(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.PORTAL_TEXTURE && tile instanceof TilePortalController)
        {
            return new GuiPortalTexture(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.PARTICLE_TEXTURE && tile instanceof TilePortalController)
        {
            return new GuiParticleTexture((TilePortalController) tile);
        }
        else if (ID == GuiIds.NETWORK_INTERFACE && tile instanceof TilePortalController)
        {
            return new GuiPortalFrameNetworkInterface((TilePortalController) tile);
        }
        else if (ID == GuiIds.MODULE_MANIPULATOR && tile instanceof TileModuleManipulator)
        {
            return new GuiModuleManipulator(player, (TileModuleManipulator) tile);
        }
        else if (ID == GuiIds.DBS && tile instanceof TileStabilizer)
        {
            TileStabilizer dbs = (TileStabilizer) tile;
            
            if (dbs.hasConfigured)
            {
                return new GuiDBS(dbs.getMainBlock());
            }
        }
        else if (ID == GuiIds.DIALLING_DEVICE && tile instanceof TileDiallingDevice)
        {
            TileDiallingDevice dialler = (TileDiallingDevice) tile;
            TilePortalController controller = dialler.getPortalController();
            
            if (controller != null)
            {
                return new GuiDiallingDevice(controller);
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PORTAL_CONTROLLER && tile instanceof TilePortalController)
        {
            CommonProxy.sendUpdatePacketToPlayer((TilePortalController) tile, player);
            return new ContainerPortalFrameController((TilePortalController) tile);
        }
        else if (ID == GuiIds.PORTAL_REDSTONE && tile instanceof TileRedstoneInterface)
        {
            CommonProxy.sendUpdatePacketToPlayer((TileRedstoneInterface) tile, player);
            return new ContainerPortalFrameRedstone((TileRedstoneInterface) tile);
        }
        else if (ID == GuiIds.PORTAL_FRAME_TEXTURE && tile instanceof TilePortalController)
        {
            return new ContainerPortalFrameTexture(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.PORTAL_TEXTURE && tile instanceof TilePortalController)
        {
            return new ContainerPortalTexture(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.PARTICLE_TEXTURE && tile instanceof TilePortalController)
        {
            return new ContainerParticleTexture((TilePortalController) tile);
        }
        else if (ID == GuiIds.NETWORK_INTERFACE && tile instanceof TilePortalController)
        {
            CommonProxy.sendUpdatePacketToPlayer((TilePortalController) tile, player);
            return new ContainerNetworkInterface((TilePortalController) tile);
        }
        else if (ID == GuiIds.MODULE_MANIPULATOR && tile instanceof TileModuleManipulator)
        {
            return new ContainerModuleManipulator(player, (TileModuleManipulator) tile);
        }
        else if (ID == GuiIds.DBS && tile instanceof TileStabilizer)
        {
            TileStabilizer dbs = (TileStabilizer) tile;

            if (dbs.hasConfigured)
            {
                CommonProxy.sendUpdatePacketToPlayer(dbs.getMainBlock(), player);
                return new ContainerDBS(dbs.getMainBlock());
            }
        }
        else if (ID == GuiIds.DIALLING_DEVICE && tile instanceof TileDiallingDevice)
        {
            TileDiallingDevice dialler = (TileDiallingDevice) tile;
            TilePortalController controller = dialler.getPortalController();
            
            if (controller != null)
            {
                return new ContainerDiallingDevice(controller);
            }
        }

        return null;
    }
}
