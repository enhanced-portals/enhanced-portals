package enhancedportals.portal;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;

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
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Settings;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketNetherPortalUpdate;
import enhancedportals.portal.teleportation.TeleportManager;
import enhancedportals.portal.upgrades.modifier.UpgradeAdvancedDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeParticles;
import enhancedportals.portal.upgrades.modifier.UpgradeSounds;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class Portal
{
    public WorldLocation portalModifier;
    public int           xCoord, yCoord, zCoord, dimension;
    public byte          shape, thickness;
    public boolean       producesSound, producesParticles;
    public String        texture;

    public Portal()
    {

    }

    public Portal(int x, int y, int z, World world)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = world.provider.dimensionId;
        shape = 0;

        if (world.getBlockId(x, y, z) == BlockIds.NetherPortal)
        {
            TileEntity te = world.getBlockTileEntity(x, y, z);

            if (te instanceof TileEntityNetherPortal)
            {
                TileEntityNetherPortal portal = (TileEntityNetherPortal) te;

                texture = portal.texture;
                producesParticles = portal.producesParticles;
                producesSound = portal.producesSound;
                thickness = portal.thickness;
                return;
            }
        }

        texture = "";
        thickness = 0;
        producesSound = producesParticles = true;
        
        findPortalShape();
    }

    public Portal(TileEntityPortalModifier modifier)
    {
        WorldLocation loc = new WorldLocation(modifier.xCoord, modifier.yCoord, modifier.zCoord, modifier.worldObj);
        WorldLocation offset = loc.getOffset(ForgeDirection.getOrientation(loc.getMetadata()));
                
        xCoord = offset.xCoord;
        yCoord = offset.yCoord;
        zCoord = offset.zCoord;
        dimension = modifier.worldObj.provider.dimensionId;
        shape = 0;
        texture = modifier.texture;
        producesParticles = !modifier.upgradeHandler.hasUpgrade(new UpgradeParticles());
        producesSound = !modifier.upgradeHandler.hasUpgrade(new UpgradeSounds());
        portalModifier = loc;
        thickness = modifier.thickness;
        
        findPortalShape();
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
                    current.setBlockAndMeta(BlockIds.NetherPortal, addedBlocks.size() == 1 ? shape + 1 : shape, 3);
                    queue = updateQueue(queue, current);

                    TileEntity te = current.getTileEntity();

                    if (te != null && te instanceof TileEntityNetherPortal)
                    {
                        TileEntityNetherPortal portal = (TileEntityNetherPortal) te;

                        portal.texture = texture;
                        portal.producesParticles = producesParticles;
                        portal.producesSound = producesSound;
                        portal.thickness = thickness;

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
        if (shape >= 2 && shape <= 7)
        {
            return true;
        }

        World world = getWorld();

        if (world.getBlockId(xCoord, yCoord, zCoord) == BlockIds.NetherPortal)
        {
            shape = (byte) world.getBlockMetadata(xCoord, yCoord, zCoord);

            if (shape == 3 || shape == 5 || shape == 7)
            {
                shape--;
                return true;
            }
            else if (shape == 2 || shape == 4 || shape == 6)
            {
                return true;
            }
        }
        else
        {
            shape = ghostPortal();

            if (shape == -1)
            {
                shape = 0;
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

        if (shape == 6)
        {
            allBlocks[0] = location.getOffset(ForgeDirection.NORTH).getBlockId();
            allBlocks[1] = location.getOffset(ForgeDirection.SOUTH).getBlockId();
            allBlocks[2] = location.getOffset(ForgeDirection.EAST).getBlockId();
            allBlocks[3] = location.getOffset(ForgeDirection.WEST).getBlockId();
        }
        else if (shape == 2)
        {
            allBlocks[0] = location.getOffset(ForgeDirection.WEST).getBlockId();
            allBlocks[1] = location.getOffset(ForgeDirection.EAST).getBlockId();
            allBlocks[2] = location.getOffset(ForgeDirection.UP).getBlockId();
            allBlocks[3] = location.getOffset(ForgeDirection.DOWN).getBlockId();
        }
        else if (shape == 4)
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

    private boolean ghostPortal(int theShape)
    {
        World world = getWorld();
        shape = (byte) theShape;

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

        if (item != null && item.itemID == Item.dyePowder.itemID)
        {
            if (updateTexture("C:" + item.getItemDamage()) && !player.capabilities.isCreativeMode)
            {
                item.stackSize--;
            }

            return true;
        }

        return false;
    }

    private void handleDialDeviceTeleportation(Entity entity, TileEntityPortalModifier modifier)
    {
        WorldLocation exitLocation = EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(modifier.tempDialDeviceNetwork).get(0);
        boolean missingUpgrade = false;
        
        if (exitLocation == null || exitLocation.isEqual(new WorldLocation(xCoord, yCoord, zCoord, dimension)))
        {
            return;
        }

        if ((exitLocation.dimension == -1 || exitLocation.dimension == 0 || exitLocation.dimension == 1) && !modifier.upgradeHandler.hasUpgrade(new UpgradeDimensional()) && !modifier.upgradeHandler.hasUpgrade(new UpgradeAdvancedDimensional()))
        {
            // Vanilla dimension but we don't have the upgrade
            missingUpgrade = true;
        }

        if (exitLocation.dimension == modifier.worldObj.provider.dimensionId && !modifier.upgradeHandler.hasUpgrade(new UpgradeAdvancedDimensional()))
        {
            // Same dimension but we don't have the upgrade
            missingUpgrade = true;
        }

        if ((exitLocation.dimension > 1 || exitLocation.dimension < -1) && !modifier.upgradeHandler.hasUpgrade(new UpgradeAdvancedDimensional()))
        {
            // Modded dimension but no upgrade
            missingUpgrade = true;
        }
        
        if (!missingUpgrade)
        {
            TeleportManager.teleportEntity(entity, exitLocation, modifier, false);
            entity.timeUntilPortal = entity.getPortalCooldown();
        }
        else
        {
            if (entity instanceof EntityPlayer)
            {
                ((EntityPlayer) entity).sendChatToPlayer("missingUpgrade");
            }
        }
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
            handleVanillaTeleportation(entity, world);
        }
        else if (portal.parentModifier != null && entity.timeUntilPortal == 0)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) portal.parentModifier.getTileEntity();

            if (modifier.isRemotelyControlled())
            {
                if (modifier.tempDialDeviceNetwork.equals(""))
                {
                    Reference.log.log(Level.WARNING, "Created a portal from a Dialling Device, but modifier doesn't have a network?");
                }
                else
                {
                    handleDialDeviceTeleportation(entity, modifier);
                }
            }
            else
            {
                if (modifier == null || modifier.modifierNetwork.equals(""))
                {
                    handleVanillaTeleportation(entity, world);
                }
                else
                {
                    handleModifierTeleportation(entity, modifier);
                }
            }
        }

        entity.timeUntilPortal = entity.getPortalCooldown();
    }

    private void handleModifierTeleportation(Entity entity, TileEntityPortalModifier modifier)
    {
        List<WorldLocation> validLocations = EnhancedPortals.proxy.ModifierNetwork.getNetworkExcluding(modifier.modifierNetwork, new WorldLocation(modifier.xCoord, modifier.yCoord, modifier.zCoord, modifier.worldObj));
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

            if ((randomLocation.dimension == -1 || randomLocation.dimension == 0 || randomLocation.dimension == 1) && !modifier.upgradeHandler.hasUpgrade(new UpgradeDimensional()) && !modifier.upgradeHandler.hasUpgrade(new UpgradeAdvancedDimensional()))
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

            if (randomLocation.dimension == modifier.worldObj.provider.dimensionId && !modifier.upgradeHandler.hasUpgrade(new UpgradeAdvancedDimensional()))
            {
                // Same dimension but we don't have the upgrade
                missingUpgrade = true;
                continue;
            }

            if ((randomLocation.dimension > 1 || randomLocation.dimension < -1) && !modifier.upgradeHandler.hasUpgrade(new UpgradeAdvancedDimensional()))
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

    public void handleNeighborChange(int id)
    {
        World world = getWorld();

        if (world.isRemote || id == 0 || !findPortalShape())
        {
            return;
        }

        WorldLocation location = new WorldLocation(xCoord, yCoord, zCoord, world);

        if (shape == 2 || shape == 3) // XY
        {
            if (location.getOffset(ForgeDirection.EAST).isBlockAir() || location.getOffset(ForgeDirection.WEST).isBlockAir() || location.getOffset(ForgeDirection.UP).isBlockAir() || location.getOffset(ForgeDirection.DOWN).isBlockAir())
            {
                removePortal();
            }
        }
        else if (shape == 4 || shape == 5) // ZY
        {
            if (location.getOffset(ForgeDirection.NORTH).isBlockAir() || location.getOffset(ForgeDirection.SOUTH).isBlockAir() || location.getOffset(ForgeDirection.UP).isBlockAir() || location.getOffset(ForgeDirection.DOWN).isBlockAir())
            {
                removePortal();
            }
        }
        else if (shape == 6 || shape == 7) // ZX
        {
            if (location.getOffset(ForgeDirection.EAST).isBlockAir() || location.getOffset(ForgeDirection.WEST).isBlockAir() || location.getOffset(ForgeDirection.NORTH).isBlockAir() || location.getOffset(ForgeDirection.SOUTH).isBlockAir())
            {
                removePortal();
            }
        }
    }

    private void handleVanillaTeleportation(Entity entity, World world)
    {
        if (world.provider.dimensionId > 0 || world.provider.dimensionId < -1)
        {
            return;
        }

        // TODO UNCOMMENT
        //entity.setInPortal();
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
            if ((shape == 2 || shape == 4) && world.canBlockSeeTheSky(xCoord, yCoord, zCoord))
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
            if (queueLoc.isEqual(loc))
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

        if (world.getBlockId(xCoord, yCoord, zCoord) != BlockIds.NetherPortal || !findPortalShape() || producesSound == sound && producesParticles == particles && this.thickness == thickness)
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
                    PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, world.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketNetherPortalUpdate((TileEntityNetherPortal) te)));
                }
            }
        }

        return true;
    }

    private Queue<WorldLocation> updateQueue(Queue<WorldLocation> queue, WorldLocation location)
    {
        if (shape == 2)
        {
            queue.add(location.getOffset(ForgeDirection.UP));
            queue.add(location.getOffset(ForgeDirection.DOWN));
            queue.add(location.getOffset(ForgeDirection.EAST));
            queue.add(location.getOffset(ForgeDirection.WEST));
        }
        else if (shape == 4)
        {
            queue.add(location.getOffset(ForgeDirection.UP));
            queue.add(location.getOffset(ForgeDirection.DOWN));
            queue.add(location.getOffset(ForgeDirection.NORTH));
            queue.add(location.getOffset(ForgeDirection.SOUTH));
        }
        else if (shape == 6)
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

        if (world.getBlockId(xCoord, yCoord, zCoord) != BlockIds.NetherPortal || !findPortalShape() || texture.equals(newTexture))
        {
            return false;
        }

        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        Queue<WorldLocation> addedBlocks = new LinkedList<WorldLocation>();
        queue.add(new WorldLocation(xCoord, yCoord, zCoord, world));
        texture = newTexture;

        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();
            TileEntity te = current.getTileEntity();

            if (te != null && te instanceof TileEntityNetherPortal && !queueContains(addedBlocks, current))
            {
                ((TileEntityNetherPortal) te).texture = texture;
                current.markBlockForUpdate();
                addedBlocks.add(current);
                updateQueue(queue, current);

                if ((current.getMetadata() == 3 || current.getMetadata() == 5 || current.getMetadata() == 7) && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
                {
                    PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 256, world.provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketNetherPortalUpdate((TileEntityNetherPortal) te)));
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
