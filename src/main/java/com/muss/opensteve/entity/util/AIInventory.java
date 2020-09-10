package com.muss.opensteve.entity.util;

import com.google.common.collect.ImmutableList;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class AIInventory
{
	public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(4, ItemStack.EMPTY);
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
		for(int i=0; i<this.mainInventory.size(); ++i)
		{
			if(this.mainInventory.get(i).isEmpty())
				return i;
		}

		return -1;
	}

	@OnlyIn(Dist.CLIENT)
	public int getSlotFor(ItemStack stack)
	{
		for(int i=0; i<this.mainInventory.size(); ++i)
		{
			if (!this.mainInventory.get(i).isEmpty() && this.stackEqualExact(stack, this.mainInventory.get(i)))
				return i;
		}

		return -1;
	}



	public int canPickUpItem(ItemStack stack)
	{
		if(stack.isEmpty())
			return 0;

		int capacity = 0;

		for(int i=0; i<this.mainInventory.size(); i++)
		{
			if(this.mainInventory.get(i).isEmpty())
			{
				capacity += stack.getMaxStackSize();
			}
			else if(stackEqualExact(stack, this.mainInventory.get(i)))
			{
				capacity += stack.getMaxStackSize() - this.mainInventory.get(i).getCount();
			}
		}

		return Math.min(capacity, stack.getCount());
	}

	public void addItemStackToInventory(ItemStack stackIn)
	{
		// merge with an existing slot if possible
		for(int i=0; i<this.mainInventory.size(); i++)
		{
			ItemStack currentStack = this.mainInventory.get(i);

			if(this.stackEqualExact(stackIn, currentStack) && currentStack.getCount() < currentStack.getMaxStackSize())
			{
				int slotCapacity = stackIn.getMaxStackSize() - currentStack.getCount();
				int store = Math.min(slotCapacity, stackIn.getCount());

				this.mainInventory.get(i).setCount(currentStack.getCount() + store);
				stackIn.setCount(stackIn.getCount() - store);

				break;
			}
		}

		// save the rest to empty slots
		while(stackIn.getCount() > 0 && this.getFirstEmptyStack() > -1)
		{
			int store = Math.min(stackIn.getMaxStackSize(), stackIn.getCount());

			this.mainInventory.set(this.getFirstEmptyStack(), new ItemStack(stackIn.getItem(), store));
			stackIn.setCount(stackIn.getCount() - store);
		}

		this.renderHeldItems();
	}

	public void renderHeldItems()
	{
		this.entity.setHeldItem(Hand.MAIN_HAND, this.mainInventory.get(this.mainHandItem));
	}

}
