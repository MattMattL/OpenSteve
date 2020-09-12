package com.muss.opensteve.entity.monster;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.DeepNNetIO;
import com.muss.opensteve.entity.ai.controller.AIBodyController;
import com.muss.opensteve.entity.ai.controller.AIJumpController;
import com.muss.opensteve.entity.ai.controller.AILookController;
import com.muss.opensteve.entity.ai.controller.AIMovementController;
import com.muss.opensteve.entity.util.AIInventory;
import com.muss.opensteve.entity.util.OpenSteveDataTable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
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

public abstract class BaseAIEntity extends MonsterEntity
{
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(BaseAIEntity.class, DataSerializers.BOOLEAN);

	private DeepNNetIO globalNNet = new DeepNNetIO(8, 3, 4, "GlobalNNet");

	private int nnetOut;

	private AIControllerBase nnetArray[];
	protected AIControllerBase aiBodyController = new AIBodyController(this);
	protected AIControllerBase aiJumpController = new AIJumpController(this);
	protected AIControllerBase aiLookController = new AILookController(this);
	protected AIControllerBase aiMovementController = new AIMovementController(this);

	public final AIInventory inventory = new AIInventory(this);

	public BaseAIEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 0;

		this.nnetArray = new AIControllerBase[4];
		this.nnetArray[0] = this.aiBodyController;
		this.nnetArray[1] = this.aiJumpController;
		this.nnetArray[2] = this.aiLookController;
		this.nnetArray[3] = this.aiMovementController;
	}

	@Nullable
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		OpenSteveDataTable.setRandomCustomName(this);
		this.enablePersistence();

		return spawnDataIn;
	}

	protected void registerData()
	{
		super.registerData();
		this.getDataManager().register(IS_CHILD, false);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes()
	{
		return MonsterEntity.func_234295_eP_()
				.func_233815_a_(Attributes.field_233818_a_, 20.0D)
				.func_233815_a_(Attributes.field_233821_d_, 1D)
				.func_233815_a_(Attributes.field_233823_f_, 2.0D)
				.func_233814_a_(Attributes.field_233829_l_);
	}


	/* Called to update the entity's position and logic. */
	public void tick()
	{
		super.tick();
	}

	/* Called to update the entity's behaviour */
	public void livingTick()
	{
		super.livingTick();
	}

	/* Called when the entity is attacked. */
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return super.attackEntityFrom(source, amount);
	}


	@Override
	public boolean canPickUpLoot()
	{
		return true;
	}

	@Override
	public boolean canPickUpItem(ItemStack itemstackIn)
	{
		return super.canPickUpItem(itemstackIn);
	}

	/* Called when the entity collides with item entities */
	@Override
	protected void updateEquipmentIfNeeded(ItemEntity itemEntity)
	{
		ItemStack itemStack = itemEntity.getItem();
		int storable = Math.min(itemStack.getCount(), this.inventory.getAvailableSpaceFor(itemStack));

		ItemStack splitStack = itemStack.split(storable);
		this.inventory.addItemStackToInventory(splitStack);
	}

	@Override
	public void onItemPickup(Entity entityIn, int quantity)
	{
		super.onItemPickup(entityIn, quantity);
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);

		this.inventory.read(compound);
		this.globalNNet.read(compound);

		for(int i=0; i<this.nnetArray.length; i++)
			this.nnetArray[i].read(compound);
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);

		this.inventory.write(compound);
		this.globalNNet.write(compound);

		for(int i=0; i<this.nnetArray.length; i++)
			this.nnetArray[i].write(compound);
	}


	public abstract boolean isAlex();

	public abstract boolean isSteve();


	public boolean isChild()
	{
		return this.getDataManager().get(IS_CHILD);
	}

	public void setChild(boolean childAI)
	{
		this.getDataManager().set(IS_CHILD, childAI);
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
	{
		return this.isChild() ? 0.93F : 1.74F;
	}

	public double getYOffset()
	{
		return this.isChild() ? 0.0D : -0.45D;
	}


	@Override
	protected SoundEvent getAmbientSound()
	{
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_PLAYER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
		super.playStepSound(pos, blockIn);
	}


	@Override
	public CreatureAttribute getCreatureAttribute()
	{
		return CreatureAttribute.UNDEAD;
	}


	protected ItemStack getSkullDrop()
	{
		return new ItemStack(Items.PLAYER_HEAD);
	}
}