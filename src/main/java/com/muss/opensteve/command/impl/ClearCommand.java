package com.muss.opensteve.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveDataTable;
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
		while(OpenSteveDataTable.aiEntityList.size() > 0)
		{
			OpenSteveDataTable.aiEntityList.get(0).setDead();
			OpenSteveDataTable.aiEntityList.get(0).remove();
			OpenSteveDataTable.aiEntityList.remove(0);
		}

		source.sendFeedback(new TranslationTextComponent("commands.opst.creator.clear.entityList"), true);
		return 1;
	}
}
