package com.muss.opensteve.entity.bgmAI;

import com.muss.opensteve.entity.ai.BgmAIBase;

import net.minecraft.world.World;

public class EntityBgmSteveAI extends EntityBgmAI
{	
	public EntityBgmSteveAI(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.8F);
	}

	@Override
	public boolean isAlex()
	{
		return false;
	}

	@Override
	public boolean isSteve()
	{
		return true;
	}

	/* Shuffles the AI tree excuding the peripheral networks */
	@Override
	protected void shuffleAITree(int depth)
	{
		//for(int i=1; i<depth-1; i++)
		for(int i=0; i<depth; i++)
		{
			BgmAIBase temp;
			int rand = (int)(Math.random() * (depth - 3)) + 1;
			
			temp = this.arrEntityAI[i];
			this.arrEntityAI[i] = this.arrEntityAI[rand];
			this.arrEntityAI[rand] = temp;
		}
		
	}
}




