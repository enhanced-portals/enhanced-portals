package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileNetworkInterface extends TilePortalPart
{
    @SideOnly(Side.CLIENT)
    public int connectedPortals = 0;
        
    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        if (payload.data.hasKey("id"))
        {
            TilePortalController controller = getPortalController();
            
            if (!controller.networkIdentifier.equals(NetworkManager.BLANK_IDENTIFIER))
            {
                CommonProxy.networkManager.removePortalFromNetwork(controller.uniqueIdentifier, controller.networkIdentifier);
            }
            
            if (!payload.data.getString("identifier").equals(NetworkManager.BLANK_IDENTIFIER))
            {
                CommonProxy.networkManager.addPortalToNetwork(controller.uniqueIdentifier, payload.data.getString("identifier"));
            }
            
            controller.networkIdentifier = payload.data.getString("identifier");
            CommonProxy.sendUpdatePacketToAllAround(this);
        }
    }
}
