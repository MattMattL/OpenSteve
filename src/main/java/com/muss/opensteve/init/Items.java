package com.muss.opensteve.init;

import com.muss.opensteve.OpenSteve;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OpenSteve.MOD_ID);

	// Items
	//public static final RegistryObject<SpawnEggItem> STEVE_SPAWN_EGG = ITEMS.register("steve_spawn_egg", () -> new SpawnEggItem());
}
