package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class AIMovementController extends AIControllerBase
{
	private BlockPos entityPos;
	private BlockPos targetPos;
	private float prevHealth;
	private int nnetOut;

	public AIMovementController(BaseAIEntity entityIn)
	{
		super(entityIn, 100, 5, 5, "AIMovementController");
	}

	@Override
	protected void aiInitialise()
	{
		this.entityPos = this.entity.func_233580_cy_();
		this.prevHealth = this.entity.getHealth();
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;
		Block block;

		for(int x = -2; x <= 2; x++)
		{
			for(int y = -2; y <= 1; y++)
			{
				for(int z = -2; z <= 2; z++)
				{
					block = this.entity.world.getBlockState(entityPos.add(x, y, z)).getBlock();
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
				this.targetPos = this.entityPos.east();
				break;
			case 1: // West
				this.targetPos = this.entityPos.west();
				break;
			case 2: // South
				this.targetPos = this.entityPos.south();
				break;
			case 3: // North
				this.targetPos = this.entityPos.north();
				break;
			case 4: // Stay
				this.targetPos = this.entityPos;
				break;
		}

		this.entity.getMoveHelper().setMoveTo((double)this.targetPos.getX(), (double)this.targetPos.getY(), (double)this.targetPos.getZ(), (double)0.5);
	}

	@Override
	protected void fixEntityBehavior()
	{
		// train negative outcomes
		if(this.prevHealth > this.entity.getHealth())
		{
			for(int i=0; i<this.deepNNet.NET_OUT; i++)
				this.deepNNet.vectorDesired[i] = (i == this.nnetOut) ? 0  : 1;

			this.deepNNet.nnRunBackprop();
		}

		// train positive outcomes

	}
}
