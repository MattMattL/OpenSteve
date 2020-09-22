package com.muss.opensteve.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.AIEntityType;
import com.muss.opensteve.util.OpenSteveDataTable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class KillCommand
{
	public static ArgumentBuilder<CommandSource, ?> register()
	{
		return Commands.literal("kill")
				.then(Commands.literal("all").executes(source-> { return killAIEntity(source.getSource()); }))
				.then(Commands.literal("alex").executes(source-> { return killEntityByType(source.getSource(), AIEntityType.ALEX); }))
				.then(Commands.literal("steve").executes(source-> { return killEntityByType(source.getSource(), AIEntityType.STEVE); }));
	}

	private static int killAIEntity(CommandSource source)
	{
		int removed = 0;

		while(OpenSteveDataTable.aiEntityList.size()>0)
		{
			OpenSteveDataTable.aiEntityList.get(0).onKillCommand();
			OpenSteveDataTable.aiEntityList.remove(0);
			removed++;
		}

		source.sendFeedback(new TranslationTextComponent("commands.opst.creator.kill.all", removed), true);
		return 1;
	}

	private static int killEntityByType(CommandSource source, AIEntityType type)
	{
		int i = 0;
		int removed = 0;

		while(i<OpenSteveDataTable.aiEntityList.size())
		{
			BaseAIEntity entity = OpenSteveDataTable.aiEntityList.get(i);

			if((type == AIEntityType.ALEX && entity.isAlex()) || (type == AIEntityType.STEVE && entity.isSteve()))
			{
				entity.onKillCommand();
				OpenSteveDataTable.aiEntityList.remove(i);
				removed++;
			}
			else
			{
				i++;
			}
		}

		switch(type)
		{
			case ALEX:
				source.sendFeedback(new TranslationTextComponent("commands.opst.creator.kill.alex", removed), true);
				break;
			case STEVE:
				source.sendFeedback(new TranslationTextComponent("commands.opst.creator.kill.steve", removed), true);
				break;
		}

		return 1;
	}

	private static int killEntityByAge(CommandSource source)
	{
		return 1;
	}
}
