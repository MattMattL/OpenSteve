package com.opensteve.client.renderer;

import com.opensteve.OpenSteve;
import com.opensteve.client.models.AlexAiModel;
import com.opensteve.client.models.BaseAiModel;
import com.opensteve.entity.EntityAlexAi;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class AlexAiRenderer extends LivingEntityRenderer<EntityAlexAi, AlexAiModel>
{
	private static final ResourceLocation ALEX_AI_TEXTURE = new ResourceLocation(OpenSteve.MODID, "textures/entity/alex_ai.png");

	public AlexAiRenderer(EntityRendererProvider.Context context)
	{
		super(context, new AlexAiModel(context.bakeLayer(AlexAiModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityAlexAi entity)
	{
		return ALEX_AI_TEXTURE;
	}
}