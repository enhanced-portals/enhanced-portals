package uk.co.shadeddimensions.enhancedportals.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.util.PortalUtils;

public class ItemWrench extends Item
{
    public ItemWrench(int id, String name) // Needs a better name, "wrench" doesn't really describe what it does.
    {
        super(id);
        setUnlocalizedName(name);
        maxStackSize = 1;
        setMaxDamage(0);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (world.isRemote)
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == CommonProxy.blockFrame.blockID)
        {
            TileEP tile = (TileEP) world.getBlockTileEntity(x, y, z);
            
            if (tile instanceof TilePortalFrame)
            {
                TilePortalFrame frame = (TilePortalFrame) tile;
    
                if (frame.checkController())
                {
                    player.openGui(EnhancedPortals.instance, Identifiers.Gui.FRAME_CONTROLLER, world, frame.controller.posX, frame.controller.posY, frame.controller.posZ);
                }
                else
                {
                    if (PortalUtils.findNearbyPortalBlock((WorldServer) world, x, y, z))
                    {
                        if (PortalUtils.linkController((WorldServer) world, x, y, z))
                        {
                            player.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumChatFormatting.RED + "Portal terminated: " + EnumChatFormatting.GREEN + "Successfully initialized the portal"));
                        }
                    }
                    else
                    {
                        player.sendChatToPlayer(ChatMessageComponent.func_111066_d(EnumChatFormatting.RED + "Failed to initialize the portal: " + EnumChatFormatting.WHITE + "No portal found"));
                    }
                }
            }
            else if (tile instanceof TilePortalController)
            {
                player.openGui(EnhancedPortals.instance, Identifiers.Gui.FRAME_CONTROLLER, world, x, y, z);
            }

            return true;
        }

        return false;
    }
}
