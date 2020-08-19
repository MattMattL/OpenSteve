package com.muss.opensteve.entity.monster;

import com.muss.opensteve.entity.ai.brain.NNetIO;
import com.muss.opensteve.entity.ai.controller.MovementController;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class BaseAIEntity extends MonsterEntity
{
//	private NNetIO globalNNet = new NNetIO(5, 5, 5);
//	private MovementController movementController = new MovementController(this);

	public BaseAIEntity(EntityType<? extends BaseAIEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 5;
	}

	/* Called to update the entity's position/logic. */
	public void tick()
	{
		super.tick();
	}

	/* Called to update the entity's behaviour */
	public void livingTick()
	{
		super.livingTick();
	}
}
