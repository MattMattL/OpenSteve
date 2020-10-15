package com.muss.opensteve.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveStatics;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class GiveoutCommand
{
	public static ArgumentBuilder<CommandSource, ?> register()
	{
		return Commands.literal("giveout")
				.then(Commands.argument("item", ItemArgument.item()).executes(source -> { return giveItem(source.getSource(), ItemArgument.getItem(source, "item"), 1); })
						.then(Commands.argument("count", IntegerArgumentType.integer(1)).executes(source -> { return giveItem(source.getSource(), ItemArgument.getItem(source, "item"), IntegerArgumentType.getInteger(source, "count")); })));
	}

	private static int giveItem(CommandSource source, ItemInput item, int count)
	{
		for(BaseAIEntity entity : OpenSteveStatics.aiEntityList)
			entity.inventory.addItemStackToInventory(new ItemStack(item.getItem(), count));

		source.sendFeedback(new TranslationTextComponent("commands.opensteve.creator.giveout.item", count, item.getItem().getName().getString()), true);

		return 1;
	}
}
