package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.entity.util.AIInventory;
import net.minecraft.item.Item;

public class AIInventoryController extends AIControllerBase
{
	AIInventory inventory;
	int nnetOut;
	int currentSlot;

	public AIInventoryController(BaseAIEntity entityIn)
	{
		super(entityIn, 6, 4, 3, "AIInventoryController");

		this.inventory = this.entity.inventory;
	}

	@Override
	protected void aiInitialise()
	{
		this.currentSlot = this.inventory.mainHandIndex;
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;

		this.deepNNet.vectorIn[iNNet] = Item.getIdFromItem(this.inventory.getItemAt(this.currentSlot - 1).getItem());
		this.deepNNet.vectorIn[iNNet] = this.inventory.getItemAt(this.currentSlot - 1).getCount();

		this.deepNNet.vectorIn[iNNet] = Item.getIdFromItem(this.inventory.getCurrentItem().getItem());
		this.deepNNet.vectorIn[iNNet] = this.inventory.getCurrentItem().getCount();

		this.deepNNet.vectorIn[iNNet] = Item.getIdFromItem(this.inventory.getItemAt(this.currentSlot + 1).getItem());
		this.deepNNet.vectorIn[iNNet] = this.inventory.getItemAt(this.currentSlot + 1).getCount();
	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		System.out.printf("[OpenSteve] current slot = %2d\n", this.currentSlot);

		switch(this.nnetOut)
		{
			case 0:
				this.inventory.setMainHandIndex(this.currentSlot - 1);
				break;
			case 1:
				this.inventory.setMainHandIndex(this.currentSlot + 1);
				break;
			case 2:
				break;
		}

		System.out.printf("[OpenSteve]     new slot = %2d\n\n", this.currentSlot);
	}

	@Override
	protected void fixEntityBehavior()
	{

	}
}
