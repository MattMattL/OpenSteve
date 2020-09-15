package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.entity.util.OpenSteveMath;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class AILookController extends AIControllerBase
{
	private Vector3d prevLookPos;
	private Vector3d lookPos;
	private Vector3d deltaLookPos;

	private int nnetOut;
	private float prevHealth;

	public AILookController(BaseAIEntity entityIn)
	{
		super(entityIn, 14, 4, 6, "AILookController");

		this.lookPos = new Vector3d((double)this.entity.func_233580_cy_().getX(), (double)this.entity.func_233580_cy_().getY() + 2, (double)this.entity.func_233580_cy_().getZ());
		this.prevLookPos = this.lookPos;
	}

	@Override
	protected void aiInitialise()
	{
		this.prevHealth = this.entity.getHealth();
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;

		this.deepNNet.vectorIn[iNNet++] = this.lookPos.x;
		this.deepNNet.vectorIn[iNNet++] = this.lookPos.y;
		this.deepNNet.vectorIn[iNNet++] = this.lookPos.z;
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosX();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosY();

		this.deepNNet.vectorIn[iNNet++] = this.entity.getPosZ();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getMaxHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.getHealth();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getFoodLevel();
		this.deepNNet.vectorIn[iNNet++] = this.entity.foodStats.getSaturationLevel();


		this.deepNNet.vectorIn[iNNet++] = this.entity.isAlex()? 1 : -1;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isBurning()? 1 : -1;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isInWater()? 1 : -1;
		this.deepNNet.vectorIn[iNNet++] = this.entity.isChild()? 1 : -1;
	}

	@Override
	protected void runEntityBehavior()
	{
		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0: // East
				this.deltaLookPos = new Vector3d(1, 0, 0);
				break;
			case 1: // West
				this.deltaLookPos = new Vector3d(-1, 0, 0);
				break;
			case 2: // Up
				this.deltaLookPos = new Vector3d(0, 1, 0);
				break;
			case 3: // Down
				this.deltaLookPos = new Vector3d(0, -1, 0);
				break;
			case 4: // South
				this.deltaLookPos = new Vector3d(0, 0, 1);
				break;
			case 5: // North
				this.deltaLookPos = new Vector3d(0, 0, -1);
				break;
			default: // Stay
				this.deltaLookPos = new Vector3d(0, 0, 0);
				break;
		}

		this.prevLookPos = this.lookPos;
		this.lookPos = this.prevLookPos.add(this.deltaLookPos);
		this.entity.getLookController().setLookPosition(this.lookPos);
	}

	@Override
	protected void fixEntityBehavior()
	{
		// negative if health drops
		if(this.entity.getHealth() < this.prevHealth)
		{
			for(int i=0; i<this.deepNNet.NET_OUT; i++)
				this.deepNNet.vectorDesired[i] = (i == this.nnetOut)? 0 : 1;

			this.deepNNet.nnRunBackprop();
		}

		// negative if the look direction is the same
		Vector3d eyePos = new Vector3d(this.entity.getPosX(), this.entity.getPosYEye(), this.entity.getPosZ());

		if(OpenSteveMath.getDistance(eyePos, prevLookPos) >= OpenSteveMath.getDistance(eyePos, lookPos) + 1)
		{
			for(int i=0; i<this.deepNNet.NET_OUT; i++)
				this.deepNNet.vectorDesired[i] = (i == this.nnetOut)? 0 : 1;

			this.deepNNet.nnRunBackprop();
		}
	}
}































