package enhancedportals.client.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementLargeTextBox;
import enhancedportals.inventory.ContainerProgrammableInterface;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.tileentity.portal.TileProgrammableInterface;

public class GuiProgrammableInterface extends BaseGui
{
    public static int CONTAINER_SIZE = 200, CONTAINER_WIDTH = 256;
    TileProgrammableInterface program;
    ElementLargeTextBox textBox;

    public GuiProgrammableInterface(TileProgrammableInterface pr, EntityPlayer p)
    {
        super(new ContainerProgrammableInterface(pr, p.inventory), CONTAINER_SIZE);
        xSize = CONTAINER_WIDTH;
        setHidePlayerInventory();
        name = "gui.programmableInterface";
        texture = new ResourceLocation("enhancedportals", "textures/gui/program_interface.png");
        program = pr;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        getFontRenderer().drawSplitString(program.scriptData, 9, 19, 1000, 0xFFFFFF);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(0, guiLeft + 78, guiTop + CONTAINER_SIZE - 27, xSize - 84, 20, EnhancedPortals.localize("gui.compile")));
        buttonList.add(new GuiButton(1, guiLeft + 7, guiTop + CONTAINER_SIZE - 27, 70, 20, EnhancedPortals.localize("gui.errorLog")));
        textBox = new ElementLargeTextBox(this, 7, 17, xSize - 14, 155);
        //textBox.setText(program.scriptData);
        addElement(textBox);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(program, GuiHandler.PROGRAMMABLE_INTERFACE_ERRORS));
        }
        else if (button.id == 0)
        {

        }
    }
}
