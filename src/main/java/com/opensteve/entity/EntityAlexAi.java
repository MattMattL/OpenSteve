package com.opensteve.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityAlexAi extends BaseAiEntity implements IOpenSteveAiEntity
{
	public EntityAlexAi(EntityType<? extends BaseAiEntity> type, Level level)
	{
		super(type, level);
	}

	@Override
	public boolean isSteve()
	{
		return false;
	}

	@Override
	public boolean isAlex()
	{
		return true;
	}
}
