package enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.inventory.ContainerRedstoneInterface;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.tileentity.TileRedstoneInterface;

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
        EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
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
                stateText = flag ? EnhancedPortals.localize("gui.portalCreated") : EnhancedPortals.localize("gui.createPortalOnSignal");
                break;

            case 1:
                stateText = flag ? EnhancedPortals.localize("gui.portalRemoved") : EnhancedPortals.localize("gui.removePortalOnSignal");
                break;

            case 2:
                stateText = flag ? EnhancedPortals.localize("gui.portalActive") : EnhancedPortals.localize("gui.createPortalOnPulse");
                break;

            case 3:
                stateText = flag ? EnhancedPortals.localize("gui.portalInactive") : EnhancedPortals.localize("gui.removePortalOnPulse");
                break;

            case 4:
                stateText = flag ? EnhancedPortals.localize("gui.entityTeleport") : EnhancedPortals.localize("gui.dialStoredIdentifier");
                break;

            case 5:
                stateText = flag ? EnhancedPortals.localize("gui.playerTeleport") : EnhancedPortals.localize("gui.dialStoredIdentifier2");
                break;

            case 6:
                stateText = flag ? EnhancedPortals.localize("gui.animalTeleport") : EnhancedPortals.localize("gui.dialRandomIdentifier");
                break;

            case 7:
                stateText = flag ? EnhancedPortals.localize("gui.monsterTeleport") : EnhancedPortals.localize("gui.dialRandomIdentifier2");
                break;
        }

        ((GuiButton) buttonList.get(0)).displayString = redstone.isOutput ? EnhancedPortals.localize("gui.output") : EnhancedPortals.localize("gui.input");
        ((GuiButton) buttonList.get(1)).displayString = stateText;
    }
}
