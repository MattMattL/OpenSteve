package com.muss.opensteve.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class AlexAIEntity extends BaseAIEntity implements IBaseAI
{
	public AlexAIEntity(EntityType<? extends AlexAIEntity> p_i48553_1_, World p_i48553_2_)
	{
		super(p_i48553_1_, p_i48553_2_);
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
