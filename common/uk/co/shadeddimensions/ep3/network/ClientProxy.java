package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.resources.ReloadableResourceManager;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.ep3.client.renderer.entity.RenderCreeper;
import uk.co.shadeddimensions.ep3.client.renderer.entity.RenderEnderman;
import uk.co.shadeddimensions.ep3.client.renderer.tile.FrameItemRenderer;
import uk.co.shadeddimensions.ep3.client.renderer.tile.PortalItemRenderer;
import uk.co.shadeddimensions.ep3.client.renderer.tile.TileFrameRenderer;
import uk.co.shadeddimensions.ep3.entity.mob.MobCreeper;
import uk.co.shadeddimensions.ep3.entity.mob.MobEnderman;
import uk.co.shadeddimensions.ep3.network.packet.PacketGuiData;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestData;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxy extends CommonProxy
{
    public class ParticleSet
    {
        public int[] frames;
        public int type;

        public ParticleSet(int t, int[] s)
        {
            frames = s;
            type = t;
        }
    }

    public static int editingDialEntry = -1;
    public static PortalTextureManager dialEntryTexture = new PortalTextureManager();

    public static ArrayList<Icon> customFrameTextures = new ArrayList<Icon>();
    public static ArrayList<Icon> customPortalTextures = new ArrayList<Icon>();
    public static ArrayList<ParticleSet> particleSets = new ArrayList<ParticleSet>();
    public static Random random = new Random();

    @Override
    public void miscSetup()
    {
        // Randomly chooses a particle then spawns it, stays static
        particleSets.add(new ParticleSet(0, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }));
        particleSets.add(new ParticleSet(0, new int[] { 16, 17 }));
        particleSets.add(new ParticleSet(0, new int[] { 19, 20, 21, 22 }));
        particleSets.add(new ParticleSet(0, new int[] { 48, 49 }));
        particleSets.add(new ParticleSet(0, new int[] { 96, 97 }));
        particleSets.add(new ParticleSet(0, new int[] { 112, 113, 114 }));
        particleSets.add(new ParticleSet(0, new int[] { 128, 129, 130, 131, 132, 133, 134, 135 }));
        particleSets.add(new ParticleSet(0, new int[] { 144, 145, 146, 147, 148, 149, 150, 151 }));
        particleSets.add(new ParticleSet(0, new int[] { 160, 161, 162, 163, 164, 165, 166, 167 }));
        particleSets.add(new ParticleSet(0, new int[] { 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250 }));

        // Will play through their animation once, then die
        particleSets.add(new ParticleSet(1, new int[] { 7, 6, 5, 4, 3, 2, 1 }));
        particleSets.add(new ParticleSet(1, new int[] { 135, 134, 133, 132, 131, 130, 129, 128 }));
        particleSets.add(new ParticleSet(1, new int[] { 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250 }));

        // Will play through their animation until they die
        particleSets.add(new ParticleSet(2, new int[] { 32 }));
        particleSets.add(new ParticleSet(2, new int[] { 33 }));
        particleSets.add(new ParticleSet(2, new int[] { 64 }));
        particleSets.add(new ParticleSet(2, new int[] { 65 }));
        particleSets.add(new ParticleSet(2, new int[] { 66 }));
        particleSets.add(new ParticleSet(2, new int[] { 80 }));
        particleSets.add(new ParticleSet(2, new int[] { 81 }));
        particleSets.add(new ParticleSet(2, new int[] { 82 }));
        particleSets.add(new ParticleSet(2, new int[] { 83 }));
        particleSets.add(new ParticleSet(2, new int[] { 164, 165 }));
    }

    @Override
    public File getWorldDir()
    {
        return new File(getBaseDir(), "saves/" + DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName());
    }

    @Override
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TilePortalPart.class, new TileFrameRenderer());
        MinecraftForgeClient.registerItemRenderer(blockFrame.blockID, new FrameItemRenderer());
        MinecraftForgeClient.registerItemRenderer(blockPortal.blockID, new PortalItemRenderer());
        RenderingRegistry.registerEntityRenderingHandler(MobEnderman.class, new RenderEnderman());
        RenderingRegistry.registerEntityRenderingHandler(MobCreeper.class, new RenderCreeper());
    }

    public static void sendGuiPacket(GuiPayload payload)
    {
        PacketDispatcher.sendPacketToServer(new PacketGuiData(payload).getPacket());
    }

    public static void requestTileData(TileEnhancedPortals tile)
    {
        PacketDispatcher.sendPacketToServer(new PacketRequestData(tile).getPacket());
    }

    public static boolean resourceExists(String file)
    {
        ReloadableResourceManager resourceManager = (ReloadableResourceManager) FMLClientHandler.instance().getClient().getResourceManager();

        try
        {
            resourceManager.getResource(new ResourceLocation("enhancedportals", file));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
