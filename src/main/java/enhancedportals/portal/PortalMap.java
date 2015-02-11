package enhancedportals.portal;

import java.util.ArrayList;
import java.util.HashMap;

import enhancedportals.util.DimensionCoordinates;

/***
 * Network map for UID and Portal <--> DBS.
 */
public class PortalMap {
    HashMap<GlyphSequence, DimensionCoordinates> portalMap = new HashMap<GlyphSequence, DimensionCoordinates>(); // UID -> Coord
    HashMap<DimensionCoordinates, GlyphSequence> portalMapReverse = new HashMap<DimensionCoordinates, GlyphSequence>(); // Coord -> UID
    HashMap<GlyphSequence, DimensionCoordinates> dbsMap = new HashMap<GlyphSequence, DimensionCoordinates>(); // UID -> DBS
    HashMap<DimensionCoordinates, ArrayList<GlyphSequence>> dbsMapList = new HashMap<DimensionCoordinates, ArrayList<GlyphSequence>>(); // DBS -> UID[]
    
    public GlyphSequence getPortalGlyphs(DimensionCoordinates c) {
        return portalMapReverse.get(c);
    }
    
    public DimensionCoordinates getPortalLocation(GlyphSequence g) {
        return portalMap.get(g);
    }
    
    public DimensionCoordinates getPortalDBS(GlyphSequence g) {
        return dbsMap.get(g);
    }
    
    public GlyphSequence[] getDBSPortals(DimensionCoordinates c) {
        ArrayList<GlyphSequence> list = dbsMapList.get(c);
        if (list == null || list.isEmpty()) return new GlyphSequence[0];
        return list.toArray(new GlyphSequence[] {});
    }
    
    boolean isIdentifierInUse(GlyphSequence g) {
        return portalMap.containsKey(g);
    }
    
    boolean isValidDBS(DimensionCoordinates d) {
        // TODO
        return true;
    }
    
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
    
    public boolean clearPortalGlyphs(DimensionCoordinates c) {
        GlyphSequence g = portalMapReverse.get(c);
        if (g == null) return false;
        
        if (dbsMap.containsKey(g)) {
            removePortalDBS(g);
        }
        
        portalMapReverse.remove(c);
        portalMap.remove(g);
        return true;
    }
    
    public boolean clearPortalGlyphs(GlyphSequence g) {
        if (!portalMapReverse.containsKey(g)) return false;
        DimensionCoordinates c = portalMap.get(g);
        
        if (dbsMap.containsKey(g)) {
            removePortalDBS(g);
        }
        
        portalMap.remove(g);
        portalMapReverse.remove(c);
        return true;
    }
    
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
    
    public boolean removePortalDBS(DimensionCoordinates c) {
        GlyphSequence g = getPortalGlyphs(c);
        if (g == null) return false;
        DimensionCoordinates d = dbsMap.get(g);
        if (d == null) return false;
        dbsMap.remove(g);
        dbsMapList.get(d).remove(g);
        return true;
    }
    
    public boolean removePortalDBS(GlyphSequence g) {
        if (!portalMap.containsKey(g)) return false;
        DimensionCoordinates d = dbsMap.get(g);
        if (d == null) return false;
        dbsMap.remove(g);
        dbsMapList.get(d).remove(g);
        return true;
    }
    
    public void removeDBS(DimensionCoordinates d) {
        if (dbsMapList.containsKey(d)) {
            for (GlyphSequence g : dbsMapList.get(d)) {
                dbsMap.remove(g);
            }
            
            dbsMapList.remove(d);
        }
    }
    
    public void load() {
        // TODO
    }
    
    public void save() {
        // TODO
    }
}
