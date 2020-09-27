package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
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
	private int nnetOut;

	private float prevHealth;
	private float prevPrevHealth;
	private double prevDist;
	private double prevPrevDist;

	public AIMovementController(BaseAIEntity entityIn)
	{
		super(entityIn, 100, 5, 5, "AIMovementController");

		this.prevHealth = this.entity.getHealth();
		this.prevPrevHealth = this.entity.getHealth();
		this.prevDist = 0;
		this.prevPrevDist = 0;
	}

	@Override
	protected void aiInitialise()
	{
		this.entityPos = this.entity.getPositionVec();

		this.prevPrevDist = this.prevDist;
		this.prevPrevHealth = this.prevHealth;
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;
		Block block;
		BlockPos origin = new BlockPos(this.entityPos.x, this.entityPos.y, this.entityPos.z);

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
			case 4: // Stay
				this.targetPos = this.entityPos;
				break;
		}

		this.entity.getMoveHelper().setMoveTo(this.targetPos.x, this.targetPos.y, this.targetPos.z, 0.5D);
	}

	@Override
	protected void fixEntityBehavior()
	{
		this.prevHealth = this.entity.getHealth();

		// train negative outcomes
		if(this.prevPrevHealth > this.entity.getHealth())
		{
			for(int i=0; i<this.deepNNet.NET_OUT; i++)
				this.deepNNet.vectorDesired[i] = (i == this.nnetOut) ? 0  : 1;

			this.deepNNet.nnRunBackprop();
		}

		this.prevDist = OpenSteveMath.distance(this.entityPos, this.targetPos);
		System.out.printf("%16.8f\n", this.prevPrevDist - OpenSteveMath.distance(this.entityPos, this.targetPos));

		if(OpenSteveMath.distance(this.entityPos, this.targetPos) >= this.prevPrevDist)
		{
			for(int i=0; i<this.deepNNet.NET_OUT; i++)
				this.deepNNet.vectorDesired[i] = (i == this.nnetOut) ? 0  : 1;

			this.deepNNet.nnRunBackprop();
		}
	}
}
