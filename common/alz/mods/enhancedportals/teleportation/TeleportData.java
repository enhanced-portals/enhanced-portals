package alz.mods.enhancedportals.teleportation;

import alz.mods.enhancedportals.helpers.EntityHelper;
import alz.mods.enhancedportals.helpers.WorldHelper;
import net.minecraft.server.MinecraftServer;

public class TeleportData
{
	private double x, y, z;
	private int dimension;

	private int[] blockOffsetLocation;
	private double[] entityOffsetLocation;
	
	public TeleportData(int x, int y, int z, int dimension)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}

	public TeleportData(double x, double y, double z, int dimension)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}

	public int getX()
	{
		return (int) Math.floor(x);
	}

	public int getY()
	{
		return (int) Math.floor(y);
	}

	public int getZ()
	{
		return (int) Math.floor(z);
	}

	private void setupEntityOffset()
	{
		if (entityOffsetLocation == null)
		{
			entityOffsetLocation = EntityHelper.offsetDirectionBased(MinecraftServer.getServer().worldServerForDimension(dimension), getX(), getY(), getZ());
		}
	}

	private void setupBlockOffset()
	{
		if (blockOffsetLocation == null)
		{
			blockOffsetLocation = WorldHelper.offsetDirectionBased(MinecraftServer.getServer().worldServerForDimension(dimension), getX(), getY(), getZ());
		}
	}

	public int getXOffsetBlock()
	{
		setupBlockOffset();

		return blockOffsetLocation[0];
	}

	public int getYOffsetBlock()
	{
		setupBlockOffset();

		return blockOffsetLocation[1];
	}

	public int getZOffsetBlock()
	{
		setupBlockOffset();

		return blockOffsetLocation[2];
	}

	public double getXOffsetEntity()
	{
		setupEntityOffset();

		return entityOffsetLocation[0];
	}

	public double getYOffsetEntity()
	{
		setupEntityOffset();

		return entityOffsetLocation[1];
	}

	public double getZOffsetEntity()
	{
		setupEntityOffset();

		return entityOffsetLocation[2];
	}

	public int getDimension()
	{
		return dimension;
	}
	
	public String getDimensionAsString()
	{
		switch (dimension)
		{
			case -1:
				return "The Nether";
			case 0:
				return "Overworld";
			case 1:
				return "The End";
			default:
				return "Unknown";
		}
	}
}
