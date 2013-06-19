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
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enhancedcore.world.WorldPosition;
import enhancedportals.lib.Reference;

public class NetworkManager
{
    public Map<String, List<WorldPosition>> networkData;
    private String saveFile;
    private MinecraftServer serverInstance;

    public NetworkManager(MinecraftServer server)
    {
        serverInstance = server;
        networkData = new HashMap<String, List<WorldPosition>>();
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

        networkData.put(key, new ArrayList<WorldPosition>());
    }

    public void addToNetwork(String key, WorldPosition data)
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

    public List<WorldPosition> getNetwork(String key)
    {
        if (!hasNetwork(key))
        {
            return null;
        }

        List<WorldPosition> list = networkData.get(key);
        List<WorldPosition> newList = new ArrayList<WorldPosition>();

        if (list == null)
        {
            return new ArrayList<WorldPosition>();
        }

        for (int i = 0; i < list.size(); i++)
        {
            newList.add(list.get(i));
        }

        return newList;
    }

    public String getNetwork(WorldPosition data)
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

    public List<WorldPosition> getNetworkExcluding(String network, WorldPosition worldLocation)
    {
        List<WorldPosition> list = networkData.get(network);
        List<WorldPosition> newList = new ArrayList<WorldPosition>();

        if (list == null)
        {
            return new ArrayList<WorldPosition>();
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

    public boolean isInNetwork(String key, WorldPosition data)
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

                        addToNetwork(tag.getName(), new WorldPosition(comp.getInteger("xCoord"), comp.getInteger("yCoord"), comp.getInteger("zCoord"), comp.getInteger("dimension")));
                    }
                }
            }
        }
        catch (Exception e)
        {
            return;
        }
    }

    public boolean networkContains(String key, WorldPosition data)
    {
        if (!hasNetwork(key))
        {
            return false;
        }

        for (WorldPosition keyData : getNetwork(key))
        {
            if (keyData.equals(data))
            {
                return true;
            }
        }

        return false;
    }

    public void removeFromAllNetworks(WorldPosition worldLocation)
    {
        for (String str : networkData.keySet())
        {
            if (isInNetwork(str, worldLocation))
            {
                removeFromNetwork(str, worldLocation);
            }
        }
    }

    public void removeFromNetwork(String key, WorldPosition data)
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
            if (networkData.get(key).get(i).equals(data))
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

        for (Entry<String, List<WorldPosition>> set : networkData.entrySet())
        {
            if (set.getValue().isEmpty())
            {
                continue;
            }

            NBTTagList list2 = new NBTTagList();

            for (WorldPosition loc : set.getValue())
            {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setInteger("xCoord", loc.getX());
                compound.setInteger("yCoord", loc.getY());
                compound.setInteger("zCoord", loc.getZ());
                compound.setInteger("dimension", loc.getDimension());

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
