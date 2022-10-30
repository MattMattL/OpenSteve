package com.opensteve.init;

import com.opensteve.OpenSteve;
import com.opensteve.entity.EntityAlexAi;
import com.opensteve.entity.EntitySteveAi;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OpenSteve.MODID);

//	public static final RegistryObject<EntityType<BaseAiEntity>> BASE_AI = ENTITIES.register(
//			"base_ai",
//			() -> EntityType.Builder.of(BaseAiEntity::new, MobCategory.CREATURE).build(OpenSteve.MODID + ":base_ai")
//	);

	public static final RegistryObject<EntityType<EntityAlexAi>> ALEX_AI = ENTITIES.register(
			"alex_ai",
			() -> EntityType.Builder.of(EntityAlexAi::new, MobCategory.CREATURE).build(OpenSteve.MODID + ":alex_ai")
	);

	public static final RegistryObject<EntityType<EntitySteveAi>> STEVE_AI = ENTITIES.register(
			"steve_ai",
			() -> EntityType.Builder.of(EntitySteveAi::new, MobCategory.CREATURE).build(OpenSteve.MODID + ":steve_ai")
	);
}
