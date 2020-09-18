package com.muss.opensteve.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveDataTable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class KillCommand
{
	public static ArgumentBuilder<CommandSource, ?> register()
	{
		return Commands.literal("kill")
				.then(Commands.literal("all").executes(source -> { return killAIEntity(source.getSource()); }))
				.then(Commands.literal("alex").executes(source -> { return killAlexAIEntity(source.getSource()); }))
				.then(Commands.literal("steve").executes(source -> { return killSteveAIEntity(source.getSource()); }));
	}

	private static int killAIEntity(CommandSource source)
	{
		while(OpenSteveDataTable.aiEntityList.size() > 0)
		{
			OpenSteveDataTable.aiEntityList.get(0).onKillCommand();
			OpenSteveDataTable.aiEntityList.remove(0);
		}

		source.sendFeedback(new TranslationTextComponent("commands.opst.creator.kill.all"), true);
		return 1;
	}

	private static int killAlexAIEntity(CommandSource source)
	{
		int i=0;

		while(i < OpenSteveDataTable.aiEntityList.size())
		{
			BaseAIEntity entity = OpenSteveDataTable.aiEntityList.get(i);

			if(entity.isAlex())
			{
				entity.onKillCommand();
				OpenSteveDataTable.aiEntityList.remove(i);
			}
			else
			{
				i++;
			}
		}

		source.sendFeedback(new TranslationTextComponent("commands.opst.creator.kill.alex"), true);
		return 1;
	}

	private static int killSteveAIEntity(CommandSource source)
	{
		int i=0;

		while(i < OpenSteveDataTable.aiEntityList.size())
		{
			BaseAIEntity entity = OpenSteveDataTable.aiEntityList.get(i);

			if(entity.isSteve())
			{
				entity.onKillCommand();
				OpenSteveDataTable.aiEntityList.remove(i);
			}
			else
			{
				i++;
			}
		}

		source.sendFeedback(new TranslationTextComponent("commands.opst.creator.kill.steve"), true);
		return 1;
	}
}
