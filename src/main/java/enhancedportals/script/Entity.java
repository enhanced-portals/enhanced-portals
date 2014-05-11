package enhancedportals.script;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class Entity
{
    private static net.minecraft.entity.Entity getEntity(int worldID, int id) throws ScriptException
    {
        net.minecraft.entity.Entity e = ScriptCommon.getWorld(worldID).getEntityByID(id);
        
        if (e == null)
        {
            throw new ScriptException("Could not find an entity with that ID");
        }
        
        return e;
    }
    
    private static EntityLivingBase getEntityLivingBase(int worldID, int id) throws ScriptException
    {
        net.minecraft.entity.Entity entity = getEntity(worldID, id);
        
        if (entity instanceof EntityLivingBase)
        {
            return (EntityLivingBase) entity;
        }
        
        throw new ScriptException("Invalid entity");
    }
    
    public static String getEntityName(int worldID, int id) throws ScriptException
    {
        return getEntity(worldID, id).getCommandSenderName();
    }
    
    public static String getEntityType(int worldID, int id) throws ScriptException
    {
        return getEntity(worldID, id).getClass().getSimpleName();
    }
    
    public static boolean isRiding(int worldID, int id) throws ScriptException
    {
        return getEntity(worldID, id).isRiding();
    }
    
    public static boolean isBeingRidden(int worldID, int id) throws ScriptException
    {
        return getEntity(worldID, id).riddenByEntity != null;
    }
    
    public static boolean isPlayer(int worldID, int id) throws ScriptException
    {
        return getEntity(worldID, id) instanceof EntityPlayer;
    }
    
    public static boolean isMonster(int worldID, int id) throws ScriptException
    {
        return getEntity(worldID, id) instanceof EntityMob;
    }
    
    public static boolean isAnimal(int worldID, int id) throws ScriptException
    {
        return getEntity(worldID, id) instanceof EntityLiving;
    }
    
    public static int getHealth(int worldID, int id) throws ScriptException
    {
        return (int) getEntityLivingBase(worldID, id).getHealth();
    }
    
    public static int getMaxHealth(int worldID, int id) throws ScriptException
    {
        return (int) getEntityLivingBase(worldID, id).getMaxHealth();
    }
    
    public static void attack(int worldID, int id, int amount) throws ScriptException
    {
        getEntityLivingBase(worldID, id).attackEntityFrom(ScriptCommon.damageSource, amount);
    }
    
    public static void sendMessage(int worldID, int id, String message) throws ScriptException
    {
        net.minecraft.entity.Entity e = getEntity(worldID, id);
        
        if (e instanceof EntityPlayer)
        {
            ((EntityPlayer) e).addChatComponentMessage(new ChatComponentText(message));
        }
    }
}
