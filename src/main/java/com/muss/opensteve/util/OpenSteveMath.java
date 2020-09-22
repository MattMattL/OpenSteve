package com.muss.opensteve.util;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

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

	public static boolean isInReach(Vector3d entityPos, Vector3i blockCoord, double reach)
	{
		Vector3d blockPos = new Vector3d((double)blockCoord.getX(), (double)blockCoord.getY(), (double)blockCoord.getZ());

		return (OpenSteveMath.getDistance(entityPos, blockPos) <= reach) ? true : false;
	}

	public static boolean isInReach(Vector3d entityPos, Vector3d blockPos, double reach)
	{
		return (OpenSteveMath.getDistance(entityPos, blockPos) <= reach) ? true : false;
	}
}
