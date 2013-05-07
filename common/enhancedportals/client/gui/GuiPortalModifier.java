package enhancedportals.client.gui;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.container.ContainerPortalModifier;
import enhancedportals.lib.BlockIds;
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
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture("/mods/enhancedportals/textures/gui/dialDeviceInventory.png"); // TODO TEMPORARY CALL
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
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, guiLeft + 152, guiTop + 54);
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {        
        fontRenderer.drawString("Portal Modifier", 8, -16, 0xFFFFFF);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        okayButton = new GuiButton(100, width / 2 - 50, height / 2 - 10, 100, 20, "Close");
        
        buttonList.add(new GuiButton(1, guiLeft, guiTop, 50, 20, "Test"));
        buttonList.add(okayButton);
        
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
        }
        else
        {
            String txt = "You cannot change any settings while the Portal Modifier is active.";
            
            drawDefaultBackground();
            fontRenderer.drawString(txt, width / 2 - (fontRenderer.getStringWidth(txt) / 2), guiTop, 0xFFFFFF);
            okayButton.drawButton(mc, 0, 0);
        }
    }
}
