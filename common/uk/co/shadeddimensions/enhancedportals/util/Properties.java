package uk.co.shadeddimensions.enhancedportals.util;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class Properties
{
    public static void bindTexture(TextureManager manager, int textureSheet)
    {
        manager.func_110577_a(textureSheet == 0 ? TextureMap.field_110575_b : TextureMap.field_110576_c);
    }

    public static void bindTexture(TextureManager manager, String texture)
    {
        manager.func_110577_a(new ResourceLocation(texture));
    }

    public static void bindTexture(TextureManager manager, String mod, String texture)
    {
        manager.func_110577_a(new ResourceLocation(mod, texture));
    }
}
