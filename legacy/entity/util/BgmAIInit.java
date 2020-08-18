package com.muss.opensteve.entity.util;

import com.muss.opensteve.entity.bgmAI.EntityBgmAI;

import net.minecraft.network.datasync.EntityDataManager;

public class BgmAIInit
{
	private static final int NAME_FREQUENCY = 5;
	private static EntityBgmAI entity;
	
	
	// Credits and easter eggs //
	
	public static final String arrNameSteve[] =
	{
		"NULL", "G_Cat", "G_Omg", "Racoon", "Notch",
		"BGM", "베배", "DummyData", "newbigbird", "웡"
	};
	
	public static final String arrNameAlex[] =
	{
		"NULL"
	};
	
	
	// Initialisation //
	
	public static void initialise(EntityBgmAI entytIn)
	{
		entity = entytIn;
		
		setCustomName();
	}

	
	public static void setCustomName()
	{
		int chance = (int)(Math.random() * (NAME_FREQUENCY-1));
		String entityName;
		
		// get a random unique name
		if(chance == 0)
		{
			if(entity.isAlex())
			{
				int randNum = (int)(Math.random() * arrNameAlex.length);
				entityName = arrNameAlex[randNum];
			}
			else
			{
				int randNum = (int)(Math.random() * arrNameSteve.length);
				entityName = arrNameSteve[randNum];
			}
		}
		else
		{
			if(entity.isAlex())
				entityName = "Alex";
			else
				entityName = "Steve";
		}
        
		// set entity name
        entity.setCustomNameTag(entityName);
        entity.enablePersistence();
        
        switch(entityName)
        {
        		case "Steve":
        		case "Alex":
        			entity.setAlwaysRenderNameTag(false);
        			break;
        			
        		default:
        			entity.setAlwaysRenderNameTag(true);
        }
	}
}




