package com.muss.opensteve.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class AlexAIEntity extends BaseAIEntity implements IBaseAI
{
	public AlexAIEntity(EntityType<? extends BaseAIEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 5;
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
}
