package com.muss.opensteve.command.impl;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class TestCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("opst").requires(context -> {return context.hasPermissionLevel(2);})
				.then(Commands.literal("print").executes(source -> {return printTestMsg(source.getSource());}))
		);
	}

	private static int printTestMsg(CommandSource source)
	{
		source.sendFeedback(new TranslationTextComponent("commands.opst.test"), true);
		return 1;
	}
}
