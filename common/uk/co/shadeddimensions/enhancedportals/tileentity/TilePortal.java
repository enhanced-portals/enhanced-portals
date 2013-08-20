package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class TilePortal extends TileEP
{
    public Texture texture;

    public TilePortal()
    {
        texture = new Texture();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        texture.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        texture = new Texture(tagCompound);
    }
}
