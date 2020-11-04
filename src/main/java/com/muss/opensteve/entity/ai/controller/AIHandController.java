package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.AIControllerHelper;
import com.muss.opensteve.entity.ai.brain.BackPropHelper;
import com.muss.opensteve.entity.ai.brain.BackPropLog;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.util.OpenSteveMath;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
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

	private BlockPos breakingPos;
	private int breakingTime;

	public AIHandController(BaseAIEntity entityIn, AIControllerBase subNNet)
	{
		super(entityIn, subNNet, 15, 4, 5, "AIHandController");

		this.backPropHelper = new BackPropHelper();
		this.backPropLog = new BackPropLog(10, this.NET_IN, this.NET_OUT);

		this.backPropHelper.create("Health", 10);
		this.backPropHelper.create("FoodLevel", 10);

		this.breakingTime = 0;
	}

	@Override
	protected void aiInitialise()
	{
		this.eyePos = AIControllerHelper.getEyePos(this.entity);
		this.heldItem = this.entity.inventory.getCurrentItem();
		this.targetPos = AIControllerHelper.getBlockRayTraceResult(this.entity).getPos();

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

		this.deepNNet.vectorIn[iNNet++] = this.eyePos.getX();
		this.deepNNet.vectorIn[iNNet++] = this.eyePos.getY();
		this.deepNNet.vectorIn[iNNet++] = this.eyePos.getZ();
		this.deepNNet.vectorIn[iNNet++] = this.targetPos.getX();
		this.deepNNet.vectorIn[iNNet++] = this.targetPos.getY();
		this.deepNNet.vectorIn[iNNet++] = this.targetPos.getZ();

		this.deepNNet.vectorIn[iNNet++] = this.entity.getHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getFoodLevel();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getSaturationLevel();

		this.deepNNet.vectorIn[iNNet++] = this.entity.isBurning()? 1 : 0;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isInWater()? 1 : 0;

		this.deepNNet.vectorIn[iNNet++] = Block.getStateId(this.entity.world.getBlockState(this.targetPos));
		this.deepNNet.vectorIn[iNNet++] = OpenSteveMath.distance(this.eyePos, this.targetPos);
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
			//case 5: // Fire Sub NNets
			//	this.subNNetResult = this.subController.runEntityAI();
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

				return ActionResultType.CONSUME;
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
		/*BlockPos blockPos = AIControllerHelper.getBlockRayTraceResult(this.entity).getPos();
		BlockState blockState = this.entity.world.getBlockState(this.targetPos);

		if(blockState.getBlock() == Blocks.AIR)
		{
			return ActionResultType.FAIL;
		}

		if(this.targetPos != this.breakingPos) // restart if the target block has been changed
		{
			this.breakingPos = this.targetPos;
			this.breakingTime = 0;
		}

		float digSpeed = this.entity.getDigSpeed(blockState, this.targetPos);
		float hardness = blockState.getBlockHardness(this.entity.world, this.breakingPos);
		this.breakingTime += digSpeed * 10 / hardness;

		if(!this.entity.isSwingInProgress)
		{
			this.entity.swingArm(this.entity.getActiveHand());
		}

		this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.targetPos, this.breakingTime);

		if(this.breakingTime > 60)
		{
			SoundEvent sound = blockState.getBlock().getSoundType(blockState, this.entity.world, blockPos, null).getBreakSound();

			this.entity.world.removeBlock(this.breakingPos, false);
			this.entity.world.playSound(this.breakingPos.getX(), this.breakingPos.getX(), this.breakingPos.getX(), sound, SoundCategory.BLOCKS, 4.0F, 1F, false);

			this.breakingPos = null;
			this.breakingTime = 0;
		}


		String debug = new OpenSteveStatics.MultiLines("[OpenSteve] AIHandController::holdLeftClick")
				.newline("  block = %s", blockState.getBlock().asItem().getName().getString())
				.newline("  digSpeed = %6.2f", digSpeed)
				.newline("  hardness = %6.2f", hardness)
				.newline("  time = %d\n", this.breakingTime)
				.getString();

		System.out.printf("%s\n", debug);*/

		return ActionResultType.PASS;
	}
}
