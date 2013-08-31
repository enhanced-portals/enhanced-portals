package uk.co.shadeddimensions.enhancedportals.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;

public class NBTHelper
{
    public static void saveCCList(NBTTagCompound tag, List<ChunkCoordinates> list, String name)
    {
        NBTTagList tagList = new NBTTagList();

        for (ChunkCoordinates c : list)
        {
            NBTTagCompound t = new NBTTagCompound();
            t.setInteger("X", c.posX);
            t.setInteger("Y", c.posY);
            t.setInteger("Z", c.posZ);

            tagList.appendTag(t);
        }

        tag.setTag(name, tagList);
    }

    public static List<ChunkCoordinates> loadCCList(NBTTagCompound tag, String name)
    {
        List<ChunkCoordinates> list = new ArrayList<ChunkCoordinates>();

        NBTTagList tagList = tag.getTagList(name);

        for (Object o : tagList.tagList)
        {
            NBTTagCompound t = (NBTTagCompound) o;

            list.add(new ChunkCoordinates(t.getInteger("X"), t.getInteger("Y"), t.getInteger("Z")));
        }

        return list;
    }
}
