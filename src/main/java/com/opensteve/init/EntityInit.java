package com.opensteve.init;

import com.opensteve.OpenSteve;
import com.opensteve.entity.BaseAiEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OpenSteve.MODID);

	public static final RegistryObject<EntityType<BaseAiEntity>> BASE_AI = ENTITIES.register(
			"base_ai",
			() -> EntityType.Builder.of(BaseAiEntity::new, MobCategory.CREATURE).build(OpenSteve.MODID + ":base_ai")
	);
}
