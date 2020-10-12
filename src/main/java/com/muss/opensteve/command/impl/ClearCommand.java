package com.muss.opensteve.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.muss.opensteve.util.OpenSteveStatics;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class ClearCommand
{
	public static ArgumentBuilder<CommandSource, ?> register()
	{
		return Commands.literal("clear")
				.then(Commands.literal("entityList").executes(source -> { return clearEntityList(source.getSource()); }));
	}

	private static int clearEntityList(CommandSource source)
	{
		while(OpenSteveStatics.aiEntityList.size() > 0)
		{
			OpenSteveStatics.aiEntityList.get(0).setDead();
			OpenSteveStatics.aiEntityList.get(0).remove();
			OpenSteveStatics.aiEntityList.remove(0);
		}

		source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.clear.entityList"), true);
		return 1;
	}
}
