package com.muss.opensteve.command;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.command.impl.TestCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpenSteve.MOD_ID)
public class OpenSteveCommands
{
	public static void registerCommands(RegisterCommandsEvent event)
	{
        TestCommand.register(event.getDispatcher());
    }
}
