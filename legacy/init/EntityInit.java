package com.muss.opensteve.init;

import com.muss.opensteve.Main;
import com.muss.opensteve.entity.bgmAI.EntityBgmAI;
import com.muss.opensteve.entity.bgmAI.EntityBgmAlexAI;
import com.muss.opensteve.entity.bgmAI.EntityBgmSteveAI;
import com.muss.opensteve.util.Ref;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit
{
	public static void registerEntities()
	{
		registerEntity("BgmSteveAI", EntityBgmSteveAI.class, Ref.ENTITY_BGMAI, 50, 0x00afaf, 0x463aa5);
		registerEntity("BgmAlexAI", EntityBgmAlexAI.class, Ref.ENTITY_ALEXAI, 50, 0x7ab577, 0xe58d40);
	}
	
	private static void registerEntity(String name, Class<? extends EntityBgmAI> entity, int id, int range, int colour1, int colour2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Ref.MOD_ID + ":" + name), (Class<? extends Entity>) entity, name, id, Main.instance, range, 1, true, colour1, colour2);
	}
}
