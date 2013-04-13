package com.alz.enhancedportals;

import com.alz.enhancedportals.reference.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = Reference.MOD_ID + "_world", name = Reference.MOD_NAME_SHORT + " World", version = Reference.MOD_VERSION, dependencies = "required-after:" + Reference.MOD_ID + "@" + Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class EnhancedPortals_World
{
    @Instance(Reference.MOD_ID + "_world")
    public static EnhancedPortals_World instance;
}
