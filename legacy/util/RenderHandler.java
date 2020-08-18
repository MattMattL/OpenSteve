package com.muss.opensteve.util;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;
import com.muss.opensteve.entity.bgmAI.EntityBgmAlexAI;
import com.muss.opensteve.entity.bgmAI.EntityBgmSteveAI;
import com.muss.opensteve.entity.render.RenderBgmAI;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;;

public class RenderHandler
{
	public static void registerEntityRenders()
	{	
		//register SteveAI
		RenderingRegistry.registerEntityRenderingHandler(EntityBgmSteveAI.class, new IRenderFactory<EntityBgmSteveAI>()
		{
			@Override
			public Render<? super EntityBgmSteveAI> createRenderFor(RenderManager manager)
			{
				return new RenderBgmAI(manager, false);
			}
		});
		
		//register AlexAI
		RenderingRegistry.registerEntityRenderingHandler(EntityBgmAlexAI.class, new IRenderFactory<EntityBgmAlexAI>()
		{
			@Override
			public Render<? super EntityBgmAlexAI> createRenderFor(RenderManager manager)
			{
				return new RenderBgmAI(manager, true);
			}
		});
	}
}
