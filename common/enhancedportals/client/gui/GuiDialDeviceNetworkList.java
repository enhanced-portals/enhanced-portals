package enhancedportals.client.gui;

import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.lib.Reference;
import enhancedportals.portal.network.DialDeviceNetworkObject;

@SideOnly(Side.CLIENT)
class GuiDialDeviceNetworkList extends GuiSlot
{
    final GuiDialDevice parent;

    public GuiDialDeviceNetworkList(GuiDialDevice par)
    {
        super(par.mc, par.width, par.height, 32, par.height - 64, 36);
        parent = par;
    }

    @Override
    protected void drawBackground()
    {
        parent.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        DialDeviceNetworkObject obj = (DialDeviceNetworkObject) parent.getSize().get(par1);

        parent.drawString(parent.fontRenderer, obj.displayName, par2 + 2, par3 + 2, 16777215);

        if (obj.network != null && !obj.network.equals(""))
        {
            String[] split = obj.network.split(Reference.glyphSeperator);
            int count = 0, x = par2, y = par3 + 14;

            for (String element2 : split)
            {
                for (int j = 0; j < Reference.glyphValues.size(); j++)
                {
                    if (Reference.glyphValues.get(j).equalsIgnoreCase(element2))
                    {
                        parent.itemRenderer.renderItemIntoGUI(parent.fontRenderer, parent.mc.renderEngine, Reference.glyphItems.get(j), x + count * 18, y);
                        count++;
                    }
                }
            }

            GL11.glDisable(GL11.GL_LIGHTING);
        }
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was
     * double clicked or not
     */
    @Override
    protected void elementClicked(int par1, boolean par2)
    {
        parent.onElementSelected(par1);
        boolean flag1 = parent.getSelected() >= 0 && parent.getSelected() < getSize() && !parent.dialDevice.active;
        parent.getDeleteButton().enabled = flag1;
        parent.getDialButton().enabled = flag1;

        if (par2 && flag1)
        {
            parent.select(par1);
        }
    }

    /**
     * return the height of the content being scrolled
     */
    @Override
    protected int getContentHeight()
    {
        return parent.getSize().size() * 36;
    }

    /**
     * Gets the size of the current slot list.
     */
    @Override
    protected int getSize()
    {
        return parent.getSize().size();
    }

    /**
     * returns true if the element passed in is currently selected
     */
    @Override
    protected boolean isSelected(int par1)
    {
        return par1 == parent.getSelected();
    }
}
