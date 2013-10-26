package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.GuiIds;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;

public class TileDiallingDevice extends TilePortalPart
{
    @Override
    public boolean activate(EntityPlayer player)
    {
        TilePortalController controller = getPortalController();
        
        if (controller != null && controller.getUniqueIdentifier() == null)
        {
            if (!worldObj.isRemote)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.noUidSet"));
            }

            return false;
        }
        else if (controller == null || !controller.hasConfigured)
        {
            return false;
        }
        else
        {
            player.openGui(EnhancedPortals.instance, GuiIds.DIALLING_DEVICE, worldObj, xCoord, yCoord, zCoord);
            return true;
        }
    }
}
