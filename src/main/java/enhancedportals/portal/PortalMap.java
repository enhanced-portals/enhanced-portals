package enhancedportals.portal;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import enhancedportals.tile.TileDimensionalBridgeStabilizerController;
import enhancedportals.util.DimensionCoordinates;

/***
 * Network map for UID and Portal <--> DBS.
 */
public class PortalMap {
    HashMap<GlyphSequence, DimensionCoordinates> portalMap = new HashMap<GlyphSequence, DimensionCoordinates>(); // UID -> Coord
    HashMap<DimensionCoordinates, GlyphSequence> portalMapReverse = new HashMap<DimensionCoordinates, GlyphSequence>(); // Coord -> UID
    HashMap<GlyphSequence, DimensionCoordinates> dbsMap = new HashMap<GlyphSequence, DimensionCoordinates>(); // UID -> DBS
    HashMap<DimensionCoordinates, ArrayList<GlyphSequence>> dbsMapList = new HashMap<DimensionCoordinates, ArrayList<GlyphSequence>>(); // DBS -> UID[]
    
    /*** 
     * Looks up the specified portal's UID
     * @param c DimensionCoordinates of the Portal Controller
     * @return GlyphSequence of the UID
     */
    public GlyphSequence getPortalGlyphs(DimensionCoordinates c) {
        return portalMapReverse.get(c);
    }
    
    /***
     * Looks up the specified portal's location
     * @param g GlyphSequence of the portal's UID
     * @return DimensionCoordinates of the Portal Controller
     */
    public DimensionCoordinates getPortalLocation(GlyphSequence g) {
        return portalMap.get(g);
    }
    
    /***
     * Looks up the portal's linked Dimensional Bridge Stabilizer
     * @param g GlyphSequence of the portal's UID
     * @return DimensionCoordinates of the Dimensional Bridge Stabilizer
     */
    public DimensionCoordinates getPortalDBS(GlyphSequence g) {
        return dbsMap.get(g);
    }
    
    /***
     * Gets all UIDs of every portal connected to the specified Dimensional Bridge Stabilizer
     * @param c DimensionCoordinates of the Dimensional Bridge Stabilizer
     * @return GlyphSequence[] containing every connected portal UID
     */
    public GlyphSequence[] getDBSPortals(DimensionCoordinates c) {
        ArrayList<GlyphSequence> list = dbsMapList.get(c);
        if (list == null || list.isEmpty()) return new GlyphSequence[0];
        return list.toArray(new GlyphSequence[] {});
    }
    
    /***
     * Checks to see if the UID is in use
     * @param g GlyphSequence of the UID to check
     * @return true if in use
     */
    boolean isIdentifierInUse(GlyphSequence g) {
        return portalMap.containsKey(g);
    }
    
    /***
     * Checks to see if the specified location contains a valid (formed) Dimensional Bridge Stabilizer
     * @param d DimensionCoordinates of the location to check
     * @return true if it is a valid location
     */
    boolean isValidDBS(DimensionCoordinates d) {
        if (d == null) return false;
        WorldServer w = DimensionManager.getWorld(d.posD);
        if (w == null) return false;
        TileEntity t = w.getTileEntity(d.posX, d.posY, d.posZ);
        
        if (t != null && t instanceof TileDimensionalBridgeStabilizerController) {
            return true;
        }
        
        return false;
    }
    
    /***
     * Sets the UID of the specified Portal Controller
     * @param c DimensionCoordinates of the Portal Controller
     * @param g GlyphSequence of the UID to set
     * @return true if successful
     */
    public boolean setPortalGlyphs(DimensionCoordinates c, GlyphSequence g) {
        if (g.isEmpty() || isIdentifierInUse(g)) return false;
        
        if (portalMapReverse.containsKey(c)) {
            GlyphSequence old = portalMapReverse.get(c);
            
            if (dbsMap.containsKey(old)) {
                DimensionCoordinates d = dbsMap.get(old);
                dbsMap.remove(old);
                dbsMapList.get(d).remove(old);
                dbsMap.put(g, d);
                
                if (dbsMapList.containsKey(d)) {
                    dbsMapList.get(d).add(g);
                } else {
                    ArrayList<GlyphSequence> list = new ArrayList<GlyphSequence>();
                    list.add(g);
                    dbsMapList.put(d, list);
                }
            }
            
            portalMapReverse.remove(c);
            portalMap.remove(old);
        }
        
        portalMap.put(g, c);
        portalMapReverse.put(c, g);
        return true;
    }
    
    /***
     * Clears a portals UID
     * @param c DimensionCoordinates of the Portal Controller to clear of its UID
     * @return true if successful (will only fail if it doesn't have a UID)
     */
    public boolean removePortalGlyphs(DimensionCoordinates c) {
        GlyphSequence g = portalMapReverse.get(c);
        if (g == null) return false;
        
        if (dbsMap.containsKey(g)) {
            removePortalDBS(g);
        }
        
        portalMapReverse.remove(c);
        portalMap.remove(g);
        return true;
    }
    
    /***
     * Clears a portals UID
     * @param g GlyphSequence of the UID of the Portal to clear
     * @return true if successful
     */
    public boolean removePortalGlyphs(GlyphSequence g) {
        if (!portalMapReverse.containsKey(g)) return false;
        DimensionCoordinates c = portalMap.get(g);
        
        if (dbsMap.containsKey(g)) {
            removePortalDBS(g);
        }
        
        portalMap.remove(g);
        portalMapReverse.remove(c);
        return true;
    }
    
    /***
     * Sets the specified Portal's DBS
     * @param c DimensionCoordinates of the Portal Controller
     * @param d DimensionCoordinates of the Dimensional Bridge Stabilizer
     * @return true if successful
     */
    public boolean setPortalDBS(DimensionCoordinates c, DimensionCoordinates d) {
        System.out.println(dbsMap.size());
        
        if (!isValidDBS(d)) return false;
        GlyphSequence g = getPortalGlyphs(c);
        if (g == null || dbsMap.containsKey(g)) return false;
        dbsMap.put(g, d);
        
        if (dbsMapList.containsKey(d)) {
            dbsMapList.get(d).add(g);
        } else {
            ArrayList<GlyphSequence> list = new ArrayList<GlyphSequence>();
            list.add(g);
            dbsMapList.put(d, list);
        }
        
        return true;
    }
    
    /***
     * Sets the specified Portal's DBS
     * @param g GlyphSequence of the portal to connect to the DBS
     * @param d DimensionCoordinates of the Dimensional Bridge Stabilizer
     * @return true if successful
     */
    public boolean setPortalDBS(GlyphSequence g, DimensionCoordinates d) {
        if (!isValidDBS(d)) return false;
        if (!portalMap.containsKey(g)) return false;
        dbsMap.put(g, d);
        
        if (dbsMapList.containsKey(d)) {
            dbsMapList.get(d).add(g);
        } else {
            ArrayList<GlyphSequence> list = new ArrayList<GlyphSequence>();
            list.add(g);
            dbsMapList.put(d, list);
        }
        
        return true;
    }
    
    /***
     * Removes a portals link to the DBS
     * @param c DimensionCoordinates of the Portal Controller
     * @return true if successful
     */
    public boolean removePortalDBS(DimensionCoordinates c) {
        GlyphSequence g = getPortalGlyphs(c);
        if (g == null) return false;
        DimensionCoordinates d = dbsMap.get(g);
        if (d == null) return false;
        dbsMap.remove(g);
        dbsMapList.get(d).remove(g);
        return true;
    }
    
    /***
     * Removes a portals link to the DBS
     * @param g GlyphSequence of the UID of the portal to clear
     * @return true if successful
     */
    public boolean removePortalDBS(GlyphSequence g) {
        if (!portalMap.containsKey(g)) return false;
        DimensionCoordinates d = dbsMap.get(g);
        if (d == null) return false;
        dbsMap.remove(g);
        dbsMapList.get(d).remove(g);
        return true;
    }
    
    /***
     * Removes a Dimensional Bridge Stabilizer. Clears every portal attached to it.
     * @param d DimensionCoordinates of the DBS to remove
     */
    public void removeDBS(DimensionCoordinates d) {
        if (dbsMapList.containsKey(d)) {
            for (GlyphSequence g : dbsMapList.get(d)) {
                dbsMap.remove(g);
            }
            
            dbsMapList.remove(d);
        }
    }
    
    /***
     * Loads all network data from json files
     */
    public void load() {
        // TODO
    }
    
    /***
     * Saves all network data to json files
     */
    public void save() {
        // TODO
    }
}
