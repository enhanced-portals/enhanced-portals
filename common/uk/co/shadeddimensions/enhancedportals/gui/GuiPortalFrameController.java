package uk.co.shadeddimensions.enhancedportals.gui;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameController;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketGuiRequest;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiPortalFrameController extends GuiEnhancedPortals
{
    class InformationLedger extends Ledger
    {
        int headerColour = 0xe1c92f;
        int textColour = 0x000000;

        public InformationLedger()
        {
            overlayColor = 0x9b2a7e;
            maxHeight = 60;
        }

        @Override
        public void draw(int x, int y)
        {
            drawBackground(x, y);

            if (isFullyOpened())
            {
                fontRenderer.drawString("Control Info.", x + 25, y + 9, headerColour);
                fontRenderer.drawString(String.format("Frames: %s", controller.getAttachedFrames() - controller.getAttachedFrameRedstone()), x + 5, y + 25, textColour);
                fontRenderer.drawString(String.format("RS Controllers: %s", controller.getAttachedFrameRedstone()), x + 5, y + 35, textColour);
                fontRenderer.drawString(String.format("Portals: %s", controller.getAttachedPortals()), x + 5, y + 45, textColour);
            }

            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(CommonProxy.blockFrame), x + 3, y + 4);
        }

        @Override
        public ArrayList<String> getTooltip()
        {
            ArrayList<String> strList = new ArrayList<String>();

            if (!isFullyOpened())
            {
                strList.add("Control Information");
            }

            return strList;
        }
    }

    TilePortalFrameController controller;
    EntityPlayer player;

    public GuiPortalFrameController(EntityPlayer play, TilePortalFrameController tile)
    {
        super(new ContainerPortalFrameController(tile), tile);

        controller = tile;
        player = play;
        ySize -= 75;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawBorderedRectangle(8, 8, 16, 16, 0x00ffffff, 0xff404040, true);
        drawBorderedRectangle(28, 8, 16, 16, 0x00ffffff, 0xff404040, true);
        drawBorderedRectangle(8, 28, 16, 16, 0x00ffffff, 0xff404040, true);
        drawBorderedRectangle(28, 28, 16, 16, 0x00ffffff, 0x44404040, true);

        drawColouredItemStack(9, 9, controller.portalTexture.TextureColour, controller.portalTexture.getItemStack(), true);
        drawColouredItemStack(9, 29, controller.frameTexture.TextureColour, controller.frameTexture.getItemStack(), true);

        drawParticle(29, 9, controller.portalTexture.ParticleColour, controller.portalTexture.getStaticParticleIndex(), true);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow("Portal Controller", xSize / 2 - fontRenderer.getStringWidth("Portal Controller") / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Portal Texture", 53, 13, 0x404040);
        fontRenderer.drawString("Frame Texture", 53, 33, 0x404040);
        fontRenderer.drawString("Network", 8, 53, 0x404040);
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    protected void initLedgers(IInventory inventory)
    {
        ledgerManager.add(new InformationLedger());
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        super.mouseClicked(par1, par2, mouseButton);

        if (par1 >= guiLeft + 8 && par1 <= guiLeft + 45)
        {
            if (par2 >= guiTop + 8 && par2 <= guiTop + 25)
            {
                PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiRequest(CommonProxy.GuiIds.PORTAL_CONTROLLER_PORTAL_TEXTURE)));
            }
            else if (par2 >= guiTop + 28 && par2 <= guiTop + 28 + 17)
            {
                PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiRequest(CommonProxy.GuiIds.PORTAL_CONTROLLER_FRAME_TEXTURE)));
            }
        }
    }
}
