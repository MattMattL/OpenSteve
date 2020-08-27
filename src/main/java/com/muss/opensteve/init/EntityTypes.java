package com.muss.opensteve.init;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import com.muss.opensteve.entity.monster.SteveAIEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypes
{
	public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, OpenSteve.MOD_ID);

	public static final RegistryObject<EntityType<SteveAIEntity>> SteveAI = ENTITY_TYPES.register("SteveAI",
			() -> EntityType.Builder.create(SteveAIEntity::new, EntityClassification.MONSTER)
					.size(0.8f, 1.8f)
					.build(new ResourceLocation(OpenSteve.MOD_ID, "SteveAI").toString()));
}
