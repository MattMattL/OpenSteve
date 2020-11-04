package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.BackPropHelper;
import com.muss.opensteve.entity.ai.brain.BackPropLog;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveMath;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class AIMovementController extends AIControllerBase
{
	private Vector3d entityPos;
	private Vector3d targetPos;

	public AIMovementController(BaseAIEntity entityIn, AIControllerBase subNNet)
	{
		super(entityIn, subNNet, 6 + 245 + 10, 20, 10, "AIMovementController");

		this.backPropHelper = new BackPropHelper();
		this.backPropLog = new BackPropLog(10, this.NET_IN, this.NET_OUT);

		this.backPropHelper.create("Health", 10);
		this.backPropHelper.create("FoodLevel", 10);
		this.backPropHelper.create("Distance", 10);

		this.entityPos = new Vector3d(0, 0, 0);
		this.targetPos = new Vector3d(0, 0, 0);
	}

	@Override
	protected void aiInitialise()
	{
		this.entityPos = this.entity.getPositionVec();

		this.backPropHelper.tick();
		this.backPropHelper.getKey("Distance").at().setValue(OpenSteveMath.distance(this.entityPos, this.targetPos));
		this.backPropHelper.getKey("FoodLevel").at().setValue(this.entity.getFoodStats().getFoodLevel() + this.entity.getFoodStats().getSaturationLevel());

		this.actionResult = ActionResultType.PASS;
		this.returnResult = ActionResultType.PASS;
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;
		Block block;
		BlockPos origin = new BlockPos(this.entityPos.x, this.entityPos.y, this.entityPos.z);

		// environmental factors
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosX();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosY();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosZ();

		this.deepNNet.vectorIn[iNNet++] = this.entity.getHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getFoodStats().getFoodLevel();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getFoodStats().getSaturationLevel();

		// surrounding info
		for(int x = -3; x <= 3; x++)
		{
			for(int y = -2; y <= 2; y++)
			{
				for(int z = -3; z <= 3; z++)
				{
					block = this.entity.world.getBlockState(origin.add(x, y, z)).getBlock();
					this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(block.asItem());
				}
			}
		}

		// RNN
		for(int i=0; i<this.deepNNet.NET_OUT; i++)
			this.deepNNet.vectorIn[iNNet++] = this.deepNNet.vectorOut[i];
	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0: // East
				this.targetPos = this.entityPos.add(1, 0, 0);
				break;
			case 1: // West
				this.targetPos = this.entityPos.add(-1, 0, 0);
				break;
			case 2: // South
				this.targetPos = this.entityPos.add(0, 0, 1);
				break;
			case 3: // North
				this.targetPos = this.entityPos.add(0, 0, -1);
				break;
			case 4: // South-East
				this.targetPos = this.entityPos.add(1, 0, 1);
				break;
			case 5: // North-East
				this.targetPos = this.entityPos.add(1, 0, -1);
				break;
			case 6: // South-West
				this.targetPos = this.entityPos.add(-1, 0, 1);
				break;
			case 7: // North-West
				this.targetPos = this.entityPos.add(-1, 0, -1);
				break;
			case 8: // Jump
				this.entity.getJumpController().setJumping();
				this.entity.getFoodStats().addExhaustion(0.8F);
				return;
			case 9: // Fire Sub NNets
				this.actionResult = this.subController.runEntityAI();
		}

		this.entity.getMoveHelper().setMoveTo(this.targetPos.x, this.targetPos.y, this.targetPos.z, 0.5D);

		this.backPropLog.tick();
		this.backPropLog.copy(this.deepNNet.vectorIn, this.deepNNet.vectorOut);
	}

	@Override
	protected ActionResultType fixEntityBehavior()
	{
		this.deepNNet.vectorIn = this.backPropLog.getInputVec();
		this.deepNNet.vectorOut = this.backPropLog.getOutputVec();

		// negative if harmed
		if(this.backPropHelper.getKey("Health").valueAt() < this.backPropHelper.getKey("Health").valueAt(-1))
			this.trainNegative();

		// negative if food level decreased
		if(this.backPropHelper.getKey("FoodLevel").valueAt() < this.backPropHelper.getKey("FoodLevel").valueAt(-1))
			this.trainNegative();

		// negative if the entity got stuck
		if(this.backPropHelper.getKey("Distance").valueAt() == this.backPropHelper.getKey("Distance").valueAt(-1))
			this.trainNegative();

		// positive if the entity is getting closer to the target
		if(this.backPropHelper.getKey("Distance").valueAt() < this.backPropHelper.getKey("Distance").valueAt(-1))
			this.trainPositive();

		return this.actionResult;
	}
}
