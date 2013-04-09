package alz.mods.enhancedportals.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiDialDevice extends GuiScreen
{
    private GuiDialDeviceSlot worldSlotContainer;
    private String screenTitle;
    private List<PortalData> theList;
    private int selectedWorld;
    private int theSelectedEntry;
    private TileEntityDialDevice DialDevice;
    private EntityPlayer Player;

    public GuiDialDevice(EntityPlayer player, TileEntityDialDevice dialDevice)
    {
        super();
        screenTitle = Localizations.getLocalizedString("tile." + Strings.DialDevice_Name + ".name");
        theSelectedEntry = dialDevice.SelectedEntry;
        theList = dialDevice.PortalDataList;
        DialDevice = dialDevice;
        Player = player;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        if (guiButton.id == 1)
        {
            ClientProxy.OpenGuiFromLocal(Player, DialDevice, Reference.GuiIDs.DialDevice + 1);
        }
        else if (guiButton.id == 2)
        {
            if (theList.isEmpty() || theList.size() < selectedWorld || theList.get(selectedWorld) == null)
            {
                theSelectedEntry = 0;
                return;
            }

            if (selectedWorld == getActive())
            {
                theSelectedEntry = 0;
            }

            theList.remove(selectedWorld);
        }
        else if (guiButton.id == 3)
        {
            System.out.println("Edit");
        }
        else if (guiButton.id == 4)
        {
            Player.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        worldSlotContainer.drawScreen(par1, par2, par3);
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 16777215);
        super.drawScreen(par1, par2, par3);
    }

    public int getActive()
    {
        return theSelectedEntry;
    }

    public int getSelectedWorld()
    {
        return selectedWorld;
    }

    public List<PortalData> getSize()
    {
        return theList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        worldSlotContainer = new GuiDialDeviceSlot(this);
        worldSlotContainer.registerScrollButtons(buttonList, 4, 5);

        int middle = width / 2;
        middle -= 75;

        buttonList.add(new GuiButton(1, middle - 80, height - 55, 150, 20, Localizations.getLocalizedString(Strings.GUI_Add)));
        buttonList.add(new GuiButton(2, middle - 80, height - 30, 150, 20, Localizations.getLocalizedString(Strings.GUI_Remove)));
        buttonList.add(new GuiButton(3, middle + 80, height - 55, 150, 20, Localizations.getLocalizedString(Strings.GUI_Edit)));
        buttonList.add(new GuiButton(4, middle + 80, height - 30, 150, 20, Localizations.getLocalizedString(Strings.GUI_Close)));
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == FMLClientHandler.instance().getClient().gameSettings.keyBindInventory.keyCode || par2 == 1)
        {
            Player.closeScreen();
            return;
        }

        super.keyTyped(par1, par2);
    }

    public int onElementSelected(int par1)
    {
        return selectedWorld = par1;
    }

    public void selectWorld(int i)
    {
        theSelectedEntry = i;
    }
}