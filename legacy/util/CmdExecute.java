package com.muss.opensteve.util;

import com.muss.opensteve.entity.util.BgmAIInit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CmdExecute
{
	//private String cmdList[][];
	//private String cmdUse[];
	
	private MinecraftServer server;
	private ICommandSender sender;
	
	CmdHandler cmd;
	BgmAIInit nameTag;
	
	public void cmdExecutionInit(MinecraftServer serverIn, ICommandSender senderIn)
	{
		this.server = serverIn;
		this.sender = senderIn;
		
		//this.cmdList = cmdIn;
		//this.cmdUse = useIn;
	}
	
	// Print helper //
	private void printToConsol(String str)
	{
		this.sender.sendMessage(new TextComponentTranslation("%s", str));
	}
	
	private void printToServer(String str)
	{
		this.server.sendMessage(new TextComponentTranslation("%s", str));
	}
	
	private void printf(String str)
	{
		System.out.printf("%s", str);
	}
	
	// Process custom command input //
	public void CommandHelp()
	{
		// print available commands to consol
		for(int i=0; i<cmd.USE.length; i++)
		{
			this.sender.sendMessage(new TextComponentTranslation("/bgmAI %s", cmd.USE[i]));
		}
	}
	
	public void CommandKillall()
	{
		// kill every bgmAI in the current world
		this.sender.sendMessage(new TextComponentTranslation("[%s] Exterminate!", Ref.NAME));
	}
	
	public void CommandSpawn(String entity, String num) throws NumberInvalidException
	{
		// a command for spawning multiple entity
		int spawnNum = CommandBase.parseInt(num);

		if(entity.toLowerCase().matches(cmd.LIST[2][1])) //steve
		{
			
		}
		else if(entity.toLowerCase().matches(cmd.LIST[2][2])) //alex
		{
			
		}
		else
		{
			this.printToConsol("No such entity registered in " + Ref.NAME);
		}
	}
	
	public void CommandCall(String name, String num) throws NumberInvalidException
	{	
		// spawn an entity with custom name if exists
		
	}
	
	public void CommandNamelist(String kind)
	{
		// print the list of custom name tags
		int flag = 0;

		if(kind.matches(cmd.LIST[4][3]) || kind.matches(cmd.LIST[4][2])) //alex
		{
			printToConsol("<Alex>");
			
			for(int i=0; i<nameTag.arrNameAlex.length; i++)
				this.sender.sendMessage(new TextComponentTranslation(" - %s", nameTag.arrNameAlex[i]));
			
			flag |= 1;
		}
		
		if(kind.matches(cmd.LIST[4][3]) || kind.matches(cmd.LIST[4][1])) //steve
		{
			printToConsol("<Steve>");
			
			for(int i=0; i<nameTag.arrNameSteve.length; i++)
				this.sender.sendMessage(new TextComponentTranslation(" - %s", nameTag.arrNameSteve[i]));
			
			flag |= 1;
		}
		
		if(flag == 0)
		{
			Default();
		}
	}
	
	public void Default()
	{
		printToConsol("/bgmAI <help>");
	}
}









