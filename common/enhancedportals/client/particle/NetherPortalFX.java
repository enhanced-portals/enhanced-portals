package enhancedportals.client.particle;

import java.awt.Color;
import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import enhancedportals.lib.Settings;
import enhancedportals.lib.Textures;

public class NetherPortalFX extends EntityFX
{
    private float portalParticleScale;
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;

    public NetherPortalFX(World par1World, String texture, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        motionX = par8;
        motionY = par10;
        motionZ = par12;
        portalPosX = posX = par2;
        portalPosY = posY = par4;
        portalPosZ = posZ = par6;

        if ((Settings.RequireFancyGraphicsForParticles && Minecraft.isFancyGraphicsEnabled() && Settings.UseNewParticleEffects) || (!Settings.RequireFancyGraphicsForParticles && Settings.UseNewParticleEffects))
        {
            Texture mainTexture = null;
            TextureStitched icon = (TextureStitched) Textures.getTexture(texture).getPortalTexture();
            ItemStack item = Textures.getItemStackFromTexture(texture);

            if (item.getItem().getSpriteNumber() == 0)
            {
                mainTexture = FMLClientHandler.instance().getClient().renderEngine.textureMapBlocks.getTexture();
            }
            else
            {
                mainTexture = FMLClientHandler.instance().getClient().renderEngine.textureMapItems.getTexture();
            }

            int sW = mainTexture.getWidth();
            int iX = icon.getOriginX(), iY = icon.getOriginY(), w = icon.width, h = icon.height;

            ByteBuffer byteBuffer = mainTexture.getTextureData();
            byte[] abyte = new byte[sW * h * 4];
            byteBuffer.position(iY * sW * 4 + iX * 4);
            byteBuffer.get(abyte);

            int j = rand.nextInt(w), i = rand.nextInt(w);
            int k = j * sW * 4 + i * 4;
            byte b0 = 0;
            int l = b0 | (abyte[k + 2] & 255) << 0;
            l |= (abyte[k + 1] & 255) << 8;
            l |= (abyte[k + 0] & 255) << 16;
            l |= (abyte[k + 3] & 255) << 24;

            Color color = Color.decode(String.valueOf(l));
                        
            particleRed = color.getRed() / 255F;
            particleGreen = color.getGreen() / 255F;
            particleBlue = color.getBlue() / 255F;
        }
        else
        {
            float f = rand.nextFloat() * 0.6F + 0.4F;
            particleRed = particleGreen = particleBlue = 1.0F * f;
            particleGreen *= 0.3F;
            particleRed *= 0.9F;
        }

        portalParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        particleMaxAge = (int) (Math.random() * 10.0D) + 40;
        noClip = true;
        setParticleTextureIndex((int) (Math.random() * 8.0D));
    }

    /**
     * Gets how bright this entity is.
     */
    @Override
    public float getBrightness(float par1)
    {
        float var2 = super.getBrightness(par1);
        float var3 = (float) particleAge / (float) particleMaxAge;
        var3 = var3 * var3 * var3 * var3;
        return var2 * (1.0F - var3) + var3;
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        int var2 = super.getBrightnessForRender(par1);
        float var3 = (float) particleAge / (float) particleMaxAge;
        var3 *= var3;
        var3 *= var3;
        int var4 = var2 & 255;
        int var5 = var2 >> 16 & 255;
        var5 += (int) (var3 * 15.0F * 16.0F);

        if (var5 > 240)
        {
            var5 = 240;
        }

        return var4 | var5 << 16;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        float var1 = (float) particleAge / (float) particleMaxAge;
        float var2 = var1;
        var1 = -var1 + var1 * var1 * 2.0F;
        var1 = 1.0F - var1;
        posX = portalPosX + motionX * var1;
        posY = portalPosY + motionY * var1 + (1.0F - var2);
        posZ = portalPosZ + motionZ * var1;

        if (particleAge++ >= particleMaxAge)
        {
            setDead();
        }
    }

    @Override
    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        float var8 = (particleAge + par2) / particleMaxAge;
        var8 = 1.0F - var8;
        var8 *= var8;
        var8 = 1.0F - var8;
        particleScale = portalParticleScale * var8;
        super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
    }
}
