package com.muss.opensteve.entity.ai;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;
import com.muss.opensteve.util.OSMaths;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BgmAIMotorControl extends BgmAIBase
{
	// switching focus block + subNNet
	private static final int NET_OUT = 6 + 1;
	//
	private static final int NET_MID = 8;
	// RNN + vision vector
	private static final int NET_IN = NET_OUT + 27;

	private final EntityBgmAI entity;
	private int instruction;

	public BgmAIMotorControl(EntityBgmAI entityIn)
	{
		super(NET_IN, NET_MID, NET_OUT, "aiMotorControl");

		this.entity = entityIn;
	}

	private void setInputVector()
	{
		int arr;

		// RNN
		for(arr=0; arr<NET_OUT; arr++)
			this.neuralNet.nnSetInputVec(arr, this.neuralNet.nnGetOutputVec(arr));

		// blocks around the vision vector
		int blockID, vision = 1;
		RayTraceResult rayTrace = OSMaths.getRayTrace(this.entity);
		BlockPos visionVec;

		if(rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
			visionVec = rayTrace.getBlockPos();
		else
			visionVec = new BlockPos(this.entity.getLookVec());

		for(int i=-vision; i<=vision; i++)
		{
			for(int j=-vision; j<=vision; j++)
			{
				for(int k=-vision; k<=vision; k++)
				{
					blockID = Block.getIdFromBlock(this.entity.world.getBlockState(visionVec).getBlock());
					this.neuralNet.nnSetInputVec(arr++, blockID);
				}
			}
		}
	}

	private void executeInstruction()
	{
		this.instruction = this.neuralNet.nnGetMaxOutputNode();

		// set a new vision vector
		Vec3d visionPos = new Vec3d(this.entity.getLookHelper().getLookPosX(), this.entity.getLookHelper().getLookPosY(), this.entity.getLookHelper().getLookPosZ());
		BlockPos visionVec = new BlockPos(visionPos);

		// TEST:
		if(false)
		{
			switch(this.instruction)
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
					this.runSubNNet();
					this.runSubNNetPostInit();
					break;
			}
		}

		switch(this.instruction)
		{
			case 0:
				visionVec = visionVec.up();
				break;
			case 1:
				visionVec = visionVec.down();
				break;
			case 2:
				visionVec = visionVec.east();
				break;
			case 3:
				visionVec = visionVec.west();
				break;
			case 4:
				visionVec = visionVec.south();
				break;
			case 5:
				visionVec = visionVec.north();
				break;
			case 6:
				this.runSubNNet();
				this.runSubNNetPostInit();
				break;
		}

		// ignore the new vector if the distance exceeds the limit
		if(Math.sqrt(this.entity.getDistanceSq(visionVec)) <= 16)
		{
			if(OSMaths.getRayTrace(this.entity) != null)
				visionVec = OSMaths.getRayTrace(this.entity).getBlockPos();

			this.entity.getLookHelper().setLookPosition(visionVec.getX(), visionVec.getY(), visionVec.getZ(), 5, 5);
		}
		else
		{
			this.runBackpropagation(false, true);
		}
	}

	private void runBackpropagation(boolean isPositive, boolean allowMutation)
	{
		for(int i=0; i<NET_OUT; i++)
		{
			int desiredOut = ((isPositive)? 0 : 1) ^ this.instruction;
			this.neuralNet.nnSetDesiredOut(i, desiredOut);
		}

		if(allowMutation)
		{
			int mutant = (int)(Math.random() * (NET_OUT - 1));
			this.neuralNet.nnSetDesiredOut(mutant, (isPositive)? 1 : 0);
		}

		this.neuralNet.nnRunBackprop();
	}


	private float prevHealth;
	private int prevFoodLevel;
	private float prevSaturation;

	@Override
	public void startExecuting()
	{
		this.resetTask();
		this.setInputVector();
		this.neuralNet.nnRunFeedforward();

		this.executeInstruction();

		if(this.prevHealth > this.entity.getHealth())
			this.runBackpropagation(false, true);

		if(this.prevFoodLevel > this.entity.foodStats.getFoodLevel())
			this.runBackpropagation(false, true);

		if(this.prevSaturation > this.entity.foodStats.getSaturationLevel())
			this.runBackpropagation(false, true);
	}

	@Override
	public void resetTask()
	{
		this.prevHealth = this.entity.getHealth();
		this.prevFoodLevel = this.entity.foodStats.getFoodLevel();
		this.prevSaturation = this.entity.foodStats.getSaturationLevel();
	}
}
