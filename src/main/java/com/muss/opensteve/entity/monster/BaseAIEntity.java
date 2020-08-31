package com.muss.opensteve.entity.monster;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.NNetBase;
import com.muss.opensteve.entity.ai.controller.AIControllerTest;
import com.muss.opensteve.util.OpenSteveStatics;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
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

public class BaseAIEntity extends MonsterEntity implements IBaseAI
{
	private NNetBase globalNNet = new NNetBase(8, 4, 3);
	private int nnetOut = 0;

//	FIXME Properly initialise nnetArray
//	private AIControllerBase nnetArray[];
	private AIControllerBase aiControllerTest = new AIControllerTest(this);

	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(BaseAIEntity.class, DataSerializers.BOOLEAN);

	public BaseAIEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 5;

//		this.nnetArray[0] = this.aiControllerTest;

		OpenSteveStatics.setRandomCustomName(this);
		this.enablePersistence();
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

	public static AttributeModifierMap.MutableAttribute setCustomAttributes()
	{
		return MonsterEntity.func_234295_eP_()
				.func_233815_a_(Attributes.field_233818_a_, 20.0D)
				.func_233815_a_(Attributes.field_233821_d_, (double)0.23F)
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

		// test
		globalNNet.inputVector[0] = (int)(this.getPosX()) >> 2;
		globalNNet.inputVector[1] = (int)(this.getPosY()) >> 2;
		globalNNet.inputVector[2] = (int)(this.getPosZ()) >> 2;
		globalNNet.inputVector[3] = this.isJumping? 1 : 0;
		globalNNet.inputVector[4] = this.inWater? 1 : 0;
		globalNNet.inputVector[5] = (int)(this.getLookVec().x) >> 2;
		globalNNet.inputVector[6] = (int)(this.getLookVec().y) >> 2;
		globalNNet.inputVector[7] = (int)(this.getLookVec().z) >> 2;

		globalNNet.nnRunFeedforward();
		nnetOut = globalNNet.nnGetMaxPerceptron();
	}

	/* Called when the entity is attacked. */
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}


	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);

//		for(int i=0; i<this.nnetArray.length; i++)
//			this.nnetArray[i].writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);

//		for(int i=0; i<this.nnetArray.length; i++)
//			this.nnetArray[i].readAdditional(compound);
	}


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
		return CreatureAttribute.UNDEFINED;
	}


	protected ItemStack getSkullDrop()
	{
		return new ItemStack(Items.ZOMBIE_HEAD);
	}


	public boolean isAlex()
	{
		return false;
	}

	public boolean isSteve()
	{
		return false;
	}
}
