package com.muss.opensteve.util;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class AIGameRules
{
	public static List<Rule> gameRuleList = new ArrayList<>();

	public static final Rule<Boolean> KEEP_INVENTORY = register("keepInventory", BoolType.setDefault(false));
	public static final Rule<Double> TEST_DOUBLE = register("testDouble", DoubleType.setDefault(0.5D));

	public static Rule register(String literal, DataType defaultValue)
	{
		Rule newRule = new Rule(literal, defaultValue);
		gameRuleList.add(newRule);

		return newRule;
	}

	private abstract static class DataType<T>
	{
		public T value;
		public abstract ArgumentBuilder<CommandSource, ?> registerArguments(ArgumentBuilder arguments, String rule);
	}

	private static class BoolType<T> extends DataType<Boolean>
	{
		public BoolType(boolean valueIn)
		{
			this.value = valueIn;
		}

		public static DataType setDefault(boolean valueIn)
		{
			return new BoolType(valueIn);
		}

		@Override
		public ArgumentBuilder<CommandSource, ?> registerArguments(ArgumentBuilder arguments, String rule)
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
			source.sendFeedback(new TranslationTextComponent("commands.opst.gamemode.setValue.bool", rule, (valueIn) ? "true" : "false"), true);

			return 1;
		}
	}

	private static class DoubleType<T> extends DataType<Double>
	{
		public DoubleType(double valueIn)
		{
			this.value = valueIn;
		}

		public static DataType setDefault(double valueIn)
		{
			return new DoubleType(valueIn);
		}

		@Override
		public ArgumentBuilder<CommandSource, ?> registerArguments(ArgumentBuilder arguments, String rule)
		{
			return arguments
					.then(Commands.argument("value::double", DoubleArgumentType.doubleArg())
							.executes(source -> { return this.setValueTo(source.getSource(), rule, DoubleArgumentType.getDouble(source, "value::double")); }));
		}

		private int setValueTo(CommandSource source, String rule, double valueIn)
		{
			this.value = valueIn;
			source.sendFeedback(new TranslationTextComponent("commands.opst.gamemode.setValue.double", rule, valueIn), true);

			return 1;
		}
	}

	public static class Rule<T>
	{
		public final String literal;
		public DataType<T> dataType;

		public Rule(String literalIn, DataType valueIn)
		{
			this.literal = literalIn;
			this.dataType = valueIn;
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
			return this.dataType.registerArguments(arguments, this.literal);
		}
	}

	public static ArgumentBuilder<CommandSource, ?> registerCommands(ArgumentBuilder arguments)
	{
		for(Rule rule : gameRuleList)
		{
			ArgumentBuilder<CommandSource, ?> subArguments = Commands.literal(rule.getLiteral());
			rule.registerArguments(subArguments);
			arguments.then(subArguments);
		}

		return arguments;
	}
}
