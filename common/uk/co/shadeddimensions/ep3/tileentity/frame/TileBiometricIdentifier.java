package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;

public class TileBiometricIdentifier extends TilePortalPart
{
    ArrayList<Class<? extends Entity>> sendingEntityTypes, recievingEntityTypes;
    boolean sendingType, recievingType, isActive, hasSeperateLists; // type: false = excluding all but these, true = excluding these
    
    public TileBiometricIdentifier()
    {
        sendingEntityTypes = new ArrayList<Class<? extends Entity>>();
        recievingEntityTypes = new ArrayList<Class<? extends Entity>>();
        sendingType = recievingType = hasSeperateLists = false;
        isActive = true;
        
        hasSeperateLists = true;
        recievingType = true;
        
        sendingEntityTypes.add(EntityPig.class);
        recievingEntityTypes.add(EntityCow.class);
    }
    
    public boolean canEntityBeSent(Entity entity)
    {        
        if (!isActive)
        {
            return true;
        }
        
        for (Class<? extends Entity> c : sendingEntityTypes)
        {
            if (c.isInstance(entity))
            {
                return sendingType ? false : true;
            }
        }
        
        return sendingType ? true : false;
    }

    public boolean canEntityBeRecieved(Entity entity)
    {
        if (!isActive)
        {
            return true;
        }
        
        for (Class<? extends Entity> c : hasSeperateLists ? recievingEntityTypes : sendingEntityTypes)
        {
            if (c.isInstance(entity))
            {
                return (hasSeperateLists ? recievingType : sendingType) ? false : true;
            }
        }
        
        return (hasSeperateLists ? recievingType : sendingType) ? true : false;
    }
}
