package com.muss.opensteve.entity.ai;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;
import com.muss.opensteve.entity.util.InventoryBgmAI;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BgmAIInventory extends BgmAIBase
{
	// scroll up/down + subNNet
	private static final int NET_OUT = 2 + 1;
	// hidden layer
	private static final int NET_MID = 4;
	// RNN + info of three items around the current slot
	private static final int NET_IN = NET_OUT + 6;
	
	private final EntityBgmAI entity;
	//private InventoryBgmAI inventory;
	private int instruction;
	
	// TEST
	private int healthBK;

	public BgmAIInventory(EntityBgmAI entityIn)
	{
		super(NET_IN, NET_MID, NET_OUT, "aiInventory");
		
		this.entity = entityIn;
		//this.inventory = entityIn.inventory;
	}

	private void setInputVector()
	{
		int arr = 0;
		
		// RNN
		for(arr=0; arr<this.neuralNet.NET_OUT; arr++)
			this.neuralNet.nnSetInputVec(arr, this.neuralNet.nnGetOutputVec(arr));
		
		// put current slot info
		for(int offset=-1; offset<=1; offset++)
		{
			ItemStack itemStack = this.entity.inventory.getItemStackAt(this.entity.inventory.getCurrentSlot() + offset);
			
			this.neuralNet.nnSetInputVec(arr++, Item.REGISTRY.getIDForObject(itemStack.getItem()));
			this.neuralNet.nnSetInputVec(arr++, itemStack.getCount());
		}
	}

	private void executeInstructions()
	{	
		this.instruction = this.neuralNet.nnGetMaxOutputNode();
				
		switch(this.instruction)
		{
			case 0:
				this.entity.inventory.setCurrentSlot(this.entity.inventory.getCurrentSlot() + 1);
				break;
				
			case 1:
				this.entity.inventory.setCurrentSlot(this.entity.inventory.getCurrentSlot() - 1);
				break;
				
			case 2:
				this.runSubNNet();
				this.runSubNNetPostInit();
				break;
		}
	}

	private void runBackpropagation(boolean isPositive, boolean allowMutation)
	{
		for(int i=0; i<NET_OUT; i++)
		{
			int desiredOut = ((isPositive)? 0 : 1) ^ this.instruction;
			this.neuralNet.nnSetDesiredOut(i, desiredOut);
		}

		if(allowMutation)
		{
			int mutant = (int)(Math.random() * (NET_OUT - 1));
			this.neuralNet.nnSetDesiredOut(mutant, (isPositive)? 1 : 0);
		}

		this.neuralNet.nnRunBackprop();
	}


	private float prevHealth;
	private int prevFoodLevel;
	private float prevSaturation;
	
	@Override
	public void startExecuting()
	{
		// run nnet
		this.resetTask();
		this.setInputVector();
		this.neuralNet.nnRunFeedforward();
		
		this.executeInstructions();

		// run backprop
		if(this.prevHealth > this.entity.getHealth())
			this.runBackpropagation(false, true);

		if(this.prevFoodLevel > this.entity.foodStats.getFoodLevel())
			this.runBackpropagation(false, true);

		if(this.prevSaturation > this.entity.foodStats.getSaturationLevel())
			this.runBackpropagation(false, true);
	}

	@Override
	public void resetTask()
	{
		this.prevHealth = this.entity.getHealth();
		this.prevFoodLevel = this.entity.foodStats.getFoodLevel();
		this.prevSaturation = this.entity.foodStats.getSaturationLevel();
	}
}
