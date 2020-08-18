package com.muss.opensteve.entity.ai;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;

import com.muss.opensteve.entity.util.OnMouseClick;
import com.muss.opensteve.util.OSMaths;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;


public class BgmAIHandControl extends BgmAIBase
{
	// left/right click + throw + subNNet
	private static final int NET_OUT = 3 + 1;
	// hidden layer
	private static final int NET_MID = 5;
	// RNN + slot info + vision vector
	private static final int NET_IN = NET_OUT + 2 + 3;

	private final EntityBgmAI entity;
	private int instruction;

	public BgmAIHandControl(EntityBgmAI entityIn)
	{
		super(NET_IN, NET_MID, NET_OUT, "aiHandControl");

		this.entity = entityIn;
	}

	private void setInputVector()
	{
		int arr;

		// RNN
		for(arr=0; arr<NET_OUT; arr++)
			this.neuralNet.nnSetInputVec(arr, this.neuralNet.nnGetOutputVec(arr));

		// current held item
		ItemStack itemStack = this.entity.inventory.getItemStackAt();

		this.neuralNet.nnSetInputVec(arr++, Item.REGISTRY.getIDForObject(itemStack.getItem()));
		this.neuralNet.nnSetInputVec(arr++, itemStack.getCount());

		// vision vector
		RayTraceResult rayTrace = OSMaths.getRayTrace(this.entity);

		if(rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			this.neuralNet.nnSetInputVec(arr++, rayTrace.getBlockPos().getX());
			this.neuralNet.nnSetInputVec(arr++, rayTrace.getBlockPos().getY());
			this.neuralNet.nnSetInputVec(arr++, rayTrace.getBlockPos().getZ());
		}
		else
		{
			this.neuralNet.nnSetInputVec(arr++, 0);
			this.neuralNet.nnSetInputVec(arr++, 0);
			this.neuralNet.nnSetInputVec(arr++, 0);
		}
	}

	private void executeInstructions()
	{
		this.instruction = this.neuralNet.nnGetMaxOutputNode();

		switch(this.instruction)
		{
			case 0: // left mouse button
				OnMouseClick.onLeftClick(this.entity, this.entity.inventory.getItemStackAt());
				break;

			case 1: // right mouse button
				OnMouseClick.onRightClick(this.entity, this.entity.inventory.getItemStackAt());
				break;

			case 2: // throw held item
				this.entity.dropItem(new ItemStack(this.entity.inventory.getItemStackAt().getItem(), 1) , false, true);
				this.entity.inventory.allInventory[this.entity.inventory.getCurrentSlot()].shrink(1);
				break;

			case 3: // run subNNet
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

	public void startExecuting()
	{
		this.resetTask();
		this.setInputVector();
		this.neuralNet.nnRunFeedforward();

		this.executeInstructions();

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
