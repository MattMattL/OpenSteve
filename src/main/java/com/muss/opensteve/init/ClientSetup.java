package com.muss.opensteve.init;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.entity.BaseAIEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = OpenSteve.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
	public static void init(final FMLCommonSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(ModEntityType.STEVE_AI.get(), BaseAIEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityType.ALEX_AI.get(), BaseAIEntityRenderer::new);
	}
}
