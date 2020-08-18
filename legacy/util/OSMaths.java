package com.muss.opensteve.util;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class OSMaths
{
	public static RayTraceResult getRayTrace(EntityBgmAI entity)
	{
		EntityLookHelper lookHelper = entity.getLookHelper();

		Vec3d eyeVec = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		Vec3d focusVec = new Vec3d(lookHelper.getLookPosX(), lookHelper.getLookPosY(), lookHelper.getLookPosZ());

		RayTraceResult rayTrace = entity.world.rayTraceBlocks(eyeVec, focusVec, false, true, false);

		if(rayTrace != null)
		{
			double distance = rayTrace.getBlockPos().getDistance((int)entity.posX, (int)entity.posY, (int)entity.posZ);

			if(distance <= entity.reachRange)
				return rayTrace;
		}

		return null;
	}
}
