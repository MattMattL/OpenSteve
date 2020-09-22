package com.muss.opensteve.entity.monster;

import com.muss.opensteve.util.AIEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class SteveAIEntity extends BaseAIEntity
{
	public SteveAIEntity(EntityType<? extends BaseAIEntity> type, World worldIn)
	{
		super(type, worldIn);

		this.entityTypes.add(AIEntityType.STEVE);
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
