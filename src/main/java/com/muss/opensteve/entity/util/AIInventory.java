package com.muss.opensteve.entity.util;

import com.google.common.collect.ImmutableList;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class AIInventory
{
	public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(8, ItemStack.EMPTY);
	public final NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
	private final List<NonNullList<ItemStack>> allInventories = ImmutableList.of(this.mainInventory, this.armorInventory);

	public final int inventoryStackLimit = 64;

	private int mainHandItem = 0;
	private int offHandItem = 0;
	private final BaseAIEntity entity;

	/* TEST */ PlayerInventory sampleInventory;

	public AIInventory(BaseAIEntity entityIn)
	{
		this.entity = entityIn;
	}

	public ItemStack getCurrentItem()
	{
		return this.mainInventory.get(this.mainHandItem);
	}

	private boolean stackEqualExact(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	public int getFirstEmptyStack()
	{
		for(int i=0; i<this.mainInventory.size(); i++)
		{
			if(this.mainInventory.get(i).isEmpty())
				return i;
		}

		return -1;
	}

	@OnlyIn(Dist.CLIENT)
	public int getSlotFor(ItemStack stack)
	{
		for(int i=0; i<this.mainInventory.size(); i++)
		{
			if (!this.mainInventory.get(i).isEmpty() && this.stackEqualExact(stack, this.mainInventory.get(i)))
				return i;
		}

		return -1;
	}


	public int getFreeInventorySpace(ItemStack stackIn)
	{
		if(stackIn.isEmpty())
			return 0;

		int storable = 0;

		for(int i=0; i<this.mainInventory.size(); i++)
		{
			ItemStack itemStack = this.mainInventory.get(i);

			if(itemStack.isEmpty())
				storable += stackIn.getMaxStackSize();
			else if(stackEqualExact(itemStack, stackIn))
				storable += stackIn.getMaxStackSize() - itemStack.getCount();

		}

		return storable;
	}

	private int getAvailableSlot(ItemStack stackIn)
	{
		int existingSlot = this.getSlotFor(stackIn);

		return (existingSlot > -1) ? existingSlot : this.getFirstEmptyStack();

	}

	public void addItemStackToInventory(ItemStack stackIn)
	{
		while(stackIn.getCount() > 0 && this.getAvailableSlot(stackIn) > -1)
		{
			int freeSlot = this.getAvailableSlot(stackIn);

			if(freeSlot < 0)
				break;

			int stored = this.mainInventory.get(freeSlot).getCount();
			int available = Math.min(stackIn.getCount(), stackIn.getMaxStackSize() - stored);
			int leftover = stackIn.getCount() - available;

			this.mainInventory.set(freeSlot, new ItemStack(stackIn.getItem(), stored + available));
			stackIn.setCount(leftover);
		}

		for(int i=0; i<this.mainInventory.size(); i++)
		{
			System.out.printf("[OpenSteve] [AIInventory] Slot %2d  %16s  %2d\n", i, this.mainInventory.get(i).getItem().getName().getString(), this.mainInventory.get(i).getCount());
		}

		System.out.printf("\n");

		this.renderHeldItems();
	}

	public void renderHeldItems()
	{
		this.entity.setHeldItem(Hand.MAIN_HAND, this.mainInventory.get(this.mainHandItem));
	}

}
