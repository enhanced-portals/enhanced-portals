package uk.co.shadeddimensions.enhancedportals.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

public class ConfigurationManager
{
    public class ConfigProperty
    {
        String ID;

        public ConfigProperty(String id)
        {
            ID = id;
        }

        public void addComment(String comment)
        {
            ConfigurationManager.this.addComment(ID, comment);
        }
    }

    public Configuration config;

    int START_BLOCK_ID = 512, START_ITEM_ID = 5000, MAX_BLOCK_ID = 4096, MAX_ITEM_ID = 32000;

    TreeMap<String, Property> blockIds, itemIds, boolValues, stringValues;
    ArrayList<String> blockEntries, itemEntries;
    ArrayList<Integer> usedBlockIds, usedItemIds;
    Map<String, Boolean> boolEntries;
    Map<String, String> stringEntries;
    Map<String, String> commentEntries;

    public ConfigurationManager(Configuration c)
    {
        blockEntries = new ArrayList<String>();
        itemEntries = new ArrayList<String>();

        blockIds = new TreeMap<String, Property>();
        itemIds = new TreeMap<String, Property>();
        boolValues = new TreeMap<String, Property>();
        stringValues = new TreeMap<String, Property>();

        usedBlockIds = new ArrayList<Integer>();
        usedItemIds = new ArrayList<Integer>();

        boolEntries = new HashMap<String, Boolean>();
        stringEntries = new HashMap<String, String>();
        commentEntries = new HashMap<String, String>();

        config = c;
        config.load();
    }

    public ConfigProperty addBlock(String name)
    {
        blockEntries.add(name);

        return new ConfigProperty(name);
    }

    public ConfigProperty addBoolean(String string)
    {
        addBoolean(string, false);

        return new ConfigProperty(string);
    }

    public ConfigProperty addBoolean(String string, boolean b)
    {
        boolEntries.put(string, b);

        return new ConfigProperty(string);
    }

    public void addComment(String id, String comment)
    {
        commentEntries.put(id, comment);
    }

    public ConfigProperty addItem(String name)
    {
        itemEntries.add(name);

        return new ConfigProperty(name);
    }

    public ConfigProperty addString(String name, String s)
    {
        stringEntries.put(name, s);

        return new ConfigProperty(name);
    }

    public void fillConfigFile()
    {
        // BLOCKS
        for (String entry : blockEntries)
        {
            if (config.hasKey("block", entry))
            {
                int id = config.getCategory("block").getValues().get(formatName(entry)).getInt();
                blockIds.put(entry, config.getBlock(formatName(entry), id, getComment(entry)));
                usedBlockIds.add(id);
            }
        }

        for (String entry : blockEntries)
        {
            if (!config.hasKey("block", entry))
            {
                for (int i = START_BLOCK_ID; i < MAX_BLOCK_ID; i++)
                {
                    if (Block.blocksList[i] == null && !usedBlockIds.contains(i))
                    {
                        blockIds.put(entry, config.getBlock(formatName(entry), i, getComment(entry)));
                        usedBlockIds.add(i);
                        break;
                    }
                }
            }
        }

        // ITEMS
        for (String entry : itemEntries)
        {
            if (config.hasKey("item", entry))
            {
                int id = config.getCategory("item").getValues().get(formatName(entry)).getInt();
                itemIds.put(entry, config.getItem(formatName(entry), id, getComment(entry)));
                usedItemIds.add(id);
            }
        }

        for (String entry : itemEntries)
        {
            if (!config.hasKey("item", entry))
            {
                for (int i = START_ITEM_ID; i < MAX_ITEM_ID; i++)
                {
                    if (Item.itemsList[i] == null && !usedItemIds.contains(i))
                    {
                        itemIds.put(entry, config.getItem(formatName(entry), i, getComment(entry)));
                        usedItemIds.add(i);
                        break;
                    }
                }
            }
        }

        // BOOLS
        for (Map.Entry<String, Boolean> entry : boolEntries.entrySet())
        {
            boolValues.put(entry.getKey(), config.get("boolean", entry.getKey(), entry.getValue(), getComment(entry.getKey())));
        }

        // STRINGS
        for (Map.Entry<String, String> entry : stringEntries.entrySet())
        {
            stringValues.put(entry.getKey(), config.get("string", entry.getKey(), entry.getValue(), getComment(entry.getKey())));
        }

        config.addCustomCategoryComment("block", "All block IDs will attempt to find the first free ID from " + START_BLOCK_ID + " to " + MAX_BLOCK_ID + ", when one is not set below");
        config.addCustomCategoryComment("item", "All item IDs will attempt to find the first free ID from " + START_ITEM_ID + " to " + MAX_ITEM_ID + ", when one is not set below");

        config.save();
    }

    public String formatName(String name)
    {
        return name.replace(Reference.SHORT_ID + ".", "");
    }

    public int getBlockId(String name)
    {
        Property prop = blockIds.get(name);

        if (prop == null)
        {
            return -1;
        }

        return prop.getInt();
    }

    public boolean getBoolean(String string)
    {
        return boolValues.get(string).getBoolean(boolEntries.get(string));
    }

    private String getComment(String id)
    {
        if (commentEntries.containsKey(id))
        {
            return commentEntries.get(id);
        }

        return null;
    }

    public int getItemId(String name)
    {
        Property prop = itemIds.get(name);

        if (prop == null)
        {
            return -1;
        }

        return prop.getInt();
    }

    public String getString(String string)
    {
        return stringValues.get(string).getString();
    }

    public Block registerBlock(Class<? extends Block> block, Class<? extends ItemBlock> item, String name)
    {
        Block b = null;

        try
        {
            b = block.getConstructor(int.class, String.class).newInstance(getBlockId(name), name);
            GameRegistry.registerBlock(b, item, name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return b;
    }

    public Block registerBlock(Class<? extends Block> block, String name)
    {
        Block b = null;

        try
        {
            b = block.getConstructor(int.class, String.class).newInstance(getBlockId(name), name);
            GameRegistry.registerBlock(b, name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return b;
    }

    public Item registerItem(Class<? extends Item> item, String name)
    {
        Item i = null;

        try
        {
            i = item.getConstructor(int.class, String.class).newInstance(getItemId(name), name);
            GameRegistry.registerItem(i, name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return i;
    }
}
