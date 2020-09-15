package com.muss.opensteve.entity.util;

import net.minecraft.util.math.vector.Vector3d;

public class OpenSteveMath
{
	public static final double E = 2.7182818284590452354;
    public static final double PI = 3.14159265358979323846;

	public static double getDistance(Vector3d vector1, Vector3d vector2)
	{
		double distance = (vector2.x - vector1.x) * (vector2.x - vector1.x);
		distance += (vector2.y - vector1.y) * (vector2.y - vector1.y);
		distance += (vector2.z - vector1.z) * (vector2.z - vector1.z);

		return Math.sqrt(distance);
	}

	public class PolarCoord
	{
		public double r;
		public double theta;
		public double phi;
		public boolean isRadian;

		public PolarCoord()
		{
			this.r = 0;
			this.theta = 0;
			this.phi = 0;
		}

		public PolarCoord(double radiusIn, double thetaIn, double phiIn)
		{
			this.r = radiusIn;
			this.theta = thetaIn;
			this.phi = phiIn;
		}
	}
}
