package com.alz.enhancedportals.computercraft;

import java.util.logging.Level;

import net.minecraft.block.Block;

import com.alz.enhancedportals.reference.Log;
import com.alz.enhancedportals.reference.Reference;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid=Reference.MOD_ID + "_cc", name=Reference.MOD_NAME + " ComputerCraft", version=Reference.MOD_VERSION, dependencies="required-after:" + Reference.MOD_ID + "@" + Reference.MOD_VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class EP_ComputerCraft
{
    public static Block ccComputer = null;
    
    @Init
    public static void init(FMLInitializationEvent event)
    {
        if (Loader.isModLoaded("ComputerCraft"))
        {
            try
            {
                ccComputer = Block.blocksList[(int) Class.forName("dan200.ComputerCraft").getField("computerBlockID").get(null)];
                loadData();
                Log.log(Level.INFO, "Successfully loaded ComputerCraft addon.");
            }
            catch (Exception e)
            {
                Log.log(Level.SEVERE, "Could not load ComputerCraft addon.");
                e.printStackTrace(System.err);
            }
        }
        else
        {
            Log.log(Level.INFO, "Could not load ComputerCraft addon: ComputerCraft was not found.");
        }
    }
    
    private static void loadData()
    {
        // add recipes
        // enable cc stuff
        // replace portal modifier and dial device with cc versions
    }
}
