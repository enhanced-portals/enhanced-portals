package uk.co.shadeddimensions.enhancedportals.multipart;

import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry.IPartConverter;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;

public class RegisterParts implements IPartConverter, IPartFactory
{
    @Override
    public TMultiPart createPart(String name, boolean client)
    {
        if (name.equals("enhancedportals:portal"))
        {
            return new PortalPart();
        }
        
        return null;
    }

    @Override
    public boolean canConvert(int blockID)
    {
        return blockID == CommonProxy.blockPortal.blockID;
    }

    @Override
    public TMultiPart convert(World world, BlockCoord pos)
    {
        int id = world.getBlockId(pos.x, pos.y, pos.z);
        int meta = world.getBlockMetadata(pos.x, pos.y, pos.z);
        
        if (id == CommonProxy.blockPortal.blockID)
        {
            return new PortalPart(meta);
        }
        
        return null;
    }
    
    public void init()
    {
        MultiPartRegistry.registerConverter(this);
        MultiPartRegistry.registerParts(this, new String[] { "enhancedportals:portal" });
    }
}
