package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.entity.util.AIInventory;
import net.minecraft.item.Item;

public class AIInventoryController extends AIControllerBase
{
	private AIInventory inventory;
	private int currentSlot;

	public AIInventoryController(BaseAIEntity entityIn)
	{
		super(entityIn, 21, 4, 3, "AIInventoryController");

		this.inventory = this.entity.inventory;
	}

	@Override
	public void setGlobalVariables()
	{

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

		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosX();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosY();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosZ();

		this.deepNNet.vectorIn[iNNet++] = this.entity.getMaxHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getFoodLevel();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getSaturationLevel();

		this.deepNNet.vectorIn[iNNet++] = this.entity.isAlex()? 1 : -1;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isBurning()? 1 : -1;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isInWater()? 1 : -1;

		this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(this.inventory.getItemAt(this.currentSlot - 2).getItem());
		this.deepNNet.vectorIn[iNNet++] = this.inventory.getItemAt(this.currentSlot - 2).getCount();

		this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(this.inventory.getItemAt(this.currentSlot - 1).getItem());
		this.deepNNet.vectorIn[iNNet++] = this.inventory.getItemAt(this.currentSlot - 1).getCount();

		this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(this.inventory.getCurrentItem().getItem());
		this.deepNNet.vectorIn[iNNet++] = this.inventory.getCurrentItem().getCount();
		this.deepNNet.vectorIn[iNNet++] = this.currentSlot;

		this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(this.inventory.getItemAt(this.currentSlot + 1).getItem());
		this.deepNNet.vectorIn[iNNet++] = this.inventory.getItemAt(this.currentSlot + 1).getCount();

		this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(this.inventory.getItemAt(this.currentSlot + 2).getItem());
		this.deepNNet.vectorIn[iNNet++] = this.inventory.getItemAt(this.currentSlot + 2).getCount();
	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

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
	}

	@Override
	protected void fixEntityBehavior()
	{

	}
}
