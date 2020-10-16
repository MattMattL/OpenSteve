package com.muss.opensteve.entity.ai.brain;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class AIControllerHelper
{
	public static Vector3d getEyePos(BaseAIEntity entity)
	{
		return new Vector3d(entity.getPosX(), entity.getPosYEye(), entity.getPosZ());
	}

	public static Vector3d getLookPos(BaseAIEntity entity)
	{
		return new Vector3d(entity.getLookController().getLookPosX(), entity.getLookController().getLookPosY(), entity.getLookController().getLookPosZ());
	}

	public static BlockRayTraceResult getBlockRayTraceResult(BaseAIEntity entity)
	{
		return entity.world.rayTraceBlocks
		(
			new RayTraceContext
			(
				AIControllerHelper.getEyePos(entity), AIControllerHelper.getLookPos(entity),
				RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE,
				entity
			)
		);
	}

	public static RayTraceResult getRayTraceResult(BaseAIEntity entity)
	{
		return null;
	}
}
