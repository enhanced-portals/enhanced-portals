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
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class NetworkManager
{
    /*** Stores locations of all portals ***/
    HashMap<GlyphIdentifier, WorldCoordinates> portalCoordinates;

    /*** Reverse lookup for {@link portalCoordinates} ***/
    HashMap<WorldCoordinates, GlyphIdentifier> portalCoordinatesReverse; // note: doesn't need to be saved seperately

    /***Portal Identifier, Network Identifier.
     * Used for looking up which portal is in which network, quickly. ***/
    HashMap<GlyphIdentifier, GlyphIdentifier> portalNetworks; // note: doesn't need to be saved seperately

    /*** Network Identifier, Portal Identifier List.
     * Used for looking up all portals in a network,
     * without searching every entry in {@link portalNetworks} ***/
    HashMap<GlyphIdentifier, ArrayList<GlyphIdentifier>> networkedPortals;

    File dataFile;
    MinecraftServer server;

    public NetworkManager(FMLServerStartingEvent event)
    {
        portalCoordinates = new HashMap<GlyphIdentifier, WorldCoordinates>();
        portalCoordinatesReverse = new HashMap<WorldCoordinates, GlyphIdentifier>();
        portalNetworks = new HashMap<GlyphIdentifier, GlyphIdentifier>();
        networkedPortals = new HashMap<GlyphIdentifier, ArrayList<GlyphIdentifier>>();
        server = event.getServer();
        dataFile = new File(EnhancedPortals.proxy.getWorldDir(), Reference.NAME + ".dat");

        try
        {
            loadAllData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        
        for (Object o : portalLocations.tagList)
        {
            NBTTagCompound t = (NBTTagCompound) o;
            
            addPortal(new GlyphIdentifier(t), new WorldCoordinates(t));
        }
        
        for (Object o : portalNetworks.tagList)
        {
            NBTTagCompound t = (NBTTagCompound) o;
            NBTTagList l = t.getTagList("Portals");
            
            GlyphIdentifier identifier = new GlyphIdentifier(t);
            
            for (Object obj : l.tagList)
            {
                NBTTagCompound tag = (NBTTagCompound) obj;                
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


    public void saveAllData()
    {
        makeFile();
        
        NBTTagCompound baseTag = new NBTTagCompound();
        NBTTagList portalLocations = new NBTTagList();
        NBTTagList portalNetworks = new NBTTagList();
        
        for (Entry<GlyphIdentifier, WorldCoordinates> entry : portalCoordinates.entrySet())
        {
            NBTTagCompound t = new NBTTagCompound();
            entry.getKey().writeToNBT(t);
            entry.getValue().writeToNBT(t);
            
            portalLocations.appendTag(t);
        }
        
        for (Entry<GlyphIdentifier, ArrayList<GlyphIdentifier>> entry : networkedPortals.entrySet())
        {
            NBTTagCompound t = new NBTTagCompound();
            NBTTagList list = new NBTTagList();
            
            entry.getKey().writeToNBT(t);
                        
            for (GlyphIdentifier i : entry.getValue())
            {
                NBTTagCompound c = new NBTTagCompound();
                i.writeToNBT(c);
                list.appendTag(c);
            }     
            
            t.setTag("Portals", list);
            portalNetworks.appendTag(t);
        }
        
        baseTag.setTag("PortalLocations", portalLocations);
        baseTag.setTag("PortalNetworks", portalNetworks);
        
        try
        {
            NBTBase.writeNamedTag(baseTag, new DataOutputStream(new FileOutputStream(dataFile)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /***
     * Gets the unique identifier of the specified controller
     * @return Null if one is not set
     */
    public GlyphIdentifier getPortalIdentifier(WorldCoordinates w)
    {
        return w == null ? null : portalCoordinatesReverse.get(w);
    }

    /***
     * Gets the network identifier of the specified controller
     * @return Null if one is not set
     */
    public GlyphIdentifier getPortalNetwork(GlyphIdentifier g)
    {
        return g == null ? null : portalNetworks.get(g);
    }

    public boolean hasIdentifier(WorldCoordinates w)
    {
        return w == null ? null : portalCoordinates.containsValue(w);
    }

    public boolean hasNetwork(GlyphIdentifier g)
    {
        return g == null ? null : networkedPortals.containsKey(g);
    }

    /***
     * Gets the world coordinates of the specified controller
     * @return Null if one is not found
     */
    public WorldCoordinates getPortalLocation(GlyphIdentifier g)
    {
        return g == null ? null : portalCoordinates.get(g);
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

        portalCoordinates.put(g, w);
        portalCoordinatesReverse.put(w, g);
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
    public void removePortal(WorldCoordinates w)
    {
        removePortal(getPortalIdentifier(w), w);
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
        
        portalCoordinates.remove(g);
        portalCoordinatesReverse.remove(w);
    }
    
    /***
     * Removes a portal from a network
     */
    public void removePortalFromNetwork(GlyphIdentifier portal, GlyphIdentifier network)
    {
        if (portal == null || network == null || getPortalNetwork(portal) != null)
        {
            return;
        }
        
        getNetwork(network).remove(portal);
        portalNetworks.remove(portal);
    }
    
    /***
     * Adds a portal to a network
     */
    public void addPortalToNetwork(GlyphIdentifier portal, GlyphIdentifier network)
    {
        if (portal == null || network == null || getPortalNetwork(portal) != null)
        {
            return;
        }
        
        getNetwork(network).add(portal);
        portalNetworks.put(portal, network);
    }
    
    /***
     * Creates a new network if one does not already exist
     */
    private void addNetwork(GlyphIdentifier network)
    {
        if (networkedPortals.get(network) == null)
        {
            networkedPortals.put(network, new ArrayList<GlyphIdentifier>());
        }
    }
    
    /***
     * Retrieves all the portals for the specified network. Will create a network if one does not already exist
     */
    private ArrayList<GlyphIdentifier> getNetwork(GlyphIdentifier network)
    {
        addNetwork(network);
        
        return networkedPortals.get(network);
    }
}
