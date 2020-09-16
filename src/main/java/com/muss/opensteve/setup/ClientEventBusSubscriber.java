package com.muss.opensteve.setup;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.command.impl.TestCommand;
import com.muss.opensteve.item.ModSpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = OpenSteve.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber
{
	@SubscribeEvent
	public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event)
	{
		ModSpawnEggItem.initSpawnEggs();
	}

	@SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event)
    {
		TestCommand.register(event.getDispatcher());
    }
}
