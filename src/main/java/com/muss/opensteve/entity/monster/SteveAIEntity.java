package com.muss.opensteve.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class SteveAIEntity extends BaseAI
{
	protected SteveAIEntity(EntityType<? extends SteveAIEntity> p_i48553_1_, World p_i48553_2_)
	{
		super(p_i48553_1_, p_i48553_2_);
		this.experienceValue = 5;
	}
}
