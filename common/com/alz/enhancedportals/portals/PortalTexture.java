package com.alz.enhancedportals.portals;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StringTranslate;

import com.alz.enhancedportals.reference.Strings;

public enum PortalTexture
{
    PURPLE(0), RED(1), GREEN(2), BROWN(3), BLUE(4), BLACK(5), CYAN(6), LIGHT_GRAY(7), GRAY(8), PINK(9), LIME(10), YELLOW(11), LIGHT_BLUE(12), MAGENTA(13), ORANGE(14), WHITE(15), LAVA(16), WATER(17), LAVA_STILL(18), WATER_STILL(19), UNKNOWN(20), CUSTOM(-1);

    private static PortalTexture[] VALID_TEXTURES = { PURPLE, RED, GREEN, BROWN, BLUE, BLACK, CYAN, LIGHT_GRAY, GRAY, PINK, LIME, YELLOW, LIGHT_BLUE, MAGENTA, ORANGE, WHITE, LAVA, WATER, LAVA_STILL, WATER_STILL, CUSTOM };
    private static int[] PARTICLE_COLOURS = { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320, 15435844, 2437522, 15435844, 2437522, 0 };
    private static Icon[] TEXTURE_ICONS = new Icon[VALID_TEXTURES.length];
    private static Icon[] PORTAL_MODIFIER_ICONS = new Icon[VALID_TEXTURES.length];

    private static String localizedName;
    private static Icon texture;
    
    public static String getLocalizedName(int id)
    {
        return getLocalizedName(getPortalTexture(id));
    }

    public static String getLocalizedName(PortalTexture texture)
    {
        StringTranslate translate = StringTranslate.getInstance();

        if (texture == LAVA || texture == LAVA_STILL)
        {
            return translate.translateKey("tile.lava.name");
        }
        else if (texture == WATER || texture == WATER_STILL)
        {
            return translate.translateKey("tile.water.name");
        }
        else if (texture.ordinal() >= 0 && texture.ordinal() <= 15)
        {
            return translate.translateKey("item.fireworksCharge." + ItemDye.dyeColorNames[swapColours(texture.ordinal())]);
        }

        return "Unknown";
    }

    public static Icon getModifierIcon(int id)
    {
        return PORTAL_MODIFIER_ICONS[id];
    }

    public static int getParticleColour(int id)
    {
        if (id < 0)
        {
            id = 0;
        }
        else if (id > PARTICLE_COLOURS.length)
        {
            id = PARTICLE_COLOURS.length;
        }

        return PARTICLE_COLOURS[id];
    }

    public static Icon getPortalIcon(int id)
    {
        return TEXTURE_ICONS[id];
    }

    public static PortalTexture getPortalTexture(int id)
    {
        if (id >= 0 && id < VALID_TEXTURES.length)
        {
            return VALID_TEXTURES[id];
        }

        return UNKNOWN;
    }

    public static PortalTexture getPortalTextureFromItem(ItemStack item, PortalTexture currentTexture, boolean consumeItem, EntityPlayer player)
    {
        if (item != null)
        {
            PortalTexture newTexture = PortalTexture.UNKNOWN;
            boolean isBucket = false;

            if (item.itemID == Item.dyePowder.itemID)
            {
                newTexture = PortalTexture.getPortalTexture(PortalTexture.swapColours(item.getItemDamage()));
            }
            else if (item.itemID == Item.bucketLava.itemID)
            {
                newTexture = PortalTexture.LAVA;
                isBucket = true;

                if (currentTexture == PortalTexture.LAVA)
                {
                    newTexture = PortalTexture.LAVA_STILL;
                    consumeItem = false;
                }
            }
            else if (item.itemID == Item.bucketWater.itemID)
            {
                newTexture = PortalTexture.WATER;
                isBucket = true;

                if (currentTexture == PortalTexture.WATER)
                {
                    newTexture = PortalTexture.WATER_STILL;
                    consumeItem = false;
                }
            }

            if (newTexture != null && newTexture != PortalTexture.UNKNOWN)
            {
                if (consumeItem)
                {
                    item.stackSize--;

                    if (isBucket && player != null)
                    {
                        player.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty));
                    }
                }

                return newTexture;
            }
        }

        return PortalTexture.UNKNOWN;
    }

    public static void registerTextures(IconRegister iconRegister)
    {
        TEXTURE_ICONS = new Icon[VALID_TEXTURES.length];
        PORTAL_MODIFIER_ICONS = new Icon[VALID_TEXTURES.length];

        for (int i = 0; i < 16; i++)
        {
            TEXTURE_ICONS[i] = iconRegister.registerIcon(String.format(Strings.Block.NETHER_PORTAL_ICON, i));
            PORTAL_MODIFIER_ICONS[i] = iconRegister.registerIcon(String.format(Strings.Block.PORTAL_MODIFIER_ICON, i));
        }

        TEXTURE_ICONS[LAVA.ordinal()] = Block.lavaMoving.getBlockTextureFromSide(2);
        PORTAL_MODIFIER_ICONS[LAVA.ordinal()] = PORTAL_MODIFIER_ICONS[14];
        TEXTURE_ICONS[LAVA_STILL.ordinal()] = Block.lavaStill.getBlockTextureFromSide(0);
        PORTAL_MODIFIER_ICONS[LAVA_STILL.ordinal()] = PORTAL_MODIFIER_ICONS[14];

        TEXTURE_ICONS[WATER.ordinal()] = Block.waterMoving.getBlockTextureFromSide(2);
        PORTAL_MODIFIER_ICONS[WATER.ordinal()] = PORTAL_MODIFIER_ICONS[4];
        TEXTURE_ICONS[WATER_STILL.ordinal()] = Block.waterStill.getBlockTextureFromSide(0);
        PORTAL_MODIFIER_ICONS[WATER_STILL.ordinal()] = PORTAL_MODIFIER_ICONS[4];
    }

    public static int swapColours(int id)
    {
        if (id == 0)
        {
            id = 5;
        }
        else if (id == 5)
        {
            id = 0;
        }

        return id;
    }

    public final int portalColour;

    private PortalTexture(int id)
    {
        portalColour = id;
        ordinal();
    }

    public void setLocalizedName(String name)
    {
        localizedName = name;
    }

    public String getLocalizedName()
    {
        if (localizedName.length() > 1)
        {
            return localizedName;
        }
        
        return getLocalizedName(ordinal());
    }
    
    public void setIcon(Icon icon)
    {
        texture = icon;
    }

    public Icon getModifierIcon()
    {
        return PORTAL_MODIFIER_ICONS[ordinal()];
    }

    public Icon getPortalIcon()
    {
        if (texture != null)
        {
            return texture;
        }
        
        return TEXTURE_ICONS[ordinal()];
    }

    public PortalTexture getPortalTextureFromItem(ItemStack item, boolean consumeItem, EntityPlayer player)
    {
        return getPortalTextureFromItem(item, this, consumeItem, player);
    }

    public int getSwapped()
    {
        return swapColours(ordinal());
    }
}
