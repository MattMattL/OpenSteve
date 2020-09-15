package com.muss.opensteve.entity.ai.controller;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.entity.util.math.OpenSteveMath;
import net.minecraft.util.math.vector.Vector3d;

public class AILookController extends AIControllerBase
{
	private Vector3d eyePosition;
	private Vector3d lookPosition;

	private Vector3d lookVector;
	private PolarCoord polarCoord;
	private PolarCoord deltaAngles;

	private int nnetOut;
	private float prevHealth;

	public AILookController(BaseAIEntity entityIn)
	{
		super(entityIn, 17, 4, 4, "AILookController");

		this.lookPosition = new Vector3d(0, 0, 0);
		this.polarCoord = new PolarCoord(1, 0, 0);
	}

	@Override
	protected void aiInitialise()
	{
		// calculate the look vector and normalise
		this.eyePosition = this.entity.getEyePosition(1.0F);
		this.lookVector = this.lookPosition.subtract(this.eyePosition);
		this.lookVector = this.lookVector.normalize();

		this.prevHealth = this.entity.getHealth();
	}

	@Override
	protected void setNNetInput()
	{
		int iNNet = 0;

		this.deepNNet.vectorIn[iNNet++] = this.lookPosition.x;
		this.deepNNet.vectorIn[iNNet++] = this.lookPosition.y;
		this.deepNNet.vectorIn[iNNet++] = this.lookPosition.z;
		this.deepNNet.vectorIn[iNNet++] = this.lookVector.x;
		this.deepNNet.vectorIn[iNNet++] = this.lookVector.y;

		this.deepNNet.vectorIn[iNNet++] = this.lookVector.z;
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
		double unitAngle = 3.14159265 / 12;

		this.deepNNet.nnRunFeedForward();
		this.nnetOut = this.deepNNet.nnGetMaxOutputIndex();

		switch(this.nnetOut)
		{
			case 0: // Up
				this.deltaAngles = new PolarCoord(0, unitAngle, 0);
				break;
			case 1: // Down
				this.deltaAngles = new PolarCoord(0, -unitAngle, 0);
				break;
			case 2: // E-N-W
				this.deltaAngles = new PolarCoord(0, 0, unitAngle);
				break;
			case 3: // E-S-W
				this.deltaAngles = new PolarCoord(0, 0, -unitAngle);
				break;
			default: // Stay
				this.deltaAngles = new PolarCoord(0, 0, 0);
				break;
		}

		// translate to polar basis
		this.polarCoord.setToPolarCoord(this.lookVector);

		// perform transformation
		this.polarCoord = this.polarCoord.add(this.deltaAngles);

		// translate back to the original basis
		this.lookVector = this.polarCoord.getCartesian();

		// calculate absolute positions
		this.lookPosition = this.lookVector.add(this.eyePosition);

		this.entity.getLookController().setLookPosition(this.lookPosition.x, this.lookPosition.y, this.lookPosition.z);
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
	}

	/* Polar coordinate but the axes aligned to the entity's head (XYZ -> ZXY) */
	public class PolarCoord
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































