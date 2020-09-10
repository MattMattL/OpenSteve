package com.muss.opensteve.entity.util;

import com.google.common.collect.ImmutableList;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class AIInventory
{
	public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
	public final NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
	private final List<NonNullList<ItemStack>> allInventories = ImmutableList.of(this.mainInventory, this.armorInventory);

	private int mainHandItem = 0;
	private int offHandItem = 0;
	private final BaseAIEntity entity;

	public AIInventory(BaseAIEntity entityIn)
	{
		this.entity = entityIn;
	}
}