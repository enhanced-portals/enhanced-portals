package alz.mods.enhancedportals.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.teleportation.TeleportData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ModifierNetwork implements INetworkManager
{
    public Map<String, List<TeleportData>> Network;
    private String SaveFile;
    private MinecraftServer serverInstance;

    public ModifierNetwork(MinecraftServer server)
    {
        serverInstance = server;
        WorldServer world = serverInstance.worldServerForDimension(0);
        Network = new HashMap<String, List<TeleportData>>();

        if (FMLCommonHandler.instance().getSide() == Side.SERVER)
        {
            SaveFile = serverInstance.getFile(world.getSaveHandler().getSaveDirectoryName() + File.separator + Reference.MOD_ID + ".dat").getAbsolutePath();
        }
        else
        {
            SaveFile = serverInstance.getFile("saves" + File.separator + world.getSaveHandler().getSaveDirectoryName() + File.separator + Reference.MOD_ID + ".dat").getAbsolutePath();
        }

        if (!new File(SaveFile).exists())
        {
            return;
        }

        loadData();
    }

    public void addNetwork(String key)
    {
        if (hasNetwork(key))
        {
            return;
        }

        Network.put(key, new ArrayList<TeleportData>());
    }

    public void addToNetwork(String key, TeleportData data)
    {
        if (!hasNetwork(key))
        {
            addNetwork(key);
        }

        if (networkContains(key, data))
        {
            return;
        }

        getNetwork(key).add(data);
    }

    public List<TeleportData> getFrequencyExcluding(String key, TeleportData data)
    {
        List<TeleportData> list = getNetwork(key);

        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).equals(data))
            {
                list.remove(i);
            }
        }

        return list;
    }

    public List<TeleportData> getNetwork(String key)
    {
        if (!hasNetwork(key))
        {
            return null;
        }

        return Network.get(key);
    }

    public String getNetwork(TeleportData data)
    {
        for (String key : Network.keySet())
        {
            if (isInNetwork(key, data))
            {
                return key;
            }
        }

        return "undefined";
    }

    public boolean hasNetwork(String key)
    {
        for (String key2 : Network.keySet())
        {
            if (key.equals(key2))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isInNetwork(String key, TeleportData data)
    {
        if (!hasNetwork(key))
        {
            return false;
        }

        return networkContains(key, data);
    }

    @Override
    public void loadData()
    {
        BufferedReader reader = null;
        String network = "undefined";
        List<TeleportData> items = new ArrayList<TeleportData>();

        try
        {
            reader = new BufferedReader(new FileReader(SaveFile));
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                if (line.startsWith(">"))
                {
                    if (network != "undefined")
                    {
                        addNetwork(network);

                        for (TeleportData item : items)
                        {
                            addToNetwork(network, item);
                        }
                    }

                    network = line.replace(">", "");
                    items = new ArrayList<TeleportData>();
                }
                else if (line.startsWith("#"))
                {
                    String theLine[] = line.replace("#", "").split(",");

                    if (theLine.length == 4)
                    {
                        items.add(new TeleportData(Integer.parseInt(theLine[0]), Integer.parseInt(theLine[1]), Integer.parseInt(theLine[2]), Integer.parseInt(theLine[3])));
                    }
                }
            }

            if (network != "undefined")
            {
                addNetwork(network);

                for (TeleportData item : items)
                {
                    addToNetwork(network, item);
                }
            }

            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean networkContains(String key, TeleportData data)
    {
        if (!hasNetwork(key))
        {
            return false;
        }

        for (TeleportData keyData : getNetwork(key))
        {
            if (keyData.equals(key))
            {
                return true;
            }
        }

        return false;
    }

    public void removeFromNetwork(String key, TeleportData data)
    {
        if (!hasNetwork(key))
        {
            return;
        }

        if (isInNetwork(key, data))
        {
            return;
        }

        for (int i = 0; i < getNetwork(key).size(); i++)
        {
            if (getNetwork(key).get(i).equals(data))
            {
                getNetwork(key).remove(i);
            }
        }
    }

    public void removeNetwork(String key)
    {
        if (!hasNetwork(key))
        {
            return;
        }

        Network.remove(key);
    }

    @Override
    public void saveData()
    {
        BufferedWriter writer;

        if (Network.keySet() == null)
        {
            return;
        }

        try
        {
            writer = new BufferedWriter(new FileWriter(SaveFile));

            for (String key : Network.keySet())
            {
                List<TeleportData> items = getNetwork(key);

                writer.write(">" + key);
                writer.newLine();

                for (TeleportData item : items)
                {
                    writer.write("#" + item.getX() + "," + item.getY() + "," + item.getZ() + "," + item.getDimension());
                    writer.newLine();
                }
            }

            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
