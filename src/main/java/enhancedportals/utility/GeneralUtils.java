package enhancedportals.utility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.tools.IToolWrench;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.item.ItemGlasses;
import enhancedportals.network.CommonProxy;

public class GeneralUtils
{
    public static double getPowerMultiplier()
    {
        return hasEnergyCost() ? CommonProxy.powerMultiplier : 0;
    }

    public static boolean hasEnergyCost()
    {
        return CommonProxy.requirePower;
    }

    public static boolean isEnergyContainerItem(ItemStack i)
    {
        return i != null && i.getItem() instanceof IEnergyContainerItem;
    }

    public static boolean isWearingGoggles()
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            if (Minecraft.getMinecraft().thePlayer == null)
            {
                return false;
            }

            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(3);
            return stack != null && stack.getItem() == ItemGlasses.instance;
        }

        return false;
    }

    public static boolean isWrench(ItemStack i)
    {
        return i != null && i.getItem() instanceof IToolWrench;
    }

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

        NBTTagList tagList = tag.getTagList(name, 10);

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound t = tagList.getCompoundTagAt(i);

            list.add(new ChunkCoordinates(t.getInteger("X"), t.getInteger("Y"), t.getInteger("Z")));
        }

        return list;
    }

    public static WorldCoordinates loadWorldCoord(NBTTagCompound tagCompound, String string)
    {
        if (tagCompound.getTag(string) == null)
        {
            return null;
        }

        NBTTagCompound t = (NBTTagCompound) tagCompound.getTag(string);

        return t.getInteger("Y") == -1 ? null : new WorldCoordinates(t.getInteger("X"), t.getInteger("Y"), t.getInteger("Z"), t.getInteger("D"));
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

    public static void saveWorldCoord(NBTTagCompound tagCompound, WorldCoordinates c, String string)
    {
        if (c == null)
        {
            return;
        }

        NBTTagCompound t = new NBTTagCompound();
        t.setInteger("X", c.posX);
        t.setInteger("Y", c.posY);
        t.setInteger("Z", c.posZ);
        t.setInteger("D", c.dimension);

        tagCompound.setTag(string, t);
    }

    public static ChunkCoordinates offset(ChunkCoordinates c, ForgeDirection f)
    {
        return new ChunkCoordinates(c.posX + f.offsetX, c.posY + f.offsetY, c.posZ + f.offsetZ);
    }
}
