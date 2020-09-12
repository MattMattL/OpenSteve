package com.muss.opensteve.entity.ai.brain;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.nbt.CompoundNBT;

public abstract class AIControllerBase
{
	protected DeepNNetIO deepNNet;
	protected BaseAIEntity entity;

	public AIControllerBase(BaseAIEntity entityIn, int netIn, int netDepth, int netOut, String compoundKey)
	{
		this.entity = entityIn;
		this.deepNNet = new DeepNNetIO(netIn, netDepth, netOut, compoundKey);
	}

	public void runEntityAI()
	{
		this.aiInitialise();
		this.setNNetInput();
		this.runEntityBehavior();
		this.fixEntityBehavior();
	}

	/* save environmental factors for back propagations */
	protected abstract void aiInitialise();

	/* put entity's environmental factors as NNet inputs */
	protected abstract void setNNetInput();

	/* execute tasks according to NNet outputs */
	protected abstract void runEntityBehavior();

	/* evaluate the outcome and run back propagations */
	protected abstract void fixEntityBehavior();

	public void read(CompoundNBT compound)
	{
		this.deepNNet.read(compound);
	}

	public void write(CompoundNBT compound)
	{
		this.deepNNet.write(compound);
	}
}
