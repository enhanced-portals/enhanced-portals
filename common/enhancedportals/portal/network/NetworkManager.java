package enhancedportals.portal.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import alz.core.lib.WorldLocation;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.lib.Reference;

public class NetworkManager
{
    public Map<String, List<WorldLocation>> networkData;
    private String                          saveFile;
    private MinecraftServer                 serverInstance;

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
            try
            {
                new File(saveFile).createNewFile();
            }
            catch (IOException e)
            {
                return;
            }
        }

        loadData();
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

        networkData.get(key).add(data);
    }

    public List<WorldLocation> getNetwork(String key)
    {
        if (!hasNetwork(key))
        {
            return null;
        }

        List<WorldLocation> list = networkData.get(key);
        List<WorldLocation> newList = new ArrayList<WorldLocation>();

        if (list == null)
        {
            return new ArrayList<WorldLocation>();
        }

        for (int i = 0; i < list.size(); i++)
        {
            newList.add(list.get(i));
        }

        return newList;
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

    public List<WorldLocation> getNetworkExcluding(String network, WorldLocation worldLocation)
    {
        List<WorldLocation> list = networkData.get(network);
        List<WorldLocation> newList = new ArrayList<WorldLocation>();

        if (list == null)
        {
            return new ArrayList<WorldLocation>();
        }

        for (int i = 0; i < list.size(); i++)
        {
            if (!list.get(i).isEqual(worldLocation))
            {
                newList.add(list.get(i));
            }
        }

        return newList;
    }

    public String getSaveFileName()
    {
        return Reference.MOD_ID + ".dat";
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

    public void loadData()
    {
        try
        {
            NBTTagCompound tagCompound = (NBTTagCompound) NBTBase.readNamedTag(new DataInputStream(new FileInputStream(saveFile)));

            for (Object obj : tagCompound.getTags())
            {
                if (obj instanceof NBTTagList)
                {
                    NBTTagList tag = (NBTTagList) obj;

                    for (int i = 0; i < tag.tagList.size(); i++)
                    {
                        NBTTagCompound comp = (NBTTagCompound) tag.tagAt(i);

                        addToNetwork(tag.getName(), new WorldLocation(comp.getInteger("xCoord"), comp.getInteger("yCoord"), comp.getInteger("zCoord"), comp.getInteger("dimension")));
                    }
                }
            }
        }
        catch (Exception e)
        {
            return;
        }
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

        for (int i = 0; i < networkData.get(key).size(); i++)
        {
            if (networkData.get(key).get(i).isEqual(data))
            {
                networkData.get(key).remove(i);
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

    public void saveData()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();

        for (Entry<String, List<WorldLocation>> set : networkData.entrySet())
        {
            NBTTagList list2 = new NBTTagList();

            for (WorldLocation loc : set.getValue())
            {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setInteger("xCoord", loc.xCoord);
                compound.setInteger("yCoord", loc.yCoord);
                compound.setInteger("zCoord", loc.zCoord);
                compound.setInteger("dimension", loc.dimension);

                list2.appendTag(compound);
            }

            tagCompound.setTag(set.getKey(), list2);
        }

        try
        {
            NBTBase.writeNamedTag(tagCompound, new DataOutputStream(new FileOutputStream(saveFile)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
