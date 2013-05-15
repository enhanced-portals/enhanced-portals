package enhancedportals.portal;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import enhancedportals.lib.Settings;
import enhancedportals.lib.Textures;

public class PortalTexture
{
    public int blockID = -1, metaData = -1;
    public byte colour = -1;
    public String liquidID = "NULL";
    
    public static int swapColours(int integer)
    {
        if (integer == 0)
        {
            integer = 5;
        }
        else if (integer == 5)
        {
            integer = 0;
        }

        return integer;
    }

    public static PortalTexture getPortalTexture(Object... arguments)
    {
        PortalTexture texture = new PortalTexture();
        
        if (arguments.length == 1)
        {
            if (arguments[0] instanceof Byte) // Colour ID
            {
                byte par1 = (byte) arguments[0];
                
                if (par1 >= 0 && par1 <= 15)
                {
                    texture.colour = par1;
                    return texture;
                }
            }
            else if (arguments[0] instanceof String) // Liquid ID
            {
                texture.liquidID = (String) arguments[0];
                return texture;
            }
            else if (arguments[0] instanceof ItemStack) // ItemStack
            {
                ItemStack stack = (ItemStack) arguments[0];
                
                if (stack.getItemName().startsWith("tile.")) // Block
                {
                    return getPortalTexture(stack.itemID, stack.getItemDamage());
                }
                else if (stack.itemID == Item.dyePowder.itemID) // Colour
                {
                    return getPortalTexture((byte) stack.getItemDamage());
                }
                else if (LiquidDictionary.findLiquidName(new LiquidStack(stack.itemID, stack.getItemDamage())) != null) // Liquid
                {
                    return getPortalTexture(LiquidDictionary.findLiquidName(new LiquidStack(stack.itemID, stack.getItemDamage())));
                }
                else if (Settings.isValidItem(stack.itemID)) // Item Map
                {
                    if (Settings.ItemPortalTextureMap.containsKey(stack.itemID + ":" + stack.getItemDamage()))
                    {                        
                        return Settings.ItemPortalTextureMap.get(stack.itemID + ":" + stack.getItemDamage());
                    }
                }
            }
        }
        else if (arguments.length == 2)
        {
            if (arguments[0] instanceof Integer && arguments[1] instanceof Integer) // Block ID & Metadata
            {
                int par1 = (int) arguments[0], par2 = (int) arguments[1];
                
                if (Settings.isBlockExcluded(par1))
                {
                    return null;
                }
                
                texture.blockID = par1;
                texture.metaData = par2;
                return texture;
            }
            else if (arguments[0] instanceof ItemStack && arguments[1] instanceof PortalTexture)
            {
                ItemStack stack = (ItemStack) arguments[0];
                PortalTexture secondTexture = (PortalTexture) arguments[1];
                
                if (Settings.isValidItem(stack.itemID)) // Item Map
                {
                    PortalTexture text = Settings.ItemPortalTextureMap.get(stack.itemID + ":" + stack.getItemDamage());
                    
                    if (text.isEqualTo(secondTexture))
                    {
                        PortalTexture text2 = Settings.ItemPortalTextureMap.get(stack.itemID + ":" + stack.getItemDamage() + "_");
                        
                        if (text2 != null)
                        {
                            return text2;
                        }
                        else
                        {
                            return text;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    public PortalTexture()
    {
        
    }
    
    public PortalTexture(byte colour)
    {
        this.colour = colour;
    }
    
    public PortalTexture(int id, int meta)
    {
        blockID = id;
        metaData = meta;
    }
    
    public PortalTexture(String str)
    {
        liquidID = str;
    }

    public byte getType()
    {
        return (byte) (colour != -1 ? 0 : (blockID != -1 ? 1 : 2));
    }
    
    public Icon getIcon(int side)
    {
        if (getType() == 0) // Colours
        {            
            return Textures.TEXTURE_ICONS[colour];
        }
        else if (getType() == 1) // Blocks
        {
            if (blockID == 9 || blockID == 11) // We want to display the still versions of these.
            {
                return Block.blocksList[blockID].getIcon(0, 0);
            }
            else if (blockID == 8 || blockID == 10) // We want to make sure that the flowing texture is visible on all sides.
            {
                return Block.blocksList[blockID].getIcon(2, 2);
            }

            return Block.blocksList[blockID].getIcon(side, metaData);
        }
        else if (getType() == 2) // Liquid
        {            
            return LiquidDictionary.getLiquid(liquidID, 1).getRenderingIcon();
        }

        return null;
    }

    public Icon getModifierIcon()
    {
        if (getType() == 0) // Colours
        {            
            return Textures.PORTAL_MODIFIER_ICONS[colour];
        }
        else if (getType() == 1) // Blocks
        {
            if (blockID == 8 || blockID == 9)
            {
                return Textures.PORTAL_MODIFIER_ICONS[4];
            }
            else if (blockID == 10 || blockID == 11)
            {
                return Textures.PORTAL_MODIFIER_ICONS[14];
            }
        }
        else if (getType() == 2) // Liquids
        {
            
        }

        return Textures.PORTAL_MODIFIER_ICONS[5];
    }

    public int getParticleColour()
    {
        if (getType() == 0) // Colours
        {
            return Textures.PARTICLE_COLOURS[swapColours(colour)];
        }
        else if (getType() == 1) // Blocks
        {
            if (blockID == 8 || blockID == 9)
            {
                return Textures.PARTICLE_COLOURS[4];
            }
            else if (blockID == 10 || blockID == 11)
            {
                return Textures.PARTICLE_COLOURS[14];
            }
        }
        else if (getType() == 2) // Liquids
        {
            
        }

        return Textures.PARTICLE_COLOURS[5];
    }

    public boolean isEqualTo(PortalTexture texture)
    {
        if (getType() == texture.getType())
        {
            if (getType() == 0)
            {
                if (colour == texture.colour)
                {
                    return true;
                }
            }
            else if (getType() == 1)
            {
                if (blockID == texture.blockID && metaData == texture.metaData)
                {
                    return true;
                }
            }
            else if (getType() == 2)
            {
                if (liquidID.equals(texture.liquidID))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
