package uk.co.shadeddimensions.enhancedportals.multipart;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import uk.co.shadeddimensions.enhancedportals.client.renderer.TilePortalPartRenderer;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import codechicken.lib.lighting.LazyLightMatrix;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.minecraft.McMetaPart;

public class PortalPart extends McMetaPart
{    
    public PortalPart(int meta)
    {
        super(meta);
    }
    
    public PortalPart()
    {
        
    }
    
    @Override
    public float getStrength(MovingObjectPosition hit, EntityPlayer player)
    {
        return -1F;
    }
    
    @Override
    public void onEntityCollision(Entity entity)
    {
        //System.out.println(entity.getEntityName() + " collided with me!");
    }
    
    @Override
    public Iterable<ItemStack> getDrops()
    {
        return Arrays.asList(new ItemStack[] { });
    }
    
    @Override
    public ItemStack pickItem(MovingObjectPosition hit)
    {
        return null;
    }
    
    @Override
    public boolean occlusionTest(TMultiPart npart)
    {
        return NormalOcclusionTest.apply(this, npart);
    }
    
    @Override
    public Iterable<Cuboid6> getOcclusionBoxes()
    {
        return super.getOcclusionBoxes();
    }
    
    @Override
    public boolean activate(EntityPlayer player, MovingObjectPosition part, ItemStack item)
    {
        //if (!getWorld().isRemote)
        //{
            //player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Activated!"));
        //}
        
        return false;
    }
    
    @Override
    public void renderDynamic(Vector3 pos, float frame, int pass)
    {
        if (pass == 1)
        {
            TilePortalPartRenderer render = new TilePortalPartRenderer();
            render.renderTileEntityAt(getTile(), pos.x, pos.y, pos.z, getMetadata());
        }
    }
    
    @Override
    public void renderStatic(Vector3 pos, LazyLightMatrix olm, int pass)
    {
        
    }
    
    @Override
    public Cuboid6 getBounds()
    {
        if (getMetadata() == 1)
        {            
            return new Cuboid6(0, 0, 0.375, 1, 1, 0.625);
        }
        else if (getMetadata() == 2)
        {
            return new Cuboid6(0.375, 0, 0, 0.625, 1, 1);
        }
        else if (getMetadata() == 3)
        {
            return new Cuboid6(0, 0.375, 0, 1, 0.625, 1);
        }
        
        return new Cuboid6(0, 0, 0, 1, 1, 1);
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes()
    {
        return super.getCollisionBoxes();
    }
    
    @Override
    public Block getBlock()
    {
        return CommonProxy.blockPortal;
    }

    @Override
    public String getType()
    {
        return "enhancedportals:portal";
    }
}
