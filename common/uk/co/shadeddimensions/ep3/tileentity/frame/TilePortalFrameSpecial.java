package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.client.Minecraft;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TilePortalFrameSpecial extends TilePortalFrame
{
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    public void updateEntity()
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && Minecraft.getSystemTime() % 10 == 0)
        {
            boolean wGoggles = GeneralUtils.isWearingGoggles();
            
            if (wGoggles != wearingGoggles)
            {
                worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
                wearingGoggles = wGoggles;
            }
        }
    }
}
