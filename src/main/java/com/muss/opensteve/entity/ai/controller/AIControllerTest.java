package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.NNetBase;
import com.muss.opensteve.entity.ai.brain.NNetIO;
import net.minecraft.entity.monster.MonsterEntity;

public class AIControllerTest extends NNetBase
{
	private MonsterEntity entity;

	public AIControllerTest(MonsterEntity entityIn)
	{
		super(5, 3, 5);
		this.entity = entityIn;
	}


}
