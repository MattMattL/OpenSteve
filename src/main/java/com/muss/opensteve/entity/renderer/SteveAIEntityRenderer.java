package com.muss.opensteve.entity.renderer;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class SteveAIEntityRenderer extends BipedRenderer<BaseAIEntity, BaseAIEntityModel<BaseAIEntity>>
{
	private static final ResourceLocation TEXTURE_STEVE_AI = new ResourceLocation(OpenSteve.MOD_ID, "textures/entity/steve_ai.png");

	public SteveAIEntityRenderer(EntityRendererManager manager)
	{
		super(manager, new BaseAIEntityModel(0, false), 0.5f);
	}

	public ResourceLocation getEntityTexture(BaseAIEntity entity)
	{
		return TEXTURE_STEVE_AI;
	}
}
