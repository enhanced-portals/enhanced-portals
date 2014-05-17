package enhancedportals.portal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import enhancedportals.EnhancedPortals;
import enhancedportals.tileentity.TileController;
import enhancedportals.utility.WorldCoordinates;

public class NetworkManager
{
    /*** Stores locations of all portals ***/
    HashMap<String, WorldCoordinates> portalCoordinates;

    /*** Reverse lookup for {@link portalCoordinates} ***/
    HashMap<WorldCoordinates, String> portalCoordinatesReverse;

    /***
     * Portal Identifier, Network Identifier. Used for looking up which portal is in which network, quickly.
     ***/
    HashMap<String, String> portalNetworks;

    /***
     * Network Identifier, Portal Identifier List. Used for looking up all portals in a network, without searching every entry in {@link portalNetworks}
     ***/
    HashMap<String, ArrayList<String>> networkedPortals;

    File portalFile, networkFile;
    MinecraftServer server;

    public NetworkManager(FMLServerStartingEvent event)
    {
        portalCoordinates = new HashMap<String, WorldCoordinates>();
        portalCoordinatesReverse = new HashMap<WorldCoordinates, String>();
        portalNetworks = new HashMap<String, String>();
        networkedPortals = new HashMap<String, ArrayList<String>>();
        server = event.getServer();
        portalFile = new File(EnhancedPortals.proxy.getWorldDir(), "EP3_PortalLocations.json");
        networkFile = new File(EnhancedPortals.proxy.getWorldDir(), "EP3_PortalNetworks.json");

        try
        {
            loadAllData();
        }
        catch (Exception e)
        {
            EnhancedPortals.logger.catching(e);
            e.printStackTrace();
        }
    }

    /***
     * Creates a new network if one does not already exist
     */
    private void addNetwork(GlyphIdentifier network)
    {
        if (networkedPortals.get(network.getGlyphString()) == null)
        {
            networkedPortals.put(network.getGlyphString(), new ArrayList<String>());
        }
    }

    /***
     * Adds a new portal to the system
     */
    public void addPortal(GlyphIdentifier g, WorldCoordinates w)
    {
        if (getPortalIdentifier(w) != null || getPortalLocation(g) != null)
        {
            return;
        }

        portalCoordinates.put(g.getGlyphString(), w);
        portalCoordinatesReverse.put(w, g.getGlyphString());
    }

    /***
     * Adds a portal to a network
     */
    public void addPortalToNetwork(GlyphIdentifier portal, GlyphIdentifier network)
    {
        if (portal == null || network == null || getPortalNetwork(portal) != null)
        {
            System.out.println("VOID");
            return;
        }

        getNetwork(network).add(portal.getGlyphString());
        portalNetworks.put(portal.getGlyphString(), network.getGlyphString());
    }

    public GlyphIdentifier getDestination(GlyphIdentifier identifier, GlyphIdentifier portalNetwork)
    {
        ArrayList<String> network = getNetwork(portalNetwork);
        int index = network.indexOf(identifier.getGlyphString());

        if (index == network.size() - 1)
        {
            return new GlyphIdentifier(network.get(0));
        }
        else
        {
            return new GlyphIdentifier(network.get(index + 1));
        }
    }

    /***
     * Retrieves all the portals for the specified network. Will create a network if one does not already exist
     */
    private ArrayList<String> getNetwork(GlyphIdentifier network)
    {
        addNetwork(network);

        return networkedPortals.get(network.getGlyphString());
    }

    public int getNetworkSize(GlyphIdentifier nID)
    {
        ArrayList<String> list = getNetwork(nID);
        return list.isEmpty() ? -1 : list.size();
    }

    /***
     * Gets the portal controller for the specified portal identifier
     */
    public TileController getPortalController(GlyphIdentifier portal)
    {
        WorldCoordinates w = getPortalLocation(portal);

        if (w == null)
        {
            return null;
        }

        TileEntity tile = w.getTileEntity();

        if (tile == null || !(tile instanceof TileController))
        {
            return null;
        }

        return (TileController) tile;
    }

    /***
     * Gets the unique identifier of the specified controller
     * 
     * @return Null if one is not set
     */
    public GlyphIdentifier getPortalIdentifier(WorldCoordinates w)
    {
        if (w == null)
        {
            return null;
        }

        String ID = portalCoordinatesReverse.get(w);

        return ID == null ? null : new GlyphIdentifier(portalCoordinatesReverse.get(w));
    }

    /***
     * Gets the world coordinates of the specified controller
     * 
     * @return Null if one is not found
     */
    public WorldCoordinates getPortalLocation(GlyphIdentifier g)
    {
        return g == null ? null : portalCoordinates.get(g.getGlyphString());
    }

    /***
     * Gets the network identifier of the specified controller
     * 
     * @return Null if one is not set
     */
    public GlyphIdentifier getPortalNetwork(GlyphIdentifier g)
    {
        if (g == null)
        {
            return null;
        }

        String ID = portalNetworks.get(g.getGlyphString());

        return ID == null ? null : new GlyphIdentifier(portalNetworks.get(g.getGlyphString()));
    }

    public boolean hasIdentifier(WorldCoordinates w)
    {
        return w == null ? null : portalCoordinates.containsValue(w);
    }

    public boolean hasNetwork(GlyphIdentifier g)
    {
        return g == null ? null : portalNetworks.containsKey(g.getGlyphString());
    }

    public boolean hasNetwork(WorldCoordinates w)
    {
        return w == null ? null : hasNetwork(getPortalIdentifier(w));
    }

    public void loadAllData() throws Exception
    {
        if (!makeFiles())
        {
            return;
        }

        Type type = new TypeToken<HashMap<String, WorldCoordinates>>()
        {
        }.getType();
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String portalData = FileUtils.readFileToString(portalFile), networkData = FileUtils.readFileToString(networkFile);

        portalCoordinates = gson.fromJson(portalData, type);
        portalNetworks = gson.fromJson(networkData, portalNetworks.getClass());

        if (portalCoordinates == null) // because for some reason fromJson can return null
        {
            portalCoordinates = new HashMap<String, WorldCoordinates>();
        }

        if (portalNetworks == null) // because for some reason fromJson can return null
        {
            portalNetworks = new HashMap<String, String>();
        }

        if (!portalCoordinates.isEmpty())
        {
            for (Entry<String, WorldCoordinates> entry : portalCoordinates.entrySet())
            {
                portalCoordinatesReverse.put(entry.getValue(), entry.getKey());
            }
        }

        if (!portalNetworks.isEmpty())
        {
            for (Entry<String, String> entry : portalNetworks.entrySet())
            {
                if (networkedPortals.containsKey(entry.getValue()))
                {
                    networkedPortals.get(entry.getValue()).add(entry.getKey());
                }
                else
                {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(entry.getKey());
                    networkedPortals.put(entry.getValue(), list);
                }
            }
        }
    }

    private boolean makeFiles()
    {
        try
        {
            if (!portalFile.exists())
            {
                portalFile.createNewFile();
            }

            if (!networkFile.exists())
            {
                networkFile.createNewFile();
            }

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean portalIdentifierExists(GlyphIdentifier id)
    {
        return portalCoordinates.containsKey(id.getGlyphString());
    }

    /***
     * Removes a portal
     */
    public void removePortal(GlyphIdentifier g)
    {
        removePortal(g, getPortalLocation(g));
    }

    /***
     * Removes a portal
     */
    public void removePortal(GlyphIdentifier g, WorldCoordinates w)
    {
        if (g == null || w == null)
        {
            return;
        }

        GlyphIdentifier n = getPortalNetwork(g);

        if (n != null)
        {
            removePortalFromNetwork(g, n);
        }

        portalCoordinates.remove(g.getGlyphString());
        portalCoordinatesReverse.remove(w);
    }

    /***
     * Removes a portal
     */
    public void removePortal(WorldCoordinates w)
    {
        removePortal(getPortalIdentifier(w), w);
    }

    /***
     * Removes a portal from a network
     */
    public void removePortalFromNetwork(GlyphIdentifier portal, GlyphIdentifier network)
    {
        if (portal == null || network == null)
        {
            return;
        }

        getNetwork(network).remove(portal.getGlyphString());
        portalNetworks.remove(portal.getGlyphString());
    }

    public void saveAllData()
    {
        makeFiles();

        try
        {
            Gson gson = new GsonBuilder().create();
            FileWriter portalWriter = new FileWriter(portalFile), networkWriter = new FileWriter(networkFile);

            gson.toJson(portalCoordinates, portalWriter);
            gson.toJson(portalNetworks, networkWriter);

            portalWriter.close();
            networkWriter.close();
        }
        catch (Exception e)
        {
            EnhancedPortals.logger.catching(e);
        }
    }
}
