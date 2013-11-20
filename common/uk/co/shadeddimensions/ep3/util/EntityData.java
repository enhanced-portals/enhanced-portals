package uk.co.shadeddimensions.ep3.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class EntityData
{
    public String EntityDisplayName;
    public Class<? extends Entity> EntityClass;
    public boolean isInverted;
    public byte checkType;

    public EntityData()
    {

    }

    public EntityData(String name, Class<? extends Entity> clazz, boolean inverted, byte type)
    {
        EntityDisplayName = name;
        EntityClass = clazz;
        isInverted = inverted;
        checkType = type;
    }

    public void saveToNBT(NBTTagCompound t)
    {
        if (getEntityID(EntityClass) == 0)
        {
            return;
        }
        
        t.setString("Name", EntityDisplayName);
        t.setInteger("ID", getEntityID(EntityClass));
        t.setBoolean("Inverted", isInverted);
        t.setByte("CheckType", checkType);
    }

    public EntityData readFromNBT(NBTTagCompound t)
    {
        EntityDisplayName = t.getString("Name");
        EntityClass = getClassFromID(t.getInteger("ID"));
        isInverted = t.getBoolean("Inverted");
        checkType = t.getByte("CheckType");
        return this;
    }

    public boolean shouldCheckName()
    {
        return checkType == 0;
    }
    
    public boolean shouldCheckClass()
    {
        return checkType == 1;
    }
    
    public boolean shouldCheckNameAndClass()
    {
        return checkType == 2;
    }
    
    public static int getEntityID(Class<? extends Entity> clazz)
    {
        if (EntityPlayer.class.equals(clazz))
        {
            return -1;
        }
        else if (EntityMob.class.equals(clazz))
        {
            return -2;
        }
        else if (EntityAnimal.class.equals(clazz))
        {
            return -3;
        }
        else if (EntityList.classToIDMapping.containsKey(clazz))
        {
            return Integer.parseInt(EntityList.classToIDMapping.get(clazz).toString());
        }
        
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends Entity> getClassFromID(int id)
    {
        if (id == 0)
        {
            return null;
        }
        else if (id == -1)
        {
            return EntityPlayer.class;
        }
        else if (id == -2)
        {
            return EntityMob.class;
        }
        else if (id == -3)
        {
            return EntityAnimal.class;
        }

        return EntityList.getClassFromID(id);
    }

    public static String getClassDisplayName(EntityData data)
    {
        if (EntityPlayer.class.equals(data.EntityClass))
        {
            return "Player";
        }
        else if (EntityMob.class.equals(data.EntityClass))
        {
            return "Monster";
        }
        else if (EntityAnimal.class.equals(data.EntityClass))
        {
            return "Animal";
        }
        else if (EntityList.classToIDMapping.containsKey(data.EntityClass))
        {
            return (String) EntityList.classToStringMapping.get(data.EntityClass);
        }
        
        return "Unknown";
    }

    public static int getParentEntityID(Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            return -1;
        }
        else if (entity instanceof EntityMob)
        {
            return -2;
        }
        else if (entity instanceof EntityAnimal)
        {
            return -3;
        }
        
        return 0;
    }
}
