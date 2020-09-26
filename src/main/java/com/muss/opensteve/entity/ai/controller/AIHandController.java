package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveMath;
import net.minecraft.block.AirBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.extensions.IForgeBlockState;

public class AIHandController extends AIControllerBase
{
	private RayTraceContext rayTraceContext;
	private BlockRayTraceResult rayTraceTarget;
	private BlockPos placeablePos;

	private ItemStack heldItem;
	private int nnetOut;
	private float prevHealth;

	public AIHandController(BaseAIEntity entityIn)
	{
		super(entityIn, 4, 4, 3, "AIHandController");
	}

	@Override
	protected void aiInitialise()
	{
		this.heldItem = this.entity.inventory.getCurrentItem();
		this.prevHealth = this.entity.getHealth();
	}

	@Override
	protected void setNNetInput()
	{

	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0: // right click
				this.onRightClick();
				break;
			case 1: // left click
				this.onLeftClick();
				break;
			case 2: // throw held item
				if(this.heldItem.getCount() > 0)
				{
					this.entity.dropItem(this.heldItem, false, false);
					this.heldItem.shrink(1);
				}

				break;
		}
	}

	@Override
	protected void fixEntityBehavior()
	{
		// train negative cases
		if(this.prevHealth > this.entity.getHealth())
		{
			for(int i=0; i<this.deepNNet.NET_OUT; i++)
				this.deepNNet.vectorDesired[i] = (i == this.nnetOut) ? 0 : 1;

			this.deepNNet.nnRunBackprop();
		}

		if(this.nnetOut == 2 && this.heldItem.getCount() <= 0)
		{
			for(int i=0; i<this.deepNNet.NET_OUT; i++)
				this.deepNNet.vectorDesired[i] = (i == this.nnetOut) ? 0 : 1;

			this.deepNNet.nnRunBackprop();
		}
	}


	private void onRightClick()
	{
		if(this.heldItem.getItem().isFood()) // eat food
		{
			if(this.entity.canEat(this.heldItem.getItem().getFood().canEatWhenFull()))
			{
				this.entity.setActiveHand(Hand.MAIN_HAND);
				this.entity.foodStats.consume(this.heldItem.getItem(), this.heldItem);
				this.heldItem.shrink(1);
			}
		}
		else if(this.heldItem.getItem() instanceof BlockItem) // place block
		{
			this.rayTraceContext = new RayTraceContext(this.entity.eyePos, this.entity.lookPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this.entity);
			this.rayTraceTarget = this.entity.world.rayTraceBlocks(this.rayTraceContext);

			switch(this.rayTraceTarget.getType())
			{
				case BLOCK:
					this.entity.targetBlock = this.rayTraceTarget.getPos();
					this.placeablePos = this.entity.targetBlock.add(this.rayTraceTarget.getFace().getDirectionVec());

					if(this.entity.world.getBlockState(this.placeablePos).isAir())
					{
						if(OpenSteveMath.isInReach(this.entity.eyePos, this.entity.targetBlock, this.entity.maxReachRange))
						{
							this.entity.world.setBlockState(this.placeablePos, ((BlockItem)this.heldItem.getItem()).getBlock().getDefaultState());
							this.heldItem.shrink(1);
						}
					}

					break;

				case ENTITY:
					break;

				case MISS:
					break;
			}
		}
	}

	private void onLeftClick()
	{
		// if weapon

		// if tool
	}
}
