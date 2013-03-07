package alz.mods.enhancedportals.client;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

import alz.mods.enhancedportals.reference.IO;

import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.FMLTextureFX;

public class TextureNetherPortalFX extends FMLTextureFX
{
	private byte[][] textureData;
	private int tickCounter;
	private int colour;
	
	public TextureNetherPortalFX(int colour)
	{
		super(colour);
		this.colour = colour;
		this.anaglyphEnabled = false;
	}

	@Override
	public void bindImage(RenderEngine renderEngine)
	{
		GL11.glBindTexture(3553, renderEngine.getTexture(IO.TerrainPath));
	}
	
	private void loadDefaultTexture()
	{
		textureData = new byte[32][tileSizeSquare << 4];
        Random var1 = new Random(100L);

        for (int var2 = 0; var2 < 32; ++var2)
        {
            for (int var3 = 0; var3 < tileSizeBase; ++var3)
            {
                for (int var4 = 0; var4 < tileSizeBase; ++var4)
                {
                    float var5 = 0.0F;
                    int var6;

                    for (var6 = 0; var6 < 2; ++var6)
                    {
                        float var7 = (float)(var6 * tileSizeBase) * 0.5F;
                        float var8 = (float)(var6 * tileSizeBase) * 0.5F;
                        float var9 = ((float)var3 - var7) / (float)tileSizeBase * 2.0F;
                        float var10 = ((float)var4 - var8) / (float)tileSizeBase * 2.0F;

                        if (var9 < -1.0F)
                        {
                            var9 += 2.0F;
                        }

                        if (var9 >= 1.0F)
                        {
                            var9 -= 2.0F;
                        }

                        if (var10 < -1.0F)
                        {
                            var10 += 2.0F;
                        }

                        if (var10 >= 1.0F)
                        {
                            var10 -= 2.0F;
                        }

                        float var11 = var9 * var9 + var10 * var10;
                        float var12 = (float)Math.atan2((double)var10, (double)var9) + ((float)var2 / 32.0F * (float)Math.PI * 2.0F - var11 * 10.0F + (float)(var6 * 2)) * (float)(var6 * 2 - 1);
                        var12 = (MathHelper.sin(var12) + 1.0F) / 2.0F;
                        var12 /= var11 + 1.0F;
                        var5 += var12 * 0.5F;
                    }

                    var5 += var1.nextFloat() * 0.1F;
                    var6 = (int)(var5 * 100.0F + 155.0F);
                    int var13 = (int)(var5 * var5 * 200.0F + 55.0F);
                    int var14 = (int)(var5 * var5 * var5 * var5 * 255.0F);
                    int var15 = (int)(var5 * 100.0F + 155.0F);
                    int var16 = var4 * tileSizeBase + var3;
                    this.textureData[var2][var16 * 4 + 0] = (byte)var13;
                    this.textureData[var2][var16 * 4 + 1] = (byte)var14;
                    this.textureData[var2][var16 * 4 + 2] = (byte)var6;
                    this.textureData[var2][var16 * 4 + 3] = (byte)var15;
                }
            }
        }
	}
	
	@Override
	public void setup()
	{
		super.setup();		
		this.tickCounter = 0;
		
		try
		{
			InputStream stream = FMLClientHandler.instance().getClient().texturePackList.getSelectedTexturePack().getResourceAsStream(IO.TextureDirectory + colour + ".png");
			
			if (stream == null)
			{
				System.out.println("Could not find texture pack file for " + colour + " using default portal for now.");
				
				loadDefaultTexture();				
				return;
			}
			
			BufferedImage image = ImageIO.read(stream);

			if (image.getWidth() % tileSizeBase > 0 || image.getHeight() != tileSizeBase)
			{
				//Reference.LogWarning("Invalid texture file shape and/or size on colour %s", false, colour);
				
				loadDefaultTexture();
				return;
			}
			
			int maxFrames = image.getWidth() / tileSizeBase;
			textureData = new byte[maxFrames][tileSizeSquare << 2];
			
			for (int frames = 0; frames < maxFrames; frames++)
			{
				for (int row = 0; row < tileSizeBase; row++)
				{
					for (int column = 0; column < tileSizeBase; column++)
					{
						int pixel = image.getRGB(column + frames * tileSizeBase, row);
						
						textureData[frames][((column + row * tileSizeBase) * 4)] = (byte)(pixel >> 16 & 0xFF);
						textureData[frames][((column + row * tileSizeBase) * 4 + 1)] = (byte)(pixel >> 8 & 0xFF);
						textureData[frames][((column + row * tileSizeBase) * 4 + 2)] = (byte)(pixel & 0xFF);
						textureData[frames][((column + row * tileSizeBase) * 4) + 3] = -56;
					}
				}				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		if (textureData == null)
			return;
		
		tickCounter++;
		
		if (tickCounter > textureData.length)
			tickCounter = 0;
		
		imageData = textureData[tickCounter % textureData.length].clone();
	}
}
