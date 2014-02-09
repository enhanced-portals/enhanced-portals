package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.portal.TilePortalPart;

public class ItemSynchronizer extends Item
{
    public static int ID;
    public static ItemSynchronizer instance;
    
    Icon texture;

    public ItemSynchronizer()
    {
        super(ID);
        ID += 256;
        instance = this;
        setCreativeTab(Reference.creativeTab);
        setUnlocalizedName("synchronizer");
    }

    @Override
    public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
    {
        return true;
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture;
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
            TileController controller = ((TilePortalPart) tile).getPortalController();

            if (controller == null)
            {
                return false;
            }
            else if (!controller.isPortalActive())
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("portalNotActive")));
                return false;
            }

            TileStabilizerMain stabilizer = controller.getDimensionalBridgeStabilizer();

            if (stabilizer != null)
            {
                GlyphIdentifier i = stabilizer.getConnectedPortal(controller.getIdentifierUnique());

                if (i == null)
                {
                    return false;
                }

                TileController pairedController = CommonProxy.networkManager.getPortalController(i);

                if (pairedController == null)
                {
                    return false;
                }

                if (tile instanceof TileBiometricIdentifier)
                {
                    TileBiometricIdentifier thisIdentifier = (TileBiometricIdentifier) tile, pairedIdentifier = pairedController.getBiometricIdentifier();

                    if (pairedIdentifier != null)
                    {
                        pairedIdentifier.defaultPermissions = thisIdentifier.defaultPermissions;
                        pairedIdentifier.entityList = thisIdentifier.copySendingEntityTypes();
                        PacketHandlerServer.sendUpdatePacketToAllAround(pairedIdentifier);
                    }

                    return true;
                }
                else if (tile instanceof TileDiallingDevice)
                {
                    // TODO
                    //TileDiallingDevice thisDialler = (TileDiallingDevice) tile, pairedDialler = pairedController.getDialDevice(world);

                    //if (pairedDialler != null)
                    //{
                    //    pairedDialler.glyphList = thisDialler.copyGlyphList();
                    //    CommonProxy.sendUpdatePacketToAllAround(pairedDialler);
                    //}

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("enhancedportals:synchronizer");
    }
}
