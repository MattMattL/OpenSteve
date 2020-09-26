package com.muss.opensteve.entity.monster;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.DeepNNetIO;
import com.muss.opensteve.entity.ai.controller.AIHandController;
import com.muss.opensteve.entity.ai.controller.AIInventoryController;
import com.muss.opensteve.entity.ai.controller.AILookController;
import com.muss.opensteve.entity.ai.controller.AIMovementController;
import com.muss.opensteve.entity.util.AIFoodStats;
import com.muss.opensteve.entity.util.AIInventory;
import com.muss.opensteve.util.AIEntityTypes;
import com.muss.opensteve.util.OpenSteveDataTable;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BaseAIEntity extends MonsterEntity
{
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(BaseAIEntity.class, DataSerializers.BOOLEAN);
	public AIEntityTypes entityTypes = new AIEntityTypes();

	public final AIInventory inventory = new AIInventory(this);
	public final AIFoodStats foodStats = new AIFoodStats(this);
	public final double maxReachRange = 4;

	private DeepNNetIO globalNNet = new DeepNNetIO(12, 4, 4, "GlobalNNet");
	private int nnetOut;
	private AIControllerBase nnetArray[];
	public AIControllerBase aiMovementController = new AIMovementController(this);
	public AIControllerBase aiLookController = new AILookController(this);
	public AIControllerBase aiInventoryController = new AIInventoryController(this);
	public AIControllerBase aiHandController = new AIHandController(this);

	public Vector3d eyePos;
	public Vector3d lookPos;
	public BlockPos targetBlock;

	public BaseAIEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 0;

		int iNNet = 0;
		this.nnetArray = new AIControllerBase[4];
		this.nnetArray[iNNet++] = this.aiMovementController;
		this.nnetArray[iNNet++] = this.aiLookController;
		this.nnetArray[iNNet++] = this.aiInventoryController;
		this.nnetArray[iNNet++] = this.aiHandController;

		if(!this.world.isRemote)
		{
			OpenSteveDataTable.aiEntityList.add(this);
		}
	}

	@Nullable
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		OpenSteveDataTable.setRandomName(this);
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

		if(!this.world.isRemote)
		{
			this.foodStats.tick();
		}
	}

	/* Called to update the entity's behaviour */
	public void livingTick()
	{
		super.livingTick();

		if(!this.world.isRemote)
		{
			this.aiTick();
		}
	}

	private void aiTick()
	{
		/*int iNNet = 0;

		this.globalNNet.vectorIn[iNNet++] = this.getPosX();
		this.globalNNet.vectorIn[iNNet++] = this.getPosY();
		this.globalNNet.vectorIn[iNNet++] = this.getPosZ();
		this.globalNNet.vectorIn[iNNet++] = this.getMaxHealth();
		this.globalNNet.vectorIn[iNNet++] = this.getHealth();
		
		this.globalNNet.vectorIn[iNNet++] = this.foodStats.getFoodLevel();
		this.globalNNet.vectorIn[iNNet++] = this.foodStats.getSaturationLevel();
		this.globalNNet.vectorIn[iNNet++] = this.isAlex()? 1 : -1;
		this.globalNNet.vectorIn[iNNet++] = this.isBurning()? 1 : -1;
		this.globalNNet.vectorIn[iNNet++] = this.isInWater()? 1 : -1;

		this.globalNNet.vectorIn[iNNet++] = this.isChild()? 1 : -1;
		this.globalNNet.vectorIn[iNNet++] = this.isJumping? 1 : -1;

		this.globalNNet.nnRunFeedForward();
		this.nnetOut = this.globalNNet.nnGetMaxOutputIndex();*/

		/* TEST */
		this.nnetArray[this.nnetOut++].runEntityAI();

		this.nnetOut %= this.nnetArray.length;
	}


	@Override
	public boolean canPickUpLoot()
	{
		return true;
	}

	/* Called when the entity collides with item entities */
	@Override
	protected void updateEquipmentIfNeeded(ItemEntity itemEntity)
	{
		ItemStack itemStack = itemEntity.getItem();
		int storable = Math.min(itemStack.getCount(), this.inventory.getAvailableSpaceFor(itemStack));

		if(storable > 0)
		{
			ItemStack splitStack = itemStack.split(storable);
			this.inventory.addItemStackToInventory(splitStack);
		}
	}

	@Override
	public void onItemPickup(Entity entityIn, int quantity)
	{
		super.onItemPickup(entityIn, quantity);
	}

	public ItemEntity dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem)
	{
		System.out.printf("[OpenSteve] dropItem called\n");

		if(droppedItem.isEmpty())
			return null;

		if(this.world.isRemote)
			this.swingArm(Hand.MAIN_HAND);

		double d0 = this.getPosYEye() - (double)0.3F;
		ItemEntity itemEntity = new ItemEntity(this.world, this.getPosX(), d0, this.getPosZ(), droppedItem);
		itemEntity.setPickupDelay(40);

		if(traceItem)
			itemEntity.setThrowerId(this.getUniqueID());

		if(dropAround)
		{
			float f = this.rand.nextFloat() * 0.5F;
			float f1 = this.rand.nextFloat() * ((float)Math.PI * 2F);
			itemEntity.setMotion((double)(-MathHelper.sin(f1) * f), (double)0.2F, (double)(MathHelper.cos(f1) * f));
		}
		else
		{
			float f7 = 0.3F;
			float f8 = MathHelper.sin(this.rotationPitch * ((float)Math.PI / 180F));
			float f2 = MathHelper.cos(this.rotationPitch * ((float)Math.PI / 180F));
			float f3 = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F));
			float f4 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F));
			float f5 = this.rand.nextFloat() * ((float)Math.PI * 2F);
			float f6 = 0.02F * this.rand.nextFloat();
			itemEntity.setMotion((double)(-f3 * f2 * 0.3F) + Math.cos((double)f5) * (double)f6, (double)(-f8 * 0.3F + 0.1F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F), (double)(f4 * f2 * 0.3F) + Math.sin((double)f5) * (double)f6);
		}

		this.world.addEntity(itemEntity);
		return itemEntity;
	}

	protected void dropInventory()
	{
		super.dropInventory();

		System.out.printf("[OpenSteve] dropInventory called\n");

		if (!this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY))
		{
			System.out.printf("[OpenSteve] set to drop all items\n");

			//this.destroyVanishingCursedItems();
			this.inventory.dropAllItems();
		}

	}


	public void addExhaustion(float exhaustion)
	{
		if(!this.world.isRemote)
		{
			this.foodStats.addExhaustion(exhaustion);
		}
	}

	public AIFoodStats getFoodStats()
	{
		return this.foodStats;
	}

	public boolean canEat(boolean ignoreHunger)
	{
		return ignoreHunger || this.foodStats.needFood();
	}

	public boolean shouldHeal()
	{
		return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
	}


	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);

		this.inventory.read(compound);
		this.globalNNet.read(compound);
		this.foodStats.read(compound);

		for(int i=0; i<this.nnetArray.length; i++)
			this.nnetArray[i].read(compound);
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);

		this.inventory.write(compound);
		this.globalNNet.write(compound);
		this.foodStats.write(compound);

		for(int i=0; i<this.nnetArray.length; i++)
			this.nnetArray[i].write(compound);
	}


	public abstract boolean isAlex();

	public abstract boolean isSteve();


	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		System.out.printf("[OpenSteve] attackEntityFrom\n");

		return super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean isAlive()
	{
		return !this.dead;
	}

	public void setDead()
	{
		this.dead = true;
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
	}

	@Override
	public void onKillCommand()
	{
		super.onKillCommand();

		this.dead = true;
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
	public SoundCategory getSoundCategory()
	{
		return SoundCategory.HOSTILE;
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