package com.muss.opensteve.entity.ai;

import com.muss.opensteve.entity.neuralnet.NNetIO;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;

public class BgmAIBase extends EntityAIBase
{
	// Neural net tree
	protected NNetIO neuralNet;
	public BgmAIBase subNNet;
	
	// NBT Compound
	protected final String aiSerialName;
	private final int INT_MODIFIER = 1000000;
	
	public BgmAIBase(int netIn, int netMid, int netOut, String nameIn)
	{
		this.neuralNet = new NNetIO(netIn, netMid, netOut);
		this.aiSerialName = nameIn;
	}
	
	
	// Methods from EntityAIBase that need or need not to be implemented when extended. //
	
	public void startExecuting()
	{
		// 1. Set input vectors.
		// 2. Run feedforward.
		// 3. Call the sub-neural net if triggered.
		//    Execute normal tasks otherwise.
		// 4. Set desired output vectors.
		// 5. Run backpropagation.
	}
	
	public void resetTask()
	{
		// Reset task
	}
	
	@Override
	public boolean shouldExecute()
	{
		// Must not be overridden
		return true;
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		// Must not be overridden
		return false;
	}
	
	
	// AI-tree structure managers //
	
	public void setSubclass(BgmAIBase subNNetIn)
	{
		this.subNNet = subNNetIn;
	}
	
	public void runSubNNet()
	{
		if(this.subNNet != null)
			this.subNNet.startExecuting();
	}
	
	public void runSubNNetPostInit()
	{
		if(this.subNNet != null)
			this.subNNet.runSubNNetPostInit();
	}
	
	
	// Saving weights to custom compound //
	
	public void writeWeightsToNBT(NBTTagCompound opstvCompound)
	{
		NBTTagCompound nnetCompound = new NBTTagCompound();
		
		int i, j, k;
		
		int weightsOnL1[] = new int[neuralNet.NET_IN * neuralNet.NET_MID];
		int weightsOnL2[] = new int[neuralNet.NET_MID * neuralNet.NET_OUT];
		
		for(i=0; i<neuralNet.NET_IN; i++)
			for(j=0; j<neuralNet.NET_MID; j++)
				weightsOnL1[i * neuralNet.NET_MID + j] = (int)(neuralNet.nnGetWeightAt(1, i, j) * INT_MODIFIER);
		
		for(j=0; j<neuralNet.NET_MID; j++)
			for(k=0; k<neuralNet.NET_OUT; k++)
				weightsOnL2[j * neuralNet.NET_OUT + k] = (int)(neuralNet.nnGetWeightAt(2, j, k) * INT_MODIFIER);
		
		nnetCompound.setIntArray("WeightsOnL1", weightsOnL1);
		nnetCompound.setIntArray("WeightsOnL2", weightsOnL2);
		
		opstvCompound.setTag(this.aiSerialName, nnetCompound);
	}

	public void readWeightsFromNBT(NBTTagCompound opstvCompound)
	{	
		if(opstvCompound.hasKey(this.aiSerialName))
		{
			NBTTagCompound nnetCompound = opstvCompound.getCompoundTag(this.aiSerialName);
		
			int i, j, k;
			
			int weightsOnL1[] = nnetCompound.getIntArray("WeightsOnL1");
			int weightsOnL2[] = nnetCompound.getIntArray("WeightsOnL2");
		
			for(i=0; i<neuralNet.NET_IN; i++)
				for(j=0; j<neuralNet.NET_MID; j++)
					neuralNet.nnSetWeightAt(1, i, j, (double)weightsOnL1[i * neuralNet.NET_MID + j] / INT_MODIFIER);
			
			for(j=0; j<neuralNet.NET_MID; j++)
				for(k=0; k<neuralNet.NET_OUT; k++)
					neuralNet.nnSetWeightAt(2, j, k, (double)weightsOnL2[j * neuralNet.NET_OUT + k]  / INT_MODIFIER);
		}
	}
	
	
	
}
