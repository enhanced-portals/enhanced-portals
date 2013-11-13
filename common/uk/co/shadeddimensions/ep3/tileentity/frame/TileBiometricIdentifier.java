package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class TileBiometricIdentifier extends TilePortalPart
{
    public class EntityPair
    {
        public String name;
        public Class<? extends Entity> clas;

        /***
         * <pre>
         * 0 - Match getEntityName()
         * 1 - Match getClass().isInstance(...)
         * 2 - Match getClass().getSuperClass().isInstance(...)
         * 3 - Match getEntityName() && getClass().isInstance(...)
         * </pre>
         */
        public byte fuzzy;
        public boolean inverted;

        public EntityPair(String n, Class<? extends Entity> c, byte f, boolean b)
        {
            name = n;
            clas = c;
            fuzzy = f;
            inverted = b;
        }
    }

    public ArrayList<EntityPair> sendingEntityTypes, recievingEntityTypes;
    public boolean notFoundSend, notFoundRecieve, isActive, hasSeperateLists;

    public TileBiometricIdentifier()
    {
        sendingEntityTypes = new ArrayList<EntityPair>();
        recievingEntityTypes = new ArrayList<EntityPair>();
        hasSeperateLists = false;
        notFoundSend = notFoundRecieve = true;
        isActive = true;
    }

    public boolean canEntityBeSent(Entity entity)
    {
        if (!isActive)
        {
            return true;
        }

        boolean wasFound = false;

        for (EntityPair pair : sendingEntityTypes)
        {            
            if (pair.fuzzy == 0 && pair.name.equals(entity.getEntityName()))
            {
                if (!pair.inverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (pair.fuzzy == 1 && pair.clas.isInstance(entity))
            {
                if (!pair.inverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (pair.fuzzy == 2 && pair.clas.getSuperclass().isInstance(entity))
            {
                if (!pair.inverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (pair.fuzzy == 3 && pair.name.equals(entity.getEntityName()) && pair.clas.isInstance(entity))
            {
                if (!pair.inverted)
                {
                    return true;
                }

                wasFound = true;
            }
        }

        return !wasFound ? notFoundSend : false;
    }

    public boolean canEntityBeRecieved(Entity entity)
    {
        if (!isActive)
        {
            return true;
        }

        boolean wasFound = false;

        for (EntityPair pair : hasSeperateLists ? recievingEntityTypes : sendingEntityTypes)
        {            
            if (pair.fuzzy == 0 && pair.name.equals(entity.getEntityName()))
            {
                if (!pair.inverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (pair.fuzzy == 1 && pair.clas.isInstance(entity))
            {
                if (!pair.inverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (pair.fuzzy == 2 && pair.clas.getSuperclass().isInstance(entity))
            {
                if (!pair.inverted)
                {
                    return true;
                }

                wasFound = true;
            }
            else if (pair.fuzzy == 3 && pair.name.equals(entity.getEntityName()) && pair.clas.isInstance(entity))
            {
                if (!pair.inverted)
                {
                    return true;
                }

                wasFound = true;
            }
        }

        return !wasFound ? (hasSeperateLists ? notFoundRecieve : notFoundSend) : false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("isActive", isActive);
        tag.setBoolean("hasSeperateLists", hasSeperateLists);
        tag.setBoolean("notFoundSend", notFoundSend);
        tag.setBoolean("notFoundRecieve", notFoundRecieve);

        NBTTagList t = new NBTTagList();
        for (EntityPair p : sendingEntityTypes)
        {
            writeEntityPairToNBT(t, p);
        }

        tag.setTag("sendingEntityTypes", t);

        if (hasSeperateLists)
        {
            NBTTagList t2 = new NBTTagList();
            for (EntityPair p : recievingEntityTypes)
            {
                writeEntityPairToNBT(t2, p);
            }

            tag.setTag("recievingEntityTypes", t2);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        isActive = tag.getBoolean("isActive");
        hasSeperateLists = tag.getBoolean("hasSeperateLists");
        notFoundSend = tag.getBoolean("notFoundSend");
        notFoundRecieve = tag.getBoolean("notFoundRecieve");

        NBTTagList l = tag.getTagList("sendingEntityTypes");
        for (int i = 0; i < l.tagCount(); i++)
        {
            EntityPair p = readEntityPairFromNBT(l, i);

            if (p != null)
            {
                sendingEntityTypes.add(p);
            }
        }

        if (tag.hasKey("recievingEntityTypes"))
        {
            NBTTagList l2 = tag.getTagList("recievingEntityTypes");

            for (int i = 0; i < l.tagCount(); i++)
            {
                EntityPair p = readEntityPairFromNBT(l2, i);

                if (p != null)
                {
                    recievingEntityTypes.add(p);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    EntityPair readEntityPairFromNBT(NBTTagList list, int i)
    {
        NBTTagCompound t = (NBTTagCompound) list.tagAt(i);
        String n = t.getString("name");
        byte f = t.getByte("fuzzy");
        boolean b = t.getBoolean("inverted");
        int entityID = t.getInteger("id");
        Class<? extends Entity> clazz = EntityList.getClassFromID(entityID);

        return clazz == null ? null : new EntityPair(n, clazz, f, b);
    }

    void writeEntityPairToNBT(NBTTagList list, EntityPair p)
    {
        if (EntityList.classToIDMapping.get(p.clas) != null)
        {
            NBTTagCompound t = new NBTTagCompound();
            
            t.setString("name", p.name);
            t.setByte("fuzzy", p.fuzzy);
            t.setBoolean("inverted", p.inverted);
            t.setInteger("id", Integer.parseInt(EntityList.classToIDMapping.get(p.clas).toString()));

            list.appendTag(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);

        isActive = stream.readBoolean();
        hasSeperateLists = stream.readBoolean();
        notFoundSend = stream.readBoolean();
        notFoundRecieve = stream.readBoolean();
        sendingEntityTypes.clear();
        recievingEntityTypes.clear();

        byte size = stream.readByte();

        for (int i = 0; i < size; i++)
        {
            String name = stream.readUTF();
            byte fuzzy = stream.readByte();
            String clazz = stream.readUTF();
            boolean b = stream.readBoolean();
            Class<? extends Entity> c;

            try
            {
                c = (Class<? extends Entity>) Class.forName(clazz);
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                return;
            }

            sendingEntityTypes.add(new EntityPair(name, c, fuzzy, b));
        }

        if (hasSeperateLists)
        {
            size = stream.readByte();

            for (int i = 0; i < size; i++)
            {
                String name = stream.readUTF();
                byte fuzzy = stream.readByte();
                String clazz = stream.readUTF();      
                boolean b = stream.readBoolean();
                Class<? extends Entity> c;

                try
                {
                    c = (Class<? extends Entity>) Class.forName(clazz);
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                    return;
                }

                recievingEntityTypes.add(new EntityPair(name, c, fuzzy, b));
            }
        }
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);

        stream.writeBoolean(isActive);
        stream.writeBoolean(hasSeperateLists);
        stream.writeBoolean(notFoundSend);
        stream.writeBoolean(notFoundRecieve);
        stream.writeByte(sendingEntityTypes.size());

        for (EntityPair pair : sendingEntityTypes)
        {
            stream.writeUTF(pair.name);
            stream.writeByte(pair.fuzzy);
            stream.writeUTF(pair.clas.getName());
            stream.writeBoolean(pair.inverted);
        }

        if (hasSeperateLists)
        {
            stream.writeByte(recievingEntityTypes.size());

            for (EntityPair pair : recievingEntityTypes)
            {
                stream.writeUTF(pair.name);
                stream.writeByte(pair.fuzzy);
                stream.writeUTF(pair.clas.getName());
                stream.writeBoolean(pair.inverted);
            }
        }
    }

    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);

        if (payload.data.hasKey("list"))
        {
            boolean list = payload.data.getBoolean("list");

            if (payload.data.hasKey("invert"))
            {
                int id = payload.data.getInteger("invert");

                (list ? sendingEntityTypes : recievingEntityTypes).get(id).inverted = !(list ? sendingEntityTypes : recievingEntityTypes).get(id).inverted;
            }
            else if (payload.data.hasKey("type"))
            {
                int id = payload.data.getInteger("type");

                (list ? sendingEntityTypes : recievingEntityTypes).get(id).fuzzy++;

                if ((list ? sendingEntityTypes : recievingEntityTypes).get(id).fuzzy > 3)
                {
                    (list ? sendingEntityTypes : recievingEntityTypes).get(id).fuzzy = 0;
                }
            }
            else if (payload.data.hasKey("remove"))
            {
                int id = payload.data.getInteger("remove");

                (list ? sendingEntityTypes : recievingEntityTypes).remove(id);
            }
            else if (payload.data.hasKey("default"))
            {
                if (list)
                {
                    notFoundSend = !notFoundSend;
                }
                else
                {
                    notFoundRecieve = !notFoundRecieve;
                }
            }

            CommonProxy.sendUpdatePacketToAllAround(this);
        }
        else if (payload.data.hasKey("toggleSeperateLists"))
        {
            hasSeperateLists = !hasSeperateLists;            
            CommonProxy.sendUpdatePacketToAllAround(this);
        }
    }

    @Override
    public void onNeighborBlockChange(int blockID)
    {
        isActive = getHighestPowerState() == 0;
    }

    public ArrayList<EntityPair> copySendingEntityTypes()
    {
        ArrayList<EntityPair> list = new ArrayList<EntityPair>();

        for (EntityPair pair : sendingEntityTypes)
        {
            list.add(new EntityPair(pair.name, pair.clas, pair.fuzzy, pair.inverted));
        }

        return list;
    }

    public ArrayList<EntityPair> copyRecievingEntityTypes()
    {
        ArrayList<EntityPair> list = new ArrayList<EntityPair>();

        for (EntityPair pair : recievingEntityTypes)
        {
            list.add(new EntityPair(pair.name, pair.clas, pair.fuzzy, pair.inverted));
        }

        return list;
    }
}
