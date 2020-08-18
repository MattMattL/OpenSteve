package com.muss.opensteve.util;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CmdHandler extends CommandBase
{
	public static final String LIST[][] =
	{
		{"help"},
		{"killall"},
		{"spawn", "steve", "alex"},
		{"call"},
		{"namelist", "steve", "alex", "all"}
	};
	
	public static final String USE[] = 
	{
		"help",
		"killall",
		"spawn <steve|alex> [#]",
		"call <name>",
		"namelist <steve|alex|all>"
	};
	
	private CmdExecute cmd = new CmdExecute();
	
	// Substitution for terrible print functions in java //
	private void printf(String str)
	{
		System.out.printf("%s", str);
	}
	
	// Handle command input //
	@Override
	public String getName()
	{
		return "bgmAI";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "/bgmAI <help>";
	}

	@Override
	public void execute(MinecraftServer serverIn, ICommandSender senderIn, String[] args) throws CommandException
	{
		for(int i=0; i<args.length; i++)
			args[i] = args[i].toLowerCase();
		
		this.cmd.cmdExecutionInit(serverIn, senderIn);
		
		//debug
		for(int i=0; i<args.length; i++)
			System.out.printf("[cmd] %2d: /%s/\n", i, args[i]);
		
		// pass variables and execute
		if(args[0].isEmpty())
		{
			this.cmd.Default();
		}
		else if(args[0].matches(LIST[0][0]))
		{
			this.cmd.CommandHelp();
		}
		else if(args[0].matches(LIST[1][0]))
		{
			this.cmd.CommandKillall();
		}
		else if(args[0].matches(LIST[2][0]))
		{
			this.cmd.CommandSpawn(args[1], args[2]);
		}
		else if(args[0].matches(LIST[3][0]))
		{
			this.cmd.CommandCall(args[1], args[2]);
		}
		else if(args[0].matches(LIST[4][0]))
		{
			this.cmd.CommandNamelist(args[1]);
		}
		else
		{
			this.cmd.Default();
		}
	}

}




