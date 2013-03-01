package alz.mods.enhancedportals.block;

import net.minecraft.block.material.Material;

public class BlockEndPortal extends net.minecraft.block.BlockEndPortal
{
	public BlockEndPortal()
	{
		super(119, Material.portal);
		setHardness(-1.0F);
		setResistance(6000000.0F);
	}
	
	// TODO:
	// - Create a custom End Portal renderer for vertical portals.
	// - Copy the portal code for End Portals. Any size/shape.
	// Planned for 2.2.
}
