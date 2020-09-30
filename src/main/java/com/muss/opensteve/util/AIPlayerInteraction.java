package com.muss.opensteve.util;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;

public class AIPlayerInteraction
{
	public static ActionResultType getResult(BaseAIEntity entity, PlayerEntity player, Vector3d vector, Hand hand)
	{
		if(player.getHeldItemMainhand().getItem() == Items.STICK)
			return AIPlayerInteraction.onStickAction(entity);

		return ActionResultType.PASS;
	}

	private static ActionResultType onStickAction(BaseAIEntity entity)
	{
		entity.setDead();
		entity.remove();
		entity.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((entity.world.rand.nextFloat() - entity.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

		return ActionResultType.SUCCESS;
	}
}
