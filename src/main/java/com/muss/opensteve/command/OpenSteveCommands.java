package com.muss.opensteve.command;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.command.impl.GreetingCommand;
import com.muss.opensteve.command.impl.KillCommand;
import com.muss.opensteve.command.impl.PrintCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpenSteve.MOD_ID)
public class OpenSteveCommands
{
	public static void registerCommands(RegisterCommandsEvent event)
	{
		event.getDispatcher()
				.register(Commands.literal("opensteve").requires(context -> { return context.hasPermissionLevel(2); })
						.executes(source -> { return instructions(source.getSource()); })
				);

        event.getDispatcher()
				.register(Commands.literal("creator").requires(context -> { return context.hasPermissionLevel(2); })
						.then(PrintCommand.register())
						.then(GreetingCommand.register())
						.then(KillCommand.register())
						/* add commands here */
				);
    }

    private static int instructions(CommandSource source)
	{
		source.sendFeedback(new TranslationTextComponent("commands.opst.opensteve"), false);
		return 1;
	}
}
