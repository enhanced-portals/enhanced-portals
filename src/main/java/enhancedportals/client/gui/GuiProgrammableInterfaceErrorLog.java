package enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementLargeTextBox;
import enhancedportals.inventory.ContainerProgrammableInterfaceErrorLog;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.tileentity.portal.TileProgrammableInterface;

public class GuiProgrammableInterfaceErrorLog extends BaseGui
{
    TileProgrammableInterface program;

    public GuiProgrammableInterfaceErrorLog(TileProgrammableInterface pi, EntityPlayer player)
    {
        super(new ContainerProgrammableInterfaceErrorLog(pi, player.inventory), GuiProgrammableInterface.CONTAINER_SIZE);
        xSize = GuiProgrammableInterface.CONTAINER_WIDTH;
        setHidePlayerInventory();
        name = "gui.programmableInterface";
        texture = new ResourceLocation("enhancedportals", "textures/gui/program_interface.png");
        program = pi;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + GuiProgrammableInterface.CONTAINER_SIZE - 27, xSize - 14, 20, "Return"));
        addElement(new ElementLargeTextBox(this, 7, 17, xSize - 14, 155));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(program, GuiHandler.PROGRAMMABLE_INTERFACE));
        }
    }
}
