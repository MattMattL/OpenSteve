package com.muss.opensteve.entity.util;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;
import com.muss.opensteve.util.OSMaths;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;

public class OnMouseClick
{
	public static void onLeftClick(EntityBgmAI entity, ItemStack itemStack)
	{
		RayTraceResult rayTrace = OSMaths.getRayTrace(entity);

		if(rayTrace != null)
		{
			if(rayTrace.getBlockPos().distanceSq(entity.posX, entity.posY+entity.getEyeHeight(), entity.posZ) <= 80)
			{
				entity.world.setBlockToAir(rayTrace.getBlockPos());
				entity.inventory.addItemToInventory(new ItemStack(entity.world.getBlockState(rayTrace.getBlockPos()).getBlock(), 1));
			}
		}

		System.out.printf("\n");
	}

	public static void onRightClick(EntityBgmAI entity, ItemStack itemStack)
	{
		if(itemStack.getItem() instanceof ItemFood)
		{
			if(entity.foodStats.needFood())
			{
				entity.foodStats.addStats((ItemFood) itemStack.getItem(), itemStack);
				entity.world.playSound((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.NEUTRAL, 0.5F, entity.world.rand.nextFloat()*0.1F+0.9F); // Thanks Mojang

				entity.inventory.allInventory[entity.inventory.getCurrentSlot()].shrink(1);
			}

			return;
		}

		if(itemStack.getItem() instanceof ItemBlock)
		{
			RayTraceResult rayTrace = OSMaths.getRayTrace(entity);

			if(rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				if(Math.sqrt(entity.getDistanceSq(rayTrace.getBlockPos())) <= entity.reachRange)
				{
					entity.world.setBlockState(rayTrace.getBlockPos().add(rayTrace.sideHit.getDirectionVec()), ((ItemBlock)itemStack.getItem()).getBlock().getDefaultState());
					entity.inventory.allInventory[entity.inventory.getCurrentSlot()].shrink(1);
				}
			}
		}

		System.out.printf("\n");

	}
}




