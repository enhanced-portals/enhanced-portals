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
import enhancedcore.world.WorldPosition;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Settings;
import enhancedportals.lib.Strings;
import enhancedportals.lib.Textures;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketNetherPortalUpdate;
import enhancedportals.portal.teleportation.TeleportManager;
import enhancedportals.portal.upgrades.modifier.UpgradeDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeMomentum;
import enhancedportals.portal.upgrades.modifier.UpgradeParticles;
import enhancedportals.portal.upgrades.modifier.UpgradeSounds;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class Portal
{
    WorldPosition portalModifier, position;
    public byte shape, thickness;
    public boolean producesSound, producesParticles;
    public String texture;

    public Portal()
    {

    }

    public Portal(int x, int y, int z, World world)
    {
        position = new WorldPosition(x, y, z, world);
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
        WorldPosition loc = new WorldPosition(modifier.xCoord, modifier.yCoord, modifier.zCoord, modifier.worldObj);

        position = loc.getOffset(ForgeDirection.getOrientation(loc.getMetadata()));
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
        if (position.getWorld().isRemote || !preChecks() || !findPortalShape())
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

        Queue<WorldPosition> queue = new LinkedList<WorldPosition>();
        Queue<WorldPosition> addedBlocks = new LinkedList<WorldPosition>();
        int usedChances = 0, MAX_CHANCES = 10;
        queue.add(position);

        while (!queue.isEmpty())
        {
            WorldPosition current = queue.remove();

            if (isBlockRemovable(current))
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
                    current.setBlock(BlockIds.NetherPortal, addedBlocks.size() == 1 ? shape + 1 : shape, 3);
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

        if (position.getBlockId() == BlockIds.NetherPortal)
        {
            shape = (byte) position.getMetadata();

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

    private int getSides(WorldPosition location)
    {
        int totalSides = 0;
        WorldPosition[] allBlocks = new WorldPosition[4];

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

        for (WorldPosition val : allBlocks)
        {
            if (isBlockFrame(val.getBlockId(), true))
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
        shape = (byte) theShape;

        if (position.getWorld().isRemote || !preChecks())
        {
            return false;
        }

        Queue<WorldPosition> queue = new LinkedList<WorldPosition>();
        Queue<WorldPosition> addedBlocks = new LinkedList<WorldPosition>();
        int usedChances = 0, MAX_CHANCES = 10;
        queue.add(position);

        while (!queue.isEmpty())
        {
            WorldPosition current = queue.remove();

            if (isBlockRemovable(current) && !queueContains(addedBlocks, current))
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
        if (position.getWorld().isRemote)
        {
            return false;
        }

        ItemStack item = player.getCurrentEquippedItem();
        int dyeColour = Textures.getDyeColour(item);
        
        if (dyeColour >= 0)
        {
            if (updateTexture("C:" + dyeColour) && !player.capabilities.isCreativeMode)
            {
                item.stackSize--;
            }

            return true;
        }

        return false;
    }

    private void handleDialDeviceTeleportation(Entity entity, TileEntityPortalModifier modifier)
    {
        WorldPosition exitLocation = EnhancedPortals.proxy.DialDeviceNetwork.getNetwork(modifier.tempDialDeviceNetwork).get(0);

        if (exitLocation == null || exitLocation.equals(position))
        {
            return;
        }

        if (upgradeCheck(modifier, modifier.worldObj.provider.dimensionId, exitLocation.getDimension()))
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
        if (!(position.getTileEntity() instanceof TileEntityNetherPortal))
        {
            return;
        }

        TileEntityNetherPortal portal = (TileEntityNetherPortal) position.getTileEntity();

        if (position.getWorld().isRemote)
        {
            if (Settings.RenderPortalEffect && !portal.texture.equals("I:" + Item.netherStar.itemID + ":0"))
            {
                entity.setInPortal();
            }

            return;
        }

        if (portal.getParentModifier() == null)
        {
            handleVanillaTeleportation(entity, position.getWorld());
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
                    handleVanillaTeleportation(entity, position.getWorld());
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
        List<WorldPosition> validLocations = EnhancedPortals.proxy.ModifierNetwork.getNetworkExcluding(modifier.modifierNetwork, new WorldPosition(modifier.xCoord, modifier.yCoord, modifier.zCoord, modifier.worldObj));
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
            WorldPosition randomLocation = validLocations.remove(new Random().nextInt(validLocations.size()));

            if (!upgradeCheck(modifier, modifier.worldObj.provider.dimensionId, randomLocation.getDimension()))
            {
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
        if (position.getWorld().isRemote || id == 0 || !findPortalShape())
        {
            return;
        }

        if (shape == 2 || shape == 3) // XY
        {
            if (position.getOffset(ForgeDirection.EAST).isAirBlock() || position.getOffset(ForgeDirection.WEST).isAirBlock() || position.getOffset(ForgeDirection.UP).isAirBlock() || position.getOffset(ForgeDirection.DOWN).isAirBlock())
            {
                removePortal();
            }
        }
        else if (shape == 4 || shape == 5) // ZY
        {
            if (position.getOffset(ForgeDirection.NORTH).isAirBlock() || position.getOffset(ForgeDirection.SOUTH).isAirBlock() || position.getOffset(ForgeDirection.UP).isAirBlock() || position.getOffset(ForgeDirection.DOWN).isAirBlock())
            {
                removePortal();
            }
        }
        else if (shape == 6 || shape == 7) // ZX
        {
            if (position.getOffset(ForgeDirection.EAST).isAirBlock() || position.getOffset(ForgeDirection.WEST).isAirBlock() || position.getOffset(ForgeDirection.NORTH).isAirBlock() || position.getOffset(ForgeDirection.SOUTH).isAirBlock())
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

    public boolean isBlockRemovable(WorldPosition pos)
    {
        if (pos.isAirBlock())
        {
            return true;
        }
        else
        {
            int val = pos.getBlockId();

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
        if (position.getWorld().isRemote)
        {
            return false;
        }

        if (isBlockRemovable(position))
        {
            if ((shape == 2 || shape == 4) && position.canBlockSeeTheSky())
            {
                return false;
            }

            return true;
        }

        return false;
    }

    private boolean queueContains(Queue<WorldPosition> queue, WorldPosition loc)
    {
        for (WorldPosition queueLoc : queue)
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
        if (position.getWorld().isRemote || position.getBlockId() != BlockIds.NetherPortal || !findPortalShape())
        {
            return false;
        }

        Queue<WorldPosition> queue = new LinkedList<WorldPosition>();
        queue.add(position);

        while (!queue.isEmpty())
        {
            WorldPosition current = queue.remove();

            if (current.getBlockId() == BlockIds.NetherPortal)
            {
                current.setBlockToAir();
                queue = updateQueue(queue, current);
            }
        }

        return true;
    }

    private void removePortal(Queue<WorldPosition> addedBlocks)
    {
        while (!addedBlocks.isEmpty())
        {
            WorldPosition current = addedBlocks.remove();
            current.setBlockToAir();
        }
    }

    public boolean updateData(boolean sound, boolean particles, byte thickness)
    {
        if (position.getBlockId() != BlockIds.NetherPortal || !findPortalShape() || producesSound == sound && producesParticles == particles && this.thickness == thickness)
        {
            return false;
        }

        Queue<WorldPosition> queue = new LinkedList<WorldPosition>();
        Queue<WorldPosition> addedBlocks = new LinkedList<WorldPosition>();
        queue.add(position);

        while (!queue.isEmpty())
        {
            WorldPosition current = queue.remove();
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
                    PacketDispatcher.sendPacketToAllAround(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 256, position.getWorld().provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketNetherPortalUpdate((TileEntityNetherPortal) te)));
                }
            }
        }

        return true;
    }

    private Queue<WorldPosition> updateQueue(Queue<WorldPosition> queue, WorldPosition location)
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
        if (position.getBlockId() != BlockIds.NetherPortal || !findPortalShape() || texture.equals(newTexture))
        {
            return false;
        }

        Queue<WorldPosition> queue = new LinkedList<WorldPosition>();
        Queue<WorldPosition> addedBlocks = new LinkedList<WorldPosition>();
        queue.add(position);
        texture = newTexture;

        while (!queue.isEmpty())
        {
            WorldPosition current = queue.remove();
            TileEntity te = current.getTileEntity();

            if (te != null && te instanceof TileEntityNetherPortal && !queueContains(addedBlocks, current))
            {
                ((TileEntityNetherPortal) te).texture = texture;
                current.markBlockForUpdate();
                addedBlocks.add(current);
                updateQueue(queue, current);

                if ((current.getMetadata() == 3 || current.getMetadata() == 5 || current.getMetadata() == 7) && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
                {
                    PacketDispatcher.sendPacketToAllAround(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 256, position.getWorld().provider.dimensionId, PacketEnhancedPortals.makePacket(new PacketNetherPortalUpdate((TileEntityNetherPortal) te)));
                }
            }
        }

        return true;
    }

    private boolean upgradeCheck(TileEntityPortalModifier modifier, int entryDimension, int exitDimension)
    {
        boolean hasUpgrade = modifier.upgradeHandler.hasUpgrade(new UpgradeDimensional());

        if ((exitDimension == -1 || exitDimension == 0 || exitDimension == 1) && !hasUpgrade)
        {
            if (exitDimension == -1 && entryDimension == 0 || exitDimension == 0 && entryDimension == -1)
            {
                // Allow overworld <--> nether travel
            }
            else
            {
                return false;
            }
        }

        if (exitDimension == entryDimension && !hasUpgrade)
        {
            return false;
        }

        if ((exitDimension > 1 || exitDimension < -1) && !hasUpgrade)
        {
            return false;
        }

        return true;
    }

    private boolean validatePortal(Queue<WorldPosition> addedBlocks)
    {
        if (position.getWorld().isRemote)
        {
            return true;
        }

        Queue<WorldPosition> queue = new LinkedList<WorldPosition>();
        Queue<WorldPosition> checkedQueue = new LinkedList<WorldPosition>();
        queue.add(position);

        while (!queue.isEmpty())
        {
            WorldPosition current = queue.remove();

            if (current.getBlockId() == BlockIds.NetherPortal && !queueContains(checkedQueue, current))
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
