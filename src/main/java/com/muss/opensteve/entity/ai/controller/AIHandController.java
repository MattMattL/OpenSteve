package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.AIControllerHelper;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveMath;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class AIHandController extends AIControllerBase
{
	private Vector3d eyePos;
	private BlockPos targetPos;
	private BlockPos placeablePos;
	private ItemStack heldItem;

	public AIHandController(BaseAIEntity entityIn)
	{
		super(entityIn, 15, 4, 5, "AIHandController");
	}

	@Override
	protected void aiInitialise()
	{
		this.eyePos = AIControllerHelper.getEyePos(this.entity);
		this.heldItem = this.entity.inventory.getCurrentItem();
		this.targetPos = AIControllerHelper.getBlockRayTraceResult(this.entity).getPos();

		this.actionResult = ActionResultType.PASS;
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;

		this.deepNNet.vectorIn[iNNet++] = this.eyePos.getX();
		this.deepNNet.vectorIn[iNNet++] = this.eyePos.getY();
		this.deepNNet.vectorIn[iNNet++] = this.eyePos.getZ();
		this.deepNNet.vectorIn[iNNet++] = this.targetPos.getX();
		this.deepNNet.vectorIn[iNNet++] = this.targetPos.getY();
		this.deepNNet.vectorIn[iNNet++] = this.targetPos.getZ();

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
			case 0: // right click
				this.actionResult = this.onRightClick();
				break;
			case 1: // hold right button
				this.actionResult = this.holdRightClick();
				break;
			case 2: // left click
				this.actionResult = this.onLeftClick();
				break;
			case 3: // hold left button
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
			BlockRayTraceResult rayTrace = AIControllerHelper.getBlockRayTraceResult(this.entity);

			if(rayTrace.getType() == RayTraceResult.Type.BLOCK)
			{
				this.targetPos = rayTrace.getPos();
				this.placeablePos = this.targetPos.add(rayTrace.getFace().getDirectionVec());

				if(this.entity.world.getBlockState(this.placeablePos).isAir())
				{
					if(OpenSteveMath.isInReach(this.eyePos, this.targetPos, this.entity.maxReachRange))
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
