package com.github.dsipaint.modessentials;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.modessentials.commands.BanCommand;
import com.github.dsipaint.modessentials.commands.KickCommand;
import com.github.dsipaint.modessentials.commands.UnbanCommand;
import com.github.dsipaint.modessentials.commands.mutes.MuteCommand;
import com.github.dsipaint.modessentials.commands.mutes.UnmuteCommand;

public class Main extends Plugin
{
	/*
	 * potential efficiency improvements to be made when it comes to mute roles
	 * and their handling/existence
	 */
	
	public void onEnable()
	{
		GuildNetwork.registerCommand(new BanCommand(this), this);
		GuildNetwork.registerCommand(new KickCommand(this), this);
		GuildNetwork.registerCommand(new MuteCommand(this), this);
		GuildNetwork.registerCommand(new UnmuteCommand(this), this);
		GuildNetwork.registerCommand(new UnbanCommand(this), this);
	}
	
	public void onDisable()
	{
		
	}
}
