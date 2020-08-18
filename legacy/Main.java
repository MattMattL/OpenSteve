package com.muss.opensteve;

import com.muss.opensteve.util.RegistryHandler;
import com.muss.opensteve.proxy.CommonProxy;
import com.muss.opensteve.util.CmdHandler;
import com.muss.opensteve.util.Ref;

import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Ref.MOD_ID, name = Ref.NAME, version = Ref.VERSION)

public class Main
{
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.InitRegistries();
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		
	}
	 
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		
	}
	 
	@EventHandler
	public void registerCommands(FMLServerStartingEvent event)
	{
		ServerCommandManager manager = (ServerCommandManager)event.getServer().getCommandManager();
	
		manager.registerCommand(new CmdHandler());
	}
	
}




