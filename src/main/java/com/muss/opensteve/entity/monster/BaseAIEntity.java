package com.muss.opensteve.entity.monster;

import com.muss.opensteve.entity.ai.brain.AIControllerBase;
import com.muss.opensteve.entity.ai.brain.BackPropHelper;
import com.muss.opensteve.entity.ai.brain.DeepNNetIO;
import com.muss.opensteve.entity.ai.controller.*;
import com.muss.opensteve.entity.util.AIFoodStats;
import com.muss.opensteve.entity.util.AIInventory;
import com.muss.opensteve.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BaseAIEntity extends MonsterEntity
{
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(BaseAIEntity.class, DataSerializers.BOOLEAN);
	public AIEntityTypes entityTypes;

	public final AIInventory inventory = new AIInventory(this);
	public final AIFoodStats foodStats = new AIFoodStats(this);
	public final double maxReachRange = 4;

	private AIControllerBase aiControllers[];
	private final AIControllerBase aiMovementController;
	private final AIControllerBase aiLookController;
	private final AIControllerBase aiInventoryController;
	private final AIControllerBase aiHandController;

	public BaseAIEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 0;

		this.aiHandController = new AIHandController(this, null);
		this.aiInventoryController = new AIInventoryController(this, this.aiHandController);
		this.aiLookController = new AILookController(this, this.aiInventoryController);
		this.aiMovementController = new AIMovementController(this, this.aiLookController);

		if(!this.world.isRemote)
		{
			OpenSteveStatics.aiEntityList.add(this);
		}
	}

	@Nullable
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		OpenSteveStatics.setRandomName(this);
		this.enablePersistence();

		return spawnDataIn;
	}

	protected void registerData()
	{
		super.registerData();

		this.entityTypes = new AIEntityTypes();
		//this.entityTypes.add(AIEntityType.ADULT);

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

	private int aiTickCount = 0;

	/* Called to update the entity's behaviour */
	public void livingTick()
	{
		super.livingTick();

		if(!this.world.isRemote && ++aiTickCount % 3 == 0)
		{
			aiTickCount %= 3;
			this.aiTick();
		}
	}

	private void aiTick()
	{
		this.aiMovementController.runEntityAI();
		System.out.printf("\n");
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

		if (!OpenSteveGameRules.KEEP_INVENTORY.getValue())
		{
			//this.destroyVanishingCursedItems();
			this.inventory.dropAllItems();
		}

	}


	public float getDigSpeed(BlockState state, @Nullable BlockPos pos)
	{
		float speed = this.inventory.getDestroySpeed(state);

		if(speed > 1.0F)
		{
			int i = EnchantmentHelper.getEfficiencyModifier(this);
			ItemStack itemstack = this.getHeldItemMainhand();

			if(i > 0 && !itemstack.isEmpty())
				speed += (float)(i * i + 1);
		}

		if(EffectUtils.hasMiningSpeedup(this))
			speed *= 1.0F + (float)(EffectUtils.getMiningSpeedup(this) + 1) * 0.2F;

		if(this.isPotionActive(Effects.MINING_FATIGUE))
		{
			switch(this.getActivePotionEffect(Effects.MINING_FATIGUE).getAmplifier())
			{
				case 0:
					speed *= 0.3F;
					break;
				case 1:
					speed *= 0.09F;
					break;
				case 2:
					speed *= 0.0027F;
					break;
				case 3:
				default:
					speed *= 8.1E-4F;
			}
		}

		if(this.areEyesInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this))
			speed /= 5.0F;

		if(!this.onGround)
			speed /= 5.0F;

		// speed = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed(this, state, speed, pos);
		return speed;
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
		this.foodStats.read(compound);

		//for(int i = 0; i<this.aiControllers.length; i++)
		//	this.aiControllers[i].read(compound);
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);

		this.inventory.write(compound);
		this.foodStats.write(compound);

		//for(int i = 0; i<this.aiControllers.length; i++)
		//	this.aiControllers[i].write(compound);
	}


	public abstract boolean isAlex();

	public abstract boolean isSteve();


	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vector, Hand hand)
	{
		if(!this.world.isRemote)
			return AIPlayerInteraction.getResult(this, player, vector, hand);
		else
			return ActionResultType.SUCCESS;
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