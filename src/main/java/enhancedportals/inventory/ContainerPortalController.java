package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiPortalController;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.tileentity.portal.TileController;

public class ContainerPortalController extends BaseContainer
{
    TileController controller;
    byte wasPublic = -100;
    String oldGlyphs = "EMPTY";

    public ContainerPortalController(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiPortalController.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        controller = c;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("public"))
        {
            controller.isPublic = !controller.isPublic;
        }
        
        if (tag.hasKey("uid"))
        {
            controller.setUID(new GlyphIdentifier(tag.getString("uid")));
        }
    }
    
    @Override
    public void updateProgressBar(int id, int val)
    {
        if (id == 0)
        {
            controller.isPublic = val == 1;
        }
    }
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        
        byte isPublic = (byte) (controller.isPublic ? 1 : 0);
        String glyphs = controller.getIdentifierUnique() == null ? "" : controller.getIdentifierUnique().getGlyphString();
        
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);
            
            if (isPublic != wasPublic)
            {
                icrafting.sendProgressBarUpdate(this, 0, isPublic);
            }
            
            if (!glyphs.equals(oldGlyphs))
            {
                NBTTagCompound t = new NBTTagCompound();
                t.setString("uid", glyphs);
                EnhancedPortals.packetPipeline.sendTo(new PacketGuiData(t), (EntityPlayerMP) icrafting);
            }
        }
        
        oldGlyphs = glyphs;
        wasPublic = isPublic;
    }
}
