package com.muss.opensteve.entity.ai.brain;

import net.minecraft.nbt.CompoundNBT;

public class AIControllerBase extends NNetBase
{
	public AIControllerBase(int netIn, int netMid, int netOut)
	{
		super(netIn, netMid, netOut);
	}

	// TODO Save and load neural weights from compounds
	public void writeAdditional(CompoundNBT compound)
	{

	}

	public void readAdditional(CompoundNBT compound)
	{

	}
}
