package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.container.ContainerTransferFluid;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferFluid;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.element.ElementFluidTank;

public class GuiTransferFluid extends GuiBaseContainer
{
    TileTransferFluid fluid;
    
    public GuiTransferFluid(TileTransferFluid fluid)
    {
        super(new ContainerTransferFluid(fluid), new ResourceLocation("enhancedportals", "textures/gui/transfer.png"));
        this.fluid = fluid;
        ySize = 72;
        name = BlockFrame.instance.getUnlocalizedName() + ".fluid.name";
        drawInventory = false;
    }
    
    @Override
    public void addElements()
    {
        addElement(new ElementFluidTank(this, 5, 5, fluid.tank, 1));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("state", true);
        PacketHandlerClient.sendGuiPacket(tag);
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(0, guiLeft + 26, guiTop + 46, xSize - 31, 20, fluid.isSending ? "Sending" : "Receiving"));
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        ((GuiButton) buttonList.get(0)).displayString = fluid.isSending ? "Sending" : "Receiving";
    }
}
