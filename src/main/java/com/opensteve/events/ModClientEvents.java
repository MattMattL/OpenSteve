package com.opensteve.events;

import com.opensteve.OpenSteve;
import com.opensteve.client.models.AlexAiModel;
import com.opensteve.client.models.BaseAiModel;
import com.opensteve.client.models.SteveAiModel;
import com.opensteve.client.renderer.AlexAiRenderer;
import com.opensteve.client.renderer.BaseAiRenderer;
import com.opensteve.client.renderer.SteveAiRenderer;
import com.opensteve.init.EntityInit;
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
		event.registerEntityRenderer(EntityInit.ALEX_AI.get(), AlexAiRenderer::new);
		event.registerEntityRenderer(EntityInit.STEVE_AI.get(), SteveAiRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
//		event.registerLayerDefinition(BaseAiModel.LAYER_LOCATION, BaseAiModel::createBodyLayer);
		event.registerLayerDefinition(AlexAiModel.LAYER_LOCATION, AlexAiModel::createBodyLayer);
		event.registerLayerDefinition(SteveAiModel.LAYER_LOCATION, SteveAiModel::createBodyLayer);
	}
}
