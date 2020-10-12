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
				.then(Commands.literal("all").executes(source -> { return killAIEntity(source.getSource()); }))
				.then(Commands.literal("alex").executes(source -> { return killEntityByType(source.getSource(), AIEntityType.ALEX); }))
				.then(Commands.literal("steve").executes(source -> { return killEntityByType(source.getSource(), AIEntityType.STEVE); }))
				.then(Commands.literal("adult").executes(source -> { return killEntityByAge(source.getSource(), AIEntityType.ADULT); }))
				.then(Commands.literal("child").executes(source -> { return killEntityByAge(source.getSource(), AIEntityType.CHILD); }));
	}

	private static int killAIEntity(CommandSource source)
	{
		int count = 0;

		for(BaseAIEntity entity : OpenSteveDataTable.aiEntityList)
		{
			if(entity.isAlive())
			{
				entity.onKillCommand();
				count++;
			}
		}

		source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.kill.all", count), true);
		return 1;
	}

	private static int killEntityByType(CommandSource source, AIEntityType type)
	{
		int i = 0;
		int count = 0;

		for(BaseAIEntity entity : OpenSteveDataTable.aiEntityList)
		{
			if(entity.isAlive())
			{
				if((type == AIEntityType.ALEX && entity.isAlex()) || (type == AIEntityType.STEVE && entity.isSteve()))
				{
					entity.onKillCommand();
					count++;
				}
			}
		}

		switch(type)
		{
			case ALEX:
				source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.kill.alex", count), true);
				break;
			case STEVE:
				source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.kill.steve", count), true);
				break;
		}

		return 1;
	}

	private static int killEntityByAge(CommandSource source, AIEntityType type)
	{
		int i = 0;
		int count = 0;

		for(BaseAIEntity entity : OpenSteveDataTable.aiEntityList)
		{
			if(entity.isAlive())
			{
				if((type == AIEntityType.ADULT && !entity.isChild()) || (type == AIEntityType.CHILD && entity.isChild()))
				{
					entity.onKillCommand();
					count++;
				}
			}
		}

		switch(type)
		{
			case ADULT:
				source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.kill.adult", count), true);
				break;
			case CHILD:
				source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.kill.child", count), true);
				break;
		}

		return 1;
	}
}
