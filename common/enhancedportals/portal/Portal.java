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
import enhancedportals.lib.Strings;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketNetherPortalUpdate;
import enhancedportals.portal.teleportation.TeleportManager;
import enhancedportals.portal.upgrades.modifier.UpgradeAdvancedDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeMomentum;
import enhancedportals.portal.upgrades.modifier.UpgradeParticles;
import enhancedportals.portal.upgrades.modifier.UpgradeSounds;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class Portal
{
    public WorldLocation portalModifier;
    public int xCoord, yCoord, zCoord, dimension;
    public byte shape, thickness;
    public boolean producesSound, producesParticles;
    public String texture;

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
        return createPortal(1, 0);
    }

    public boolean createPortal(int minLimit, int maxLimit)
    {
        World world = getWorld();

        if (world.isRemote || !preChecks() || !findPortalShape())
        {
            return false;
        }

        if (Settings.MinimumPortalSize > 0)
        {
            minLimit = Settings.MinimumPortalSize;
        }
        
        if (Settings.MaximumPortalSize > 0)
        {
            maxLimit = Settings.MaximumPortalSize;
        }
        
        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        Queue<WorldLocation> addedBlocks = new LinkedList<WorldLocation>();
        int usedChances = 0, MAX_CHANCES = 10;
        queue.add(new WorldLocation(xCoord, yCoord, zCoord, world));

        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();

            if (isBlockRemovable(world, current.xCoord, current.yCoord, current.zCoord))
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
                            portal.setParentModifier(portalModifier);
                        }
                    }
                }
            }
        }

        if (!validatePortal(addedBlocks))
        {
            return false;
        }

        if (addedBlocks.size() < minLimit)
        {
            removePortal(addedBlocks);
            return false;
        }
        else if (maxLimit != 0 && addedBlocks.size() > maxLimit)
        {
            removePortal(addedBlocks);
            return false;
        }

        return true;
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

    public boolean createPortal(int[] extraBorderBlocks, ItemStack stack)
    {
        if (extraBorderBlocks == null || extraBorderBlocks.length == 0)
        {
            return createPortal(stack);
        }

        int size = Settings.BorderBlocks.size();

        for (int i : extraBorderBlocks)
        {
            Settings.BorderBlocks.add(i);
        }

        boolean status = createPortal(stack);

        for (int i = size; i < Settings.BorderBlocks.size(); i++)
        {
            Settings.BorderBlocks.remove(i);
        }

        return status;
    }

    public boolean createPortal(ItemStack stack)
    {
        if (stack.itemID == Item.flintAndSteel.itemID)
        {
            return Settings.AllowFlintSteel ? createPortal(6, 6) : createPortal(1, 0);
        }
        else if (stack.itemID == EnhancedPortals.proxy.itemEnhancedFlintSteel.itemID)
        {
            return createPortal(1, 0);
        }

        return false;
    }

    public boolean createPortal(String texture, byte thickness, int[] extraBorderBlocks)
    {
        this.texture = texture;
        this.thickness = thickness;
        return createPortal(extraBorderBlocks);
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
        WorldLocation[] allBlocks = new WorldLocation[4];

        if (shape == 6)
        {
            allBlocks[0] = location.getOffset(ForgeDirection.NORTH);
            allBlocks[1] = location.getOffset(ForgeDirection.SOUTH);
            allBlocks[2] = location.getOffset(ForgeDirection.EAST);
            allBlocks[3] = location.getOffset(ForgeDirection.WEST);
        }
        else if (shape == 2)
        {
            allBlocks[0] = location.getOffset(ForgeDirection.WEST);
            allBlocks[1] = location.getOffset(ForgeDirection.EAST);
            allBlocks[2] = location.getOffset(ForgeDirection.UP);
            allBlocks[3] = location.getOffset(ForgeDirection.DOWN);
        }
        else if (shape == 4)
        {
            allBlocks[0] = location.getOffset(ForgeDirection.NORTH);
            allBlocks[1] = location.getOffset(ForgeDirection.SOUTH);
            allBlocks[2] = location.getOffset(ForgeDirection.UP);
            allBlocks[3] = location.getOffset(ForgeDirection.DOWN);
        }

        for (WorldLocation val : allBlocks)
        {
            if (isBlockFrame(val.getBlockId(), true))
            {
                totalSides++;
            }
            else if (!isBlockRemovable(val.getWorld(), val.xCoord, val.yCoord, val.zCoord))
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

            if (isBlockRemovable(world, current.xCoord, current.yCoord, current.zCoord) && !queueContains(addedBlocks, current))
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
            TeleportManager.teleportEntity(entity, exitLocation, modifier, modifier.upgradeHandler.hasUpgrade(new UpgradeMomentum()), false);
            TeleportManager.setCanEntityTravel(entity, false);
        }
        else
        {
            if (entity instanceof EntityPlayer)
            {
                ((EntityPlayer) entity).sendChatToPlayer(Strings.ChatMissingUpgrade.toString());
            }
        }
    }

    public void handleEntityCollide(Entity entity)
    {
        World world = getWorld();

        if (!(world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityNetherPortal))
        {
            return;
        }

        TileEntityNetherPortal portal = (TileEntityNetherPortal) world.getBlockTileEntity(xCoord, yCoord, zCoord);

        if (world.isRemote)
        {
            if (Settings.RenderPortalEffect && !portal.texture.equals("I:" + Item.netherStar.itemID + ":0"))
            {
                entity.setInPortal();
            }

            return;
        }

        if (portal.getParentModifier() == null)
        {
            handleVanillaTeleportation(entity, world);
            return;
        }
        else if (portal.getParentModifier() != null && entity.timeUntilPortal == 0)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) portal.getParentModifier().getTileEntity();

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
                    return;
                }
                else
                {
                    handleModifierTeleportation(entity, modifier);
                }
            }
        }

        TeleportManager.setCanEntityTravel(entity, false);
    }

    private void handleModifierTeleportation(Entity entity, TileEntityPortalModifier modifier)
    {
        List<WorldLocation> validLocations = EnhancedPortals.proxy.ModifierNetwork.getNetworkExcluding(modifier.modifierNetwork, new WorldLocation(modifier.xCoord, modifier.yCoord, modifier.zCoord, modifier.worldObj));
        boolean missingUpgrade = false, teleport = false;

        if (validLocations.isEmpty())
        {
            if (entity instanceof EntityPlayer)
            {
                ((EntityPlayer) entity).sendChatToPlayer(Strings.ChatNoLinkedPortals.toString());
            }

            TeleportManager.setCanEntityTravel(entity, false);
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

            if (TeleportManager.teleportEntity(entity, randomLocation, modifier, modifier.upgradeHandler.hasUpgrade(new UpgradeMomentum()), validLocations.size() <= 1))
            {
                teleport = true;
                validLocations.clear();
            }
        }

        if (missingUpgrade && !teleport)
        {
            if (entity instanceof EntityPlayer)
            {
                ((EntityPlayer) entity).sendChatToPlayer(Strings.ChatMissingUpgrade.toString());
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

        entity.setInPortal();
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

    public boolean isBlockRemovable(World world, int x, int y, int z)
    {
        if (world.isAirBlock(x, y, z))
        {
            return true;
        }
        else
        {
            int val = world.getBlockId(x, y, z);

            for (int i : Settings.DestroyBlocks)
            {
                if (i == val)
                {
                    return true;
                }
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

        if (isBlockRemovable(world, xCoord, yCoord, zCoord))
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

        if (world.isRemote || location.getBlockId() != BlockIds.NetherPortal || !findPortalShape())
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
