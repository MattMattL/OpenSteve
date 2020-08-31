package com.muss.opensteve.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class SteveAIEntity extends BaseAIEntity implements IBaseAI
{
	public SteveAIEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 5;
	}

	public boolean isSteve()
	{
		return true;
	}

	public boolean isAlex()
	{
		return false;
	}
}
