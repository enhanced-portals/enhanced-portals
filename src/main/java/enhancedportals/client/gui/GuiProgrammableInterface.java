package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import enhancedportals.inventory.ContainerProgrammableInterface;
import enhancedportals.tileentity.portal.TileProgrammableInterface;

public class GuiProgrammableInterface extends BaseGui
{
    public static int CONTAINER_SIZE = 200, CONTAINER_WIDTH = 256;

    public GuiProgrammableInterface(TileProgrammableInterface pr, EntityPlayer p)
    {
        super(new ContainerProgrammableInterface(pr, p.inventory), CONTAINER_SIZE);
        xSize = CONTAINER_WIDTH;
        setHidePlayerInventory();
        name = "gui.programmableInterface";
        texture = new ResourceLocation("enhancedportals", "textures/gui/program_interface.png");
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
    }
}
