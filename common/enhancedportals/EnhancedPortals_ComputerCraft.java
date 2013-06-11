package enhancedportals;

import net.minecraft.block.Block;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.computercraft.block.BlockPortalModifier_cc;
import enhancedportals.computercraft.tileentity.TileEntityPortalModifier_cc;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;

@Mod(modid = Reference.MOD_ID + "_CC", name = "EP2 ComputerCraft", version = Reference.MOD_VERSION, dependencies = "required-after:" + Reference.MOD_ID + ";required-after:ComputerCraft")
public class EnhancedPortals_ComputerCraft
{
    @Instance(Reference.MOD_ID + "_CC")
    public static EnhancedPortals_ComputerCraft instance;

    @Init
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerTileEntity(TileEntityPortalModifier_cc.class, "EPPModifierCC");
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        Block.blocksList[BlockIds.PortalModifier] = null;
        EnhancedPortals.proxy.blockPortalModifier = null;

        EnhancedPortals.proxy.blockPortalModifier = new BlockPortalModifier_cc();
    }
}
