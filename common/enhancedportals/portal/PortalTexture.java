package enhancedportals.portal;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
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
        else if (ID.startsWith("L:") && portalTexture == null)
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
            /*LiquidStack liquid = LiquidDictionary.getLiquid(objects[0].toString(), 1);

            if (liquid != null)
            {
                Icon icon = liquid.canonical().getRenderingIcon();

                if (icon == null)
                {
                    if (liquid.itemID < Block.blocksList.length && Block.blocksList[liquid.itemID] != null)
                    {
                        return Block.blocksList[liquid.itemID].getIcon(2, 0);
                    }
                    else
                    {
                        return getDefaultPortalTexture();
                    }
                }

                return icon;
            }*/
        	// TODO
        }
        else if (objects.length == 2 && objects[0] instanceof Integer && objects[1] instanceof Integer)
        {
            int id = Integer.parseInt(objects[0].toString()), side = 2, meta = Integer.parseInt(objects[1].toString());

            /*if (LiquidDictionary.findLiquidName(new LiquidStack(id, 1, meta)) != null)
            {
                side = 0;
            }*/ // TODO

            return Block.blocksList[id].getIcon(side, meta);
        }

        return getDefaultPortalTexture();
    }
}
