package com.muss.opensteve.entity.renderer;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class AlexAIEntityRenderer extends BipedRenderer<BaseAIEntity, BaseAIEntityModel<BaseAIEntity>>
{
	private static final ResourceLocation TEXTURE_ALEX_AI = new ResourceLocation(OpenSteve.MOD_ID, "textures/entity/alex_ai.png");

	public AlexAIEntityRenderer(EntityRendererManager manager)
	{
		super(manager, new BaseAIEntityModel(0, true), 0.5f);
	}

	public ResourceLocation getEntityTexture(BaseAIEntity entity)
	{
		return TEXTURE_ALEX_AI;
	}
}
