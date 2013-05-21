package enhancedportals.portal;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Settings;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.portal.teleportation.TeleportManager;
import enhancedportals.tileentity.TileEntityEnhancedPortals;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class Portal
{
    public int xCoord, yCoord, zCoord, dimension;
    public String portalTexture;
    public byte portalShape, portalThickness;
    public boolean producesParticles, producesSound;
    public WorldLocation portalModifier;

    public Portal()
    {

    }

    public Portal(int x, int y, int z, World world)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = world.provider.dimensionId;
        portalShape = 0;

        if (world.getBlockId(x, y, z) == BlockIds.NetherPortal)
        {
            TileEntity te = world.getBlockTileEntity(x, y, z);

            if (te instanceof TileEntityNetherPortal)
            {
                TileEntityNetherPortal portal = (TileEntityNetherPortal) te;

                portalTexture = portal.texture;
                producesSound = portal.producesSound;
                producesParticles = portal.producesParticles;
                portalThickness = portal.thickness;
            }
            else
            {
                portalTexture = "";
                producesParticles = true;
                producesSound = true;
                portalThickness = 0;
            }
        }
        else
        {
            portalTexture = "";
            producesParticles = true;
            producesSound = true;
            portalThickness = 0;
        }
    }

    public Portal(int x, int y, int z, World world, String portaltexture)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = world.provider.dimensionId;
        portalTexture = portaltexture;
        portalShape = 0;

        if (world.getBlockId(x, y, z) == BlockIds.NetherPortal)
        {
            TileEntity te = world.getBlockTileEntity(x, y, z);

            if (te instanceof TileEntityNetherPortal)
            {
                TileEntityNetherPortal portal = (TileEntityNetherPortal) te;

                producesSound = portal.producesSound;
                producesParticles = portal.producesParticles;
                portalThickness = portal.thickness;
            }
            else
            {
                producesParticles = true;
                producesSound = true;
                portalThickness = 0;
            }
        }
        else
        {
            producesParticles = true;
            producesSound = true;
            portalThickness = 0;
        }
    }

    public Portal(int x, int y, int z, World world, String portaltexture, byte portalshape)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = world.provider.dimensionId;
        portalTexture = portaltexture;
        portalShape = portalshape;

        if (world.getBlockId(x, y, z) == BlockIds.NetherPortal)
        {
            TileEntity te = world.getBlockTileEntity(x, y, z);

            if (te instanceof TileEntityNetherPortal)
            {
                TileEntityNetherPortal portal = (TileEntityNetherPortal) te;

                producesSound = portal.producesSound;
                producesParticles = portal.producesParticles;
                portalThickness = portal.thickness;
            }
            else
            {
                producesParticles = true;
                producesSound = true;
                portalThickness = 0;
            }
        }
        else
        {
            producesParticles = true;
            producesSound = true;
            portalThickness = 0;
        }
    }

    public Portal(int x, int y, int z, World world, TileEntityPortalModifier portalmodifier)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = world.provider.dimensionId;
        portalTexture = portalmodifier.texture;
        portalShape = 0;
        producesSound = portalmodifier.getSounds();
        producesParticles = portalmodifier.getParticles();
        portalThickness = portalmodifier.thickness;
        portalModifier = new WorldLocation(portalmodifier.xCoord, portalmodifier.yCoord, portalmodifier.zCoord, world);
    }

    public boolean createPortal()
    {
        World world = getWorld();

        if (world.isRemote || !preChecks() || !findPortalShape())
        {
            return false;
        }

        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        Queue<WorldLocation> addedBlocks = new LinkedList<WorldLocation>();
        int usedChances = 0, MAX_CHANCES = 10;
        queue.add(new WorldLocation(xCoord, yCoord, zCoord, world));

        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();
            int currentBlockID = current.getBlockId();

            if (isBlockRemovable(currentBlockID))
            {
                int sides = getSides(current);

                if (sides == -1)
                {
                    removePortal(addedBlocks);
                    return false;
                }
                else if (sides < 2 && usedChances < MAX_CHANCES)
                {
                    usedChances++;
                    sides += 2;
                }

                if (sides >= 2)
                {
                    addedBlocks.add(current);
                    current.setBlockAndMeta(BlockIds.NetherPortal, addedBlocks.size() == 1 ? portalShape + 1 : portalShape, 3);
                    queue = updateQueue(queue, current);

                    TileEntity te = current.getTileEntity();

                    if (te != null && te instanceof TileEntityNetherPortal)
                    {
                        TileEntityNetherPortal portal = (TileEntityNetherPortal) te;

                        portal.texture = portalTexture;
                        portal.producesParticles = producesParticles;
                        portal.producesSound = producesSound;
                        portal.thickness = portalThickness;

                        if (portalModifier != null)
                        {
                            portal.parentModifier = portalModifier;
                        }
                    }
                }
            }
        }

        return validatePortal(addedBlocks);
    }

    public boolean createPortal(int[] extraBorderBlocks)
    {
        if (extraBorderBlocks == null || extraBorderBlocks.length == 0)
        {
            return createPortal();
        }

        int size = Settings.BorderBlocks.size();

        for (int i : extraBorderBlocks)
        {
            Settings.BorderBlocks.add(i);
        }

        boolean status = createPortal();

        for (int i = size; i < Settings.BorderBlocks.size(); i++)
        {
            Settings.BorderBlocks.remove(i);
        }

        return status;
    }

    private boolean findPortalShape()
    {
        if (portalShape >= 2 && portalShape <= 7)
        {
            return true;
        }

        World world = getWorld();

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.NetherPortal)
        {
            portalShape = (byte) world.getBlockMetadata(xCoord, yCoord, zCoord);

            if (portalShape == 3 || portalShape == 5 || portalShape == 7)
            {
                portalShape--;
                return true;
            }
            else if (portalShape == 2 || portalShape == 4 || portalShape == 6)
            {
                return true;
            }
        }
        else
        {
            portalShape = ghostPortal();

            if (portalShape == -1)
            {
                portalShape = 0;
                return false;
            }
            else
            {
                return true;
            }
        }

        return false;
    }

    private int getSides(WorldLocation location)
    {
        int totalSides = 0;
        int[] allBlocks = new int[4];

        if (portalShape == 6)
        {
            allBlocks[0] = location.getOffset(ForgeDirection.NORTH).getBlockId();
            allBlocks[1] = location.getOffset(ForgeDirection.SOUTH).getBlockId();
            allBlocks[2] = location.getOffset(ForgeDirection.EAST).getBlockId();
            allBlocks[3] = location.getOffset(ForgeDirection.WEST).getBlockId();
        }
        else if (portalShape == 2)
        {
            allBlocks[0] = location.getOffset(ForgeDirection.WEST).getBlockId();
            allBlocks[1] = location.getOffset(ForgeDirection.EAST).getBlockId();
            allBlocks[2] = location.getOffset(ForgeDirection.UP).getBlockId();
            allBlocks[3] = location.getOffset(ForgeDirection.DOWN).getBlockId();
        }
        else if (portalShape == 4)
        {
            allBlocks[0] = location.getOffset(ForgeDirection.NORTH).getBlockId();
            allBlocks[1] = location.getOffset(ForgeDirection.SOUTH).getBlockId();
            allBlocks[2] = location.getOffset(ForgeDirection.UP).getBlockId();
            allBlocks[3] = location.getOffset(ForgeDirection.DOWN).getBlockId();
        }

        for (int val : allBlocks)
        {
            if (isBlockFrame(val, true))
            {
                totalSides++;
            }
            else if (!isBlockRemovable(val))
            {
                return -1;
            }
        }

        return totalSides;
    }

    private World getWorld()
    {
        return EnhancedPortals.proxy.getWorld(dimension);
    }

    private byte ghostPortal()
    {
        if (ghostPortal(2))
        {
            return 2;
        }
        else if (ghostPortal(4))
        {
            return 4;
        }
        else if (ghostPortal(6))
        {
            return 6;
        }

        return -1;
    }

    private boolean ghostPortal(int shape)
    {
        World world = getWorld();
        portalShape = (byte) shape;

        if (world.isRemote || !preChecks())
        {
            return false;
        }

        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        Queue<WorldLocation> addedBlocks = new LinkedList<WorldLocation>();
        int usedChances = 0, MAX_CHANCES = 10;
        queue.add(new WorldLocation(xCoord, yCoord, zCoord, world));

        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();
            int currentBlockID = current.getBlockId();

            if (isBlockRemovable(currentBlockID) && !queueContains(addedBlocks, current))
            {
                int sides = getSides(current);

                if (sides == -1)
                {
                    removePortal(addedBlocks);
                    return false;
                }
                else if (sides < 2 && usedChances < MAX_CHANCES)
                {
                    usedChances++;
                    sides += 2;
                }

                if (sides >= 2)
                {
                    addedBlocks.add(current);
                    queue = updateQueue(queue, current);
                }
            }
        }

        return true;
    }

    public boolean handleBlockActivation(EntityPlayer player)
    {
        if (getWorld().isRemote)
        {
            return false;
        }

        ItemStack item = player.inventory.mainInventory[player.inventory.currentItem];
        if (item != null)
        {
            TileEntityNetherPortal portal = (TileEntityNetherPortal) getWorld().getBlockTileEntity(xCoord, yCoord, zCoord);

            if (Settings.isValidItem(item.itemID) && portal.parentModifier != null)
            {
                if (((TileEntityPortalModifier) portal.parentModifier.getTileEntity()).isActive())
                {
                    player.sendChatToPlayer("You must modify this portal through its Portal Modifier.");
                    return false;
                }
            }

            if (item.itemID == Item.dyePowder.itemID)
            {
                if (updateTexture("C:" + item.getItemDamage()) && !player.capabilities.isCreativeMode)
                {
                    item.stackSize--;
                }

                return true;
            }
            else if (item.itemID == Item.bucketLava.itemID)
            {
                /*PortalTexture pTexture = new PortalTexture(Block.lavaMoving.blockID, 0);
                boolean consumeLiquid = true;

                if (portalTexture.isEqualTo(pTexture))
                {
                    pTexture = new PortalTexture(Block.lavaStill.blockID, 0);
                    consumeLiquid = false;
                }
                else if (portalTexture.isEqualTo(new PortalTexture(Block.lavaStill.blockID, 0)))
                {
                    consumeLiquid = false;
                }

                if (updateTexture(pTexture) && !player.capabilities.isCreativeMode && consumeLiquid)
                {
                    item.stackSize--;
                    player.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty, 1));
                    playerMP.mcServer.getConfigurationManager().syncPlayerInventory(playerMP);
                }*/

                return true;
            }
            else if (item.itemID == Item.bucketWater.itemID)
            {
                /*PortalTexture pTexture = new PortalTexture(Block.waterMoving.blockID, 0);
                boolean consumeLiquid = true;

                if (portalTexture.isEqualTo(pTexture))
                {
                    pTexture = new PortalTexture(Block.waterStill.blockID, 0);
                    consumeLiquid = false;
                }
                else if (portalTexture.isEqualTo(new PortalTexture(Block.waterStill.blockID, 0)))
                {
                    consumeLiquid = false;
                }

                if (updateTexture(pTexture) && !player.capabilities.isCreativeMode && consumeLiquid)
                {
                    item.stackSize--;
                    player.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty, 1));
                    playerMP.mcServer.getConfigurationManager().syncPlayerInventory(playerMP);
                }*/

                return true;
            }
        }

        return false;
    }

    public void handleEntityCollide(Entity entity)
    {
        World world = getWorld();

        if (world.isRemote)
        {
            if (Settings.RenderPortalEffect)
            {
                entity.setInPortal();
            }

            return;
        }

        if (!(world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityNetherPortal))
        {
            return;
        }

        TileEntityNetherPortal portal = (TileEntityNetherPortal) world.getBlockTileEntity(xCoord, yCoord, zCoord);

        if (portal.parentModifier == null)
        {
            if (world.provider.dimensionId == 0 || world.provider.dimensionId == -1)
            {
                entity.setInPortal();
                return;
            }
        }
        else if (portal.parentModifier != null && entity.timeUntilPortal == 0)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) portal.parentModifier.getTileEntity();

            if (modifier == null || modifier.network == null || modifier.network.equalsIgnoreCase("0") || modifier.network.equalsIgnoreCase(""))
            {
                if (world.provider.dimensionId == 0 || world.provider.dimensionId == -1)
                {
                    entity.setInPortal();
                    return;
                }
            }
            else
            {
                List<WorldLocation> validLocations = EnhancedPortals.proxy.ModifierNetwork.getNetworkExcluding(modifier.network, new WorldLocation(modifier.xCoord, modifier.yCoord, modifier.zCoord, modifier.worldObj));
                boolean missingUpgrade = false, teleport = false;

                if (validLocations.isEmpty())
                {
                    if (entity instanceof EntityPlayer)
                    {
                        ((EntityPlayer) entity).sendChatToPlayer("Could not find any linked portals.");
                    }

                    entity.timeUntilPortal = entity.getPortalCooldown();
                    return;
                }

                while (!validLocations.isEmpty())
                {
                    WorldLocation randomLocation = validLocations.remove(new Random().nextInt(validLocations.size()));

                    if ((randomLocation.dimension == -1 || randomLocation.dimension == 0 || randomLocation.dimension == 1) && !modifier.hasUpgrade(2) && !modifier.hasUpgrade(3))
                    {
                        // Vanilla dimension but we don't have the upgrade

                        if (randomLocation.dimension == -1 && modifier.worldObj.provider.dimensionId == 0 || randomLocation.dimension == 0 && modifier.worldObj.provider.dimensionId == -1)
                        {
                            // Allow overworld <--> nether travel
                        }
                        else
                        {
                            missingUpgrade = true;
                            continue;
                        }
                    }

                    if (randomLocation.dimension == modifier.worldObj.provider.dimensionId && !modifier.hasUpgrade(3))
                    {
                        // Same dimension but we don't have the upgrade
                        missingUpgrade = true;
                        continue;
                    }

                    if ((randomLocation.dimension > 1 || randomLocation.dimension < -1) && !modifier.hasUpgrade(3))
                    {
                        // Modded dimension but no upgrade
                        missingUpgrade = true;
                        continue;
                    }

                    if (TeleportManager.teleportEntity(entity, randomLocation, modifier, validLocations.size() <= 1))
                    {
                        teleport = true;
                        validLocations.clear();
                    }
                }

                if (missingUpgrade && !teleport)
                {
                    if (entity instanceof EntityPlayer)
                    {
                        ((EntityPlayer) entity).sendChatToPlayer("The Portal Modifier is missing an upgrade.");
                    }
                }
            }
        }

        entity.timeUntilPortal = entity.getPortalCooldown();
    }

    public void handleNeighborChange(int id)
    {
        World world = getWorld();

        if (world.isRemote || id == 0 || !findPortalShape())
        {
            return;
        }

        WorldLocation location = new WorldLocation(xCoord, yCoord, zCoord, world);

        if (portalShape == 2 || portalShape == 3) // XY
        {
            if (location.getOffset(ForgeDirection.EAST).isBlockAir() || location.getOffset(ForgeDirection.WEST).isBlockAir() || location.getOffset(ForgeDirection.UP).isBlockAir() || location.getOffset(ForgeDirection.DOWN).isBlockAir())
            {
                removePortal();
            }
        }
        else if (portalShape == 4 || portalShape == 5) // ZY
        {
            if (location.getOffset(ForgeDirection.NORTH).isBlockAir() || location.getOffset(ForgeDirection.SOUTH).isBlockAir() || location.getOffset(ForgeDirection.UP).isBlockAir() || location.getOffset(ForgeDirection.DOWN).isBlockAir())
            {
                removePortal();
            }
        }
        else if (portalShape == 6 || portalShape == 7) // ZX
        {
            if (location.getOffset(ForgeDirection.EAST).isBlockAir() || location.getOffset(ForgeDirection.WEST).isBlockAir() || location.getOffset(ForgeDirection.NORTH).isBlockAir() || location.getOffset(ForgeDirection.SOUTH).isBlockAir())
            {
                removePortal();
            }
        }
    }

    public boolean isBlockFrame(int val, boolean includeSelf)
    {
        if (includeSelf && val == BlockIds.NetherPortal)
        {
            return true;
        }

        for (int i : Settings.BorderBlocks)
        {
            if (i == val)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isBlockFrame(int val, int[] extraIds)
    {
        if (!isBlockFrame(val, false))
        {
            if (extraIds == null || extraIds.length == 0)
            {
                return false;
            }

            for (int i : extraIds)
            {
                if (i == val)
                {
                    return true;
                }
            }

            return false;
        }

        return true;
    }

    public boolean isBlockRemovable(int val)
    {
        for (int i : Settings.DestroyBlocks)
        {
            if (i == val)
            {
                return true;
            }
        }

        return false;
    }

    private boolean preChecks()
    {
        World world = getWorld();

        if (world.isRemote)
        {
            return false;
        }

        if (world.isAirBlock(xCoord, yCoord, zCoord))
        {
            if ((portalShape == 2 || portalShape == 4) && world.canBlockSeeTheSky(xCoord, yCoord, zCoord))
            {
                return false;
            }

            return true;
        }

        return false;
    }

    private boolean queueContains(Queue<WorldLocation> queue, WorldLocation loc)
    {
        for (WorldLocation queueLoc : queue)
        {
            if (queueLoc.equals(loc))
            {
                return true;
            }
        }

        return false;
    }

    public boolean removePortal()
    {
        World world = getWorld();
        WorldLocation location = new WorldLocation(xCoord, yCoord, zCoord, world);

        if (location.getBlockId() != BlockIds.NetherPortal || !findPortalShape())
        {
            return false;
        }

        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        queue.add(location);

        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();
            int currentBlockID = current.getBlockId();

            if (currentBlockID == BlockIds.NetherPortal)
            {
                current.setBlockToAir();
                queue = updateQueue(queue, current);
            }
        }

        return true;
    }

    private void removePortal(Queue<WorldLocation> addedBlocks)
    {
        while (!addedBlocks.isEmpty())
        {
            WorldLocation current = addedBlocks.remove();
            current.setBlockToAir();
        }
    }

    public boolean updateData(boolean sound, boolean particles, byte thickness)
    {
        World world = getWorld();

        if (world.getBlockId(xCoord, yCoord, zCoord) != BlockIds.NetherPortal || !findPortalShape() || producesSound == sound && producesParticles == particles && portalThickness == thickness)
        {
            return false;
        }

        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        Queue<WorldLocation> addedBlocks = new LinkedList<WorldLocation>();
        queue.add(new WorldLocation(xCoord, yCoord, zCoord, world));

        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();
            TileEntity te = current.getTileEntity();

            if (te != null && te instanceof TileEntityNetherPortal && !queueContains(addedBlocks, current))
            {
                TileEntityNetherPortal portal = (TileEntityNetherPortal) te;
                portal.producesSound = sound;
                portal.producesParticles = particles;
                portal.thickness = thickness;

                current.markBlockForUpdate();
                addedBlocks.add(current);
                updateQueue(queue, current);

                if ((current.getMetadata() == 3 || current.getMetadata() == 5 || current.getMetadata() == 7) && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
                {
                    PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, world.provider.dimensionId, new PacketTEUpdate((TileEntityEnhancedPortals) te).getPacket());
                }
            }
        }

        return true;
    }

    private Queue<WorldLocation> updateQueue(Queue<WorldLocation> queue, WorldLocation location)
    {
        if (portalShape == 2)
        {
            queue.add(location.getOffset(ForgeDirection.UP));
            queue.add(location.getOffset(ForgeDirection.DOWN));
            queue.add(location.getOffset(ForgeDirection.EAST));
            queue.add(location.getOffset(ForgeDirection.WEST));
        }
        else if (portalShape == 4)
        {
            queue.add(location.getOffset(ForgeDirection.UP));
            queue.add(location.getOffset(ForgeDirection.DOWN));
            queue.add(location.getOffset(ForgeDirection.NORTH));
            queue.add(location.getOffset(ForgeDirection.SOUTH));
        }
        else if (portalShape == 6)
        {
            queue.add(location.getOffset(ForgeDirection.NORTH));
            queue.add(location.getOffset(ForgeDirection.SOUTH));
            queue.add(location.getOffset(ForgeDirection.EAST));
            queue.add(location.getOffset(ForgeDirection.WEST));
        }

        return queue;
    }

    public boolean updateTexture(String newTexture)
    {
        World world = getWorld();

        if (world.getBlockId(xCoord, yCoord, zCoord) != BlockIds.NetherPortal || !findPortalShape() || portalTexture.equals(newTexture))
        {
            return false;
        }

        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        Queue<WorldLocation> addedBlocks = new LinkedList<WorldLocation>();
        queue.add(new WorldLocation(xCoord, yCoord, zCoord, world));
        portalTexture = newTexture;

        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();
            TileEntity te = current.getTileEntity();

            if (te != null && te instanceof TileEntityNetherPortal && !queueContains(addedBlocks, current))
            {
                ((TileEntityNetherPortal) te).texture = portalTexture;
                current.markBlockForUpdate();
                addedBlocks.add(current);
                updateQueue(queue, current);

                if ((current.getMetadata() == 3 || current.getMetadata() == 5 || current.getMetadata() == 7) && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
                {
                    PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, world.provider.dimensionId, new PacketTEUpdate((TileEntityEnhancedPortals) te).getPacket());
                }
            }
        }

        return true;
    }

    private boolean validatePortal(Queue<WorldLocation> addedBlocks)
    {
        World world = getWorld();

        if (world.isRemote)
        {
            return true;
        }

        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        Queue<WorldLocation> checkedQueue = new LinkedList<WorldLocation>();
        queue.add(new WorldLocation(xCoord, yCoord, zCoord, world));

        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();
            int currentBlockID = current.getBlockId();

            if (currentBlockID == BlockIds.NetherPortal && !queueContains(checkedQueue, current))
            {
                int sides = getSides(current);

                if (sides != 4)
                {
                    removePortal(addedBlocks);
                    return false;
                }

                queue = updateQueue(queue, current);
                checkedQueue.add(current);
            }
        }

        return true;
    }
}
