package com.alz.enhancedportals.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDialHomeDevice extends ModelBase
{
    ModelRenderer Base;
    ModelRenderer SmallBase;
    ModelRenderer RaisedArea;

    public ModelDialHomeDevice()
    {
        textureWidth = 64;
        textureHeight = 64;

        Base = new ModelRenderer(this, 0, 35);
        Base.addBox(0F, 0F, 0F, 16, 13, 16);
        Base.setRotationPoint(-8F, 11F, -8F);
        Base.setTextureSize(64, 32);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        SmallBase = new ModelRenderer(this, 0, 19);
        SmallBase.addBox(0F, 0F, 0F, 14, 1, 14);
        SmallBase.setRotationPoint(-7F, 10F, -7F);
        SmallBase.setTextureSize(64, 32);
        SmallBase.mirror = true;
        setRotation(SmallBase, 0F, 0F, 0F);
        RaisedArea = new ModelRenderer(this, 0, 0);
        RaisedArea.addBox(0F, 0F, 0F, 14, 4, 14);
        RaisedArea.setRotationPoint(-7F, 8F, -7F);
        RaisedArea.setTextureSize(64, 32);
        RaisedArea.mirror = true;
        setRotation(RaisedArea, -0.1745329F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Base.render(f5);
        SmallBase.render(f5);
        RaisedArea.render(f5);
    }

    public void renderAll()
    {
        Base.render(0.0625F);
        SmallBase.render(0.0625F);
        RaisedArea.render(0.0625F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
    }
}
