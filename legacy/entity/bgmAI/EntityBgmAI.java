package com.muss.opensteve.entity.bgmAI;

import java.util.List;

import javax.annotation.Nullable;

import com.muss.opensteve.entity.ai.*;
import com.muss.opensteve.entity.neuralnet.NNetIO;
import com.muss.opensteve.entity.util.BgmAIInit;
import com.muss.opensteve.entity.util.FoodStats;
import com.muss.opensteve.entity.util.InventoryBgmAI;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

/*import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;*/

public abstract class EntityBgmAI extends MonsterEntity
{
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.<Boolean>createKey(EntityBgmSteveAI.class, DataSerializers.BOOLEAN);
	
	// Entity variables
	private float bgmAIWidth = -0.6F;
	private float bgmAIHeight = 1.8F;

	// Player instance
	public FoodStats foodStats = new FoodStats();
	public InventoryBgmAI inventory = new InventoryBgmAI(this);
	public int reachRange = 16;
	
	// BgmAI instance
	protected BgmAIBase arrEntityAI[];
	
	public BlockPos homePos;
	
	// Family tree
	public EntityBgmAI parentAlex;
	public EntityBgmAI parentSteve;
	public List<EntityBgmAI> childList;

	public EntityBgmAI(EntityType<? extends ZombieEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public EntityBgmAI(World worldIn)
	{
		this(EntityType.ZOMBIE, worldIn);
	}
	
	/*public EntityBgmAI(World worldIn)
	{
		super(worldIn);

		WorldProvider worldProvider;

		this.isWet();

		this.experienceValue = 0;
	}*/
	
	public EntityBgmAI(World worldIn, EntityBgmAI parentAlex, EntityBgmAI parentSteve, double posX, double posY, double posZ)
	{
		this(worldIn);
		
		this.parentAlex = parentAlex;
		this.parentSteve = parentSteve;
		
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}


	// Entity Initialisations //

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		// BgmAI inits
		BgmAIInit.initialise(this);
		
		this.homePos = new BlockPos(this);
		//this.getLookHelper().setLookPosition(this.posX + 10, this.posY, this.posZ, 10, 10);

		livingdata = super.onInitialSpawn(difficulty, livingdata);

		return livingdata;
	}

	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(IS_CHILD, Boolean.valueOf(false));
	}
	

	// BgmAI Attributes //

	public abstract boolean isAlex();
	
	public abstract boolean isSteve();
	
	protected abstract void shuffleAITree(int MAX_ARR);


	// Player Attributes //

	@Override
	public void onItemPickup(Entity entityIn, int quantity)
	{
		int slot = 0;
		
		super.onItemPickup(entityIn, quantity);
		
		if(entityIn instanceof EntityItem)
			this.inventory.addItemToInventory(((EntityItem) entityIn).getItem());
	}

	@Override
	/*
	protected void updateEquipmentIfNeeded(EntityItem itemEntityIn)
	{
		ItemStack itemstack = itemEntityIn.getItem();

		if(this.inventory.canPickUpItem(itemstack) >= 0)
		{
			this.onItemPickup(itemEntityIn, itemstack.getCount());
			itemEntityIn.setDead();
		}
	}

	public boolean shouldHeal()
    {
        return (0.0F < this.getHealth()) && (this.getHealth() < this.getMaxHealth());
    }

    public void addExhaustion(float exhaustion)
	{
		if(!this.world.isRemote && this.world.getDifficulty() != EnumDifficulty.PEACEFUL)
			this.foodStats.addExhaustion(exhaustion);
	}

	@Nullable
	public EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem)
	{
		if (droppedItem.isEmpty())
		{
			return null;
		}
		else
		{
			double d0 = this.posY - 0.30000001192092896D + (double)this.getEyeHeight();
			EntityItem entityItem = new EntityItem(this.world, this.posX, d0, this.posZ, droppedItem);

			entityItem.setPickupDelay(40);

			if (traceItem)
				entityItem.setThrower(this.getName());

			if (dropAround)
			{
				float f = this.rand.nextFloat() * 0.5F;
				float f1 = this.rand.nextFloat() * ((float)Math.PI * 2F);
				entityItem.motionX = (double)(-MathHelper.sin(f1) * f);
				entityItem.motionZ = (double)(MathHelper.cos(f1) * f);
				entityItem.motionY = 0.20000000298023224D;
			}
			else
			{
				float f2 = 0.3F;
				entityItem.motionX = (double)(-MathHelper.sin(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F) * f2);
				entityItem.motionZ = (double)(MathHelper.cos(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F) * f2);
				entityItem.motionY = (double)(-MathHelper.sin(this.rotationPitch * 0.017453292F) * f2 + 0.1F);
				float f3 = this.rand.nextFloat() * ((float)Math.PI * 2F);
				f2 = 0.02F * this.rand.nextFloat();
				entityItem.motionX += Math.cos((double)f3) * (double)f2;
				entityItem.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				entityItem.motionZ += Math.sin((double)f3) * (double)f2;
			}

			this.world.spawnEntity(entityItem);
			//ItemStack itemStack = entityItem.getItem();

			return entityItem;
		}
	}


	// AI Tree Manager //

	protected void initEntityAI()
	{
		// initialise AI tree
		this.arrEntityAI = new BgmAIBase[4];

		this.applyEntityAI(this.arrEntityAI.length);
		this.shuffleAITree(this.arrEntityAI.length);

		// initialise sub-neural network
		for(int i=0; i<this.arrEntityAI.length-1; i++)
			arrEntityAI[i].setSubclass(arrEntityAI[i+1]);

		arrEntityAI[this.arrEntityAI.length - 1].setSubclass(null);

		// enqueuing the root class
		this.tasks.addTask(1, arrEntityAI[0]);
	}

	protected void applyEntityAI(int max)
	{
		int arr = max;

		// Branch AI
		this.arrEntityAI[--arr] = new BgmAIHandControl(this);
		this.arrEntityAI[--arr] = new BgmAIMotorControl(this);
		this.arrEntityAI[--arr] = new BgmAIInventory(this);
		this.arrEntityAI[--arr] = new BgmAIMovement(this);
		// Root AI
	}


	// Update Manager //

	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if(this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean("naturalRegeneration"))
		{
			if(this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0)
				this.heal(1.0F);

			if(this.foodStats.needFood() && this.ticksExisted % 10 == 0)
				this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
		}

		this.updateArmSwingProgress();
		float f = this.getBrightness();

		if (f > 0.5F)
		{
			this.idleTime += 2;
		}
	}

	public void onUpdate()
	{
		super.onUpdate();

		if(!this.world.isRemote)
		{
			this.foodStats.onUpdate(this);
		}
	}


	// Data Manager //

	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		NBTTagCompound opstvCompound = new NBTTagCompound();

		// save home position
		if(this.homePos == null)
			this.homePos = new BlockPos(this);

		opstvCompound.setDouble("HomePosX", this.homePos.getX());
		opstvCompound.setDouble("HomePosY", this.homePos.getY());
		opstvCompound.setDouble("HomePosZ", this.homePos.getZ());

		// save neural weights
		if(this.arrEntityAI != null)
		{
			for(int i=0; i<this.arrEntityAI.length-1; i++)
				this.arrEntityAI[i].writeWeightsToNBT(opstvCompound);
		}

		// save others
		opstvCompound.setBoolean("IsEntityBaby", this.isChild());

		this.foodStats.writeNBT(opstvCompound);
		this.inventory.writeToNBT(opstvCompound);

		compound.setTag("OpenSteveCompound", opstvCompound);
	}

	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		if(compound.hasKey("OpenSteveCompound"))
		{
			NBTTagCompound opstvCompound = compound.getCompoundTag("OpenSteveCompound");

			// set home position
			double homePosX = opstvCompound.getDouble("HomePosX");
			double homePosY = opstvCompound.getDouble("HomePosY");
			double homePosZ = opstvCompound.getDouble("HomePosZ");
			this.homePos = new BlockPos(homePosX, homePosY, homePosZ);

			// set nnet weights
			if(this.arrEntityAI != null)
			{
				for(int i=0; i<this.arrEntityAI.length-1; i++)
					this.arrEntityAI[i].readWeightsFromNBT(opstvCompound);
			}

			// set others
			this.setChild(opstvCompound.getBoolean("IsEntityBaby"));

			this.foodStats.readNBT(opstvCompound);
			this.inventory.readFromNBT(opstvCompound);
		}
	}


	// Sound Event Handler //

	public SoundCategory getSoundCategory()
	{
		return SoundCategory.NEUTRAL;
	}

	protected SoundEvent getSwimSound()
	{
		return SoundEvents.ENTITY_PLAYER_SWIM;
	}

	protected SoundEvent getSplashSound()
	{
		return SoundEvents.ENTITY_PLAYER_SPLASH;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		if(damageSourceIn == DamageSource.ON_FIRE)
			return SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
		else
			return (damageSourceIn == DamageSource.DROWN)? SoundEvents.ENTITY_PLAYER_HURT_DROWN : SoundEvents.ENTITY_PLAYER_HURT;
	}

	protected SoundEvent getFallSound(int heightIn)
	{
		return (heightIn > 4)? SoundEvents.ENTITY_PLAYER_BIG_FALL : SoundEvents.ENTITY_PLAYER_SMALL_FALL;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	protected SoundEvent getAmbientSound()
	{
		return null;
	}


	// Entity Attributes //


	@Override
	public boolean canPickUpLoot()
	{
		return true;
	}

	@Override
	public boolean getCanSpawnHere()
	{
		return false;
	}

	@Override
	protected boolean canDropLoot() // or exps
	{
		return false;
	}

	@Override
	public boolean isChild()
	{
		return ((Boolean)this.getDataManager().get(IS_CHILD)).booleanValue();
	}

	public void setChild(boolean isBaby)
	{
		this.getDataManager().set(IS_CHILD, Boolean.valueOf(isBaby));
		this.setChildSize(isBaby);
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
	}

	/* Get the experience points the entity currently has. */

	protected int getExperiencePoints(EntityPlayer player)
	{
		if(this.isChild())
		{
			this.experienceValue = (int)((float)this.experienceValue * 2.5F);
		}

		return super.getExperiencePoints(player);
	}

	public void notifyDataManagerChange(DataParameter<?> key)
	{
		if(IS_CHILD.equals(key))
		{
			this.setChildSize(this.isChild());
		}

		super.notifyDataManagerChange(key);
	}

	/* Called when the entity is attacked. */
	public boolean attackEntityFrom(DamageSource source, float amount)
	{	
		if(this.isEntityInvulnerable(source))
		{
			return false;
		}
		else
		{
			return super.attackEntityFrom(source, amount);
		}
	}

	public static void registerFixesZombie(DataFixer fixer)
	{
		//EntityLiving.registerFixesMob(fixer, EntityBgmSteveAI.class);
	}

	/* This method gets called when the entity kills another one. */
	public void onKillEntity(EntityLivingBase entityLivingIn)
	{
		return;
	}

	public float getEyeHeight()
	{
		return 1.74F;
	}

	@Override
	protected boolean canEquipItem(ItemStack stack)
	{
		//return stack.getItem() == Items.EGG && this.isChild() && this.isRiding() ? false : super.canEquipItem(stack);
		return true;
	}

	/* sets the size of the entity to be half of its current size if true. */
	public void setChildSize(boolean isChild)
	{
		this.multiplySize(isChild? 0.5F : 1.0F);
	}
	
	/* Multiplies the height and width by the provided float. */
	protected final void multiplySize(float size)
	{
		super.setSize(this.bgmAIWidth * size, this.bgmAIHeight * size);
	}

	/* Sets the width and height of the entity. */
	protected final void setSize(float width, float height)
	{
		boolean flag = this.bgmAIWidth > 0.0F && this.bgmAIHeight > 0.0F;
		this.bgmAIWidth = width;
		this.bgmAIHeight = height;

		if(!flag)
		{
			this.multiplySize(1.0F);
		}
	}

	public double getYOffset()
	{
		return this.isChild() ? 0.0D : -0.45D;
	}

	public void onDeath(DamageSource cause)
	{
		if(net.minecraftforge.common.ForgeHooks.onLivingDeath(this,  cause))
			return;

		super.onDeath(cause);

		this.setSize(0.2F, 0.2F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionY = 0.10000000149011612D;

		captureDrops = true;
		capturedDrops.clear();

		if(this.world.getGameRules().getBoolean("keepInventory") == false)
		{
			//this.destroyVanishingCursedItems();
			this.inventory.dropAllItems();
		}

		captureDrops = false;
		//if (!world.isRemote) net.minecraftforge.event.ForgeEventFactory.onPlayerDrops(this, cause, capturedDrops, recentlyHit > 0);

		if (cause != null)
		{
			this.motionX = (double)(-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 0.017453292F) * 0.1F);
			this.motionZ = (double)(-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 0.017453292F) * 0.1F);
		}
		else
		{
			this.motionX = 0.0D;
			this.motionZ = 0.0D;
		}

		this.extinguish();
		this.setFlag(0, false);
	}

	class GroupData implements IEntityLivingData
	{
		public boolean isChild;

		private GroupData(boolean p_i47328_2_)
		{
			this.isChild = p_i47328_2_;
		}
	}

	
}




