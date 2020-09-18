package com.muss.opensteve.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveDataTable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class KillCommand
{
	public static ArgumentBuilder<CommandSource, ?> register()
	{
		return Commands.literal("kill")
				.then(Commands.literal("all").executes(source -> { return killAIEntities(source.getSource(), "all"); }))
				.then(Commands.literal("alex").executes(source -> { return killAIEntities(source.getSource(), "alex"); }))
				.then(Commands.literal("steve").executes(source -> { return killAIEntities(source.getSource(), "steve"); }));
	}

	private static int killAIEntities(CommandSource source, String type)
	{
		int i=0;

		while(i < OpenSteveDataTable.aiEntityList.size())
		{
			BaseAIEntity entity = OpenSteveDataTable.aiEntityList.get(i);

			if((type == "alex" && entity.isAlex()) || (type == "steve" && entity.isSteve()) || (type == "all"))
			{
				entity.onKillCommand();
				OpenSteveDataTable.aiEntityList.remove(i);
			}
			else
			{
				i++;
			}
		}

		source.sendFeedback(new TranslationTextComponent("commands.opst.kill.all"), true);
		return 1;
	}
}
