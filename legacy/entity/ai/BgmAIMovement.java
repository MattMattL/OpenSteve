package com.muss.opensteve.entity.ai;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class BgmAIMovement extends BgmAIBase
{	
	// w, a, s, d, jump + subNNet
	public final static int NET_OUT = 5 + 1;
	// hidden layer
	public final static int NET_MID = 16;
	// RNN + coordinate info + CNN
	public final static int NET_IN = NET_OUT + 9 + 27;
	
	private final EntityBgmAI entity;
	private int instruction;

	// Initialise the entity, priority index, and the size of the net //
	
	public BgmAIMovement(EntityBgmAI entityIn)
	{
		super(NET_IN, NET_MID, NET_OUT, "aiMovement");
		
		this.entity = entityIn;
	}
	
	
	// Set input, run feedforward, execute the output result, and run backpropagation //
	
	private void setInputVector()
	{
		int arr;
		double posX = this.entity.posX;
		double posY = this.entity.posY;
		double posZ = this.entity.posZ;
		
		// RNNs
		for(arr=0; arr<this.NET_OUT; arr++)
			this.neuralNet.nnSetInputVec(arr, this.neuralNet.nnGetOutputVec(arr));
		
		// positions relative to homePos
		this.neuralNet.nnSetInputVec(arr++, Math.sqrt(Math.abs(posX - this.entity.homePos.getX())));
		this.neuralNet.nnSetInputVec(arr++, Math.sqrt(Math.abs(posY - this.entity.homePos.getY())));
		this.neuralNet.nnSetInputVec(arr++, Math.sqrt(Math.abs(posZ - this.entity.homePos.getZ())));

		// chunk coordinates
		this.neuralNet.nnSetInputVec(arr++, (int)posX >> 4);
		this.neuralNet.nnSetInputVec(arr++, (int)posY >> 4);
		this.neuralNet.nnSetInputVec(arr++, (int)posZ >> 4);

		// chunk-relative coordinates
		this.neuralNet.nnSetInputVec(arr++, (int)posX & 15);
		this.neuralNet.nnSetInputVec(arr++, (int)posY & 15);
		this.neuralNet.nnSetInputVec(arr++, (int)posZ & 15);
		
		// block IDs within 3x3x3 space
		int blockID = 1, vision = 1;
		
		for(int i=-vision; i<=vision; i++)
		{
			for(int j=-vision; j<=vision; j++)
			{
				for(int k=-vision; k<=vision; k++)
				{
					BlockPos blockPos = new BlockPos(posX, posY, posZ).add(j, i, k);
					blockID = Block.getIdFromBlock(this.entity.world.getBlockState(blockPos).getBlock());
							
					this.neuralNet.nnSetInputVec(arr++, blockID);
				}
			}
		}
	}
	
	private void executeInstructions()
	{
		double posX = this.entity.posX;
		double posY = this.entity.posY;
		double posZ = this.entity.posZ;
		
		this.instruction = this.neuralNet.nnGetMaxOutputNode();
		
		double stepSize = 0.25;
		
		float yaw = 0, pitch = 0;
		
		switch(this.instruction)
		{
			case 0: // move towards East (-x)
				yaw = 270;
				pitch = -30;
				this.entity.setPositionAndRotation(posX+stepSize, posY, posZ, yaw, pitch);
				break;
			
			case 1: // South (z)
				yaw = 0;
				pitch = 0;
				this.entity.setPositionAndRotation(posX, posY, posZ+stepSize, yaw, pitch);
				break;
				
			case 2: // North (-z)
				yaw = 180;
				pitch = 30;
				this.entity.setPositionAndRotation(posX, posY, posZ-stepSize, yaw, pitch);
				break;
				
			case 3: // West (x)
				yaw = 90;
				pitch = 60;
				this.entity.setPositionAndRotation(posX-stepSize, posY, posZ, yaw, pitch);
				break;

			case 4: // jump
				this.entity.getJumpHelper().setJumping();
				break;
				
			case 5:
				this.runSubNNet();
				this.runSubNNetPostInit();
				break;
		}

		if(this.instruction == 4)
			this.entity.addExhaustion(5);
		else if(this.instruction != 5)
			this.entity.addExhaustion(1);
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

	public void startExecuting()
	{
		// run neural network
		this.resetTask();
		this.setInputVector();
		this.neuralNet.nnRunFeedforward();
		
		this.executeInstructions();

		// run backprop
		if(this.prevHealth > this.entity.getHealth())
			this.runBackpropagation(false, true);

		if(this.prevFoodLevel > this.entity.foodStats.getFoodLevel())
			this.runBackpropagation(false, true);

		if(this.prevSaturation > this.entity.foodStats.getSaturationLevel())
			this.runBackpropagation(false, true);

		if(this.entity.isNotColliding() == false)
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




