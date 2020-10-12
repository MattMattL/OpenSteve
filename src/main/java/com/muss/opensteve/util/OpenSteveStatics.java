package com.muss.opensteve.util;

import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class OpenSteveStatics
{

	/* A List of Server-Side AIs */

	public static List<BaseAIEntity> aiEntityList = new ArrayList<>();


	/* Random Entity Names */

	public static final String namesForSteve[] = {
			"M (TM)", "G_Cat", "G_Omg", "Racoon", "BGM",
			"Mr O", "DummyData", "newbigbird", "Mr Song", "skyblue_6_",
		};

	public static final String namesForAlex[] = {
			"J (TM)",
		};

	public static void setRandomName(BaseAIEntity entity)
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


	/* String Formatter */

	public static class MultiLines
	{
		private String formatted;

		public MultiLines(@Nonnull String format, Object... args)
		{
			this.formatted = String.format(format + "\n", args);
		}

		public MultiLines newline(String format, Object... args)
		{
			this.formatted = String.format(this.formatted + format + "\n", args);

			return this;
		}

		public MultiLines endline(String format, Object... args)
		{
			this.formatted = String.format(this.formatted + format, args);

			return this;
		}

		public String getString()
		{
			return this.formatted;
		}
	}


	/* Debugging Helpers */

	public static void print(String message)
	{
		System.out.printf("[OpenSteve] %s\n", message);
	}

	public static void print(String method, String message)
	{
		System.out.printf("[OpenSteve] [%s] %s\n", method, message);
	}
}
