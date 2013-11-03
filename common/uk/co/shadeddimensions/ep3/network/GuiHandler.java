package uk.co.shadeddimensions.ep3.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileEnhancedPortals)
        {
            TileEnhancedPortals t = (TileEnhancedPortals) tile;
            GUIs gui = GUIs.getGUI(ID);            
            return gui.getClientGui(t, player);
        }
        
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileEnhancedPortals)
        {
            TileEnhancedPortals t = (TileEnhancedPortals) tile;
            GUIs gui = GUIs.getGUI(ID);
            return gui.getServerGui(t, player);
        }
        
        return null;
    }
}
