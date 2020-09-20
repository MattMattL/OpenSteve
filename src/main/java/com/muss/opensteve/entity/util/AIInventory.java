package com.muss.opensteve.entity.util;

import com.google.common.collect.ImmutableList;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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

	public int mainHandIndex = 0;
	public int offHandIndex = 0;
	private final BaseAIEntity entity;

	public AIInventory(BaseAIEntity entityIn)
	{
		this.entity = entityIn;
	}

	public ItemStack getCurrentItem()
	{
		return this.mainInventory.get(this.mainHandIndex);
	}

	public ItemStack getItemAt(int index)
	{
		index = this.boundHandIndex(index);

		if(0 <= index && index < this.mainInventory.size())
		{
			return this.mainInventory.get(index);
		}
		else
		{
			/* DEBUG */
			System.out.printf("[OpenSteve] [AIInventory::getItemAt] Index out of boundary: %2d out of %2d\n", index, this.mainInventory.size());
			return getCurrentItem();
		}
	}

	public void setMainHandIndex(int index)
	{
		index = this.boundHandIndex(index);

		if(0 <= index && index < this.mainInventory.size())
		{
			this.mainHandIndex = index;
		}
		else
		{
			/* DEBUG */
			System.out.printf("[OpenSteve] [AIInventory::setMainHandIndex] Index out of boundary: %2d out of %2d\n", index, this.mainInventory.size());
		}

		this.renderHeldItem();
	}

	private int boundHandIndex(int index)
	{
//		return (index + this.mainInventory.size()) % this.mainInventory.size();

		if(index < 0)
		{
			index = this.mainInventory.size() - Math.abs(index);
		}

		return index % this.mainInventory.size();
	}


	private boolean stackEqualExact(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	public int getFirstEmptySlot()
	{
		for(int i=0; i<this.mainInventory.size(); i++)
		{
			if(this.mainInventory.get(i).isEmpty())
				return i;
		}

		return -1;
	}

	@OnlyIn(Dist.CLIENT)
	public int getExistingSlotFor(ItemStack stackIn)
	{
		ItemStack currentStack;

		for(int i=0; i<this.mainInventory.size(); i++)
		{
			currentStack = this.mainInventory.get(i);

			if(!currentStack.isEmpty() && this.stackEqualExact(stackIn, currentStack) && currentStack.getCount() < stackIn.getMaxStackSize())
				return i;
		}

		return -1;
	}

	public int getAvailableSpaceFor(ItemStack stackIn)
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

	public int getAvailableSlotFor(ItemStack stackIn)
	{
		int existingSlot = this.getExistingSlotFor(stackIn);

		return (existingSlot > -1) ? existingSlot : this.getFirstEmptySlot();
	}

	public void addItemStackToInventory(ItemStack stackIn)
	{
		while(stackIn.getCount() > 0 && this.getAvailableSlotFor(stackIn) > -1)
		{
			int freeSlot = this.getAvailableSlotFor(stackIn);

			int stored = this.mainInventory.get(freeSlot).getCount();
			int storable = Math.min(stackIn.getCount(), stackIn.getMaxStackSize() - stored);
			int leftover = stackIn.getCount() - storable;

			this.mainInventory.set(freeSlot, new ItemStack(stackIn.getItem(), stored + storable));
			stackIn.setCount(leftover);
		}

		this.renderHeldItem();
	}

	public void renderHeldItem()
	{
		this.entity.setHeldItem(Hand.MAIN_HAND, this.mainInventory.get(this.mainHandIndex));
	}

	public void debug()
	{
		for(int i=0; i<this.mainInventory.size(); i++)
		{
			String itemName = this.mainInventory.get(i).getItem().getName().getString();
			int itemCount = this.mainInventory.get(i).getCount();

			System.out.printf("[OpenSteve] [AIInventory] Slot %2d  %16s  %2d\n", i, itemName, itemCount);
		}

		System.out.printf("\n");
	}


	public void read(CompoundNBT compound)
	{
		ListNBT listnbt = compound.getList("Inventory", 10);

		this.mainInventory.clear();
		this.armorInventory.clear();

		for(int i=0; i<listnbt.size(); i++)
		{
			CompoundNBT compoundnbt = listnbt.getCompound(i);
			int j = compoundnbt.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.read(compoundnbt);

			if(!itemstack.isEmpty())
			{
				if(j >= 0 && j < this.mainInventory.size())
				{
					this.mainInventory.set(j, itemstack);
				}
				else if(j >= 100 && j < this.armorInventory.size() + 100)
				{
					this.armorInventory.set(j - 100, itemstack);
				}
			}
		}
	}

	public void write(CompoundNBT compound)
	{
		ListNBT listnbt = new ListNBT();

		for(int i=0; i<this.mainInventory.size(); i++)
		{
			if(!this.mainInventory.get(i).isEmpty())
			{
				CompoundNBT compoundMain = new CompoundNBT();

				compoundMain.putByte("Slot", (byte)i);
				this.mainInventory.get(i).write(compoundMain);
				listnbt.add(compoundMain);
			}
		}

		for(int j=0; j<this.armorInventory.size(); j++)
		{
			if(!this.armorInventory.get(j).isEmpty())
			{
				CompoundNBT compoundArmor = new CompoundNBT();

				compoundArmor.putByte("Slot", (byte)(j+100));
				this.armorInventory.get(j).write(compoundArmor);
				listnbt.add(compoundArmor);
			}
		}

		compound.put("Inventory", listnbt);
	}
}
