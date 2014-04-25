package enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileRedstoneInterface;
import enhancedportals.inventory.ContainerRedstoneInterface;

public class GuiRedstoneInterface extends BaseGui
{
    public static final int CONTAINER_SIZE = 68;
    TileRedstoneInterface redstone;

    public GuiRedstoneInterface(TileRedstoneInterface ri, EntityPlayer p)
    {
        super(new ContainerRedstoneInterface(ri, p.inventory), CONTAINER_SIZE);
        name = "gui.redstoneInterface";
        redstone = ri;
        setHidePlayerInventory();
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(0, guiLeft + 8, guiTop + 18, xSize - 16, 20, ""));
        buttonList.add(new GuiButton(1, guiLeft + 8, guiTop + 40, xSize - 16, 20, ""));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("id", button.id);
        PacketHandlerClient.sendGuiPacket(tag);
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();

        String stateText = "";
        boolean flag = redstone.isOutput;

        switch (redstone.state)
        {
            case 0:
                stateText = flag ? Localization.getGuiString("portalCreated") : Localization.getGuiString("createPortalOnSignal");
                break;

            case 1:
                stateText = flag ? Localization.getGuiString("portalRemoved") : Localization.getGuiString("removePortalOnSignal");
                break;

            case 2:
                stateText = flag ? Localization.getGuiString("portalActive") : Localization.getGuiString("createPortalOnPulse");
                break;

            case 3:
                stateText = flag ? Localization.getGuiString("portalInactive") : Localization.getGuiString("removePortalOnPulse");
                break;

            case 4:
                stateText = flag ? Localization.getGuiString("entityTeleport") : Localization.getGuiString("dialStoredIdentifier");
                break;

            case 5:
                stateText = flag ? Localization.getGuiString("playerTeleport") : Localization.getGuiString("dialStoredIdentifier2");
                break;

            case 6:
                stateText = flag ? Localization.getGuiString("animalTeleport") : Localization.getGuiString("dialRandomIdentifier");
                break;

            case 7:
                stateText = flag ? Localization.getGuiString("monsterTeleport") : Localization.getGuiString("dialRandomIdentifier2");
                break;
        }

        ((GuiButton) buttonList.get(0)).displayString = redstone.isOutput ? Localization.getGuiString("output") : Localization.getGuiString("input");
        ((GuiButton) buttonList.get(1)).displayString = stateText;
    }
}
