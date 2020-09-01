package com.muss.opensteve.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class SteveAIEntity extends BaseAIEntity // implements IBaseAI
{
	public SteveAIEntity(EntityType<? extends BaseAIEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 5;
	}

	@Override
	public boolean isSteve()
	{
		return true;
	}

	@Override
	public boolean isAlex()
	{
		return false;
	}
}
