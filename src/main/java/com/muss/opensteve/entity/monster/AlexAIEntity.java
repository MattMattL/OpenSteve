package com.muss.opensteve.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class AlexAIEntity extends BaseAIEntity
{
	public AlexAIEntity(EntityType<? extends BaseAIEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 5;
	}

	@Override
	public boolean isAlex()
	{
		return true;
	}

	@Override
	public boolean isSteve()
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
