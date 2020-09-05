package com.muss.opensteve.entity.ai.brain;

import net.minecraft.nbt.CompoundNBT;

public class AIControllerBase extends DeepNNetBase
{
	public AIControllerBase(int netIn, int netOut, int netDepth)
	{
		super(netIn, netOut, netDepth);
	}

	public void writeAdditional(CompoundNBT compound)
	{

	}

	public void readAdditional(CompoundNBT compound)
	{

	}
}
