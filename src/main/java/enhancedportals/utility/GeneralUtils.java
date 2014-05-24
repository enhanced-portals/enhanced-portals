package enhancedportals.utility;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import enhancedportals.network.ClientProxy;
import enhancedportals.network.CommonProxy;
import enhancedportals.portal.GlyphIdentifier;

public class GeneralUtils
{
    public static List listFormattedStringToWidth(String str, int width)
    {
        return Arrays.asList(wrapFormattedStringToWidth(str, width).split("\n"));
    }
    
    static String wrapFormattedStringToWidth(String str, int width)
    {
        int j = sizeStringToWidth(str, width);

        if (str.length() <= j)
        {
            return str;
        }
        else
        {
            String s1 = str.substring(0, j);
            char c0 = str.charAt(j);
            boolean flag = c0 == 32 || c0 == 10;
            String s2 = getFormatFromString(s1) + str.substring(j + (flag ? 1 : 0));
            return s1 + "\n" + wrapFormattedStringToWidth(s2, width);
        }
    }
    
    static int sizeStringToWidth(String par1Str, int par2)
    {
        int j = par1Str.length();
        int k = 0;
        int l = 0;
        int i1 = -1;

        for (boolean flag = false; l < j; ++l)
        {
            char c0 = par1Str.charAt(l);

            switch (c0)
            {
                case 10:
                    --l;
                    break;
                case 167:
                    if (l < j - 1)
                    {
                        ++l;
                        char c1 = par1Str.charAt(l);

                        if (c1 != 108 && c1 != 76)
                        {
                            if (c1 == 114 || c1 == 82 || isFormatColor(c1))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = true;
                        }
                    }

                    break;
                case 32:
                    i1 = l;
                default:
                    k += ClientProxy.bookFont.getWidth("" + c0);

                    if (flag)
                    {
                        ++k;
                    }
            }

            if (c0 == 10)
            {
                ++l;
                i1 = l;
                break;
            }

            if (k > par2)
            {
                break;
            }
        }

        return l != j && i1 != -1 && i1 < l ? i1 : l;
    }
    
    static String getFormatFromString(String par0Str)
    {
        String s1 = "";
        int i = -1;
        int j = par0Str.length();

        while ((i = par0Str.indexOf(167, i + 1)) != -1)
        {
            if (i < j - 1)
            {
                char c0 = par0Str.charAt(i + 1);

                if (isFormatColor(c0))
                {
                    s1 = "\u00a7" + c0;
                }
                else if (isFormatSpecial(c0))
                {
                    s1 = s1 + "\u00a7" + c0;
                }
            }
        }

        return s1;
    }
    
    static boolean isFormatColor(char character)
    {
        return character >= 48 && character <= 57 || character >= 97 && character <= 102 || character >= 65 && character <= 70;
    }
    
    static boolean isFormatSpecial(char character)
    {
        return character >= 107 && character <= 111 || character >= 75 && character <= 79 || character == 114 || character == 82;
    }
    
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

    public static ArrayList<WorldCoordinates> loadWorldCoordList(NBTTagCompound tag, String name)
    {
        ArrayList<WorldCoordinates> list = new ArrayList<WorldCoordinates>();

        NBTTagList tagList = tag.getTagList(name, 10);

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound t = tagList.getCompoundTagAt(i);

            list.add(new WorldCoordinates(t.getInteger("X"), t.getInteger("Y"), t.getInteger("Z"), t.getInteger("D")));
        }

        return list;
    }

    public static ChunkCoordinates offset(ChunkCoordinates coord, ForgeDirection dir)
    {
        return new ChunkCoordinates(coord.posX + dir.offsetX, coord.posY + dir.offsetY, coord.posZ + dir.offsetZ);
    }

    public static ChunkCoordinates readChunkCoord(DataInputStream stream) throws IOException
    {
        ChunkCoordinates c = new ChunkCoordinates(stream.readInt(), stream.readInt(), stream.readInt());

        return c.posY == -1 ? null : c;
    }

    public static GlyphIdentifier readGlyphIdentifier(DataInputStream stream) throws IOException
    {
        return new GlyphIdentifier(stream.readUTF());
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

    public static void saveWorldCoordList(NBTTagCompound tag, List<WorldCoordinates> list, String name)
    {
        NBTTagList tagList = new NBTTagList();

        for (WorldCoordinates c : list)
        {
            NBTTagCompound t = new NBTTagCompound();
            t.setInteger("X", c.posX);
            t.setInteger("Y", c.posY);
            t.setInteger("Z", c.posZ);
            t.setInteger("D", c.dimension);

            tagList.appendTag(t);
        }

        tag.setTag(name, tagList);
    }

    public static void writeChunkCoord(DataOutputStream stream, ChunkCoordinates c) throws IOException
    {
        if (c == null)
        {
            stream.writeInt(0);
            stream.writeInt(-1);
            stream.writeInt(0);
        }
        else
        {
            stream.writeInt(c.posX);
            stream.writeInt(c.posY);
            stream.writeInt(c.posZ);
        }
    }

    public static void writeGlyphIdentifier(DataOutputStream stream, GlyphIdentifier i) throws IOException
    {
        stream.writeUTF(i == null ? "" : i.getGlyphString());
    }
}
