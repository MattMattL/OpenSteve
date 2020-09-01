package com.muss.opensteve;

import com.muss.opensteve.entity.monster.AlexAIEntity;
import com.muss.opensteve.entity.monster.SteveAIEntity;
import com.muss.opensteve.init.ClientSetup;
import com.muss.opensteve.init.ModEntityType;
import com.muss.opensteve.init.ModItem;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("opst")
public class OpenSteve
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "opst";

	public static final ItemGroup TAB = new ItemGroup("OpenSteve")
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(Items.PLAYER_HEAD);
		}
	};

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
			DeferredWorkQueue.runLater(() -> {
				GlobalEntityTypeAttributes.put(ModEntityType.STEVE_AI.get(), SteveAIEntity.setCustomAttributes().func_233813_a_());
				GlobalEntityTypeAttributes.put(ModEntityType.ALEX_AI.get(), AlexAIEntity.setCustomAttributes().func_233813_a_());
			});
	}

	private void doClientStuff(final FMLClientSetupEvent event)
	{

	}
}