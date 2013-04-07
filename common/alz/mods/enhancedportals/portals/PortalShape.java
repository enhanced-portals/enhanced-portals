package alz.mods.enhancedportals.portals;

import alz.mods.enhancedportals.common.WorldLocation;

public enum PortalShape
{
	/***
	 * X & Y
	 */
	X,
	/***
	 * Z & Y
	 */
	Z,
	/***
	 * X & Z
	 */
	HORIZONTAL, UNKNOWN;

	public static PortalShape getPortalShape(WorldLocation location)
	{
		if (location.worldObj != null)
			return getPortalShape(location, true);
		else if (location.worldObj == null && location.blockAccess != null)
			return getPortalShape(location, false);

		return PortalShape.UNKNOWN;
	}

	private static PortalShape getPortalShape(WorldLocation location, boolean worldObj)
	{
		if (worldObj)
		{
			if (PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord - 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord + 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord, location.yCoord + 1, location.zCoord), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord, location.yCoord - 1, location.zCoord), true))
				return PortalShape.X;
			else if (PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord, location.yCoord, location.zCoord - 1), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord, location.yCoord, location.zCoord + 1), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord, location.yCoord + 1, location.zCoord), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord, location.yCoord - 1, location.zCoord), true))
				return PortalShape.Z;
			else if (PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord - 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord + 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord, location.yCoord, location.zCoord + 1), true) && PortalHandler.isBlockFrame(location.worldObj.getBlockId(location.xCoord, location.yCoord, location.zCoord - 1), true))
				return PortalShape.HORIZONTAL;

			return PortalShape.UNKNOWN;
		}
		else
		{
			if (PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord - 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord + 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord, location.yCoord + 1, location.zCoord), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord, location.yCoord - 1, location.zCoord), true))
				return PortalShape.X;
			else if (PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord, location.yCoord, location.zCoord - 1), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord, location.yCoord, location.zCoord + 1), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord, location.yCoord + 1, location.zCoord), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord, location.yCoord - 1, location.zCoord), true))
				return PortalShape.Z;
			else if (PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord - 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord + 1, location.yCoord, location.zCoord), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord, location.yCoord, location.zCoord + 1), true) && PortalHandler.isBlockFrame(location.blockAccess.getBlockId(location.xCoord, location.yCoord, location.zCoord - 1), true))
				return PortalShape.HORIZONTAL;

			return PortalShape.UNKNOWN;
		}
	}
}
