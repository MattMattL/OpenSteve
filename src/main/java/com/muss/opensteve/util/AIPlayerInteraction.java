package com.muss.opensteve.util;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

public class AIPlayerInteraction
{
	public static ActionResultType getResult(BaseAIEntity entity, PlayerEntity player, Vector3d vector, Hand hand)
	{
		if(player.getHeldItemMainhand().getItem() == Items.STICK)
			return AIPlayerInteraction.onStickAction(entity);

		if(player.getHeldItemMainhand().getItem() == Items.CHEST && entity.shouldReceiveFeedback())
			return AIPlayerInteraction.onChestAction(entity, player);

		if(player.getHeldItemMainhand().getItem() == Items.APPLE && entity.shouldReceiveFeedback())
			return AIPlayerInteraction.onAppleAction(entity, player);

		return ActionResultType.PASS;
	}

	private static ActionResultType onStickAction(BaseAIEntity entity)
	{
		entity.setDead();
		entity.remove();
		entity.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.35F, ((entity.world.rand.nextFloat() - entity.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

		return ActionResultType.SUCCESS;
	}

	private static ActionResultType onChestAction(BaseAIEntity entity, PlayerEntity player)
	{
		OpenSteveStatics.MultiLines formatted = new OpenSteveStatics.MultiLines("[Inventory]");

		for(ItemStack itemStack : entity.inventory.mainInventory)
		{
			formatted.newline("  %2d  %s", itemStack.getCount(), itemStack.getItem().getName().getString());
		}

		player.sendMessage(new TranslationTextComponent("interaction.opensteve.onChestAction", formatted.getString()), Util.field_240973_b_);

		return ActionResultType.SUCCESS;
	}

	private static ActionResultType onAppleAction(BaseAIEntity entity, PlayerEntity player)
	{
		OpenSteveStatics.MultiLines formatted = new OpenSteveStatics.MultiLines("[Stats]")
				.newline("  Health: %5.2f", entity.getHealth())
				.newline("  FoodLevel: %2d", entity.getFoodStats().getFoodLevel())
				.newline("  Saturation: %5.2f", entity.getFoodStats().getSaturationLevel())
				.newline("  Exhaustion: %5.2f", entity.getFoodStats().getFoodExhaustionLevel());

		player.sendMessage(new TranslationTextComponent("text.opensteve.printString", formatted.getString()), Util.field_240973_b_);

		return ActionResultType.SUCCESS;
	}
}
