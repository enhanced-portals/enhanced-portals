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
import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.teleportation.TeleportData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class DialDeviceNetwork implements INetworkManager
{
    public Map<String, List<PortalData>> Network;
    private String SaveFile;
    private MinecraftServer serverInstance;

    public DialDeviceNetwork(MinecraftServer server)
    {
        serverInstance = server;
        WorldServer world = serverInstance.worldServerForDimension(0);
        Network = new HashMap<String, List<PortalData>>();

        if (FMLCommonHandler.instance().getSide() == Side.SERVER)
        {
            SaveFile = serverInstance.getFile(world.getSaveHandler().getSaveDirectoryName() + File.separator + Reference.MOD_ID + "_dd.dat").getAbsolutePath();
        }
        else
        {
            SaveFile = serverInstance.getFile("saves" + File.separator + world.getSaveHandler().getSaveDirectoryName() + File.separator + Reference.MOD_ID + "_dd.dat").getAbsolutePath();
        }

        if (!new File(SaveFile).exists())
        {
            return;
        }

        loadData();
    }

    public void addNetwork(String key)
    {
        if (!hasNetwork(key))
        {
            return;
        }

        Network.put(key, new ArrayList<PortalData>());
    }

    public void addToNetwork(String key, PortalData data)
    {
        if (!hasNetwork(key))
        {
            return;
        }

        if (networkContains(key, data))
        {
            return;
        }

        getNetwork(key).add(data);
    }

    public String getFrequency(PortalData data)
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

    public List<PortalData> getNetwork(String key)
    {
        if (!hasNetwork(key))
        {
            return null;
        }

        return Network.get(key);
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

    public boolean isInNetwork(String key, PortalData data)
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
        List<PortalData> items = new ArrayList<PortalData>();

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

                        for (PortalData item : items)
                        {
                            addToNetwork(network, item);
                        }
                    }

                    network = line.replace(">", "");
                    items = new ArrayList<PortalData>();
                }
                else if (line.startsWith("#"))
                {
                    String theLine[] = line.replace("#", "").split(",");

                    if (theLine.length == 6)
                    {
                        items.add(new PortalData(theLine[0], Integer.parseInt(theLine[1]), new TeleportData(Integer.parseInt(theLine[2]), Integer.parseInt(theLine[3]), Integer.parseInt(theLine[4]), Integer.parseInt(theLine[5]))));
                    }
                }
            }

            if (network != "undefined")
            {
                addNetwork(network);

                for (PortalData item : items)
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

    public boolean networkContains(String key, PortalData data)
    {
        if (!hasNetwork(key))
        {
            return false;
        }

        for (PortalData keyData : getNetwork(key))
        {
            if (keyData.equals(key))
            {
                return true;
            }
        }

        return false;
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
                List<PortalData> items = getNetwork(key);

                writer.write(">" + key);
                writer.newLine();

                for (PortalData item : items)
                {
                    writer.write("#" + item.DisplayName + "," + item.Texture.ordinal() + "," + item.TeleportData.getX() + "," + item.TeleportData.getY() + "," + item.TeleportData.getZ() + "," + item.TeleportData.getDimension());
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
