package com.opensteve.client.renderer;

import com.opensteve.OpenSteve;
import com.opensteve.client.models.BaseAiModel;
import com.opensteve.entity.BaseAiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

@Deprecated
public class BaseAiRenderer extends LivingEntityRenderer<BaseAiEntity, BaseAiModel>
{
	private static final ResourceLocation STEVE_AI_TEXTURE = new ResourceLocation(OpenSteve.MODID, "textures/entity/steve_ai.png");
	private static final ResourceLocation ALEX_AI_TEXTURE = new ResourceLocation(OpenSteve.MODID, "textures/entity/alex_ai.png");

	public BaseAiRenderer(EntityRendererProvider.Context context)
	{
		super(context, new BaseAiModel(context.bakeLayer(BaseAiModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(BaseAiEntity entity)
	{
		return entity.isSteve()? STEVE_AI_TEXTURE : ALEX_AI_TEXTURE;
	}
}
