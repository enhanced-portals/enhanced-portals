package uk.co.shadeddimensions.enhancedportals.client.gui;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;

public class GuiPortalController extends GuiContainer
{
    EntityPlayer player;
    TilePortalController tile;

    static GuiRectangle[] rectangles = new GuiRectangle[7];
    static boolean[] disabled = new boolean[7];

    static
    {
        int offset = 90;

        rectangles[0] = new GuiRectangle(10, offset + 10, "U");
        rectangles[1] = new GuiRectangle(10, offset + 50, "D");

        rectangles[2] = new GuiRectangle(50, offset + 10, "N");
        rectangles[3] = new GuiRectangle(30, offset + 30, "W");
        rectangles[4] = new GuiRectangle(70, offset + 30, "E");
        rectangles[5] = new GuiRectangle(50, offset + 50, "S");

        rectangles[6] = new GuiRectangle(50, offset + 30, "A");

        for (int i = 0; i < disabled.length; i++)
        {
            disabled[i] = false;
        }
    }

    public GuiPortalController(EntityPlayer player, TilePortalController frame)
    {
        super(new ContainerPortalController(player.inventory, frame));
        this.player = player;
        tile = frame;

        disabled[0] = !(frame.worldObj.isAirBlock(frame.xCoord, frame.yCoord + 1, frame.zCoord) && frame.worldObj.isAirBlock(frame.xCoord, frame.yCoord + 2, frame.zCoord));
        disabled[1] = !(frame.worldObj.isAirBlock(frame.xCoord, frame.yCoord - 1, frame.zCoord) && frame.worldObj.isAirBlock(frame.xCoord, frame.yCoord - 2, frame.zCoord));

        disabled[2] = !(frame.worldObj.isAirBlock(frame.xCoord, frame.yCoord, frame.zCoord - 1) && frame.worldObj.isAirBlock(frame.xCoord, frame.yCoord + 1, frame.zCoord - 1));
        disabled[5] = !(frame.worldObj.isAirBlock(frame.xCoord, frame.yCoord, frame.zCoord + 1) && frame.worldObj.isAirBlock(frame.xCoord, frame.yCoord + 1, frame.zCoord + 1));

        disabled[4] = !(frame.worldObj.isAirBlock(frame.xCoord + 1, frame.yCoord, frame.zCoord) && frame.worldObj.isAirBlock(frame.xCoord + 1, frame.yCoord + 1, frame.zCoord));
        disabled[3] = !(frame.worldObj.isAirBlock(frame.xCoord - 1, frame.yCoord, frame.zCoord) && frame.worldObj.isAirBlock(frame.xCoord - 1, frame.yCoord + 1, frame.zCoord));

        boolean foundOne = false;
        for (int i = 0; i < 6; i++)
        {
            if (!disabled[i])
            {
                foundOne = true;
                break;
            }
        }

        rectangles[6].selected = foundOne;
        disabled[6] = !foundOne;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        super.actionPerformed(par1GuiButton);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiButton(1, guiLeft + xSize / 2 - 70, guiTop + 7, 140, 20, "Portal Texture           "));
        buttonList.add(new GuiButton(2, guiLeft + xSize / 2 - 70, guiTop + 30, 140, 20, "Particle Texture         "));
        buttonList.add(new GuiButton(3, guiLeft + xSize / 2 - 70, guiTop + 53, 140, 20, "Teleport Data             "));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        for (int k = 0; k < rectangles.length; k++)
        {
            rectangles[k].draw(this, i, j, disabled[k]);
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        GL11.glDisable(GL11.GL_LIGHTING);

        int x = guiLeft + xSize / 2 - 60 + 100, y = guiTop + 9;
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(Block.portal), x, y); // TODO Dynamic
        // drawRect(x, y, x + 16, y + 16, 0xFFFFFFFF);
        y += 23;
        GL11.glColor4f(1f, 0f, 0f, 1f); // TODO Change to actual colour
        mc.renderEngine.func_110577_a(new ResourceLocation("textures/particle/particles.png")); // TODO Dynamic
        drawTexturedModalRect(x, y, 112, 0, 16, 16); // TODO ^
        GL11.glColor4f(1f, 1f, 1f, 1f);
        // drawRect(x, y, x + 16, y + 16, 0xFFFFBBCC);
        y += 23;
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameController.png"));
        drawTexturedModalRect(x, y, 0, ySize, 16, 16);
        fontRenderer.drawString("A", x + 16 / 2 - fontRenderer.getStringWidth("A") / 2, y + 16 / 2 - 4, 0x909090); // TODO: Dynamic
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRenderer.drawStringWithShadow("Portal Controller", xSize / 2 - fontRenderer.getStringWidth("Portal Controller") / 2, -10, 0xFFFFFF);

        fontRenderer.drawString("Teleport Location", 8, 85, 0x404040);
        fontRenderer.drawString("Facing", 120, 85, 0x404040);

        for (GuiRectangle rect : rectangles)
        {
            rect.drawForeground(this, par1, par2);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        for (int i = 0; i < rectangles.length; i++)
        {
            GuiRectangle rect = rectangles[i];

            if (rect.inRectangle(this, par1, par2) && par3 == 0 && disabled[i] == false)
            {
                for (GuiRectangle r : rectangles)
                {
                    r.selected = false;
                }

                rect.selected = true;
            }
        }
    }

    protected int getLeft()
    {
        return guiLeft;
    }

    protected int getTop()
    {
        return guiTop;
    }

    protected int getYSize()
    {
        return ySize;
    }
}
