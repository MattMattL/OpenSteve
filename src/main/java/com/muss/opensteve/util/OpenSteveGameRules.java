package com.muss.opensteve.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.muss.opensteve.entity.monster.BaseAIEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class OpenSteveGameRules
{
	public static List<OpenSteveGameRules.Rule> gameRuleList = new ArrayList<>();

	public static final OpenSteveGameRules.Rule<Boolean> KEEP_INVENTORY = register("keepInventory", OpenSteveGameRules.BoolType.setDefault(false), null);
	public static final OpenSteveGameRules.Rule<Double> TEST_DOUBLE = register("testDouble", OpenSteveGameRules.DoubleType.setDefault(0.5D), null);
	public static final OpenSteveGameRules.Rule<Integer> COMMAND_TEST = register("commandTest", OpenSteveGameRules.NullType.setDefault(), source ->
	{
		return 1;
	});


	public static OpenSteveGameRules.Rule register(String literal, OpenSteveGameRules.DataType defaultValue, Command<CommandSource> command)
	{
		OpenSteveGameRules.Rule newRule = new OpenSteveGameRules.Rule(literal, defaultValue, command);
		OpenSteveGameRules.gameRuleList.add(newRule);

		return newRule;
	}

	private abstract static class DataType<T>
	{
		public T value;
		public abstract ArgumentBuilder<CommandSource, ?> registerType(ArgumentBuilder arguments, String rule, final Command<CommandSource> command);
	}

	public static class BoolType<T> extends OpenSteveGameRules.DataType<Boolean>
	{
		public BoolType(boolean valueIn)
		{
			this.value = valueIn;
		}

		public static OpenSteveGameRules.DataType setDefault(boolean valueIn)
		{
			return new BoolType(valueIn);
		}

		@Override
		public ArgumentBuilder<CommandSource, ?> registerType(ArgumentBuilder arguments, String rule, final Command<CommandSource> command)
		{
			return arguments
					.then(Commands.argument("value::bool", BoolArgumentType.bool())
							.executes(source -> { return this.setValueTo(source.getSource(), rule, BoolArgumentType.getBool(source, "value::bool")); }))
					.then(Commands.argument("value::bool", BoolArgumentType.bool())
							.executes(source -> { return this.setValueTo(source.getSource(), rule, BoolArgumentType.getBool(source, "value::bool")); }));
		}

		private int setValueTo(CommandSource source, String rule, boolean valueIn)
		{
			this.value = valueIn;
			source.sendFeedback(new TranslationTextComponent("commands.opensteve.gamemode.setValue.bool", rule, (valueIn) ? "true" : "false"), true);

			return 1;
		}
	}

	public static class DoubleType<T> extends OpenSteveGameRules.DataType<Double>
	{
		public DoubleType(double valueIn)
		{
			this.value = valueIn;
		}

		public static OpenSteveGameRules.DataType setDefault(double valueIn)
		{
			return new DoubleType(valueIn);
		}

		@Override
		public ArgumentBuilder<CommandSource, ?> registerType(ArgumentBuilder arguments, String rule, final Command<CommandSource> command)
		{
			return arguments
					.then(Commands.argument("value::double", DoubleArgumentType.doubleArg())
							.executes(source -> { return this.setValueTo(source.getSource(), rule, DoubleArgumentType.getDouble(source, "value::double")); }));
		}

		private int setValueTo(CommandSource source, String rule, double valueIn)
		{
			this.value = valueIn;
			source.sendFeedback(new TranslationTextComponent("commands.opensteve.gamemode.setValue.double", rule, valueIn), true);

			return 1;
		}
	}

	public static class IntegerType<T> extends OpenSteveGameRules.DataType<Integer>
	{
		public IntegerType(int valueIn)
		{
			this.value = valueIn;
		}

		public static OpenSteveGameRules.DataType setDefault(int valueIn)
		{
			return new IntegerType(valueIn);
		}

		@Override
		public ArgumentBuilder<CommandSource, ?> registerType(ArgumentBuilder arguments, String rule, final Command<CommandSource> command)
		{
			return arguments
					.then(Commands.argument("value::int", IntegerArgumentType.integer())
							.executes(source -> { return this.setValueTo(source.getSource(), rule, IntegerArgumentType.getInteger(source, "value::int")); }));
		}

		private int setValueTo(CommandSource source, String rule, int valueIn)
		{
			this.value = valueIn;
			source.sendFeedback(new TranslationTextComponent("commands.opensteve.gamemode.setValue.int", rule, valueIn), true);

			return 1;
		}
	}

	public static class NullType<T> extends OpenSteveGameRules.DataType<Integer>
	{
		public NullType()
		{
			this.value = 0;
		}

		public static OpenSteveGameRules.DataType setDefault()
		{
			return new NullType();
		}

		@Override
		public ArgumentBuilder<CommandSource, ?> registerType(ArgumentBuilder arguments, String rule, final Command<CommandSource> command)
		{
			return arguments.executes(command);
		}
	}

	public static class Rule<T>
	{
		public final String literal;
		public OpenSteveGameRules.DataType<T> dataType;
		private final Command<CommandSource> command;

		public Rule(String literalIn, OpenSteveGameRules.DataType valueIn, Command commandIn)
		{
			this.literal = literalIn;
			this.dataType = valueIn;
			this.command = commandIn;
		}

		public String getLiteral()
		{
			return this.literal;
		}

		public T getValue()
		{
			return this.dataType.value;
		}

		public ArgumentBuilder<CommandSource, ?> registerArguments(ArgumentBuilder arguments)
		{
			return this.dataType.registerType(arguments, this.literal, this.command);
		}
	}

	public static ArgumentBuilder<CommandSource, ?> registerCommands(ArgumentBuilder arguments)
	{
		for(OpenSteveGameRules.Rule rule : OpenSteveGameRules.gameRuleList)
		{
			ArgumentBuilder<CommandSource, ?> gameruleCommand = Commands.literal(rule.getLiteral());

			rule.registerArguments(gameruleCommand);
			arguments.then(gameruleCommand);
		}

		return arguments;
	}
}
