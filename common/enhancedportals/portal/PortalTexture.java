package enhancedportals.portal;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import enhancedportals.lib.Textures;

public class PortalTexture
{
    public static Icon getDefaultPortalTexture()
    {
        return Textures.getTexture("").getPortalTexture();
    }

    private String ID;
    private Icon portalTexture;
    private boolean forceBlockSheet;

    public PortalTexture(String id)
    {
        ID = id;
        portalTexture = null;
        forceBlockSheet = false;
    }

    public PortalTexture(String id, Icon portaltexture)
    {
        ID = id;
        portalTexture = portaltexture;
        forceBlockSheet = false;
    }

    public PortalTexture(String id, Icon portaltexture, boolean blockSheet)
    {
        ID = id;
        portalTexture = portaltexture;
        forceBlockSheet = blockSheet;
    }

    public boolean getBlockSheet()
    {
        return forceBlockSheet;
    }

    public String getID()
    {
        return ID;
    }

    public ItemStack getItemStack()
    {
        return Textures.getItemStackFromTexture(this);
    }

    public Icon getPortalTexture()
    {
        if (ID.startsWith("B:") && portalTexture == null)
        {
            int id = Integer.parseInt(ID.substring(2).split(":")[0]), meta = Integer.parseInt(ID.substring(2).split(":")[1]);

            return getPortalTexture(id, meta);
        }
        else if (ID.startsWith("F:") && portalTexture == null)
        {
            return getPortalTexture(ID.substring(2));
        }
        else if (portalTexture != null)
        {
            return portalTexture;
        }

        return getDefaultPortalTexture();
    }

    public Icon getPortalTexture(Object... objects)
    {
        if (objects.length == 1 && objects[0] instanceof String)
        {
        	String str = objects[0].toString();        	
        	ItemStack stack = new ItemStack(Integer.parseInt(str.split(":")[0]), 1, Integer.parseInt(str.split(":")[1]));
        	
        	if (FluidContainerRegistry.isFilledContainer(stack))
        	{
        		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
        		Icon icon = fluid.getFluid().getStillIcon();
        		
        		if (icon != null)
        		{
        			return icon;
        		}
        		else
        		{
        			return Block.blocksList[fluid.getFluid().getBlockID()].getIcon(0, 0);
        		}
        	}
        }
        else if (objects.length == 2 && objects[0] instanceof Integer && objects[1] instanceof Integer)
        {
            int id = Integer.parseInt(objects[0].toString()), side = 2, meta = Integer.parseInt(objects[1].toString());

            return Block.blocksList[id].getIcon(side, meta);
        }

        return getDefaultPortalTexture();
    }
}
