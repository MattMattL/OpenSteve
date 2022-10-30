package com.opensteve.events;

import com.opensteve.OpenSteve;
import com.opensteve.entity.BaseAiEntity;
import com.opensteve.init.EntityInit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpenSteve.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents
{
	@SubscribeEvent
	public static void entityAttributes(EntityAttributeCreationEvent event)
	{
		event.put(EntityInit.BASE_AI.get(), BaseAiEntity.getBaseAiAttributes().build());
	}
}
