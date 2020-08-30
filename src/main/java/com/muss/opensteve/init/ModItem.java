package com.muss.opensteve.init;

import com.muss.opensteve.OpenSteve;
import com.muss.opensteve.item.ModSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItem
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OpenSteve.MOD_ID);

	public static void register()
	{
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<ModSpawnEggItem> STEVE_SPAWN_EGG = ITEMS.register("steve_spawn_egg", () -> new ModSpawnEggItem(ModEntityType.STEVE_AI, 0x00afaf, 0x463aa5, new Item.Properties().group(OpenSteve.TAB)));
	public static final RegistryObject<ModSpawnEggItem> ALEX_SPAWN_EGG = ITEMS.register("alex_spawn_egg", () -> new ModSpawnEggItem(ModEntityType.ALEX_AI, 0x7ab577, 0xe58d40, new Item.Properties().group(OpenSteve.TAB)));
}

