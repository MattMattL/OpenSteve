package com.muss.opensteve.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class PrintCommand
{
	public static ArgumentBuilder<CommandSource, ?> register()
	{
		return Commands.literal("print")
				.executes(source -> {return printTestMsg(source.getSource());} );
	}

	private static int printTestMsg(CommandSource source)
	{
		source.sendFeedback(new TranslationTextComponent("commands.opst.print"), true);
		return 1;
	}
}
