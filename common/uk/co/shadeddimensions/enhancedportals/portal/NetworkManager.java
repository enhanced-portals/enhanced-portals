package uk.co.shadeddimensions.enhancedportals.portal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.util.WorldCoordinates;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class NetworkManager
{
    // UID , Location
    Map<String, WorldCoordinates> portalLocations;
    // UID , Portals UIDs
    Map<String, ArrayList<String>> basicNetwork;

    File dataFile;
    MinecraftServer server;

    public static final String BLANK_IDENTIFIER = "NOT_SET";
    
    public NetworkManager(FMLServerStartingEvent event)
    {
        portalLocations = new HashMap<String, WorldCoordinates>();
        basicNetwork = new HashMap<String, ArrayList<String>>();
        server = event.getServer();
        dataFile = new File(EnhancedPortals.proxy.getWorldDir(), Reference.NAME + ".dat");

        loadAllData();
    }

    public void addNewPortal(String UID, WorldCoordinates pos)
    {
        if (!portalExists(UID) && !UID.equals(BLANK_IDENTIFIER))
        {
            portalLocations.put(UID, pos);
        }
    }

    public void addPortalToNetwork(String UID, String network)
    {
        if (!isPortalInNetwork(UID, network) && !UID.equals(BLANK_IDENTIFIER) && !network.equals(BLANK_IDENTIFIER))
        {
            if (!networkExists(network))
            {
                basicNetwork.put(network, new ArrayList<String>());
            }

            getNetworkedPortals(network).add(UID);
        }
    }

    public ArrayList<String> getNetworkedPortals(String UID)
    {
        return networkExists(UID) ? basicNetwork.get(UID) : new ArrayList<String>();
    }

    public WorldCoordinates getPortalLocation(String UID)
    {
        return UID.equals(BLANK_IDENTIFIER) ? null : portalLocations.get(UID);
    }
    
    public TilePortalController getPortalLocationController(String UID)
    {
        WorldCoordinates c = getPortalLocation(UID);
        
        if (c != null)
        {
            TileEntity tile = DimensionManager.getWorld(c.dimension).getBlockTileEntity(c.posX, c.posY, c.posZ);
            
            if (tile != null && tile instanceof TilePortalController)
            {
                return (TilePortalController) tile;
            }
        }
        
        return null;
    }

    public boolean isPortalInNetwork(String UID, String networkID)
    {
        return getNetworkedPortals(networkID).contains(UID);
    }

    public void loadAllData()
    {
        makeFile();
        NBTTagCompound mainTag = null;

        try
        {
            mainTag = (NBTTagCompound) NBTBase.readNamedTag(new DataInputStream(new FileInputStream(dataFile)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (mainTag == null)
        {
            return;
        }

        NBTTagCompound portalTag = (NBTTagCompound) mainTag.getTag("PortalLocations");
        NBTTagCompound networkTag = (NBTTagCompound) mainTag.getTag("Networks");

        for (Object o : portalTag.getTags())
        {
            NBTTagCompound tag = (NBTTagCompound) o;
            portalLocations.put(tag.getName(), new WorldCoordinates(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"), tag.getInteger("D")));
        }

        for (Object o : networkTag.getTags())
        {
            NBTTagList tag = (NBTTagList) o;
            ArrayList<String> sList = new ArrayList<String>();

            for (Object ob : tag.tagList)
            {
                NBTTagCompound c = (NBTTagCompound) ob;
                sList.add(c.getString("ID"));
            }

            basicNetwork.put(tag.getName(), sList);
        }
    }

    private void makeFile()
    {
        try
        {
            if (!dataFile.exists())
            {
                dataFile.createNewFile();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean networkExists(String UID)
    {
        return UID.equals(BLANK_IDENTIFIER) ? false : basicNetwork.containsKey(UID);
    }

    public boolean portalExists(String UID)
    {
        return UID.equals(BLANK_IDENTIFIER) ? false : portalLocations.containsKey(UID);
    }

    public void removePortal(String UID)
    {
        if (!UID.equals(BLANK_IDENTIFIER))
        {
            portalLocations.remove(UID);
        }
    }

    public void removePortalFromNetwork(String UID, String network)
    {
        if (isPortalInNetwork(UID, network))
        {
            getNetworkedPortals(network).remove(UID);
        }
    }

    public void saveAllData()
    {
        makeFile();
        NBTTagCompound mainTag = new NBTTagCompound();
        NBTTagCompound portalTag = new NBTTagCompound();
        NBTTagCompound networkTag = new NBTTagCompound();

        for (Entry<String, WorldCoordinates> entry : portalLocations.entrySet())
        {
            NBTTagCompound t = new NBTTagCompound();
            t.setInteger("X", entry.getValue().posX);
            t.setInteger("Y", entry.getValue().posY);
            t.setInteger("Z", entry.getValue().posZ);
            t.setInteger("D", entry.getValue().dimension);

            portalTag.setTag(entry.getKey(), t);
        }

        for (Entry<String, ArrayList<String>> entry : basicNetwork.entrySet())
        {
            NBTTagList t = new NBTTagList();

            for (String s : entry.getValue())
            {
                NBTTagCompound c = new NBTTagCompound();
                c.setString("ID", s);
                t.appendTag(c);
            }

            networkTag.setTag(entry.getKey(), t);
        }

        mainTag.setTag("PortalLocations", portalTag);
        mainTag.setTag("Networks", networkTag);

        try
        {
            NBTBase.writeNamedTag(mainTag, new DataOutputStream(new FileOutputStream(dataFile)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void updateExistingPortal(String oldUID, String newUID)
    {
        if (portalExists(oldUID) && !newUID.equals(BLANK_IDENTIFIER))
        {
            portalLocations.put(newUID, portalLocations.get(oldUID));
            removePortal(oldUID);
        }
    }

    public void updateExistingPortal(String oldUID, String newUID, WorldCoordinates newPos)
    {
        if (portalExists(oldUID) && !newUID.equals(BLANK_IDENTIFIER))
        {
            portalLocations.put(newUID, newPos);
            removePortal(oldUID);
        }
    }

    public void updateExistingPortal(String UID, WorldCoordinates newPos)
    {
        if (portalExists(UID))
        {
            removePortal(UID);
            portalLocations.put(UID, newPos);
        }
    }
    
    public ArrayList<String> getDestinationsExcluding(String network, String excludedUid)
    {
        ArrayList<String> strList = new ArrayList<String>();
        
        for (String s : getNetworkedPortals(network))
        {
            if (!s.equals(excludedUid))
            {
                strList.add(s);
            }
        }
        
        return strList;
    }
    
    public String getNextDestination(String network, String UID)
    {
        ArrayList<String> sList = getNetworkedPortals(network);
        
        if (!sList.contains(UID))
        {
            return null; // Should never happen, but let's make sure.
        }
        
        int index = sList.indexOf(UID);
        
        if (index == sList.size() - 1)
        {
            return sList.get(0);
        }
        else
        {
            return sList.get(index + 1);
        }
    }
}
