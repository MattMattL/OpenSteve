package com.muss.opensteve.entity.ai.brain;

import net.minecraft.nbt.CompoundNBT;

public class DeepNNetIO extends DeepNNetBase
{
	public DeepNNetIO(int netIn, int netDepth, int netOut)
	{
		super(netIn, netDepth, netOut);
	}

	public void setVectorIn(int index, double value)
	{
		if(index < 0 || index >= this.NET_IN)
		{
			System.out.printf("[OpenSteve] [DeepNNetIO::setVectorIn] Error: index out of boundary\n");
			return;
		}

		this.vectorIn[index] = value;

	}

	public void setVectorDesired(int index, double value)
	{
		if(index < 0 || index >= this.NET_OUT)
		{
			System.out.printf("[OpenSteve] [DeepNNetIO::setVectorDesired] Error: index out of boundary\n");
			return;
		}

		this.vectorDesired[index] = value;

	}

	public int nnGetMaxOutputIndex()
	{
		int iMax = 0;

		for(int i=1; i<NET_OUT; i++)
		{
			if(vectorOut[i] > vectorOut[iMax])
				iMax = i;
		}

		return iMax;
	}

	public void read(CompoundNBT compound, String tag)
	{

	}

	public void write(CompoundNBT compound, String tag)
	{

	}
}
