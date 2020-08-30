package com.muss.opensteve;

import com.muss.opensteve.entity.monster.SteveAIEntity;
import com.muss.opensteve.init.ClientSetup;
import com.muss.opensteve.init.ModEntityType;
import com.muss.opensteve.init.ModItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("opst")
public class OpenSteve
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "opst";

	public OpenSteve()
	{
		// TODO: merge registry methods into one class
		ModEntityType.register();
		ModItem.register();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event)
	{

	}

	private void doClientStuff(final FMLClientSetupEvent event)
	{

	}

	public static final ItemGroup TAB = new ItemGroup("OpenSteve")
	{
		@Override
		public ItemStack createIcon()
		{
//			return new ItemStack(ModItem.STEVE_SPAWN_EGG.get());
			return new ItemStack(Items.PLAYER_HEAD);
		}
	};
}