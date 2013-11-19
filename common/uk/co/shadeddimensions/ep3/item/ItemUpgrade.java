package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.item.base.ItemEnhancedPortals;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;

public class ItemUpgrade extends ItemEnhancedPortals
{
    Icon textures[];

    public ItemUpgrade(int par1, String name)
    {
        super(par1, true);
        setUnlocalizedName(name);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return textures[par1];
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        textures = new Icon[BlockFrame.FRAME_TYPES - 2];

        for (int i = 0; i < textures.length; i++)
        {
            textures[i] = par1IconRegister.registerIcon("enhancedportals:upgrade_" + i);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return super.getUnlocalizedName() + "." + ItemFrame.unlocalizedName[par1ItemStack.getItemDamage() + 2];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < textures.length; i++)
        {
            par3List.add(new ItemStack(itemID, 1, i));
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return false;
        }
        
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        int blockMeta = stack.getItemDamage() + 2;

        if (tile instanceof TileFrame)
        {
            TileFrame frame = (TileFrame) tile;

            if (frame.portalController == null)
            {
                frame = null;
                world.setBlock(x, y, z, CommonProxy.blockFrame.blockID, blockMeta, 2);
                decrementStack(stack);
                return true;
            }
            else
            {
                TilePortalController controller = frame.getPortalController();

                if (controller == null)
                {
                    frame = null;
                    world.setBlock(x, y, z, CommonProxy.blockFrame.blockID, blockMeta, 2);
                    decrementStack(stack);
                    return true;
                }
                else
                {
                    if (controller.blockManager.getHasBiometricIdentifier() && blockMeta == BlockFrame.BIOMETRIC_IDENTIFIER)
                    {
                        // cry 2 bios
                        
                        return false;
                    }
                    else if (controller.blockManager.getHasDialDevice() && (blockMeta == BlockFrame.DIALLING_DEVICE || blockMeta == BlockFrame.NETWORK_INTERFACE))
                    {
                        if (blockMeta == BlockFrame.DIALLING_DEVICE)
                        {
                            // cry 2 diallers
                        }
                        else
                        {
                            // cry dialler and network
                        }
                        
                        return false;
                    }
                    else if (controller.blockManager.getHasNetworkInterface() && (blockMeta == BlockFrame.DIALLING_DEVICE || blockMeta == BlockFrame.NETWORK_INTERFACE))
                    {
                        if (blockMeta == BlockFrame.NETWORK_INTERFACE)
                        {
                            // cry 2 network
                        }
                        else
                        {
                            // cry dialler and network
                        }
                        
                        return false;
                    }
                    else if (controller.blockManager.getHasModuleManipulator() && blockMeta == BlockFrame.MODULE_MANIPULATOR)
                    {
                        // cry 2 modules
                        
                        return false;
                    }

                    controller.blockManager.removeFrame(frame.getChunkCoordinates());
                    frame = null;
                    world.setBlock(x, y, z, CommonProxy.blockFrame.blockID, blockMeta, 2);
                    decrementStack(stack);
                    TilePortalPart t = (TilePortalPart) world.getBlockTileEntity(x, y, z);
                    
                    if (t instanceof TileRedstoneInterface)
                    {
                        controller.blockManager.addRedstoneInterface(t.getChunkCoordinates());
                    }
                    else if (t instanceof TileDiallingDevice)
                    {
                        controller.blockManager.setDialDevice(t.getChunkCoordinates());
                    }
                    else if (t instanceof TileBiometricIdentifier)
                    {
                        controller.blockManager.setBiometricIdentifier(t.getChunkCoordinates());
                    }
                    else if (t instanceof TileNetworkInterface)
                    {
                        controller.blockManager.setNetworkInterface(t.getChunkCoordinates());
                    }
                    else if (t instanceof TileModuleManipulator)
                    {
                        controller.blockManager.setModuleManipulator(t.getChunkCoordinates());
                    }
                    
                    t.portalController = controller.getChunkCoordinates();
                    CommonProxy.sendUpdatePacketToAllAround(t);
                    return true;
                }
            }
        }

        return false;
    }

    private void decrementStack(ItemStack stack)
    {
        stack.stackSize--;

        if (stack.stackSize <= 0)
        {
            stack = null;
        }
    }
}
