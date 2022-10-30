package com.opensteve.init;

import com.opensteve.OpenSteve;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OpenSteve.MODID);

	public static final RegistryObject<ForgeSpawnEggItem> ALEX_AI_SPAWN_EGG = ITEMS.register(
			"alex_ai_spawn_egg",
			() -> new ForgeSpawnEggItem(EntityInit.ALEX_AI, 0x1DEE93, 0x83D92A, properties().stacksTo(16))
	);

	public static final RegistryObject<ForgeSpawnEggItem> STEVE_AI_SPAWN_EGG = ITEMS.register(
			"steve_ai_spawn_egg",
			() -> new ForgeSpawnEggItem(EntityInit.STEVE_AI, 0x125DA3, 0x092CE2, properties().stacksTo(16))
	);

	private static Item.Properties properties()
	{
        return new Item.Properties().tab(CreativeModeTab.TAB_MISC);
    }
}
