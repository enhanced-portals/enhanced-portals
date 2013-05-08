package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
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
    GuiTextField textBox;

    public GuiPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
    {
        super(new ContainerPortalModifier(player, modifier));
        portalModifier = modifier;
        isActive = portalModifier.isActive();

        xSize += 3;
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

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.GUI_LOCATION + "portalModifier.png");
        int x = (width - xSize) / 2 - 12;
        int y = (height - ySize) / 2 - 4;
        drawTexturedModalRect(x, y, 0, 0, xSize + 22, ySize + 4);

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
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, guiLeft + xSize - 27, guiTop + 64);
        }

        // Draw redstone control icons
        int x2 = guiLeft + xSize + 13, y2 = guiTop + 5, y3 = y2, colour = 0xFF666666, activeColour = 0xFFCC0000, backColour = 0xFF343434;

        // 1
        drawRect(x2, y2, x2 + 16, y2 + 16, backColour);
        drawRect(x2, y2, x2 + 16, y2 - 1, portalModifier.redstoneSetting == 0 ? activeColour : colour);
        drawRect(x2, y2 + 16, x2 + 16, y2 - 1 + 16, portalModifier.redstoneSetting == 0 ? activeColour : colour);
        drawRect(x2, y2, x2 + 1, y2 - 1 + 16, portalModifier.redstoneSetting == 0 ? activeColour : colour);
        drawRect(x2 + 16, y2 - 1, x2 + 17, y2 + 16, portalModifier.redstoneSetting == 0 ? activeColour : colour);

        // 2  
        y2 += 18;
        drawRect(x2, y2, x2 + 16, y2 + 16, backColour);
        drawRect(x2, y2, x2 + 16, y2 - 1, portalModifier.redstoneSetting == 1 ? activeColour : colour);
        drawRect(x2, y2 + 16, x2 + 16, y2 - 1 + 16, portalModifier.redstoneSetting == 1 ? activeColour : colour);
        drawRect(x2, y2, x2 + 1, y2 - 1 + 16, portalModifier.redstoneSetting == 1 ? activeColour : colour);
        drawRect(x2 + 16, y2 - 1, x2 + 17, y2 + 16, portalModifier.redstoneSetting == 1 ? activeColour : colour);

        drawRect(0, 0, 0, 0, 0xFFFFFFFF); // Otherwise the icons seem to change colour

        // 1
        mc.renderEngine.bindTexture("/textures/blocks/redtorch_lit.png");
        drawTexturedRect(x2, y3 - 1, 0, 0, 16, 16);

        // 2
        y3 += 18;
        mc.renderEngine.bindTexture("/textures/blocks/redtorch.png");
        drawTexturedRect(x2, y3 - 1, 0, 0, 16, 16);

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
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String pModifier = "Portal Modifier", frequency = "Frequency:", thickness = "Thickness", camo = "Camouflage"; // TODO LANGUAGE

        fontRenderer.drawString(pModifier, xSize / 2 - fontRenderer.getStringWidth(pModifier) / 2, -15, 0xFFFFFF);
        fontRenderer.drawString(frequency, (xSize - 70) / 2 - fontRenderer.getStringWidth(frequency) / 2, 15, 4210752);
        fontRenderer.drawString(thickness, 27, 68, 4210752);
        fontRenderer.drawString(camo, xSize - 58 - fontRenderer.getStringWidth(camo) / 2, 68, 4210752);
    }

    @Override
    public void drawScreen(int x, int y, float par3)
    {
        if (!isActive)
        {
            super.drawScreen(x, y, par3);
            textBox.drawTextBox();

            int x2 = xSize + 13, y2 = 5;

            if (isPointInRegion(x2, y2, 16, 16, x, y))
            {
                List<String> list = new ArrayList<String>();
                list.add("High");
                list.add(EnumChatFormatting.GRAY + "Activate on a redstone signal."); // TODO LANGUAGE
                drawHoveringText(list, x, y, fontRenderer);
            }

            y2 += 18;

            if (isPointInRegion(x2, y2, 16, 16, x, y))
            {
                List<String> list = new ArrayList<String>();
                list.add("Low");
                list.add(EnumChatFormatting.GRAY + "Activate without a redstone signal."); // TODO LANGUAGE
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
                list.add(thickness);
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
                list.add(txt);
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

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        okayButton = new GuiButton(100, width / 2 - 50, height / 2 - 10, 100, 20, "Close");

        buttonList.add(new GuiButton(1, guiLeft + xSize - 60, guiTop + 5, 60, 20, (portalModifier.particles ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Particles"));
        buttonList.add(new GuiButton(2, guiLeft + xSize - 60, guiTop + 30, 60, 20, (portalModifier.sounds ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Sounds"));
        buttonList.add(okayButton);

        textBox = new GuiTextField(fontRenderer, guiLeft + 2, guiTop + 32, 100, 16);

        okayButton.drawButton = isActive;
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (!textBox.isFocused() || par2 == FMLClientHandler.instance().getClient().gameSettings.keyBindInventory.keyCode || par2 == 1)
        {
            super.keyTyped(par1, par2);
            return;
        }

        if (!Character.isDigit(par1) && par2 != 14 && par2 != 211 && par2 != 203 && par2 != 205)
        {
            return; // We only want integers, leaving full manipulation intact
        }

        textBox.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int x, int y, int buttonClicked)
    {
        super.mouseClicked(x, y, buttonClicked);
        textBox.mouseClicked(x, y, buttonClicked);

        if (buttonClicked == 0)
        {
            int x2 = xSize + 13, y2 = 5;

            if (isPointInRegion(x2, y2, 16, 16, x, y))
            {
                portalModifier.redstoneSetting = 0;
                hasInteractedWith = true;
            }

            y2 += 18;

            if (isPointInRegion(x2, y2, 16, 16, x, y))
            {
                portalModifier.redstoneSetting = 1;
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

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        textBox.updateCursorCounter();
    }
}
