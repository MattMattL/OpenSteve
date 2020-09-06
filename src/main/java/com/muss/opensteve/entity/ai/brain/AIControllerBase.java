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

	public void runEntityAI()
	{
		this.setNNetInput();
		this.runEntityBehavior();
		this.fixEntityBehavior();
	}

	/* put entity's environmental factors as NNet inputs */
	protected abstract void setNNetInput();

	/* execute tasks according to NNet outputs */
	protected abstract void runEntityBehavior();

	/* evaluate the outcome and run back propagations */
	protected abstract void fixEntityBehavior();


	public void writeAdditional(CompoundNBT compound)
	{

	}

	public void readAdditional(CompoundNBT compound)
	{

	}
}
