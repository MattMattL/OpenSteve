package com.muss.opensteve.entity.monster;

import com.muss.opensteve.entity.ai.brain.NNetIO;
import com.muss.opensteve.entity.ai.controller.MovementController;
import com.sun.xml.internal.bind.v2.TODO;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class BaseAIEntity extends MonsterEntity
{
	private NNetIO globalNNet = new NNetIO(5, 5, 5);
	private MovementController movementController = new MovementController(this);

	protected BaseAIEntity(EntityType<? extends BaseAIEntity> p_i48553_1_, World p_i48553_2_)
	{
		super(p_i48553_1_, p_i48553_2_);
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
		// TODO: add a global neural net

		super.livingTick();
	}
}
