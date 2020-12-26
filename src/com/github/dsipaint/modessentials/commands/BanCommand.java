package com.github.dsipaint.modessentials.commands;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BanCommand extends Command
{
	public BanCommand(Plugin plugin)
	{
		super(plugin, "ban");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^ban
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()) && this.hasPermission(e.getMember()))
		{
			if(args.length == 1)
			{
				e.getChannel().sendMessage("Please specify a user to ban").queue();
				return;
			}
			
			//^ban {id}
			if(args[1].matches("\\d{18}"))
			{
				//if user is in server
				if(e.getGuild().getMemberById(args[1]) != null)
				{
					String name = e.getGuild().getMemberById(args[1]).getUser().getAsTag();
					//string formatting
					String reason = "";
					for(int i = 1; i < args.length; i++)
						reason += " " + args[i];
					
					reason = reason.substring(1);
					
					e.getGuild().ban(args[1], 0).queue();
					e.getChannel().sendMessage("Banned user " + name + " for " + reason).queue();
					GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " banned "
							+ name + " for " + reason);
				}
				else
				{
					e.getGuild().retrieveBanById(args[1]).queue(ban ->
					{
						//if user is already banned
						if(ban != null)
							e.getChannel().sendMessage("User is already banned").queue();
						else
							e.getChannel().sendMessage("User is not in server").queue();
					});
				}
				
				return;
			}
			
			//tag a user
			if(args[1].matches("<@!\\d{18}>"))
			{
				String id = args[1].substring(3, 21);
				
				//if user is in server
				if(e.getGuild().getMemberById(id) != null)
				{
					String name = e.getGuild().getMemberById(id).getUser().getAsTag();
					//string formatting
					String reason = "";
					for(int i = 1; i < args.length; i++)
						reason += " " + args[i];
					
					reason = reason.substring(1);
					
					e.getGuild().ban(id, 0).queue();
					e.getChannel().sendMessage("Banned user " + name + " for " + reason).queue();
					GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " banned "
							+ name + " for " + reason);
				}
				else
				{
					e.getGuild().retrieveBanById(id).queue(ban ->
					{
						//if user is already banned
						if(ban != null)
							e.getChannel().sendMessage("User is already banned").queue();
						else
							e.getChannel().sendMessage("User is not in server").queue();
					});
				}
				
				return;
			}
		}
	}
}
