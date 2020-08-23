package com.muss.opensteve.entity.monster;

import com.muss.opensteve.entity.ai.brain.NNetIO;
import com.muss.opensteve.entity.ai.controller.MovementController;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BaseAIEntity extends MonsterEntity
{
//	private NNetIO globalNNet = new NNetIO(5, 5, 5);
//	private MovementController movementController = new MovementController(this);

	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(BaseAIEntity.class, DataSerializers.BOOLEAN);

	public BaseAIEntity(EntityType<? extends BaseAIEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 5;
	}

	/* Called to update the entity's position/logic. */
	public void tick()
	{
		super.tick();
	}

	/* Called to update the entity's behaviour */
	public void livingTick()
	{
		super.livingTick();
	}

	@Nullable
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		return spawnDataIn;
	}


	protected void registerData()
	{
		super.registerData();
		this.getDataManager().register(IS_CHILD, false);
	}

	public boolean isChild()
	{
		return this.getDataManager().get(IS_CHILD);
	}

	public void setChild(boolean childAI)
	{
		this.getDataManager().set(IS_CHILD, childAI);
	}


	/* Called when the entity is attacked. */
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
	{
		return this.isChild() ? 0.93F : 1.74F;
	}

	public double getYOffset()
	{
		return this.isChild() ? 0.0D : -0.45D;
	}


	// TODO: add appropriate sound events
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_ZOMBIE_DEATH;
	}

	protected SoundEvent getStepSound()
	{
		return SoundEvents.ENTITY_ZOMBIE_STEP;
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn) // ?
	{
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	public CreatureAttribute getCreatureAttribute() // ?
	{
		return CreatureAttribute.UNDEAD;
	}


	protected ItemStack getSkullDrop()
	{
		return new ItemStack(Items.ZOMBIE_HEAD);
	}
}
