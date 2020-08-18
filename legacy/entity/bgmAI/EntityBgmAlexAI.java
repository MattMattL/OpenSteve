package com.muss.opensteve.entity.bgmAI;

import net.minecraft.world.World;

public class EntityBgmAlexAI extends EntityBgmAI
{
	public EntityBgmAlexAI(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.8F);
	}

	@Override
	public boolean isAlex()
	{
		return true;
	}

	@Override
	public boolean isSteve()
	{
		return false;
	}

	@Override
	protected void shuffleAITree(int depth)
	{
	}
	
	

}
