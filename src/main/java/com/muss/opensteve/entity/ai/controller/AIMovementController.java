package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class AIMovementController extends AIControllerBase
{
	private BlockPos entityBlockPos;
	private BlockPos targetBlockPos;
	private float prevHealth;
	private int nnetOut;

	public AIMovementController(BaseAIEntity entityIn)
	{
		super(entityIn, 27, 5, 4, "AIMovementController");
	}

	@Override
	protected void aiInitialise()
	{
		this.entityBlockPos = this.entity.func_233580_cy_();
		this.prevHealth = this.entity.getHealth();
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;
		Block block;

		for(int x=-1; x<=1; x++)
		{
			for(int y=-1; y<=1; y++)
			{
				for(int z=-1; z<=1; z++)
				{
					block = this.entity.world.getBlockState(entityBlockPos.add(x, y, z)).getBlock();
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
			case 0:
				this.targetBlockPos = this.entityBlockPos.east();
				break;
			case 1:
				this.targetBlockPos = this.entityBlockPos.west();
				break;
			case 2:
				this.targetBlockPos = this.entityBlockPos.south();
				break;
			case 3:
				this.targetBlockPos = this.entityBlockPos.north();
				break;
			default:
				this.targetBlockPos = this.entityBlockPos;
				break;
		}

		this.entity.getNavigator().tryMoveToXYZ((double)((float)this.targetBlockPos.getX()) + 0.5D, (double)(this.targetBlockPos.getY() + 1), (double)((float)this.targetBlockPos.getZ()) + 0.5D, 1.0D);
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
