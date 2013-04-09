package alz.mods.enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.common.ContainerDialDeviceAdd;
import alz.mods.enhancedportals.item.ItemScroll;
import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.teleportation.TeleportData;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiDialDeviceAdd extends GuiContainer
{
    private TileEntityDialDevice DialDevice;
    private EntityPlayer Player;

    private GuiImprovedTextBox[] textBoxes;
    private String moanA, moanB;

    public GuiDialDeviceAdd(EntityPlayer player, TileEntityDialDevice dialDevice)
    {
        super(new ContainerDialDeviceAdd(player.inventory, dialDevice));
        Player = player;
        DialDevice = dialDevice;
        textBoxes = new GuiImprovedTextBox[10];
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        if (guiButton.id == 1)
        {
            /*ItemStack stack = inventorySlots.getSlot(0).getStack();
            //ItemStack stack2 = inventorySlots.getSlot(1).getStack();
            TeleportData data = ((ItemScroll)stack.getItem()).getLocationData(stack);
            
            PortalData portalData = new PortalData();
            portalData.DisplayName = textBoxes[0].getText();
            portalData.Texture = PortalTexture.getPortalTexture(0);
            portalData.TeleportData = new TeleportData(data.getX(), data.getY(), data.getZ(), data.getDimension());
            
            DialDevice.PortalDataList.add(portalData);
            // send packet to add portal data and remove the items*/
            ClientProxy.SendNewPortalData(DialDevice, textBoxes[0].getText());
        }

        ClientProxy.OpenGuiFromLocal(Player, DialDevice, Reference.GuiIDs.DialDevice);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        drawBackground(0);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.File.GuiTextureDirectory + "dialDeviceInventory.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String scrollString = Localizations.getLocalizedString(Strings.GUI_DialDevice_Scroll), modifierString = Localizations.getLocalizedString(Strings.GUI_DialDevice_Modifier);

        drawString(mc.fontRenderer, scrollString, width / 2 - guiLeft - mc.fontRenderer.getStringWidth(scrollString) - 35, 24, 0xFFFFFF);
        drawString(mc.fontRenderer, modifierString, width / 2 - guiLeft - mc.fontRenderer.getStringWidth(modifierString) + 67, 24, 0xFFFFFF);

        drawString(mc.fontRenderer, moanA, width / 2 - guiLeft - mc.fontRenderer.getStringWidth(moanA) / 2, 50, 0xDD0000);
        drawString(mc.fontRenderer, moanB, width / 2 - guiLeft - mc.fontRenderer.getStringWidth(moanB) / 2, 60, 0x990000);

        for (GuiImprovedTextBox textBox : textBoxes)
        {
            if (textBox == null)
            {
                continue;
            }

            textBox.drawTextBox();
        }
    }

    private boolean getAcceptButtonState()
    {
        return ((GuiButton) buttonList.get(0)).enabled;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        textBoxes[0] = new GuiImprovedTextBox(mc.fontRenderer, guiLeft, guiTop, width - guiLeft * 2, 16, 35, false, true, 0, 0, Localizations.getLocalizedString(Strings.GUI_Name));
        textBoxes[1] = new GuiImprovedTextBox(mc.fontRenderer, guiLeft, guiTop + 20, 16, 16, 35, false, false, 0, 20);
        textBoxes[2] = new GuiImprovedTextBox(mc.fontRenderer, width - guiLeft * 2 - 16, guiTop + 20, 16, 16, 35, false, false, width - guiLeft * 2 - 16, 20);

        buttonList.add(new GuiButton(1, guiLeft + 102, height - 30, 75, 20, Localizations.getLocalizedString(Strings.GUI_Accept)));
        buttonList.add(new GuiButton(2, guiLeft, height - 30, 75, 20, Localizations.getLocalizedString(Strings.GUI_Cancel)));

        moanA = Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid2a);
        moanB = Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid2b);
        setAcceptButtonState(false);
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        for (GuiImprovedTextBox textBox : textBoxes)
        {
            if (textBox == null)
            {
                continue;
            }

            if (textBox.isFocused())
            {
                textBox.textboxKeyTyped(par1, par2);
                return;
            }
        }

        if (par2 == FMLClientHandler.instance().getClient().gameSettings.keyBindInventory.keyCode || par2 == 1)
        {
            ClientProxy.OpenGuiFromLocal(Player, DialDevice, Reference.GuiIDs.DialDevice);
            return;
        }

        super.keyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        for (GuiImprovedTextBox textBox : textBoxes)
        {
            if (textBox == null)
            {
                continue;
            }

            textBox.mouseClicked(par1, par2, par3);
        }
    }

    private void setAcceptButtonState(boolean state)
    {
        ((GuiButton) buttonList.get(0)).enabled = state;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        for (GuiImprovedTextBox textBox : textBoxes)
        {
            if (textBox == null)
            {
                continue;
            }

            textBox.updateCursorCounter();
        }

        if (!inventorySlots.getSlot(0).getHasStack() && getAcceptButtonState())
        {
            moanA = Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid2a);
            moanB = Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid2b);
            setAcceptButtonState(false);
        }
        else if (inventorySlots.getSlot(0).getHasStack() && !getAcceptButtonState())
        {
            ItemStack stack = inventorySlots.getSlot(0).getStack();

            if (stack.getItem() instanceof ItemScroll)
            {
                ItemScroll scroll = (ItemScroll) stack.getItem();
                TeleportData data = scroll.getLocationData(stack);

                if (!data.linksToModifier())
                {
                    moanA = Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid1a);
                    moanB = Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid1b);
                }
                else
                {
                    moanA = "";
                    moanB = "";
                    setAcceptButtonState(true);
                    return;
                }
            }

            setAcceptButtonState(false);
        }
        else if (!inventorySlots.getSlot(0).getHasStack() && !getAcceptButtonState() && moanA != Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid2a))
        {
            moanA = Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid2a);
            moanB = Localizations.getLocalizedString(Strings.GUI_DialDevice_Invalid2b);
        }
    }
}
