package com.muss.opensteve.entity.ai.brain;

import net.minecraft.nbt.CompoundNBT;

public class DeepNNetIO extends DeepNNetBase
{
	public final String compoundKey;

	public DeepNNetIO(int netIn, int netDepth, int netOut, String keyIn)
	{

		super(netIn, netDepth, netOut);
		this.compoundKey = keyIn;
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

	public void read(CompoundNBT compound)
	{
		if(compound.contains(this.compoundKey))
		{
			CompoundNBT compoundWeights = compound.getCompound(this.compoundKey);
			int index = 0;

			for(int layer=0; layer<this.NET_DEPTH; layer++)
			{
				for(int i=0; i<this.NET_MAX_WIDTH; i++)
				{
					for(int j=0; j<this.NET_MAX_WIDTH; j++)
					{
						this.weights[layer][i][j] = compoundWeights.getDouble(Integer.toString(index++));
					}
				}
			}
		}
	}

	public void write(CompoundNBT compound)
	{
		CompoundNBT compoundWeights = new CompoundNBT();
		int index = 0;

		for(int layer=0; layer<this.NET_DEPTH; layer++)
		{
			for(int i=0; i<this.NET_MAX_WIDTH; i++)
			{
				for(int j=0; j<this.NET_MAX_WIDTH; j++)
				{
					compoundWeights.putDouble(Integer.toString(index++), this.weights[layer][i][j]);
				}
			}
		}

		compound.put(this.compoundKey, compoundWeights);
	}
}

