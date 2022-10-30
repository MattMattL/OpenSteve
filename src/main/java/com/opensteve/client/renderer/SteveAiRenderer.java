package com.opensteve.client.renderer;

import com.opensteve.OpenSteve;
import com.opensteve.client.models.SteveAiModel;
import com.opensteve.entity.EntitySteveAi;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class SteveAiRenderer extends LivingEntityRenderer<EntitySteveAi, SteveAiModel>
{
	private static final ResourceLocation STEVE_AI_TEXTURE = new ResourceLocation(OpenSteve.MODID, "textures/entity/steve_ai.png");

	public SteveAiRenderer(EntityRendererProvider.Context context)
	{
		super(context, new SteveAiModel(context.bakeLayer(SteveAiModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(EntitySteveAi entity)
	{
		return STEVE_AI_TEXTURE;
	}
}