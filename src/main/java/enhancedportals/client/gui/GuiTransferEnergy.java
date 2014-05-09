package enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementRedstoneFlux;
import enhancedportals.inventory.ContainerTransferEnergy;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.tileentity.portal.TileTransferEnergy;

public class GuiTransferEnergy extends BaseGui
{
    public static final int CONTAINER_SIZE = 55;
    TileTransferEnergy energy;

    public GuiTransferEnergy(TileTransferEnergy e, EntityPlayer p)
    {
        super(new ContainerTransferEnergy(e, p.inventory), CONTAINER_SIZE);
        name = "gui.transferEnergy";
        energy = e;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(1, guiLeft + 7, guiTop + 29, 140, 20, EnhancedPortals.localize("gui." + (energy.isSending ? "sending" : "receiving"))));
        addElement(new ElementRedstoneFlux(this, xSize - 21, 7, energy.storage));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(new NBTTagCompound()));
        }
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        ((GuiButton) buttonList.get(0)).displayString = EnhancedPortals.localize("gui." + (energy.isSending ? "sending" : "receiving"));
    }
}
