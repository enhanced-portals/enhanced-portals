package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.container.ContainerPortalModifier;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiPortalModifier extends GuiContainer
{
    TileEntityPortalModifier portalModifier;
    boolean hasInteractedWith = false, isActive = false;
    GuiButton okayButton;
    
    List<GuiItemStackButton> elementList;

    public GuiPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
    {
        super(new ContainerPortalModifier(player, modifier));
        portalModifier = modifier;
        isActive = portalModifier.isActive();
        elementList = new ArrayList<GuiItemStackButton>();
        
        List<String> strList = new ArrayList<String>();        
        strList.add("Redstone Control");
        strList.add(EnumChatFormatting.GRAY + "High");        
        elementList.add(new GuiItemStackButton(xSize + 4, 4, new ItemStack(Block.torchRedstoneActive), this, strList, "redstoneHigh"));
        elementList.get(0).active = portalModifier.redstoneSetting == 0;
        
        List<String> strList1 = new ArrayList<String>();
        strList1.add("Redstone Control");
        strList1.add(EnumChatFormatting.GRAY + "Low");        
        elementList.add(new GuiItemStackButton(xSize + 4, 24, new ItemStack(Block.torchRedstoneIdle), this, strList1, "redstoneLow"));
        elementList.get(1).active = portalModifier.redstoneSetting == 1;
        
        List<String> strList3 = new ArrayList<String>();
        strList3.add("Particles");
        strList3.add(EnumChatFormatting.GRAY + (portalModifier.particles ? "Enabled" : "Disabled"));        
        elementList.add(new GuiItemStackButton(8, 15, new ItemStack(Block.bedrock), this, strList3, "particles", true));
        elementList.get(2).active = portalModifier.particles;
        
        List<String> strList4 = new ArrayList<String>();
        strList4.add("Sounds");
        strList4.add(EnumChatFormatting.GRAY + (portalModifier.sounds ? "Enabled" : "Disabled"));
        elementList.add(new GuiItemStackButton(26, 15, new ItemStack(Block.jukebox), this, strList4, "sounds", true));
        elementList.get(3).active = portalModifier.sounds;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 100)
        {
            mc.thePlayer.closeScreen();
        }

        hasInteractedWith = true;

        if (button.id == 5)
        {
            PacketDispatcher.sendPacketToServer(new PacketGui(true, false, GuiIds.PortalModifierNetwork, portalModifier).getPacket());
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.GUI_LOCATION + "portalModifier.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2 - 3;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        int padding = 5, width = 10;

        if (portalModifier.thickness == 1)
        {
            padding = 4;
            width = 8;
        }
        else if (portalModifier.thickness == 2)
        {
            padding = 2;
            width = 4;
        }
        else if (portalModifier.thickness == 3)
        {
            padding = 0;
            width = 0;
        }
                
        drawTexturedModalRect(0, 0, 0, 0, 0, 0);        
        drawRect(guiLeft + 134 + padding, guiTop + 15, guiLeft + 16 - width + 134 + padding, guiTop + 15 + 16, 0xFF555555);
        ItemStack itemstack = null;
        
        if (portalModifier.texture.blockID != -1)
        {
            itemstack = new ItemStack(Block.blocksList[portalModifier.texture.blockID], 1, portalModifier.texture.metaData);
        }
        else if (portalModifier.texture.colour != null)
        {
            itemstack = new ItemStack(Block.blocksList[BlockIds.DummyPortal], 1, portalModifier.texture.colour.ordinal());
        }

        if (itemstack != null)
        {
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, guiLeft + xSize - 24, guiTop + 15);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String pModifier = "Portal Modifier"; // TODO LANGUAGE

        fontRenderer.drawString(pModifier, xSize / 2 - fontRenderer.getStringWidth(pModifier) / 2, -15, 0xFFFFFF);
        fontRenderer.drawString("Upgrades", 8, 3, 0xFF444444);
        fontRenderer.drawString("Modifications", xSize - 8 - fontRenderer.getStringWidth("Modifications"), 3, 0xFF444444);
        fontRenderer.drawString("Network", 8, 35, 0xFF444444);
    }

    @Override
    public void drawScreen(int x, int y, float par3)
    {
        if (!isActive)
        {
            super.drawScreen(x, y, par3);
            
            for (int i = elementList.size() - 1; i >= 0; i--)
            {
                elementList.get(i).drawElement(guiLeft, guiTop, x, y, fontRenderer, itemRenderer, mc.renderEngine);
            }
            
            if (isPointInRegion(134, 15, 16, 16, x, y))
            {
                String thickness = "Unknown";

                switch (portalModifier.thickness)
                // TODO LANGUAGE
                {
                    case 0:
                        thickness = "Normal";
                        break;

                    case 1:
                        thickness = "Thick";
                        break;

                    case 2:
                        thickness = "Thicker";
                        break;

                    case 3:
                        thickness = "Full Block";
                        break;
                }

                List<String> list = new ArrayList<String>();
                list.add("Thickness");
                list.add(EnumChatFormatting.GRAY + thickness);
                drawHoveringText(list, x, y, fontRenderer);                
            }
            else if (isPointInRegion(152, 15, 16, 16, x, y))
            {
                String txt = "Unknown";

                if (portalModifier.texture.blockID == -1) // TODO BETTER LANGUAGE
                {
                    txt = StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.dyeColorNames[PortalTexture.swapColours(portalModifier.texture.colour.ordinal())]) + " Portal";
                }
                else
                {
                    txt = Block.blocksList[portalModifier.texture.blockID].getLocalizedName();

                    if (portalModifier.texture.blockID == 9 || portalModifier.texture.blockID == 11)
                    {
                        txt = "Still " + txt;
                    }
                    else if (portalModifier.texture.blockID == 8 || portalModifier.texture.blockID == 10)
                    {
                        txt = "Flowing " + txt;
                    }
                }

                List<String> list = new ArrayList<String>();
                list.add("Facade");
                list.add(EnumChatFormatting.GRAY + txt);
                drawHoveringText(list, x, y, fontRenderer);
            }
            
            if (portalModifier.network != "")
            {
                String[] split = portalModifier.network.split(Reference.glyphSeperator);
                
                for (int i = 0; i < split.length; i++)
                {
                    for (int j = 0; j < Reference.glyphValues.size(); j++)
                    {
                        if (Reference.glyphValues.get(j).equalsIgnoreCase(split[i]))
                        {
                            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, Reference.glyphItems.get(j), guiLeft + 8 + (i * 18), guiTop + 47);
                        }
                    }
                }
            }
        }
        else
        {
            String txt = "You cannot change any settings while the Portal Modifier is active."; // TODO LANGUAGE

            drawDefaultBackground();
            fontRenderer.drawString(txt, width / 2 - fontRenderer.getStringWidth(txt) / 2, guiTop, 0xFFFFFF);
            okayButton.drawButton(mc, 0, 0);
        }
    }

    public void drawTexturedRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.062F;
        float f1 = 0.062F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, zLevel, (par3 + 0) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, zLevel, (par3 + par5) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, zLevel, (par3 + par5) * f, (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, (par3 + 0) * f, (par4 + 0) * f1);
        tessellator.draw();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        okayButton = new GuiButton(100, width / 2 - 50, height / 2 - 10, 100, 20, "Close");        
        buttonList.add(okayButton);
        okayButton.drawButton = isActive;
        
        buttonList.add(new GuiButton(5, 10, 10, 100, 20, "Network"));
    }

    @Override
    protected void mouseClicked(int x, int y, int buttonClicked)
    {
        super.mouseClicked(x, y, buttonClicked);
        
        for (int i = 0; i < elementList.size(); i++)
        {
            elementList.get(i).handleMouseClick(guiLeft, guiTop, x, y, buttonClicked);
        }
        
        if (isPointInRegion(134, 15, 16, 16, x, y))
        {
            portalModifier.thickness++;
            
            if (portalModifier.thickness >= 4)
            {
                portalModifier.thickness = 0;
            }
        }
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();

        if (hasInteractedWith)
        {
            PacketDispatcher.sendPacketToServer(new PacketTEUpdate(portalModifier).getPacket());
        }
    }

    public void elementClicked(GuiItemStackButton itemStackButton, int button)
    {
        if (itemStackButton.value.equalsIgnoreCase("redstoneHigh"))
        {
            portalModifier.redstoneSetting = 0;
            itemStackButton.active = true;            
            elementList.get(1).active = false;
        }
        else if (itemStackButton.value.equalsIgnoreCase("redstoneLow"))
        {
            portalModifier.redstoneSetting = 1;
            itemStackButton.active = true;
            elementList.get(0).active = false;
        }
        else if (itemStackButton.value.equalsIgnoreCase("particles"))
        {
            portalModifier.particles = !portalModifier.particles;
            itemStackButton.textList.set(1, EnumChatFormatting.GRAY + (portalModifier.particles ? "Enabled" : "Disabled"));
            itemStackButton.active = portalModifier.particles;
        }
        else if (itemStackButton.value.equalsIgnoreCase("sounds"))
        {
            portalModifier.sounds = !portalModifier.sounds;
            itemStackButton.textList.set(1, EnumChatFormatting.GRAY + (portalModifier.sounds ? "Enabled" : "Disabled"));
            itemStackButton.active = portalModifier.sounds;
        }
        
        hasInteractedWith = true;
    }

    public void drawText(List<String> list, int x2, int y2)
    {
        drawHoveringText(list, x2, y2, fontRenderer);
    }
}
