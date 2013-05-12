package enhancedportals.portal.network;

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
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.lib.Reference;
import enhancedportals.lib.WorldLocation;

public class NetworkManager
{
    public Map<String, List<WorldLocation>> networkData;
    private String saveFile;
    private MinecraftServer serverInstance;
    
    public NetworkManager(MinecraftServer server)
    {
        serverInstance = server;
        networkData = new HashMap<String, List<WorldLocation>>();
        WorldServer world = serverInstance.worldServerForDimension(0);
        
        if (FMLCommonHandler.instance().getSide() == Side.SERVER)
        {
            saveFile = serverInstance.getFile(world.getSaveHandler().getWorldDirectoryName() + File.separator + getSaveFileName()).getAbsolutePath();
        }
        else
        {
            saveFile = serverInstance.getFile("saves" + File.separator + world.getSaveHandler().getWorldDirectoryName() + File.separator + getSaveFileName()).getAbsolutePath();
        }

        if (!new File(saveFile).exists())
        {
            return;
        }

        loadData();
    }
    
    public String getSaveFileName()
    {
        return Reference.MOD_ID + ".dat";
    }

    public void loadData()
    {
        BufferedReader reader = null;
        String network = "undefined";
        List<WorldLocation> items = new ArrayList<WorldLocation>();

        try
        {
            reader = new BufferedReader(new FileReader(saveFile));
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                if (line.startsWith(">"))
                {
                    if (network != "undefined")
                    {
                        addNetwork(network);

                        for (WorldLocation item : items)
                        {
                            addToNetwork(network, item);
                        }
                    }

                    network = line.substring(1);
                    items = new ArrayList<WorldLocation>();
                }
                else if (line.startsWith("#"))
                {
                    String theLine[] = line.substring(1).split(",");

                    if (theLine.length == 4)
                    {
                        items.add(new WorldLocation(Integer.parseInt(theLine[0]), Integer.parseInt(theLine[1]), Integer.parseInt(theLine[2]), Integer.parseInt(theLine[3])));
                    }
                }
            }

            if (network != "undefined")
            {
                addNetwork(network);

                for (WorldLocation item : items)
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
    
    public void saveData()
    {
        BufferedWriter writer;
        boolean isFirst = true;

        if (networkData.keySet() == null)
        {
            return;
        }

        try
        {
            writer = new BufferedWriter(new FileWriter(saveFile));

            for (String key : networkData.keySet())
            {
                List<WorldLocation> items = getNetwork(key);

                if (items.isEmpty())
                {
                    continue;
                }
                
                if (!isFirst)
                {
                    writer.newLine();
                }
                else
                {
                    isFirst = false;
                }
                
                writer.write(">" + key);                

                for (WorldLocation item : items)
                {
                    writer.newLine();
                    writer.write("#" + item.xCoord + "," + item.yCoord + "," + item.zCoord + "," + item.dimension);
                }
            }

            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void addNetwork(String key)
    {
        if (hasNetwork(key))
        {
            return;
        }

        networkData.put(key, new ArrayList<WorldLocation>());
    }

    public void addToNetwork(String key, WorldLocation data)
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
    
    public List<WorldLocation> getNetworkExcluding(String network, WorldLocation worldLocation)
    {
        List<WorldLocation> list = getNetwork(network);
        List<WorldLocation> newList = new ArrayList<WorldLocation>();
        
        if (list == null)
        {
            return new ArrayList<WorldLocation>();
        }
        
        for (int i = 0; i < list.size(); i++)
        {
            if (!list.get(i).equals(worldLocation))
            {
                newList.add(list.get(i));
            }
        }
        
        return newList;
    }

    public List<WorldLocation> getNetwork(String key)
    {
        if (!hasNetwork(key))
        {
            return null;
        }

        return networkData.get(key);
    }

    public String getNetwork(WorldLocation data)
    {
        for (String key : networkData.keySet())
        {
            if (isInNetwork(key, data))
            {
                return key;
            }
        }

        return "";
    }

    public boolean hasNetwork(String key)
    {
        for (String key2 : networkData.keySet())
        {
            if (key.equals(key2))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isInNetwork(String key, WorldLocation data)
    {
        if (!hasNetwork(key))
        {
            return false;
        }

        return networkContains(key, data);
    }

    public boolean networkContains(String key, WorldLocation data)
    {
        if (!hasNetwork(key))
        {
            return false;
        }

        for (WorldLocation keyData : getNetwork(key))
        {
            if (keyData.equals(data))
            {
                return true;
            }
        }

        return false;
    }

    public void removeFromNetwork(String key, WorldLocation data)
    {
        if (!hasNetwork(key))
        {
            return;
        }

        if (!isInNetwork(key, data))
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

        networkData.remove(key);
    }
    
    public void removeFromAllNetworks(WorldLocation worldLocation)
    {
        for (String str : networkData.keySet())
        {
            if (isInNetwork(str, worldLocation))
            {
                removeFromNetwork(str, worldLocation);
            }
        }
    }
}
