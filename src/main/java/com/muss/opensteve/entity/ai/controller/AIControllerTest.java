package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;

public class AIControllerTest extends AIControllerBase
{
	private MonsterEntity entity;

	public AIControllerTest(MonsterEntity entityIn)
	{
		super(5, 3, 5);
		this.entity = entityIn;
	}
}
