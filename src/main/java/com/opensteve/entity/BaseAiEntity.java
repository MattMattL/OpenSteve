package com.opensteve.entity;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;

public class BaseAiEntity extends Pig
{
	public BaseAiEntity(EntityType<? extends Pig> type, Level level)
	{
		super(type, level);
	}

	public static AttributeSupplier.Builder getBaseAiAttributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.ARMOR, 0.0);
	}

	@Override
	public void tick()
	{
		super.tick();
	}
}
