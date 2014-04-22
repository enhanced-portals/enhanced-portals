package uk.co.shadeddimensions.ep3.portal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import enhancedportals.EnhancedPortals;

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

    File dataFile;
    MinecraftServer server;

    public NetworkManager(FMLServerStartingEvent event)
    {
        portalCoordinates = new HashMap<String, WorldCoordinates>();
        portalCoordinatesReverse = new HashMap<WorldCoordinates, String>();
        portalNetworks = new HashMap<String, String>();
        networkedPortals = new HashMap<String, ArrayList<String>>();
        server = event.getServer();
        dataFile = new File(EnhancedPortals.proxy.getWorldDir(), EnhancedPortals.NAME + ".dat");

        try
        {
            loadAllData();
        }
        catch (Exception e)
        {
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

        TileEntity tile = w.getBlockTileEntity();

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
        if (makeFile())
        {
            return;
        }

        NBTTagCompound baseTag = (NBTTagCompound) NBTBase.readNamedTag(new DataInputStream(new FileInputStream(dataFile)));

        if (baseTag == null)
        {
            return;
        }

        NBTTagList portalLocations = baseTag.getTagList("PortalLocations");
        NBTTagList portalNetworks = baseTag.getTagList("PortalNetworks");

        for (int i = 0; i < portalLocations.tagCount(); i++)
        {
            NBTTagCompound t = (NBTTagCompound) portalLocations.tagAt(i);

            addPortal(new GlyphIdentifier(t), new WorldCoordinates(t));
        }

        for (int i = 0; i < portalNetworks.tagCount(); i++)
        {
            NBTTagCompound t = (NBTTagCompound) portalNetworks.tagAt(i);
            NBTTagList l = t.getTagList("Portals");

            GlyphIdentifier identifier = new GlyphIdentifier(t);

            for (int j = 0; j < l.tagCount(); j++)
            {
                NBTTagCompound tag = (NBTTagCompound) l.tagAt(j);
                addPortalToNetwork(new GlyphIdentifier(tag), identifier);
            }
        }
    }

    private boolean makeFile()
    {
        try
        {
            if (!dataFile.exists())
            {
                dataFile.createNewFile();
                return true;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
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
        makeFile();

        NBTTagCompound baseTag = new NBTTagCompound();
        NBTTagList portalLocations = new NBTTagList();
        NBTTagList portalNetworks = new NBTTagList();

        for (Entry<String, WorldCoordinates> entry : portalCoordinates.entrySet())
        {
            NBTTagCompound t = new NBTTagCompound();
            t.setString("GlyphIdentifier", entry.getKey());
            entry.getValue().writeToNBT(t);

            portalLocations.appendTag(t);
        }

        for (Entry<String, ArrayList<String>> entry : networkedPortals.entrySet())
        {
            if (entry.getValue().isEmpty())
            {
                continue;
            }

            NBTTagCompound t = new NBTTagCompound();
            NBTTagList list = new NBTTagList();

            t.setString("GlyphIdentifier", entry.getKey());

            for (String i : entry.getValue())
            {
                NBTTagCompound c = new NBTTagCompound();
                c.setString("GlyphIdentifier", i);
                list.appendTag(c);
            }

            t.setTag("Portals", list);
            portalNetworks.appendTag(t);
        }

        baseTag.setTag("PortalLocations", portalLocations);
        baseTag.setTag("PortalNetworks", portalNetworks);

        try
        {
            DataOutputStream s = new DataOutputStream(new FileOutputStream(dataFile));
            NBTBase.writeNamedTag(baseTag, s);
            s.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean portalIdentifierExists(GlyphIdentifier id)
    {
        return portalCoordinates.containsKey(id.getGlyphString());
    }
}
