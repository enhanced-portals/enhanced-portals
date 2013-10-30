package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.api.IPortalModule;
import uk.co.shadeddimensions.ep3.client.particle.PortalFX;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;

public class ItemPortalModule extends ItemEnhancedPortals implements IPortalModule
{
    public static enum PortalModules
    {
        REMOVE_PARTICLES, RAINBOW_PARTICLES, REMOVE_SOUNDS, KEEP_MOMENTUM, INVISIBLE_PORTAL, TINTSHADE_PARTICLES, GHOST_FRAME;

        public String getUniqueID()
        {
            ItemStack s = new ItemStack(CommonProxy.itemUpgrade, 1, ordinal());
            return ((IPortalModule) s.getItem()).getID(s);
        }
    }

    static Icon baseIcon;
    static Icon[] overlayIcons = new Icon[PortalModules.values().length];

    public ItemPortalModule(int par1, String name)
    {
        super(par1, true);
        setUnlocalizedName(name);
        setMaxDamage(0);
        setMaxStackSize(32);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add("Portal Module");        
        list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".desc"));
        
        if (stack.getItemDamage() == PortalModules.KEEP_MOMENTUM.ordinal() || stack.getItemDamage() == PortalModules.GHOST_FRAME.ordinal())
        {
            list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".desc2"));
        }
    }

    @Override
    public boolean canInstallUpgrade(TileModuleManipulator moduleManipulator, IPortalModule[] installedUpgrades, ItemStack upgrade)
    {
        return true;
    }

    @Override
    public boolean canRemoveUpgrade(TileModuleManipulator moduleManipulator, IPortalModule[] installedUpgrades, ItemStack upgrade)
    {
        return true;
    }

    @Override
    public boolean disableParticles(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {
        return upgrade.getItemDamage() == PortalModules.REMOVE_PARTICLES.ordinal();
    }

    @Override
    public boolean disablePortalRendering(TileModuleManipulator modulemanipulator, ItemStack upgrade)
    {
        return upgrade.getItemDamage() == PortalModules.INVISIBLE_PORTAL.ordinal();
    }

    @Override
    public Icon getIconFromDamageForRenderPass(int damage, int pass)
    {
        if (pass == 1)
        {
            return overlayIcons[damage];
        }

        return baseIcon;
    }

    @Override
    public String getID(ItemStack upgrade)
    {
        return Reference.SHORT_ID + "." + upgrade.getItemDamage();
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() == PortalModules.INVISIBLE_PORTAL.ordinal() || itemStack.getItemDamage() == PortalModules.GHOST_FRAME.ordinal())
        {
            return EnumRarity.epic;
        }
        else if (itemStack.getItemDamage() == PortalModules.KEEP_MOMENTUM.ordinal())
        {
            return EnumRarity.rare;
        }

        return EnumRarity.common;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(int itemID, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < PortalModules.values().length; i++)
        {
            list.add(new ItemStack(itemID, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public void onParticleCreated(TileModuleManipulator moduleManipulator, ItemStack upgrade, PortalFX particle)
    {
        if (upgrade.getItemDamage() == PortalModules.RAINBOW_PARTICLES.ordinal())
        {
            particle.setRBGColorF((float) Math.random(), (float) Math.random(), (float) Math.random());
        }
        else if (upgrade.getItemDamage() == PortalModules.TINTSHADE_PARTICLES.ordinal())
        {
            float particleRed = particle.getRedColorF(), particleGreen = particle.getGreenColorF(), particleBlue = particle.getBlueColorF();
            int i = ClientProxy.random.nextInt(3);
            
            if (i == 0)
            {
                particleRed *= (particleRed / 4) * 3;
                particleGreen *= (particleGreen / 4) * 3;
                particleBlue *= (particleBlue / 4) * 3;
            }
            else if (i == 1)
            {
                particleRed = (255 - particleRed) * ((particleRed / 4) * 3);
                particleGreen = (255 - particleGreen) * ((particleGreen / 4) * 3);
                particleBlue = (255 - particleBlue) * ((particleBlue / 4) * 3);
            }
            
            particle.setRBGColorF(particleRed, particleGreen, particleBlue);
        }
    }

    @Override
    public void onPortalCreated(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {

    }

    @Override
    public void onPortalRemoved(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {

    }

    @Override
    public void onUpgradeInstalled(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {

    }

    @Override
    public void onUpgradeRemoved(TileModuleManipulator moduleManipulator, ItemStack upgrade)
    {

    }

    @Override
    public boolean ghostPortalFrame(TileModuleManipulator tileModuleManipulator, ItemStack i)
    {
        return i.getItemDamage() == PortalModules.GHOST_FRAME.ordinal();
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
        baseIcon = register.registerIcon("enhancedportals:portalModule_base");

        for (int i = 0; i < overlayIcons.length; i++)
        {
            overlayIcons[i] = register.registerIcon("enhancedportals:portalModule_" + i);
        }
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
}
