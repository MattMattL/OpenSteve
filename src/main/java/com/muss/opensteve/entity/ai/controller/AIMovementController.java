package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.BackPropLog;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveMath;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class AIMovementController extends AIControllerBase
{
	private Vector3d entityPos;
	private Vector3d targetPos;

	private BackPropLog backPropLog;

	public AIMovementController(BaseAIEntity entityIn)
	{
		super(entityIn, 106, 6, 10, "AIMovementController");

		this.entityPos = new Vector3d(0, 0, 0);
		this.targetPos = new Vector3d(0, 0, 0);

		this.backPropLog = new BackPropLog(10, 106, 10);

		this.backProp.create("Distance", 10);
	}

	@Override
	protected void aiInitialise()
	{
		this.entityPos = this.entity.getPositionVec();

		this.backProp.tick();
		this.backProp.getKey("Distance").at().setValue(OpenSteveMath.distance(this.entityPos, this.targetPos));
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;
		Block block;
		BlockPos origin = new BlockPos(this.entityPos.x, this.entityPos.y, this.entityPos.z);

		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosX();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosY();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosZ();

		this.deepNNet.vectorIn[iNNet++] = this.entity.getHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getFoodStats().getFoodLevel();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getFoodStats().getSaturationLevel();

		for(int x = -2; x <= 2; x++)
		{
			for(int y = -2; y <= 1; y++)
			{
				for(int z = -2; z <= 2; z++)
				{
					block = this.entity.world.getBlockState(origin.add(x, y, z)).getBlock();
					this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(block.asItem());
				}
			}
		}
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
			case 9: // Stay
				this.targetPos = this.entityPos;
				break;
		}

		this.entity.getMoveHelper().setMoveTo(this.targetPos.x, this.targetPos.y, this.targetPos.z, 0.5D);

		this.backPropLog.tick();
		this.backPropLog.copy(this.deepNNet.vectorIn, this.deepNNet.vectorOut);
	}

	@Override
	protected void fixEntityBehavior()
	{
		this.deepNNet.vectorIn = this.backPropLog.getInputVec();
		this.deepNNet.vectorOut = this.backPropLog.getOutputVec();

		// negative if harmed
		if(this.entity.getBackPropData("Global_Health", 0) < this.entity.getBackPropData("Global_Health", -1))
			this.trainNegative();

		// negative if food level decreased
		if(this.entity.getBackPropData("Global_FoodLevel", 0) < this.entity.getBackPropData("Global_FoodLevel", -1))
			this.trainNegative();

		// negative if the entity got stuck
		if(this.backProp.getKey("Distance").at(0).value() == this.backProp.getKey("Distance").at(-1).value())
			this.trainNegative();

		// positive if the entity is getting closer to the target
		if(this.backProp.getKey("Distance").at(0).value() < this.backProp.getKey("Distance").at(-1).value())
			this.trainPositive();
	}
}
