package com.muss.opensteve.entity.render;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;
import com.muss.opensteve.util.Ref;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBgmAI extends RenderBiped<EntityBgmAI>
{
	public static final ResourceLocation BGMAI_ALEX_TEXTURES = new ResourceLocation(Ref.MOD_ID + ":textures/entity/alexai.png");
	public static final ResourceLocation BGMAI_STEVE_TEXTURES = new ResourceLocation(Ref.MOD_ID + ":textures/entity/steveai.png");

	public RenderBgmAI(RenderManager manager, boolean smallArmIn)
	{
		super(manager, new ModelBgmAI(0.0F, smallArmIn), 0.5F);
	}
	
	protected ResourceLocation getEntityTexture(EntityBgmAI entity)
	{	
		if(entity.isAlex())
			return BGMAI_ALEX_TEXTURES;
		else
			return BGMAI_STEVE_TEXTURES;
	}
	
	protected void applyRotations(EntityBgmAI entityLiving, float p_77043_2_, float rotationYaw, float particleTricks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, particleTricks);
	}
}
