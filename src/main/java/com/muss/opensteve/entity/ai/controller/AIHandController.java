package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;

public class AIHandController extends AIControllerBase
{
	private RayTraceContext rayTraceContext;
	private BlockRayTraceResult targetBlock;
	private BlockPos blockPos;
	private int nnetOut;

	public AIHandController(BaseAIEntity entityIn)
	{
		super(entityIn, 4, 4, 4, "AIHandController");
	}

	@Override
	protected void aiInitialise()
	{
		this.rayTraceContext = new RayTraceContext(this.entity.eyePos, this.entity.lookPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, this.entity);
		this.targetBlock = this.entity.world.rayTraceBlocks(this.rayTraceContext);
		this.blockPos = this.targetBlock.getPos();
	}

	@Override
	protected void setNNetInput()
	{

	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0: // right click
				break;
			case 1: // left click
				break;
			case 2:
				break;
		}
	}

	@Override
	protected void fixEntityBehavior()
	{

	}
}
