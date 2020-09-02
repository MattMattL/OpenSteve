package com.muss.opensteve.util;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.util.text.ITextComponent;

public class OpenSteveDataTable
{
	public static final String namesForSteve[] = {
			"Muss", "G_Cat", "G_Omg", "Racoon", "Notch",
			"BGM", "베배", "DummyData", "newbigbird", "웡",
			"skyblue_6_",
		};

	public static final String namesForAlex[] = {
			"J",
		};

	public static void setRandomCustomName(BaseAIEntity entity)
	{
		final int CUSTOM_NAME_CHANCE = 10;
		boolean giveCustomName = ((int)(Math.random() * (CUSTOM_NAME_CHANCE - 1)) == 0)? true : false;
		String entityName;

		if(giveCustomName)
		{
			if(entity.isSteve())
				entityName = namesForSteve[(int)(Math.random() * namesForSteve.length)];
			else
				entityName = namesForAlex[(int)(Math.random() * namesForAlex.length)];

			entity.setCustomNameVisible(true);
		}
		else
		{
			if(entity.isSteve())
				entityName = "Steve";
			else
				entityName = "Alex";

			entity.setCustomNameVisible(false);
		}

		entity.setCustomName(ITextComponent.func_244388_a(entityName));
	}
}
