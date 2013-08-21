package uk.co.shadeddimensions.enhancedportals.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.registry.GameRegistry;

public class ConfigurationManager
{
    Configuration config;
    
    int START_BLOCK_ID = 512, START_ITEM_ID = 5000, MAX_BLOCK_ID = 4096, MAX_ITEM_ID = 32000;
    
    TreeMap<String, Property> blockIds, itemIds;
    ArrayList<String> blockEntries, itemEntries;
    ArrayList<Integer> usedBlockIds, usedItemIds;
    
    public ConfigurationManager(Configuration c)
    {
        blockEntries = new ArrayList<String>();
        itemEntries = new ArrayList<String>();
        
        blockIds = new TreeMap<String, Property>();
        itemIds = new TreeMap<String, Property>();
        
        usedBlockIds = new ArrayList<Integer>();
        usedItemIds = new ArrayList<Integer>();
        
        config = c;
        config.load();
    }
    
    public void addBlock(String name)
    {
        blockEntries.add(name);
    }
    
    public void addItem(String name)
    {
        itemEntries.add(name);
    }
    
    public String formatName(String name)
    {
        return name.replace("ep2.", "");
    }
    
    public int getBlockId(String name)
    {
        Property prop = (Property) blockIds.get(name);
        
        if (prop == null)
        {
            return -1;
        }
        
        return prop.getInt();
    }
    
    public int getItemId(String name)
    {
        Property prop = (Property) itemIds.get(name);
        
        if (prop == null)
        {
            return -1;
        }
        
        return prop.getInt();
    }
    
    public void registerIds()
    {
        // BLOCKS
        for (String entry : blockEntries)
        {
            if (config.hasKey("block", entry))
            {
                int id = ((Property) config.getCategory("block").getValues().get(formatName(entry))).getInt();
                blockIds.put(entry, config.getBlock(formatName(entry), id));
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
                        blockIds.put(entry, config.getBlock(formatName(entry), i));
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
                int id = ((Property) config.getCategory("item").getValues().get(formatName(entry))).getInt();
                itemIds.put(entry, config.getItem(formatName(entry), id));
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
                        itemIds.put(entry, config.getItem(formatName(entry), i));
                        usedItemIds.add(i);
                        break;
                    }
                }
            }
        }
        
        config.save();
    }
    
    public Block registerBlock(Class<? extends Block> block, String name)
    {
        Block b = null;
        
        try
        {
            b = block.getConstructor(int.class, String.class).newInstance(getBlockId(name), name);        
            GameRegistry.registerBlock(b, name);        
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
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
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            e.printStackTrace();
        }
        
        return i;
    }
}
