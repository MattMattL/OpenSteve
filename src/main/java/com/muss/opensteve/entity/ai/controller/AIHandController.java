package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.AIControllerHelper;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveMath;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;

public class AIHandController extends AIControllerBase
{
	private BlockPos targetBlock;
	private BlockPos placeablePos;
	private ItemStack heldItem;

	public AIHandController(BaseAIEntity entityIn)
	{
		super(entityIn, 12, 4, 5, "AIHandController");
	}

	@Override
	protected void aiInitialise()
	{
		this.heldItem = this.entity.inventory.getCurrentItem();
		this.targetBlock = AIControllerHelper.getBlockRayTraceResult(this.entity).getPos();

		this.actionResult = ActionResultType.PASS;
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;

		this.deepNNet.vectorIn[iNNet++] = this.targetBlock.getX();
		this.deepNNet.vectorIn[iNNet++] = this.targetBlock.getY();
		this.deepNNet.vectorIn[iNNet++] = this.targetBlock.getZ();

		this.deepNNet.vectorIn[iNNet++] = this.entity.getMaxHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getFoodLevel();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getSaturationLevel();

		this.deepNNet.vectorIn[iNNet++] = this.entity.isAlex()? 1 : -1;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isBurning()? 1 : -1;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isInWater()? 1 : -1;

		this.deepNNet.vectorIn[iNNet++] = Item.getIdFromItem(this.heldItem.getItem());
		this.deepNNet.vectorIn[iNNet++] = this.heldItem.getCount();
	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0:
				this.actionResult = this.onRightClick();
				break;
			case 1:
				this.actionResult = this.holdRightClick();
				break;
			case 2:
				this.actionResult = this.onLeftClick();
				break;
			case 3:
				this.actionResult = this.holdLeftClick();
				break;
			case 4: // throw held item
				if(this.heldItem.getCount() > 0)
				{
					this.entity.dropItem(new ItemStack(this.heldItem.getItem(), 1), false, false);
					this.heldItem.shrink(1);

					this.actionResult = ActionResultType.SUCCESS;
				}
				else
				{
					this.actionResult = ActionResultType.FAIL;
				}

				break;
		}
	}

	@Override
	protected void fixEntityBehavior()
	{
		if(this.nnetOut == 4 && this.heldItem.getCount() <= 0)
			this.trainNegative();

		if(!this.actionResult.isSuccessOrConsume())
			this.trainNegative();
	}

	private ActionResultType onRightClick()
	{
		if(this.heldItem.getItem() instanceof BlockItem) // place block
		{
			BlockRayTraceResult target = AIControllerHelper.getBlockRayTraceResult(this.entity);

			if(target.getType() == RayTraceResult.Type.BLOCK)
			{
				BlockPos placeablePos = this.entity.targetBlock.add(target.getFace().getDirectionVec());

				if(this.entity.world.getBlockState(this.placeablePos).isAir())
				{
					if(OpenSteveMath.isInReach(this.entity.eyePos, this.entity.targetBlock, this.entity.maxReachRange))
					{
						this.entity.world.setBlockState(placeablePos, ((BlockItem) this.heldItem.getItem()).getBlock().getDefaultState());
						this.heldItem.shrink(1);

						return ActionResultType.SUCCESS;
					}
				}
			}

			return ActionResultType.FAIL;
		}

		return ActionResultType.PASS;
	}

	private ActionResultType holdRightClick()
	{
		if(this.heldItem.getItem().isFood()) // consume food
		{
			if(this.entity.canEat(this.heldItem.getItem().getFood().canEatWhenFull()))
			{
				this.entity.setActiveHand(Hand.MAIN_HAND);
				this.entity.foodStats.consume(this.heldItem.getItem(), this.heldItem);
				this.heldItem.shrink(1);
				this.entity.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.5F, this.entity.world.rand.nextFloat() * 0.1F + 0.9F);

				return ActionResultType.SUCCESS;
			}
		}
		// if holding bow
		// if holding shield

		return ActionResultType.PASS;
	}

	private ActionResultType onLeftClick()
	{

		return ActionResultType.PASS;
	}

	private ActionResultType holdLeftClick()
	{


		return ActionResultType.PASS;
	}
}
