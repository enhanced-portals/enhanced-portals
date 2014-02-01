package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import cofh.api.tileentity.ISidedBlockTexture;

public class TilePortalFrame extends TilePortalPart implements ISidedBlockTexture
{
    protected boolean wearingGoggles = GeneralUtils.isWearingGoggles();

    @Override
    public Icon getBlockTexture(int side, int pass)
    {
        if (pass == 0)
        {
            TilePortalController controller = getPortalController();

            if (controller != null)
            {
                if (controller.activeTextureData.hasCustomFrameTexture() && ClientProxy.customFrameTextures.size() > controller.activeTextureData.getCustomFrameTexture() && ClientProxy.customFrameTextures.get(controller.activeTextureData.getCustomFrameTexture()) != null)
                {
                    return ClientProxy.customFrameTextures.get(controller.activeTextureData.getCustomFrameTexture());
                }
                else if (controller.activeTextureData.getFrameItem() != null && controller.activeTextureData.getFrameItem().getItem() instanceof ItemBlock)
                {
                    return Block.blocksList[((ItemBlock) controller.activeTextureData.getFrameItem().getItem()).getBlockID()].getIcon(side, controller.activeTextureData.getFrameItem().getItemDamage());
                }
            }
            else
            {
                return BlockFrame.connectedTextures.getBaseIcon();
            }

            return BlockFrame.connectedTextures.getIconForSide(worldObj, xCoord, yCoord, zCoord, side);
        }

        return BlockFrame.overlayIcons[0];
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    public void blockDismantled()
    {
        TilePortalController controller = getPortalController();

        if (controller != null)
        {
            controller.partBroken(false);
        }
    }
}
