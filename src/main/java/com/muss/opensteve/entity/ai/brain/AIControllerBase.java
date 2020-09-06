package com.muss.opensteve.entity.ai.brain;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.nbt.CompoundNBT;

public abstract class AIControllerBase
{
	protected DeepNNetBase deepNNet;
	protected BaseAIEntity entity;

	public AIControllerBase(BaseAIEntity entityIn, int netIn, int netOut, int netDepth)
	{
		this.entity = entityIn;
		this.deepNNet = new DeepNNetBase(netIn, netOut, netDepth);
	}

	public int getMaxNNetOutput()
	{
		int iMax = 0;

		for(int i=1; i<this.deepNNet.NET_OUT; i++)
		{
			if(this.deepNNet.vectorOut[i] > this.deepNNet.vectorOut[iMax])
				iMax = i;
		}

		return iMax;
	}

	/* put entity's environmental factors as NNet inputs */
	public abstract void setNNetInput();

	/* execute tasks according to NNet outputs */
	public abstract void runEntityBehavior();

	/* evaluate the outcome and run back propagations */
	public abstract void fixEntityBehavior();


	public void writeAdditional(CompoundNBT compound)
	{

	}

	public void readAdditional(CompoundNBT compound)
	{

	}
}
