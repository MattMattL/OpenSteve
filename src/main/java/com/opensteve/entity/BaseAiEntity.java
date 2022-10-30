package com.opensteve.entity;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;

public class BaseAiEntity extends LivingEntity
{
	public BaseAiEntity(EntityType<? extends LivingEntity> type, Level level)
	{
		super(type, level);
	}

	public boolean isSteve()
	{
		return false;
	}

	public boolean isAlex()
	{
		return false;
	}

	@Override
	public Iterable<ItemStack> getArmorSlots()
	{
		// TODO: return proper armour slots

		return new Iterable<ItemStack>()
		{
			@NotNull
			@Override
			public Iterator<ItemStack> iterator()
			{
				return new Iterator<ItemStack>()
				{
					@Override
					public boolean hasNext()
					{
						return false;
					}

					@Override
					public ItemStack next()
					{
						return ItemStack.EMPTY;
					}
				};
			}
		};
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot)
	{
		// TODO: return proper item

		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot slot, ItemStack item)
	{

	}

	public static AttributeSupplier.Builder getBaseAiAttributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.ARMOR, 0.0D)
				.add(Attributes.MOVEMENT_SPEED, 1.0D);
	}

	@Override
	public HumanoidArm getMainArm()
	{
		Random random = new Random();

		return (random.nextInt(100) >= 90)? HumanoidArm.RIGHT : HumanoidArm.LEFT;
	}
}
