package uk.co.shadeddimensions.ep3.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class FrameMaterial extends Material
{
    public FrameMaterial()
    {
        super(MapColor.ironColor);
        setRequiresTool();
        setImmovableMobility();
    }
}
