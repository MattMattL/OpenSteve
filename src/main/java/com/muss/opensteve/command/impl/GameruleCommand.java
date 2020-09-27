package com.muss.opensteve.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.muss.opensteve.util.AIGameRules;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class GameruleCommand
{
	public static ArgumentBuilder<CommandSource, ?> register()
	{
		ArgumentBuilder<CommandSource, ?> arguments = Commands.literal("gamerule");
		AIGameRules.registerCommands(arguments);

		return arguments;
	}
}
