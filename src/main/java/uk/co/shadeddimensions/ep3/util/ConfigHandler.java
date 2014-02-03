package uk.co.shadeddimensions.ep3.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * This is effectively a wrapper for Forge Configurations. It allows for easier manipulation of Config files.
 * 
 * @author King Lemming
 * 
 */
public class ConfigHandler
{
    @SuppressWarnings("unchecked")
    ArrayList<String>[] blockEntries = new ArrayList[3];
    @SuppressWarnings("unchecked")
    ArrayList<String>[] itemEntries = new ArrayList[3];

    TreeMap<String, Property> blockIds = new TreeMap<String, Property>();
    TreeMap<String, Property> itemIds = new TreeMap<String, Property>();

    int blockIdCounter = 0;
    int itemIdCounter = 0;
    int moduleCounter = 0;

    @SuppressWarnings("rawtypes")
    Set assignedIds = new HashSet();

    Configuration modConfiguration;
    String modVersion;

    int blockIdStart = 1000;
    int itemIdStart = 10000;

    public ConfigHandler(String version)
    {
        modVersion = version;

        for (int i = 0; i < blockEntries.length; i++)
        {
            blockEntries[i] = new ArrayList<String>();
        }

        for (int i = 0; i < itemEntries.length; i++)
        {
            itemEntries[i] = new ArrayList<String>();
        }
    }

    public ConfigHandler(String version, int blockStart, int itemStart)
    {
        modVersion = version;
        blockIdStart = blockStart;
        itemIdStart = itemStart;

        for (int i = 0; i < blockEntries.length; i++)
        {
            blockEntries[i] = new ArrayList<String>();
        }

        for (int i = 0; i < itemEntries.length; i++)
        {
            itemEntries[i] = new ArrayList<String>();
        }
    }

    public void addBlockEntry(String name)
    {
        addBlockEntry(name, 0);
    }

    public void addBlockEntry(String name, int level)
    {

        blockEntries[level].add(name);
        blockIdCounter++;
    }

    public void addItemEntry(String name)
    {

        addItemEntry(name, 0);
    }

    public void addItemEntry(String name, int level)
    {

        itemEntries[level].add(name);
        itemIdCounter++;
    }

    public void cleanUp(boolean delConfig, boolean saveVersion)
    {

        removeProperty("general", "version");
        removeProperty("general", "Version");

        if (saveVersion)
        {
            get("general", "Version", modVersion);
        }
        modConfiguration.save();

        for (ArrayList<String> blockEntrie : blockEntries)
        {
            blockEntrie.clear();
        }
        blockEntries = null;

        for (ArrayList<String> itemEntrie : itemEntries)
        {
            itemEntrie.clear();
        }
        itemEntries = null;

        blockIds.clear();
        itemIds.clear();
        assignedIds.clear();

        if (delConfig)
        {
            modConfiguration = null;
        }
    }

    public boolean get(String category, String key, boolean defaultValue)
    {

        return modConfiguration.get(category, key, defaultValue).getBoolean(defaultValue);
    }

    public int get(String category, String key, int defaultValue)
    {

        return modConfiguration.get(category, key, defaultValue).getInt();
    }

    public String get(String category, String key, String defaultValue)
    {

        return modConfiguration.get(category, key, defaultValue).getString();
    }

    public int getBlockId(String name)
    {

        Property ret = blockIds.get(name);

        if (ret == null)
        {
            return -1;
        }
        return ret.getInt();
    }

    public ConfigCategory getCategory(String category)
    {

        return modConfiguration.getCategory(category);
    }

    public Set<String> getCategoryKeys(String category)
    {

        return modConfiguration.getCategory(category).getValues().keySet();
    }

    @SuppressWarnings("rawtypes")
    public Map getCategoryMap(String category)
    {

        return modConfiguration.getCategory(category).getValues();
    }

    public Configuration getConfiguration()
    {
        return modConfiguration;
    }

    public int getItemId(String name)
    {

        Property ret = itemIds.get(name);

        if (ret == null)
        {
            return -1;
        }
        return ret.getInt();
    }

    public Property getProperty(String category, String key, boolean defaultValue)
    {

        return modConfiguration.get(category, key, defaultValue);
    }

    public Property getProperty(String category, String key, int defaultValue)
    {

        return modConfiguration.get(category, key, defaultValue);
    }

    public Property getProperty(String category, String key, String defaultValue)
    {

        return modConfiguration.get(category, key, defaultValue);
    }

    public String getVersion()
    {
        return modVersion;
    }

    public boolean hasCategory(String category)
    {

        return modConfiguration.hasCategory(category);
    }

    public boolean hasKey(String category, String key)
    {

        return modConfiguration.hasKey(category, key);
    }

    @SuppressWarnings("unchecked")
    public void init()
    {

        // get ids for existing blocks
        for (ArrayList<String> blockEntrie : blockEntries)
        {
            for (String entry : blockEntrie)
            {
                if (modConfiguration.hasKey(Configuration.CATEGORY_BLOCK, entry))
                {
                    int existingId = modConfiguration.getCategory(Configuration.CATEGORY_BLOCK).getValues().get(entry).getInt();
                    assignedIds.add(existingId);
                    blockIds.put(entry, modConfiguration.getBlock(entry, existingId));
                }
            }
        }
        // get ids for new blocks
        for (ArrayList<String> blockEntrie : blockEntries)
        {
            for (String entry : blockEntrie)
            {
                if (!modConfiguration.hasKey(Configuration.CATEGORY_BLOCK, entry))
                {
                    boolean idFound = false;
                    for (int j = blockIdStart; j < blockIdStart + blockIdCounter && !idFound; ++j)
                    {
                        if (!assignedIds.contains(j))
                        {
                            assignedIds.add(j);
                            blockIds.put(entry, modConfiguration.getBlock(entry, j));
                            idFound = true;
                        }
                    }
                }
            }
        }
        // get ids for existing items
        for (ArrayList<String> itemEntrie : itemEntries)
        {
            for (String entry : itemEntrie)
            {
                if (modConfiguration.hasKey(Configuration.CATEGORY_ITEM, entry))
                {
                    int existingId = modConfiguration.getCategory(Configuration.CATEGORY_ITEM).getValues().get(entry).getInt();
                    assignedIds.add(existingId);
                    itemIds.put(entry, modConfiguration.getItem(entry, existingId));
                }
            }
        }
        // get ids for new items
        for (ArrayList<String> itemEntrie : itemEntries)
        {
            for (String entry : itemEntrie)
            {
                if (!modConfiguration.hasKey(Configuration.CATEGORY_ITEM, entry))
                {

                    boolean idFound = false;
                    for (int j = itemIdStart; j < itemIdStart + itemIdCounter && !idFound; ++j)
                    {
                        if (!assignedIds.contains(j))
                        {

                            assignedIds.add(j);
                            itemIds.put(entry, modConfiguration.getItem(entry, j));
                            idFound = true;
                        }
                    }
                }
            }
        }
        modConfiguration.save();
    }

    public boolean removeCategory(String category)
    {

        if (!modConfiguration.hasCategory(category))
        {
            return false;
        }
        modConfiguration.removeCategory(modConfiguration.getCategory(category));
        return true;
    }

    public boolean removeProperty(String category, String key)
    {

        if (!modConfiguration.hasKey(category, key))
        {
            return false;
        }
        modConfiguration.getCategory(category).remove(key);
        return true;
    }

    public boolean renameCategory(String category, String newCategory)
    {

        if (!modConfiguration.hasCategory(category))
        {
            return false;
        }
        for (Property prop : modConfiguration.getCategory(category).values())
        {
            renameProperty(category, prop.getName(), newCategory, prop.getName(), true);
        }
        removeCategory(category);
        return true;
    }

    public boolean renameProperty(String category, String key, String newCategory, String newKey, boolean forceValue)
    {

        if (modConfiguration.hasKey(category, key))
        {
            Property prop = modConfiguration.getCategory(category).get(key);

            if (prop.isIntValue())
            {
                int value = modConfiguration.getCategory(category).getValues().get(key).getInt();
                removeProperty(category, key);

                if (forceValue)
                {
                    removeProperty(newCategory, newKey);
                }
                modConfiguration.get(newCategory, newKey, value);
            }
            else if (prop.isBooleanValue())
            {
                boolean value = modConfiguration.getCategory(category).getValues().get(key).getBoolean(false);
                removeProperty(category, key);

                if (forceValue)
                {
                    removeProperty(newCategory, newKey);
                }
                modConfiguration.get(newCategory, newKey, value);
            }
            else if (prop.isDoubleValue())
            {
                double value = modConfiguration.getCategory(category).getValues().get(key).getDouble(0.0);
                removeProperty(category, key);

                if (forceValue)
                {
                    removeProperty(newCategory, newKey);
                }
                modConfiguration.get(newCategory, newKey, value);
            }
            else
            {
                String value = modConfiguration.getCategory(category).getValues().get(key).getString();
                removeProperty(category, key);

                if (forceValue)
                {
                    removeProperty(newCategory, newKey);
                }
                modConfiguration.get(newCategory, newKey, value);
            }
            return true;
        }
        return false;
    }

    public void save()
    {

        modConfiguration.save();
    }

    public void setConfiguration(Configuration config)
    {
        modConfiguration = config;
        modConfiguration.load();
    }

}
