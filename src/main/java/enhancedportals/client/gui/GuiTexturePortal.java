package enhancedportals.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidContainerRegistry;
import enhancedportals.EnhancedPortals;
import enhancedportals.block.BlockFrame;
import enhancedportals.block.BlockPortal;
import enhancedportals.client.gui.button.GuiBetterSlider;
import enhancedportals.client.gui.button.GuiRGBSlider;
import enhancedportals.client.gui.elements.ElementFakeItemSlot;
import enhancedportals.client.gui.elements.ElementScrollPortalIcons;
import enhancedportals.client.gui.tabs.TabColour;
import enhancedportals.client.gui.tabs.TabTip;
import enhancedportals.client.gui.tabs.TabTipSecondary;
import enhancedportals.common.IFakeSlotHandler;
import enhancedportals.inventory.ContainerTexturePortal;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.portal.PortalTextureManager;
import enhancedportals.tileentity.portal.TileController;

public class GuiTexturePortal extends BaseGui implements IFakeSlotHandler
{
    public static final int CONTAINER_SIZE = 92, CONTAINER_WIDTH = 190;
    protected TileController controller;
    protected GuiRGBSlider sliderR, sliderG, sliderB;
    protected GuiButton buttonReset, buttonSave;
    protected int particleFrameType = -1, particleFrame, particleFrameCycle;
    protected int[] particleFrames = new int[] { 0 };

    public GuiTexturePortal(TileController c, EntityPlayer p)
    {
        super(new ContainerTexturePortal(c, p.inventory), CONTAINER_SIZE);
        controller = c;
        xSize = CONTAINER_WIDTH;
        name = "gui.portal";
        texture = new ResourceLocation("enhancedportals", "textures/gui/textures.png");
        leftNudge = 7;
        hasSingleTexture = true;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String s = EnhancedPortals.localize("gui.facade");
        getFontRenderer().drawString(s, xSize - 30 - getFontRenderer().getStringWidth(s), containerSize - 12, 0x404040);

        itemRenderer.renderWithColor = false;
        ItemStack frame = new ItemStack(BlockFrame.instance, 0, 0);
        Color frameColour = new Color(getPTM().getFrameColour()), particleColour = new Color(0x0077D8);
        int particleType = 0;

        if (getPTM() != null)
        {
            frameColour = new Color(getPTM().getFrameColour());
            particleColour = new Color(getPTM().getParticleColour());
            particleType = getPTM().getParticleType();

            if (getPTM().getFrameItem() != null)
            {
                frame = getPTM().getFrameItem();
            }
            
            if (particleFrameType != particleType)
            {
                particleFrameType = particleType;
                particleFrame = 0;
                particleFrameCycle = 0;
                particleFrames = ClientProxy.particleSets.get(getPTM().getParticleType()).frames;
            }
        }

        GL11.glColor3f(frameColour.getRed() / 255F, frameColour.getGreen() / 255F, frameColour.getBlue() / 255F);

        if (getPTM().hasCustomFrameTexture())
        {
            drawIconNoReset(ClientProxy.customFrameTextures.get(getPTM().getCustomFrameTexture()), 9, containerSize - 16, 0);
        }
        else
        {
            drawItemStack(frame, 9, containerSize - 16);
        }
        
        GL11.glColor3f(particleColour.getRed() / 255F, particleColour.getGreen() / 255F, particleColour.getBlue() / 255F);
        getTextureManager().bindTexture(new ResourceLocation("textures/particle/particles.png"));
        drawTexturedModalRect(30, containerSize - 16, particleFrames[particleFrame] % 16 * 16, particleFrames[particleFrame] / 16 * 16, 16, 16);
        
        GL11.glColor3f(1f, 1f, 1f);
        super.drawGuiContainerForegroundLayer(par1, par2);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == buttonSave.id || button.id == buttonReset.id)
        {
            if (button.id == buttonSave.id)
            {
                int hex = Integer.parseInt(String.format("%02x%02x%02x", sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), 16);
                getPTM().setPortalColour(hex);
            }
            else if (button.id == buttonReset.id)
            {
                int colour = 0xffffff;
                getPTM().setPortalColour(colour);

                Color c = new Color(colour);
                sliderR.sliderValue = c.getRed() / 255f;
                sliderG.sliderValue = c.getGreen() / 255f;
                sliderB.sliderValue = c.getBlue() / 255f;
            }

            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("colour", Integer.parseInt(String.format("%02x%02x%02x", sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), 16));
            EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
        }
        else if (button.id == 500)
        {
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(controller, GuiHandler.TEXTURE_A));
        }
        else if (button.id == 501)
        {
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(controller, GuiHandler.TEXTURE_C));
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();

        Color c = new Color(getPTM().getPortalColour());
        sliderR = new GuiRGBSlider(100, guiLeft + xSize + 4, guiTop + 25, EnhancedPortals.localize("gui.red"), c.getRed() / 255f, 105);
        sliderG = new GuiRGBSlider(101, guiLeft + xSize + 4, guiTop + 46, EnhancedPortals.localize("gui.green"), c.getGreen() / 255f, 105);
        sliderB = new GuiRGBSlider(102, guiLeft + xSize + 4, guiTop + 67, EnhancedPortals.localize("gui.blue"), c.getBlue() / 255f, 105);

        buttonList.add(sliderR);
        buttonList.add(sliderG);
        buttonList.add(sliderB);

        buttonSave = new GuiButton(110, guiLeft + xSize + 4, guiTop + 88, 53, 20, EnhancedPortals.localize("gui.save"));
        buttonReset = new GuiButton(111, guiLeft + xSize + 57, guiTop + 88, 53, 20, EnhancedPortals.localize("gui.reset"));

        buttonList.add(buttonSave);
        buttonList.add(buttonReset);

        buttonList.add(new GuiButton(500, guiLeft + 7, guiTop + containerSize - 18, 20, 20, ""));
        buttonList.add(new GuiButton(501, guiLeft + 28, guiTop + containerSize - 18, 20, 20, ""));

        addTab(new TabColour(this, sliderR, sliderG, sliderB, buttonSave, buttonReset));
        addTab(new TabTip(this, "colourTip"));
        addTab(new TabTipSecondary(this, "portalCustomTexture"));
        addElement(new ElementScrollPortalIcons(this, 7, 17, texture));
        addElement(new ElementFakeItemSlot(this, xSize - 24, containerSize - 16, getPTM().getPortalItem()));
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        super.mouseMovedOrUp(par1, par2, par3);

        if (par3 == 0)
        {
            for (Object o : buttonList)
            {
                if (o instanceof GuiBetterSlider)
                {
                    GuiBetterSlider slider = (GuiBetterSlider) o;
                    slider.mouseReleased(par1, par2);
                }
            }
        }
    }

    public void iconSelected(int icon)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("custom", icon);
        EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
    }

    @Override
    public void onItemChanged(ItemStack newItem)
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (newItem != null)
        {
            newItem.writeToNBT(tag);
        }
        else
        {
            tag.setBoolean("removeItem", true);
        }

        EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
    }

    @Override
    public boolean isItemValid(ItemStack s)
    {
        if (s == null)
        {
            return true;
        }

        if (FluidContainerRegistry.isFilledContainer(s))
        {
            return true;
        }

        Block b = Block.getBlockFromItem(s.getItem());

        if (b == Blocks.air)
        {
            return false;
        }

        return true;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        if (particleFrameCycle >= 20)
        {
            particleFrame++;
            particleFrameCycle = 0;
            
            if (particleFrame >= particleFrames.length)
            {
                particleFrame = 0;
            }
        }
        
        particleFrameCycle++;
    }
    
    public PortalTextureManager getPTM()
    {
        return controller.activeTextureData;
    }
}
