package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.AIControllerHelper;
import com.muss.opensteve.entity.ai.brain.BackPropHelper;
import com.muss.opensteve.entity.ai.brain.BackPropLog;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector3d;

public class AILookController extends AIControllerBase
{
	private Vector3d eyePos;
	private Vector3d lookPos;
	private Vector3d lookVec;
	private PolarCoord polarCoord;
	private PolarCoord deltaAngle;

	private float prevHealth;

	public AILookController(BaseAIEntity entityIn, AIControllerBase subNNet)
	{
		super(entityIn, subNNet, 17, 4, 5, "AILookController");

		this.backPropHelper = new BackPropHelper();
		this.backPropLog = new BackPropLog(10, this.NET_IN, this.NET_OUT);

		this.backPropHelper.create("Health", 10);
		this.backPropHelper.create("FoodLevel", 10);

		this.polarCoord = new PolarCoord(1, 0, 0);
	}

	@Override
	protected void aiInitialise()
	{
		this.eyePos = AIControllerHelper.getEyePos(this.entity);
		this.lookPos = AIControllerHelper.getLookPos(this.entity);

		this.lookVec = this.lookPos.subtract(this.eyePos);
		this.lookVec = this.lookVec.normalize();

		this.backPropHelper.tick();
		this.backPropHelper.getKey("Health").at().setValue(this.entity.getHealth());
		this.backPropHelper.getKey("FoodLevel").at().setValue(this.entity.getFoodStats().getFoodLevel() + this.entity.getFoodStats().getSaturationLevel());

		this.actionResult = ActionResultType.PASS;
		this.returnResult = ActionResultType.PASS;
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;

		this.deepNNet.vectorIn[iNNet++] = this.lookPos.x;
		this.deepNNet.vectorIn[iNNet++] = this.lookPos.y;
		this.deepNNet.vectorIn[iNNet++] = this.lookPos.z;
		this.deepNNet.vectorIn[iNNet++] = this.lookVec.x;
		this.deepNNet.vectorIn[iNNet++] = this.lookVec.y;
		this.deepNNet.vectorIn[iNNet++] = this.lookVec.z;
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
		final double unitAngle = 3.14159265 / 12;

		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0: // Up
				this.deltaAngle = new PolarCoord(0, unitAngle, 0);
				break;
			case 1: // Down
				this.deltaAngle = new PolarCoord(0, -unitAngle, 0);
				break;
			case 2: // E-N-W
				this.deltaAngle = new PolarCoord(0, 0, unitAngle);
				break;
			case 3: // E-S-W
				this.deltaAngle = new PolarCoord(0, 0, -unitAngle);
				break;
			case 4: // Fire Sub NNets
				this.actionResult = this.subController.runEntityAI();
			default: // Stay
				this.deltaAngle = new PolarCoord(0, 0, 0);
				break;
		}

		this.polarCoord.setToPolarCoord(this.lookVec); // translate to polar basis
		this.polarCoord = this.polarCoord.add(this.deltaAngle); // perform transformation
		this.lookVec = this.polarCoord.getCartesian(); // translate back to the original basis
		this.lookPos = this.lookVec.add(this.eyePos); // calculate absolute positions

		this.entity.getLookController().setLookPosition(this.lookPos.x, this.lookPos.y, this.lookPos.z);

		this.backPropLog.tick();
		this.backPropLog.copy(this.deepNNet.vectorIn, this.deepNNet.vectorOut);
	}

	@Override
	protected ActionResultType fixEntityBehavior()
	{
		// negative if harmed
		if(this.backPropHelper.getKey("Health").valueAt() < this.backPropHelper.getKey("Health").valueAt(-1))
			this.trainNegative();

		// negative if food level decreased
		if(this.backPropHelper.getKey("FoodLevel").valueAt() < this.backPropHelper.getKey("FoodLevel").valueAt(-1))
			this.trainNegative();

		// train with passed result
		if(this.actionResult.isSuccessOrConsume())
			this.trainPositive();
		else
			this.trainNegative();

		return this.returnResult;
	}

	/*
	 * Polar coordinate centred around the entity's head movement.
	 * Arranged to ZX-Y instead of XY-Z to match the game's coordinate system
	 */
	private class PolarCoord
	{
		public double r;
		public double theta;
		public double phi;

		public PolarCoord()
		{
			this.r = 1;
			this.theta = 0;
			this.phi = 0;
		}

		public PolarCoord(double radiusIn, double thetaIn, double phiIn)
		{
			this.r = radiusIn;
			this.theta = thetaIn;
			this.phi = phiIn;
		}

		public PolarCoord add(PolarCoord polar)
		{
			return new PolarCoord(this.r + polar.r, this.theta + polar.theta, this.phi + polar.phi);
		}

		public void setToPolarCoord(Vector3d vector)
		{
			this.r = Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
			this.theta = Math.atan(Math.sqrt(vector.x * vector.x + vector.z * vector.z) / vector.y);
			this.phi = Math.atan(vector.z / vector.x);
		}

		public Vector3d getCartesian()
		{
			double x = this.r * Math.sin(this.theta) * Math.cos(this.phi);
			double y = this.r * Math.cos(this.theta);
			double z = this.r * Math.sin(this.theta) * Math.sin(this.phi);

			return new Vector3d(x, y, z);
		}
	}

}
