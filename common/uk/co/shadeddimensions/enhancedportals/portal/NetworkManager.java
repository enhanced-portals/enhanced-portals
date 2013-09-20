package uk.co.shadeddimensions.enhancedportals.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.enhancedportals.util.WorldCoordinates;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class NetworkManager
{
           //  UID   , Location
    Map<String, WorldCoordinates> portalLocations;
           //  UID   , Portals UIDs
    Map<String, ArrayList<String>> basicNetwork;
    
    public NetworkManager(FMLServerStartingEvent event)
    {
        portalLocations = new HashMap<String, WorldCoordinates>();
        basicNetwork = new HashMap<String, ArrayList<String>>();
    }
    
    public ChunkCoordinates getPortalLocation(String UID)
    {
        return portalLocations.get(UID);
    }
    
    public ArrayList<String> getNetworkedPortals(String UID)
    {
        return networkExists(UID) ? basicNetwork.get(UID) : new ArrayList<String>();
    }
    
    public boolean portalExists(String UID)
    {
        return portalLocations.containsKey(UID);
    }
    
    public boolean networkExists(String UID)
    {
        return basicNetwork.containsKey(UID);
    }
    
    public boolean isPortalInNetwork(String UID, String networkID)
    {
        return getNetworkedPortals(networkID).contains(UID);
    }
    
    public void addNewPortal(String UID, WorldCoordinates pos)
    {
        if (!portalExists(UID))
        {
            portalLocations.put(UID, pos);
        }
    }
    
    public void updateExistingPortal(String oldUID, String newUID)
    {
        if (portalExists(oldUID))
        {
            portalLocations.put(newUID, portalLocations.get(oldUID));
            removePortal(oldUID);
        }
    }
    
    public void updateExistingPortal(String oldUID, String newUID, WorldCoordinates newPos)
    {
        if (portalExists(oldUID))
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
    
    public void removePortal(String UID)
    {
        portalLocations.remove(UID);
    }
    
    public void addPortalToNetwork(String UID, String network)
    {
        if (!isPortalInNetwork(UID, network))
        {
            if (!networkExists(network))
            {
                basicNetwork.put(network, new ArrayList<String>());
            }
            
            getNetworkedPortals(network).add(UID);
        }
    }
    
    public void removePortalFromNetwork(String UID, String network)
    {
        if (isPortalInNetwork(UID, network))
        {
            getNetworkedPortals(network).remove(UID);
        }
    }
}
