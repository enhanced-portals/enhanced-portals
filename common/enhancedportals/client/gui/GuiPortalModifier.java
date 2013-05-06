package enhancedportals.client.gui;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import enhancedportals.container.ContainerPortalModifier;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiPortalModifier extends GuiContainer
{
    TileEntityPortalModifier portalModifier;
    
    public GuiPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
    {
        super(new ContainerPortalModifier(player, modifier));
        portalModifier = modifier;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture("/mods/enhancedportals/textures/gui/dialDeviceInventory.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        if (portalModifier.texture.blockID != -1)
        {
            ItemStack itemstack = new ItemStack(Block.blocksList[portalModifier.texture.blockID], 1, portalModifier.texture.metaData);
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, guiLeft + 155, guiTop + 55);
        }
        else if (portalModifier.texture.colour != null)
        {
            ItemStack itemstack = new ItemStack(Block.blocksList[BlockIds.DummyPortal], 1, portalModifier.texture.colour.ordinal());
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, guiLeft + 155, guiTop + 55);
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRenderer.drawString("Portal Modifier", 8, -16, 4210752);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        buttonList.add(new GuiButton(1, guiLeft, guiTop, 50, 20, "Test"));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        
    }
}
