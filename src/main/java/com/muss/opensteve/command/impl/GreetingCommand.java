package com.muss.opensteve.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class GreetingCommand
{
	public static ArgumentBuilder<CommandSource, ?> register()
	{
		return Commands.literal("greet").executes(source -> {return printHelloMsg(source.getSource());})
				.then(Commands.literal("Dev").executes(source -> {return printGreetMsg(source.getSource());})
		);
	}

	private static int printHelloMsg(CommandSource source)
	{
		source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.greet", source.getName()), true);
		return 1;
	}

	private static int printGreetMsg(CommandSource source)
	{
		source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.greet.Dev", source.getEntity().getName().getString()), true);
		return 1;
	}
}
