package com.muss.opensteve.command;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.command.impl.*;
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
		event.getDispatcher().register(Commands.literal("opensteve").requires(context -> { return context.hasPermissionLevel(2); })
				.executes(source -> { return instructions(source.getSource()); })
		);

        event.getDispatcher().register(Commands.literal("creator").requires(context -> { return context.hasPermissionLevel(2); })
				.then(KillCommand.register())
				.then(ClearCommand.register())
				.then(GameruleCommand.register())
				/* add commands here */
		);
    }

    private static int instructions(CommandSource source)
	{
		source.sendFeedback(new TranslationTextComponent("commands.opensteve.opensteve"), false);
		return 1;
	}
}
