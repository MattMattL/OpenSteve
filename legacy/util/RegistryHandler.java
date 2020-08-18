package com.muss.opensteve.util;

import com.muss.opensteve.init.EntityInit;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RegistryHandler
{	
	public static void InitRegistries()
	{
		EntityInit.registerEntities();
		RenderHandler.registerEntityRenders();
	}
}
