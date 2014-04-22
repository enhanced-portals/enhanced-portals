package uk.co.shadeddimensions.ep3.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.container.ContainerDefault;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;
import uk.co.shadeddimensions.library.gui.element.ElementBase;
import uk.co.shadeddimensions.library.gui.tab.TabBase;
import uk.co.shadeddimensions.library.util.GuiUtils;
import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;

public abstract class GuiDefault extends GuiContainer implements INEIGuiHandler
{
    protected static final ResourceLocation playerInventoryTexture = new ResourceLocation("enhancedportals", "textures/gui/playerInventory.png"), resizableInterfaceTexture = new ResourceLocation("enhancedportals", "textures/gui/resizableInterace.png");
    public static final int defaultContainerSize = 144, playerInventorySize = 90, bufferSpace = 2, defaultGuiSize = defaultContainerSize + playerInventorySize + bufferSpace;
    
    protected int containerSize = defaultContainerSize, guiSize = defaultGuiSize, leftNudge = 0, mouseX = 0, mouseY = 0;
    protected ResourceLocation texture;
    protected String name;
    
    protected ArrayList<TabBase> tabs = new ArrayList<TabBase>();
    protected ArrayList<ElementBase> elements = new ArrayList<ElementBase>();
    
    public GuiDefault(TileEP tile, EntityPlayer player, ResourceLocation t)
    {
        super(new ContainerDefault(tile, player));
        ySize = defaultGuiSize;
        texture = t;
    }
    
    public GuiDefault(TileEP tile, EntityPlayer player, int containerheight)
    {
        super(new ContainerDefault(tile, player, containerheight + playerInventorySize + bufferSpace, 0));
        containerSize = containerheight;
        guiSize = containerSize + playerInventorySize + bufferSpace;
        ySize = guiSize;
    }
    
    public GuiDefault(TileEP tile, EntityPlayer player, ResourceLocation t, int containerheight)
    {
        super(new ContainerDefault(tile, player, containerheight + playerInventorySize + bufferSpace, 0));
        containerSize = containerheight;
        guiSize = containerSize + playerInventorySize + bufferSpace;
        ySize = guiSize;
        texture = t;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        tabs.clear();
        elements.clear();
        buttonList.clear();
    }
    
    public int getMouseX()
    {
        return mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }
    
    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    public int getGuiLeft()
    {
        return guiLeft;
    }

    public int getGuiTop()
    {
        return guiTop;
    }
    
    public Minecraft getMinecraft()
    {
        return mc;
    }
    
    public void drawBackgroundTexture()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        getMinecraft().renderEngine.bindTexture(playerInventoryTexture);
        drawTexturedModalRect(guiLeft + leftNudge, guiTop + containerSize + bufferSpace, 0, 0, xSize, playerInventorySize);
        
        if (texture != null)
        {
            getMinecraft().renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, containerSize);
        }
        else
        {
            getMinecraft().renderEngine.bindTexture(resizableInterfaceTexture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize - 4, containerSize - 4);
            drawTexturedModalRect(guiLeft + xSize - 4, guiTop, 252, 0, 4, containerSize - 4);
            drawTexturedModalRect(guiLeft, guiTop + containerSize - 4, 0, 252, xSize - 4, 4);
            drawTexturedModalRect(guiLeft + xSize - 4, guiTop + containerSize - 4, 252, 252, 4, 4);
        }
    }
    
    @Override
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility)
    {
        return null;
    }

    @Override
    public int getItemSpawnSlot(GuiContainer gui, ItemStack item)
    {
        return 0;
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui)
    {
        return null;
    }

    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button)
    {
        return false;
    }

    @Override
    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h)
    {
        return false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        drawBackgroundTexture();
    }
    
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        if (name != null && name.length() > 0)
        {
            fontRenderer.drawString(StatCollector.translateToLocal(name), (xSize - getFontRenderer().getStringWidth(StatCollector.translateToLocal(name))) / 2, 6, 0x404040);
        }
    }
}
