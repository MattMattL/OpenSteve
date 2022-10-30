package com.opensteve.events;

import com.opensteve.OpenSteve;
import com.opensteve.client.models.BaseAiModel;
import com.opensteve.client.renderer.BaseAiRenderer;
import com.opensteve.init.EntityInit;
import net.minecraft.client.model.EntityModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpenSteve.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents
{
	@SubscribeEvent
	public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(EntityInit.BASE_AI.get(), BaseAiRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(BaseAiModel.LAYER_LOCATION, BaseAiModel::createBodyLayer);
	}
}
