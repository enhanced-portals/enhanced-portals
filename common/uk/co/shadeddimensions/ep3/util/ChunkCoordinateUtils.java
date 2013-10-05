package uk.co.shadeddimensions.ep3.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;

public class ChunkCoordinateUtils
{
    public static ChunkCoordinates loadChunkCoord(NBTTagCompound tagCompound, String string)
    {
        if (tagCompound.getTag(string) == null)
        {
            return null;
        }

        NBTTagCompound t = (NBTTagCompound) tagCompound.getTag(string);

        return t.getInteger("Y") == -1 ? null : new ChunkCoordinates(t.getInteger("X"), t.getInteger("Y"), t.getInteger("Z"));
    }

    public static ArrayList<ChunkCoordinates> loadChunkCoordList(NBTTagCompound tag, String name)
    {
        ArrayList<ChunkCoordinates> list = new ArrayList<ChunkCoordinates>();

        NBTTagList tagList = tag.getTagList(name);

        for (Object o : tagList.tagList)
        {
            NBTTagCompound t = (NBTTagCompound) o;

            list.add(new ChunkCoordinates(t.getInteger("X"), t.getInteger("Y"), t.getInteger("Z")));
        }

        return list;
    }

    public static ChunkCoordinates offset(ChunkCoordinates coord, ForgeDirection dir)
    {
        return new ChunkCoordinates(coord.posX + dir.offsetX, coord.posY + dir.offsetY, coord.posZ + dir.offsetZ);
    }

    public static void saveChunkCoord(NBTTagCompound tagCompound, ChunkCoordinates c, String string)
    {
        if (c == null)
        {
            return;
        }

        NBTTagCompound t = new NBTTagCompound();
        t.setInteger("X", c.posX);
        t.setInteger("Y", c.posY);
        t.setInteger("Z", c.posZ);

        tagCompound.setTag(string, t);
    }

    public static void saveChunkCoordList(NBTTagCompound tag, List<ChunkCoordinates> list, String name)
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
}
