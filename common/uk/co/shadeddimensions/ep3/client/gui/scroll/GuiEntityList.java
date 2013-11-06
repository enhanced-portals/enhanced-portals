package uk.co.shadeddimensions.ep3.client.gui.scroll;

import java.util.ArrayList;

import net.minecraft.entity.EntityList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier.EntityPair;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiEntityList extends GuiScrollList
{
    TileBiometricIdentifier biometricIdentifier;
    boolean isSending;
    ResourceLocation textureLocation;
    
    public GuiEntityList(GuiEnhancedPortals p, int X, int Y, int W, int H, TileBiometricIdentifier tile, boolean t, ResourceLocation texture)
    {
        super(p, X, Y, W, H);
        biometricIdentifier = tile;
        isSending = t;
        textureLocation = texture;
    }

    @Override
    protected void elementClicked(int i, int mouseX, int mouseY, boolean wasDoubleClick)
    {
        if (wasDoubleClick)
        {
            GuiPayload payload = null;
            
            if (mouseX >= parent.getGuiLeft() + x && mouseX <= parent.getGuiLeft() + x + w - 33)
            {
                payload = new GuiPayload();
                payload.data.setBoolean("list", isSending);
                payload.data.setInteger("invert", i);
                parent.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
            else if (mouseX >= parent.getGuiLeft() + x + w - 15 && mouseX <= parent.getGuiLeft() + x + w)
            {
                payload = new GuiPayload();
                payload.data.setBoolean("list", isSending);
                payload.data.setInteger("remove", i);
                parent.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
            else if (mouseX >= parent.getGuiLeft() + x + w - 31 && mouseX <= parent.getGuiLeft() + x + w - 17)
            {
                payload = new GuiPayload();
                payload.data.setBoolean("list", isSending);
                payload.data.setInteger("type", i);                
                parent.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
            
            if (payload != null)
            {
                ClientProxy.sendGuiPacket(payload);
            }
        }
    }

    @Override
    protected void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY)
    {
        boolean isMouseOver = isMouseOver(i, mouseX, mouseY);
        ArrayList<EntityPair> list = isSending ? biometricIdentifier.sendingEntityTypes : biometricIdentifier.recievingEntityTypes;
        byte type = list.get(i).fuzzy;
        String str = type == 0 ? list.get(i).name : type == 1 ? (String) EntityList.classToStringMapping.get(list.get(i).clas) : type == 2 ? (String) EntityList.classToStringMapping.get(list.get(i).clas.getSuperclass()) : type == 3 ? list.get(i).name : null;
        
        if (str == null)
        {
            str = list.get(i).name;
        }
        
        parent.drawSmallButton(x, y, w - 32, isMouseOver && mouseX >= x && mouseX <= x + w - 33, false, 1f, 1f, 1f);
        parent.drawTinyButton(x + w - 15, y, isMouseOver && mouseX >= x + w - 15 && mouseX <= x + w, false, 1f, 1f, 1f, 3, true);
        parent.drawTinyButton(x + w - 31, y, isMouseOver && mouseX >= x + w - 31 && mouseX <= x + w - 17, false, 1f, 1f, 1f, 2, true);
        
        parent.getFontRenderer().drawStringWithShadow(str, x + ((w - 32) / 2) - (parent.getFontRenderer().getStringWidth(str) / 2), y + 3, list.get(i).inverted ? 0xFF0000 : 0x00FF00);
        parent.getFontRenderer().drawStringWithShadow("" + type, x + w - 26, y + 3, 0xFFFFFF);
    }
    
    @Override
    protected void drawOverlays()
    {
        parent.getMinecraft().renderEngine.bindTexture(textureLocation);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        
        if (isSending)
        {
            drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y - getEntryHeight(), x, y - getEntryHeight(), w, getEntryHeight());
            drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y + h, x, y + h, w, getEntryHeight());
        }
        else
        {
            drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y - getEntryHeight(), x - 150, y - getEntryHeight(), w, getEntryHeight());
            drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y + h, x - 150, y + h, w, getEntryHeight());
        }
    }
    
    @Override
    protected int getElementCount()
    {
        return isSending ? biometricIdentifier.sendingEntityTypes.size() : biometricIdentifier.recievingEntityTypes.size();
    }

    @Override
    protected int getEntryHeight()
    {
        return 15;
    }

    @Override
    protected int getEntrySpacing()
    {
        return 1;
    }

    @Override
    protected void drawBackgroundStart()
    {
        
    }

    @Override
    protected void drawBackgroundEnd()
    {
        if ((isSending ? biometricIdentifier.sendingEntityTypes : biometricIdentifier.recievingEntityTypes).isEmpty())
        {
            parent.getFontRenderer().drawSplitString("No entries found", parent.getGuiLeft() + x + 5, parent.getGuiTop() + y + 5, w - 5, 0x000000);
        }
    }
    
    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (mouseY >= top && mouseY <= top + h && mouseX >= left && mouseX <= left + w)
        {
            int k1 = mouseY - top + (int) scrolled;
            int l1 = k1 / (getEntryHeight() + getEntrySpacing());
            ArrayList<EntityPair> list = isSending ? biometricIdentifier.sendingEntityTypes : biometricIdentifier.recievingEntityTypes;
                        
            if (l1 >= 0 && l1 < list.size())
            {
                ArrayList<String> strList = new ArrayList<String>();
                int type = list.get(l1).fuzzy;
                
                if (mouseX >= left && mouseX <= left + w - 33)
                {
                    strList.add(list.get(l1).inverted ? EnumChatFormatting.RED + "Not Allowing" : EnumChatFormatting.GREEN + "Allowing");
                    
                    if (type == 0)
                    {
                        strList.add("Any entities called");
                        strList.add(EnumChatFormatting.GRAY + " " + list.get(l1).name);
                    }
                    else if (type == 1)
                    {
                        strList.add("Any entities of the type");
                        strList.add(EnumChatFormatting.GRAY + " " + EntityList.classToStringMapping.get(list.get(l1).clas));
                    }
                    else if (type == 2)
                    {
                        String s = (String) EntityList.classToStringMapping.get(list.get(l1).clas.getSuperclass());
                        strList.add("Any entities of the type");
                        strList.add(EnumChatFormatting.GRAY + " " + (s == null ? "Unknown" : s));
                    }
                    else if (type == 3)
                    {
                        strList.add("Any entities called");
                        strList.add(EnumChatFormatting.GRAY + " " + list.get(l1).name);
                        strList.add("With the type of");
                        strList.add(EnumChatFormatting.GRAY + " " + EntityList.classToStringMapping.get(list.get(l1).clas));
                    }
                }
                else if (mouseX >= left + w - 31 && mouseX <= left + w - 17)
                {
                    strList.add("Matches:");
                    
                    if (type == 0)
                    {
                        strList.add(EnumChatFormatting.GRAY + " Entity Name");
                    }
                    else if (type == 1)
                    {
                        strList.add(EnumChatFormatting.GRAY + " Entity Type");
                    }
                    else if (type == 2)
                    {
                        strList.add(EnumChatFormatting.GRAY + " Parent Type");
                    }
                    else if (type == 3)
                    {
                        strList.add(EnumChatFormatting.GRAY + " Entity Name");
                        strList.add(EnumChatFormatting.GRAY + " Entity Type");
                    }
                }
                
                parent.drawHoverText(strList, mouseX - parent.getGuiLeft(), mouseY);
            }
        }
        
        GL11.glDisable(GL11.GL_LIGHTING);
    }
}
