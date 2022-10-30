package com.opensteve.client.renderer;

import com.opensteve.OpenSteve;
import com.opensteve.client.models.BaseAiModel;
import com.opensteve.entity.BaseAiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class BaseAiRenderer extends LivingEntityRenderer<BaseAiEntity, BaseAiModel>
{
	private static final ResourceLocation BASE_AI_TEXTURE = new ResourceLocation(OpenSteve.MODID, "textures/entities/steve_ai.png");

	public BaseAiRenderer(EntityRendererProvider.Context context)
	{
		super(context, new BaseAiModel(context.bakeLayer(BaseAiModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(BaseAiEntity entity)
	{
		return BASE_AI_TEXTURE;
	}
}
