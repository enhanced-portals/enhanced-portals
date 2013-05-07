package enhancedportals.client.gui;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
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
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture("/mods/enhancedportals/textures/gui/dialDeviceInventory.png"); // TODO TEMPORARY
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
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
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, guiLeft + xSize - 16, guiTop + 54);
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String pModifier = "Portal Modifier", txt = "", frequency = "Frequency:";
        
        if (portalModifier.texture.blockID == -1)
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
        
        fontRenderer.drawString(pModifier, (xSize / 2) - (fontRenderer.getStringWidth(pModifier) / 2), -16, 0xFFFFFF);
        fontRenderer.drawString(frequency, ((xSize - 70) / 2) - (fontRenderer.getStringWidth(frequency) / 2), 10, 0xFFFFFF);
        fontRenderer.drawString(txt, xSize - (20 + fontRenderer.getStringWidth(txt)), 58, 0xFFFFFF);
        fontRenderer.drawString(EnumChatFormatting.AQUA + "Normal", 0, 58, 0xFFFFFF);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        okayButton = new GuiButton(100, width / 2 - 50, height / 2 - 10, 100, 20, "Close");
        
        buttonList.add(new GuiButton(1, guiLeft + xSize - 60, guiTop, 60, 20, (portalModifier.particles ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Particles"));
        buttonList.add(new GuiButton(2, guiLeft + xSize - 60, guiTop + 25, 60, 20, (portalModifier.sounds ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Sounds"));
        buttonList.add(okayButton);
        
        textBox = new GuiTextField(fontRenderer, guiLeft + 2, guiTop + 27, 100, 16);
        
        okayButton.drawButton = isActive;
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
            button.displayString = (portalModifier.particles ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Particles";
        }
        else if (button.id == 2) // sounds
        {
            portalModifier.sounds = !portalModifier.sounds;
            button.displayString = (portalModifier.sounds ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED) + "Sounds";
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
    public void drawScreen(int par1, int par2, float par3)
    {        
        if (!isActive)
        {
            super.drawScreen(par1, par2, par3);
            textBox.drawTextBox();
        }
        else
        {
            String txt = "You cannot change any settings while the Portal Modifier is active.";
            
            drawDefaultBackground();
            fontRenderer.drawString(txt, width / 2 - (fontRenderer.getStringWidth(txt) / 2), guiTop, 0xFFFFFF);
            okayButton.drawButton(mc, 0, 0);
        }
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
    public void updateScreen()
    {
        super.updateScreen();
        textBox.updateCursorCounter();
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        textBox.mouseClicked(par1, par2, par3);
    }
}
