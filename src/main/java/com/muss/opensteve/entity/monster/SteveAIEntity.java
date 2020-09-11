package com.muss.opensteve.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class SteveAIEntity extends BaseAIEntity
{
	public SteveAIEntity(EntityType<? extends BaseAIEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 5;
	}

	@Override
	public boolean isSteve()
	{
		return true;
	}

	@Override
	public boolean isAlex()
	{
		return false;
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
	}
}
