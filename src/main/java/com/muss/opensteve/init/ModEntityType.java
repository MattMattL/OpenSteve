package com.muss.opensteve.init;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.entity.monster.AlexAIEntity;
import com.muss.opensteve.entity.monster.SteveAIEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityType
{
	public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, OpenSteve.MOD_ID);

	public static final RegistryObject<EntityType<SteveAIEntity>> STEVE_AI = ENTITY_TYPES.register("SteveAI", () -> EntityType.Builder.create(SteveAIEntity::new, EntityClassification.MONSTER)
			.size(0.8f, 1.8f)
			.build(new ResourceLocation(OpenSteve.MOD_ID, "steve_ai")
			.toString()));

	public static final RegistryObject<EntityType<AlexAIEntity>> ALEX_AI = ENTITY_TYPES.register("AlexAI", () -> EntityType.Builder.create(AlexAIEntity::new, EntityClassification.MONSTER)
			.size(0.8f, 1.8f)
			.build(new ResourceLocation(OpenSteve.MOD_ID, "alex_ai")
			.toString()));

	public static void register()
	{
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
