package com.muss.opensteve.command;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.command.impl.GreetingCommand;
import com.muss.opensteve.command.impl.PrintCommand;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpenSteve.MOD_ID)
public class OpenSteveCommands
{
	public static void registerCommands(RegisterCommandsEvent event)
	{
        event.getDispatcher()
				.register(Commands.literal("creator").requires(context -> {return context.hasPermissionLevel(2);})
						.then(PrintCommand.register())
						.then(GreetingCommand.register())
						/* add commands here */
				);
    }
}
