package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveDataTable;

public class AIMotionController extends AIControllerBase
{
	public AIMotionController(BaseAIEntity entityIn)
	{
		super(entityIn, 10, 4, 4, "AIMotionController");
	}

	@Override
	protected void aiInitialise()
	{

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
	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0:
				this.resetMotion();
				this.entity.setSprinting(true);
				break;

			case 1:
				this.resetMotion();
				this.entity.setSneaking(true);
				break;

			case 2:
				this.resetMotion();
				this.entity.setSwimming(true);
				break;

			case 3:
				this.resetMotion();
				break;
		}
	}

	private void resetMotion()
	{
		this.entity.setSprinting(false);
		this.entity.setSneaking(false);
		this.entity.setSwimming(false);
	}

	@Override
	protected void fixEntityBehavior()
	{

	}
}
