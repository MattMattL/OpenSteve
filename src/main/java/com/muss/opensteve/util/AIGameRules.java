package com.muss.opensteve.util;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.ArrayList;
import java.util.List;

public class AIGameRules
{
	public static List<Rule> gameRuleList = new ArrayList<>();

	public static final Rule<Boolean> KEEP_INVENTORY = register("keepInventory", BoolType.setDefault(false));

	public static Rule register(String literal, DataType defaultValue)
	{
		Rule newRule = new Rule(literal, defaultValue);
		gameRuleList.add(newRule);

		return newRule;
	}

	public abstract static class DataType<T>
	{
		public T value;
		public abstract ArgumentBuilder<CommandSource, ?> registerArguments(ArgumentBuilder arguments);
	}

	public static class BoolType extends DataType<Boolean>
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
		public ArgumentBuilder<CommandSource, ?> registerArguments(ArgumentBuilder arguments)
		{
			return arguments
					.then(Commands.literal("true").executes(source -> { return this.setValueTo(true); }))
					.then(Commands.literal("false").executes(source -> { return this.setValueTo(false); }));
		}

		private int setValueTo(boolean value)
		{
			this.value = value;
			return 1;
		}
	}

	public static class DoubleType extends DataType<Double>
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
		public ArgumentBuilder<CommandSource, ?> registerArguments(ArgumentBuilder arguments)
		{
			return arguments.then(Commands.argument("", DoubleArgumentType.doubleArg()));
		}

		private int setValueTo(double value)
		{
			return 1;
		}
	}

	public static class Rule<T>
	{
		public final String literal;
		public DataType dataType;

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
			return (T)(this.dataType.value);
		}

		public ArgumentBuilder<CommandSource, ?> registerArguments(ArgumentBuilder arguments)
		{
			return this.dataType.registerArguments(arguments);
		}
	}

	public static ArgumentBuilder<CommandSource, ?> registerCommands(ArgumentBuilder arguments)
	{
		for(Rule rule : gameRuleList)
		{
			ArgumentBuilder<CommandSource, ?> subArguments = Commands.literal(rule.getLiteral());

			rule.registerArguments(subArguments);
//			arguments.then(Commands.literal(rule.getLiteral()).then(subArguments));
			arguments.then(subArguments);
		}

		return arguments;
	}
}
