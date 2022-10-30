package com.opensteve.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntitySteveAi extends BaseAiEntity implements IOpenSteveAiEntity
{
	public EntitySteveAi(EntityType<? extends BaseAiEntity> type, Level level)
	{
		super(type, level);
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
