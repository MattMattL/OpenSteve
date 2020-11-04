package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.AIControllerHelper;
import com.muss.opensteve.entity.ai.brain.BackPropHelper;
import com.muss.opensteve.entity.ai.brain.BackPropLog;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.entity.util.AIInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

public class AIInventoryController extends AIControllerBase
{
	private AIInventory inventory;
	private int currentSlot;

	public AIInventoryController(BaseAIEntity entityIn, AIControllerBase subNNet)
	{
		super(entityIn, subNNet, 12 + 2 * entityIn.inventory.mainInventory.size(), 5, 4, "AIInventoryController");

		this.backPropHelper = new BackPropHelper();
		this.backPropLog = new BackPropLog(10, this.NET_IN, this.NET_OUT);

		this.backPropHelper.create("Health", 10);
		this.backPropHelper.create("FoodLevel", 10);

		this.inventory = this.entity.inventory;
	}

	@Override
	protected void aiInitialise()
	{
		this.currentSlot = this.inventory.mainHandIndex;

		this.backPropHelper.tick();
		this.backPropHelper.getKey("Health").at().setValue(this.entity.getHealth());
		this.backPropHelper.getKey("FoodLevel").at().setValue(this.entity.getFoodStats().getFoodLevel() + this.entity.getFoodStats().getSaturationLevel());

		this.actionResult = ActionResultType.PASS;
		this.returnResult = ActionResultType.PASS;
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;

		BlockPos targetPos = AIControllerHelper.getBlockRayTraceResult(this.entity).getPos();

		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosX();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosY();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosZ();
		this.deepNNet.vectorIn[iNNet++] = targetPos.getX();
		this.deepNNet.vectorIn[iNNet++] = targetPos.getY();
		this.deepNNet.vectorIn[iNNet++] = targetPos.getZ();

		this.deepNNet.vectorIn[iNNet++] = this.entity.getHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getFoodLevel();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getSaturationLevel();

		this.deepNNet.vectorIn[iNNet++] = this.entity.isBurning()? 1 : 0;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isInWater()? 1 : 0;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isInLava()? 1 : 0;


		for(int i=0; i<this.inventory.mainInventory.size(); i++)
		{
			ItemStack slot = this.inventory.getItemAt(this.currentSlot + i);

			this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(slot.getItem());
			this.deepNNet.vectorIn[iNNet++] = slot.getCount();
		}
	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0:
				this.inventory.addMainHandIndex(-1);
				break;
			case 1:
				this.inventory.addMainHandIndex(1);
				break;
			case 2:
				if(this.entity.inventory.getCurrentItem().getCount() > 0)
				{
					this.entity.dropItem(new ItemStack(this.entity.inventory.getCurrentItem().getItem(), 1), false, false);
					this.entity.inventory.getCurrentItem().shrink(1);

					this.actionResult = ActionResultType.SUCCESS;
				}
				else
				{
					this.actionResult = ActionResultType.FAIL;
				}

				break;
			case 3: // Fire Sub NNets
				this.actionResult = this.subController.runEntityAI();
		}

		this.backPropLog.tick();
		this.backPropLog.copy(this.deepNNet.vectorIn, this.deepNNet.vectorOut);
	}

	@Override
	protected ActionResultType fixEntityBehavior()
	{
		// negative if harmed
		if(this.backPropHelper.getKey("Health").valueAt() < this.backPropHelper.getKey("Health").valueAt(-1))
			this.trainNegative();

		// negative if food level decreased
		if(this.backPropHelper.getKey("FoodLevel").valueAt() < this.backPropHelper.getKey("FoodLevel").valueAt(-1))
			this.trainNegative();

		// train with passed result
		if(this.actionResult.isSuccessOrConsume())
			this.trainPositive();
		else
			this.trainNegative();

		return this.actionResult;
	}
}
