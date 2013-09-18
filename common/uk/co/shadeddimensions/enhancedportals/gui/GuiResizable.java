package uk.co.shadeddimensions.enhancedportals.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.enhancedportals.container.ContainerEnhancedPortals;

public class GuiResizable extends GuiEnhancedPortals
{
    static final int RESIZE_RATE = 1;
    int MAX_HEIGHT, MAX_WIDTH, MIN_HEIGHT, MIN_WIDTH, CURRENT_HEIGHT, CURRENT_WIDTH;
    boolean expanded, expanding, isChanging;

    public GuiResizable(ContainerEnhancedPortals container, IInventory inventory)
    {
        super(container, inventory);
        
        MAX_HEIGHT = MIN_HEIGHT = CURRENT_HEIGHT = ySize;
        MAX_WIDTH = MIN_WIDTH = CURRENT_WIDTH = xSize;
        expanded = isChanging = false;
        expanding = true;
        
        ySize = MAX_HEIGHT;
        xSize = MAX_WIDTH;
    }
    
    public GuiResizable(ContainerEnhancedPortals container, IInventory inventory, int width, int height)
    {
        super(container, inventory);
        
        MAX_HEIGHT = MIN_HEIGHT = CURRENT_HEIGHT = height;
        MAX_WIDTH = MIN_WIDTH = CURRENT_WIDTH = width;
        expanded = isChanging = false;
        expanding = true;
        
        ySize = MAX_HEIGHT;
        xSize = MAX_WIDTH;
    }
    
    public GuiResizable(ContainerEnhancedPortals container, IInventory inventory, int width, int height, int maxWidth, int maxHeight)
    {
        super(container, inventory);
                
        MIN_HEIGHT = CURRENT_HEIGHT = height;
        MIN_WIDTH = CURRENT_WIDTH = width;
        MAX_WIDTH = maxWidth;
        MAX_HEIGHT = maxHeight;
        expanded = isChanging = false;
        expanding = true;
        
        ySize = MAX_HEIGHT;
        xSize = MAX_WIDTH;
    }

    protected void toggleState()
    {
        if (!isChanging)
        {
            isChanging = true;
    
            if (expanded)
            {
                expanded = false;
                onShrinkGui(); // This needs to be called before it's started shrinking - otherwise you'll have floating GUI elements
            }
        }
    }
    
    protected void onExpandGui()
    {
        
    }
    
    protected void onShrinkGui()
    {
        
    }
    
    protected void update()
    {
        if (isChanging)
        {
            if (expanding)
            {
                if (CURRENT_WIDTH < MAX_WIDTH && CURRENT_HEIGHT < MAX_HEIGHT)
                {
                    CURRENT_WIDTH += RESIZE_RATE;
                    CURRENT_HEIGHT += RESIZE_RATE;
                }
                else if (CURRENT_WIDTH < MAX_WIDTH)
                {
                    CURRENT_WIDTH += RESIZE_RATE;
                }
                else if (CURRENT_HEIGHT < MAX_HEIGHT)
                {
                    CURRENT_HEIGHT += RESIZE_RATE;
                }
                else
                {
                    expanding = false;
                    expanded = true;
                    isChanging = false;
                    onExpandGui();
                }
            }
            else
            {
                if (CURRENT_WIDTH > MIN_WIDTH && CURRENT_HEIGHT > MIN_HEIGHT)
                {
                    CURRENT_WIDTH -= RESIZE_RATE;
                    CURRENT_HEIGHT -= RESIZE_RATE;
                }
                else if (CURRENT_WIDTH > MIN_WIDTH)
                {
                    CURRENT_WIDTH -= RESIZE_RATE;
                }
                else if (CURRENT_HEIGHT > MIN_HEIGHT)
                {
                    CURRENT_HEIGHT -= RESIZE_RATE;
                }
                else
                {
                    expanding = true;
                    isChanging = false;
                }
            }
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/sizableGui.png"));
        GL11.glColor4f(1f, 1f, 1f, 1f);
        
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, CURRENT_WIDTH - 4, CURRENT_HEIGHT - 4); // Top, TR, Left, Centre
        drawTexturedModalRect(guiLeft + 4, guiTop + CURRENT_HEIGHT - 4, 4, 256 - 4, CURRENT_WIDTH - 8, 4); // Bottom
        drawTexturedModalRect(guiLeft + CURRENT_WIDTH - 4, guiTop + 4, 256 - 4, 4, 4, CURRENT_HEIGHT - 8); // Right
        drawTexturedModalRect(guiLeft + CURRENT_WIDTH - 4, guiTop, 256 - 4, 0, 4, 4); // Top Right
        drawTexturedModalRect(guiLeft, guiTop + CURRENT_HEIGHT - 4, 0, 256 - 4, 4, 4); // Bottom Left
        drawTexturedModalRect(guiLeft + CURRENT_WIDTH - 4, guiTop + CURRENT_HEIGHT - 4, 256 - 4, 256 - 4, 4, 4); // Bottom Right
        
        update();
    }
}
