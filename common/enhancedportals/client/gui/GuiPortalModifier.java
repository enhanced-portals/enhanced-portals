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
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiPortalModifier extends GuiContainer
{
    TileEntityPortalModifier portalModifier;
    boolean hasInteractedWith = false, isActive = false;
    GuiButton okayButton;

    public GuiPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
    {
        super(new ContainerPortalModifier(player, modifier));
        portalModifier = modifier;
        isActive = portalModifier.isActive();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 100)
        {
            mc.thePlayer.closeScreen();
        }

        hasInteractedWith = true;

        if (button.id == 1) // particles
        {
            portalModifier.particles = !portalModifier.particles;
            button.displayString = (portalModifier.particles ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Particles"; // TODO LANGUAGE
        }
        else if (button.id == 2) // sounds
        {
            portalModifier.sounds = !portalModifier.sounds;
            button.displayString = (portalModifier.sounds ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Sounds"; // TODO LANGUAGE
        }
    }

    private void drawRune(int x, int y, int x2, int y2, boolean selected)
    {
        if (selected)
        {
            GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
        }
        else
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        
        drawTexturedModalRect(x, y, x2, y2, 16, 16);
    }
    
    private void clickRune(int mX, int mY, int x, int y, int val)
    {
        if (isPointInRegion(x, y, 16, 16, mX, mY))
        {
            if ((Integer.parseInt(portalModifier.network) & val) == 0)
            {
                portalModifier.network = "" + (Integer.parseInt(portalModifier.network) | val);
                
                System.out.println("Added " + val + " (" + portalModifier.network + ")");
            }
            else
            {
                int flags = Integer.parseInt(portalModifier.network);
                flags &= ~val;                
                portalModifier.network = "" + flags;
                
                System.out.println("Removed " + val + " (" + portalModifier.network + ")");
            }
            
            hasInteractedWith = true;
        }
    }
    
    private boolean isFlagSet(int flag)
    {
        return (Integer.parseInt(portalModifier.network) & flag) != 0;
    }
    
    private void drawFakeButton(int x, int y, String iconLocation, boolean active, int offColour, int onColour, int backColour)
    {
        drawRect(x, y, x + 18, y + 18, active ? onColour : offColour);
        
        x += 1;
        y += 1;
        
        drawRect(x, y, x + 16, y + 16, backColour);        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        mc.renderEngine.bindTexture(iconLocation);
        drawTexturedRect(x, y, 0, 0, 16, 16);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.GUI_LOCATION + "portalModifier.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2 - 4;
        drawTexturedModalRect(x, y, 0, 0, xSize + 22, ySize + 4);

        x = guiLeft + 13;
        y = guiTop + 4;
        mc.renderEngine.bindTexture("/font/alternate.png");
        
        drawRune(x, y, 16, 64, isFlagSet(1));
        drawRune(x + 20, y, 32, 64, isFlagSet(2));
        drawRune(x + 40, y, 48, 64, isFlagSet(4));
        drawRune(x + 60, y, 64, 64, isFlagSet(8));
        drawRune(x + 80, y, 80, 64, isFlagSet(16));
        drawRune(x + 100, y, 96, 64, isFlagSet(32));
        drawRune(x + 120, y, 112, 64, isFlagSet(64));
        drawRune(x + 140, y, 128, 64, isFlagSet(128));
        
        drawRune(x, y + 20, 16, 80, isFlagSet(256));
        drawRune(x + 20, y + 20, 32, 80, isFlagSet(512));
        drawRune(x + 40, y + 20, 48, 80, isFlagSet(1024));
        drawRune(x + 60, y + 20, 64, 80, isFlagSet(2048));
        drawRune(x + 80, y + 20, 80, 80, isFlagSet(4096));
        drawRune(x + 100, y + 20, 96, 80, isFlagSet(8192));
        drawRune(x + 120, y + 20, 112, 80, isFlagSet(16384));
        drawRune(x + 140, y + 20, 128, 80, isFlagSet(32768));
        
        drawRune(x, y + 40, 0, 80, isFlagSet(65536));
        drawRune(x + 20, y + 40, 160, 64, isFlagSet(131072));
        drawRune(x + 40, y + 40, 176, 64, isFlagSet(262144));
        drawRune(x + 60, y + 40, 192, 64, isFlagSet(524288));
        drawRune(x + 80, y + 40, 208, 64, isFlagSet(1048576));
        drawRune(x + 100, y + 40, 224, 64, isFlagSet(2097152));
        drawRune(x + 120, y + 40, 240, 64, isFlagSet(4194304));
        drawRune(x + 140, y + 40, 160, 80, isFlagSet(8388608));
        
        drawFakeButton(guiLeft + xSize + 2, guiTop + 5, "/textures/blocks/redtorch_lit.png", portalModifier.redstoneSetting == 0, 0xFF666666, 0xFFCC0000, 0xFF343434);
        drawFakeButton(guiLeft + xSize + 2, guiTop + 25, "/textures/blocks/redtorch.png", portalModifier.redstoneSetting == 1, 0xFF666666, 0xFFCC0000, 0xFF343434);
        
        drawFakeButton(guiLeft - 20, guiTop + 5, Reference.GUI_LOCATION + "particleButton.png", portalModifier.particles, 0xFFCC0000, 0xFF00CC00, 0xFF343434);
        drawFakeButton(guiLeft - 20, guiTop + 25, Reference.GUI_LOCATION + "soundButton.png", portalModifier.sounds, 0xFFCC0000, 0xFF00CC00, 0xFF343434);
        
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
        drawRect(guiLeft + 8 + padding, guiTop + 64, guiLeft + 16 - width + 8 + padding, guiTop + 64 + 16, 0xFF555555);
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
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, guiLeft + xSize - 24, guiTop + 64);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String pModifier = "Portal Modifier"; // TODO LANGUAGE

        fontRenderer.drawString(pModifier, xSize / 2 - fontRenderer.getStringWidth(pModifier) / 2, -15, 0xFFFFFF);
    }

    @Override
    public void drawScreen(int x, int y, float par3)
    {
        if (!isActive)
        {
            super.drawScreen(x, y, par3);
            int x2 = xSize, y2 = 5;

            if (isPointInRegion(x2, y2, 18, 18, x, y))
            {
                List<String> list = new ArrayList<String>();
                list.add("High");
                list.add(EnumChatFormatting.GRAY + "Activate on a redstone signal."); // TODO LANGUAGE
                drawHoveringText(list, x, y, fontRenderer);
            }

            y2 += 20;

            if (isPointInRegion(x2, y2, 18, 18, x, y))
            {
                List<String> list = new ArrayList<String>();
                list.add("Low");
                list.add(EnumChatFormatting.GRAY + "Activate without a redstone signal."); // TODO LANGUAGE
                drawHoveringText(list, x, y, fontRenderer);
            }
            
            x2 = -20;
            y2 = 5;
            
            if (isPointInRegion(x2, y2, 18, 18, x, y))
            {
                List<String> list = new ArrayList<String>();
                list.add("Particles");
                list.add(EnumChatFormatting.GRAY + (portalModifier.particles ? "Active" : "Inactive")); // TODO LANGUAGE
                drawHoveringText(list, x, y, fontRenderer);
            }
            
            y2 += 20;
            
            if (isPointInRegion(x2, y2, 18, 18, x, y))
            {
                List<String> list = new ArrayList<String>();
                list.add("Sounds");
                list.add(EnumChatFormatting.GRAY + (portalModifier.sounds ? "Active" : "Inactive")); // TODO LANGUAGE
                drawHoveringText(list, x, y, fontRenderer);
            }

            if (isPointInRegion(8, 64, 16, 16, x, y))
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
            else if (isPointInRegion(152, 64, 16, 16, x, y))
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

        //buttonList.add(new GuiButton(1, guiLeft + xSize - 60, guiTop + 5, 60, 20, (portalModifier.particles ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Particles"));
        //buttonList.add(new GuiButton(2, guiLeft + xSize - 60, guiTop + 30, 60, 20, (portalModifier.sounds ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Sounds"));
        buttonList.add(okayButton);

        okayButton.drawButton = isActive;
    }

    @Override
    protected void mouseClicked(int x, int y, int buttonClicked)
    {
        super.mouseClicked(x, y, buttonClicked);
        //textBox.mouseClicked(x, y, buttonClicked);

        if (buttonClicked == 0)
        {
            int x2 = xSize, y2 = 5;

            if (isPointInRegion(x2, y2, 18, 18, x, y))
            {
                portalModifier.redstoneSetting = 0;
                hasInteractedWith = true;
            }

            y2 += 20;

            if (isPointInRegion(x2, y2, 18, 18, x, y))
            {
                portalModifier.redstoneSetting = 1;
                hasInteractedWith = true;
            }
            
            x2 = -20;
            y2 = 5;
            
            if (isPointInRegion(x2, y2, 18, 18, x, y))
            {
                portalModifier.particles = !portalModifier.particles;
                hasInteractedWith = true;
            }
            
            y2 += 20;
            
            if (isPointInRegion(x2, y2, 18, 18, x, y))
            {
                portalModifier.sounds = !portalModifier.sounds;
                hasInteractedWith = true;
            }

            if (isPointInRegion(8, 64, 16, 16, x, y))
            {
                if (portalModifier.thickness < 3)
                {
                    portalModifier.thickness++;
                }
                else
                {
                    portalModifier.thickness = 0;
                }

                hasInteractedWith = true;
            }
            
            // Runes
            x2 = 13;
            y2 = 4;
            clickRune(x, y, x2, y2, 1);
            clickRune(x, y, x2 + 20, y2, 2);
            clickRune(x, y, x2 + 40, y2, 4);
            clickRune(x, y, x2 + 60, y2, 8);
            clickRune(x, y, x2 + 80, y2, 16);
            clickRune(x, y, x2 + 100, y2, 32);
            clickRune(x, y, x2 + 120, y2, 64);
            clickRune(x, y, x2 + 140, y2, 128);
            
            clickRune(x, y, x2, y2 + 20, 256);
            clickRune(x, y, x2 + 20, y2 + 20, 512);
            clickRune(x, y, x2 + 40, y2 + 20, 1024);
            clickRune(x, y, x2 + 60, y2 + 20, 2048);
            clickRune(x, y, x2 + 80, y2 + 20, 4096);
            clickRune(x, y, x2 + 100, y2 + 20, 8192);
            clickRune(x, y, x2 + 120, y2 + 20, 16384);
            clickRune(x, y, x2 + 140, y2 + 20, 32768);
            
            clickRune(x, y, x2, y2 + 40, 65536);
            clickRune(x, y, x2 + 20, y2 + 40, 131072);
            clickRune(x, y, x2 + 40, y2 + 40, 262144);
            clickRune(x, y, x2 + 60, y2 + 40, 524288);
            clickRune(x, y, x2 + 80, y2 + 40, 1048576);
            clickRune(x, y, x2 + 100, y2 + 40, 2097152);
            clickRune(x, y, x2 + 120, y2 + 40, 4194304);
            clickRune(x, y, x2 + 140, y2 + 40, 8388608);
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
}
