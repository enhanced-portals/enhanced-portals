package uk.co.shadeddimensions.enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.client.gui.GuiPortalController;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalController;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TilePortalController && tile instanceof TilePortalController)
        {
            return new ContainerPortalController(player.inventory, (TilePortalController) tile);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == Identifiers.Gui.FRAME_CONTROLLER && tile instanceof TilePortalController)
        {
            return new GuiPortalController(player, (TilePortalController) tile);
        }

        return null;
    }
}
