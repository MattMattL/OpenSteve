package com.muss.opensteve.util;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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

		if(player.getHeldItemMainhand().getItem() == Items.CHEST)
			return AIPlayerInteraction.onChestAction(entity, player);

		return ActionResultType.PASS;
	}

	private static ActionResultType onStickAction(BaseAIEntity entity)
	{
		entity.setDead();
		entity.remove();
		entity.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((entity.world.rand.nextFloat()-entity.world.rand.nextFloat())*0.7F+1.0F)*2.0F);

		return ActionResultType.SUCCESS;
	}

	private static ActionResultType onChestAction(BaseAIEntity entity, PlayerEntity player)
	{
		if(!entity.shouldReceiveFeedback())
			return ActionResultType.PASS;

		for(ItemStack itemStack : entity.inventory.mainInventory)
		{
			String formatted = String.format("%3d  %s", itemStack.getCount(), itemStack.getItem().getName().getString());

			player.sendMessage(new TranslationTextComponent("interaction.opst.onChestAction", formatted), Util.field_240973_b_);
		}

		return ActionResultType.SUCCESS;
	}
}
