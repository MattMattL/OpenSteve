package com.muss.opensteve.entity.ai.brain;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;

public abstract class AIControllerBase
{
	protected BaseAIEntity entity;
	protected AIControllerBase subController;

	protected int NET_IN;
	protected int NET_DEPTH;
	protected int NET_OUT;
	protected DeepNNetIO deepNNet;
	protected String key;
	protected int nnetOut;

	protected ActionResultType actionResult;
	protected ActionResultType returnResult;
	protected BackPropHelper backPropHelper;
	protected BackPropLog backPropLog;

	public AIControllerBase(BaseAIEntity entityIn, AIControllerBase subNNet, int netIn, int netDepth, int netOut,  String compoundKey)
	{
		this.entity = entityIn;
		this.subController = subNNet;

		this.NET_IN = netIn;
		this.NET_DEPTH = netDepth;
		this.NET_OUT = netOut;
		this.deepNNet = new DeepNNetIO(netIn, netDepth, netOut, compoundKey);

		this.key = compoundKey;
	}

	public ActionResultType runEntityAI()
	{
		System.out.printf("[OpenSteve] %s executed\n", this.key);

		this.aiInitialise();
		this.setNNetInput();
		this.runEntityBehavior();

		return this.fixEntityBehavior();
	}

	/* save environmental factors for back propagations */
	protected abstract void aiInitialise();

	/* put entity's environmental factors as NNet inputs */
	protected abstract void setNNetInput();

	/* execute tasks according to NNet outputs */
	protected abstract void runEntityBehavior();

	/* evaluate the outcome and run back propagations */
	protected abstract ActionResultType fixEntityBehavior();

	protected void trainPositive()
	{
		for(int i=0; i<this.deepNNet.NET_OUT; i++)
			this.deepNNet.vectorDesired[i] = (i == this.nnetOut) ? 1 : 0;

		this.deepNNet.nnRunBackprop();
	}

	protected void trainNegative()
	{
		for(int i=0; i<this.deepNNet.NET_OUT; i++)
			this.deepNNet.vectorDesired[i] = (i == this.nnetOut) ? 0 : 1;

		this.deepNNet.nnRunBackprop();
	}

	public void read(CompoundNBT compound)
	{
		this.deepNNet.read(compound);
	}

	public void write(CompoundNBT compound)
	{
		this.deepNNet.write(compound);
	}
}
