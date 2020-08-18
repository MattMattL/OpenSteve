package com.muss.opensteve.entity.util;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;

import net.minecraft.block.BlockBookshelf;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryBgmAI
{
	public final int MAIN_INVENTORY = 10; //36;
	public final int ARMOUR_INVENTORY = 4;
	public final int OFFHAND_INVENTORY = 1;
	public final int ALL_INVENTORY= MAIN_INVENTORY + ARMOUR_INVENTORY + OFFHAND_INVENTORY;
	
	public final ItemStack allInventory[] = new ItemStack[ALL_INVENTORY];
	public final EntityBgmAI entity;
	
	private int currentSlot;
	
	public InventoryBgmAI(EntityBgmAI entityIn)
	{
		this.entity = entityIn;
		
		for(int i=0; i<ALL_INVENTORY; i++)
			this.allInventory[i] = ItemStack.EMPTY;
		
		this.currentSlot = 0;
	}
	
	
	// Each section of the inventories can be referred using functions below and local index. //
	
	private int indexMain(int index)
	{
		return index;
	}
	
	private int indexArmour(int index)
	{
		return MAIN_INVENTORY + index;
	}
	
	private int indexOffHand(int index)
	{
		return MAIN_INVENTORY + ARMOUR_INVENTORY + index;
	}
	
	
	// Inventory IO //
	
	public int canPickUpItem(ItemStack itemIn)
	{
		// return the index of a suitable slot
		for(int i=0; i<this.MAIN_INVENTORY; i++)
		{
			ItemStack currentSlot = this.allInventory[i];
			
			if(currentSlot.isItemEqual(itemIn))
			{
				if(currentSlot.getCount() < currentSlot.getMaxStackSize())
					return i;
			}
		}
		
		// return an empty slot
		for(int i=0; i<this.MAIN_INVENTORY; i++)
		{
			ItemStack currentSlot = this.allInventory[i];
			
			if(currentSlot.isEmpty())
				return i;
		}
		
		// no empty slots
		return -1;
	}
	
	/* Adds items to the inventory if the same item or an empty slot is found. */
	public void addItemToInventory(ItemStack itemIn)
	{
		int index = this.canPickUpItem(itemIn);
				
		if(index >= 0 && itemIn.getCount() > 0)
		{
			ItemStack currentSlot = this.allInventory[indexMain(index)];
			int freeSpace = itemIn.getMaxStackSize() - currentSlot.getCount();
			int delta = Math.min(itemIn.getCount(), freeSpace);

			// optimised version
			this.allInventory[indexMain(index)] = new ItemStack(itemIn.getItem(), currentSlot.getCount() + delta);
			itemIn.shrink(delta);
			
			// recursively search for free slots
			this.addItemToInventory(itemIn);
		}
		
		this.updateItemHolding();
	}
	
	public ItemStack getItemStackAt(int slot)
	{
		slot = Math.floorMod(slot, MAIN_INVENTORY-1);
		return this.allInventory[slot];
	}

	public ItemStack getItemStackAt()
	{
		return this.allInventory[this.currentSlot];
	}
	
	public ItemStack getArmourAt(int slot)
	{
		slot = Math.floorMod(slot, ARMOUR_INVENTORY-1);
		return this.allInventory[this.indexArmour(slot)];
	}
	
	public ItemStack getOffhandItemStack()
	{
		return this.allInventory[this.indexOffHand(0)];
	}
	
	
	// Managing currnet item stack this entity is hloding //
	
	public int getCurrentSlot()
	{
		return this.currentSlot;
	}
	
	public void setCurrentSlot(int slot)
	{
		this.currentSlot = Math.floorMod(slot, MAIN_INVENTORY-1);
		this.updateItemHolding();
	}
	
	public void updateItemHolding()
	{
		this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.getItemStackAt());
	}
	
	
	// Player Attributes //

	public void dropAllItems()
	{
		for (int i=0; i<ALL_INVENTORY; i++)
		{
			ItemStack itemStack = this.allInventory[i];

			if (!itemStack.isEmpty())
			{
				this.entity.dropItem(itemStack, true, false);
				this.allInventory[i] = ItemStack.EMPTY;
			}
		}
	}
	
	
	// Saving and loading inventory data (opstvCompound -> invenTagList -> itemCompound -> elements).
	
	public void writeToNBT(NBTTagCompound opstvCompound)
	{
		NBTTagCompound invenCompound = new NBTTagCompound();
		NBTTagList invenTagList = new NBTTagList();
		
		for(int i=0; i<ALL_INVENTORY; i++)
		{
			NBTTagCompound itemCompound = new NBTTagCompound();
			
			if(this.allInventory[i] != null)
				this.allInventory[i].writeToNBT(itemCompound);
			
			invenTagList.appendTag(itemCompound);
		}

		invenCompound.setTag("ItemSlots", invenTagList);
		invenCompound.setInteger("CurrentSlot", this.currentSlot);

		opstvCompound.setTag("Inventory", invenCompound);
	}

	public void readFromNBT(NBTTagCompound opstvCompound)
	{
		NBTTagCompound invenCompound = opstvCompound.getCompoundTag("Inventory");
		NBTTagList invenTagList = invenCompound.getTagList("ItemSlots", 10);

		for(int i=0; i<ALL_INVENTORY; i++)
		{
			if((NBTTagCompound)invenTagList.get(i) != null)
				this.allInventory[i] = new ItemStack((NBTTagCompound)invenTagList.get(i));
			else
				this.allInventory[i] = ItemStack.EMPTY;
			
			if(this.allInventory[i] == null)
				this.allInventory[i] = new ItemStack(new ItemAir(null));
		}

		this.currentSlot = invenCompound.getInteger("CurrentSlot");
	}
}




