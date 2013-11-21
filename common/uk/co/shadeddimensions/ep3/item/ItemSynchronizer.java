package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.item.base.ItemPortalTool;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class ItemSynchronizer extends ItemPortalTool
{
    Icon texture;

    public ItemSynchronizer(int id, String name)
    {
        super(id, true, name);
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("enhancedportals:synchronizer");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (world.isRemote)
        {
            return true;
        }

        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null && tile instanceof TilePortalPart)
        {
            TilePortalController controller = ((TilePortalPart) tile).getPortalController();

            if (controller == null)
            {
                return false;
            }
            else if (!controller.isPortalActive)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("portalNotActive")));
                return false;
            }

            TileStabilizerMain stabilizer = controller.blockManager.getDimensionalBridgeStabilizerTile();

            if (stabilizer != null)
            {
                GlyphIdentifier i = stabilizer.getConnectedPortal(controller.getUniqueIdentifier());

                if (i == null)
                {
                    return false;
                }

                TilePortalController pairedController = CommonProxy.networkManager.getPortalController(i);

                if (pairedController == null)
                {
                    return false;
                }

                if (tile instanceof TileBiometricIdentifier)
                {
                    TileBiometricIdentifier thisIdentifier = (TileBiometricIdentifier) tile, pairedIdentifier = pairedController.blockManager.getBiometricIdentifier(world);

                    if (pairedIdentifier != null)
                    {
                        pairedIdentifier.hasSeperateLists = thisIdentifier.hasSeperateLists;
                        pairedIdentifier.notFoundRecieve = thisIdentifier.notFoundRecieve;
                        pairedIdentifier.notFoundSend = thisIdentifier.notFoundSend;
                        pairedIdentifier.recievingEntityTypes = thisIdentifier.copyRecievingEntityTypes();
                        pairedIdentifier.sendingEntityTypes = thisIdentifier.copySendingEntityTypes();
                        CommonProxy.sendUpdatePacketToAllAround(pairedIdentifier);
                    }

                    return true;
                }
                else if (tile instanceof TileDiallingDevice)
                {
                    TileDiallingDevice thisDialler = (TileDiallingDevice) tile, pairedDialler = pairedController.blockManager.getDialDevice(world);

                    if (pairedDialler != null)
                    {
                        pairedDialler.glyphList = thisDialler.copyGlyphList();
                        CommonProxy.sendUpdatePacketToAllAround(pairedDialler);
                    }

                    return true;
                }
            }
        }

        return false;
    }
}
