package com.muss.opensteve.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

public class OpenSteveMath
{
	public static final double E = 2.7182818284590452354;
	public static final double PI = 3.14159265358979323846;

	public static double distance(Vector3d vector1, Vector3d vector2)
	{
		double distance = (vector2.x - vector1.x) * (vector2.x - vector1.x);
		distance += (vector2.y - vector1.y) * (vector2.y - vector1.y);
		distance += (vector2.z - vector1.z) * (vector2.z - vector1.z);

		return Math.sqrt(distance);
	}

	public static double distance(BlockPos block1, BlockPos block2)
	{
		Vector3d vector1 = new Vector3d(block1.getX(), block1.getY(), block1.getZ());
		Vector3d vector2 = new Vector3d(block2.getX(), block2.getY(), block2.getZ());

		return OpenSteveMath.distance(vector1, vector2);
	}

	public static boolean isInReach(Vector3d entityPos, Vector3i blockCoord, double reach)
	{
		Vector3d blockPos = new Vector3d((double)blockCoord.getX(), (double)blockCoord.getY(), (double)blockCoord.getZ());

		return (OpenSteveMath.distance(entityPos, blockPos) <= reach) ? true : false;
	}

	public static boolean isInReach(Vector3d entityPos, Vector3d blockPos, double reach)
	{
		return (OpenSteveMath.distance(entityPos, blockPos) <= reach) ? true : false;
	}
}
