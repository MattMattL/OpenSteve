package com.muss.opensteve.entity;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.entity.monster.SteveAIEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class BaseAIEntityRenderer extends MobRenderer<BaseAIEntity, BaseAIEntityModel>
{
	private static final ResourceLocation TEXTURE_ALEX_AI = new ResourceLocation(OpenSteve.MOD_ID, "textures/entity/alex_ai.png");
	private static final ResourceLocation TEXTURE_STEVE_AI = new ResourceLocation(OpenSteve.MOD_ID, "textures/entity/steve_ai.png");

	public BaseAIEntityRenderer(EntityRendererManager manager)
	{
		super(manager, new BaseAIEntityModel(), 0.5f);
	}

	public ResourceLocation getEntityTexture(BaseAIEntity entity)
	{
		if(entity.isAlex())
			return TEXTURE_ALEX_AI;
		else if(entity.isSteve())
			return TEXTURE_STEVE_AI;
		else
			return null;
	}
}
