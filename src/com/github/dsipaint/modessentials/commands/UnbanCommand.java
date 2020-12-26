package com.github.dsipaint.modessentials.commands;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UnbanCommand extends Command
{
	public UnbanCommand(Plugin plugin)
	{
		super(plugin, "unban");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^unban
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()) && this.hasPermission(e.getMember()))
		{
			if(args.length == 1)
			{
				e.getChannel().sendMessage("Please specify a user to unban").queue();
				return;
			}
			
			//^unban {id}
			if(args[1].matches("\\d{18}"))
			{
				e.getGuild().retrieveBanById(args[1]).queue(ban ->
				{
					//if user is banned
					if(ban != null)
					{
						e.getGuild().unban(args[1]).queue(); //unban
						e.getChannel().sendMessage("User " + ban.getUser().getAsTag() + " was unbanned.").queue();
						GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " unbanned "
								+ ban.getUser().getAsTag());
					}
					else
						e.getChannel().sendMessage("User is not banned!").queue();
				});
			}
			
			//tag a user
			if(args[1].matches("<@!\\d{18}>"))
			{
				String id = args[1].substring(3, 21);
				
				e.getGuild().retrieveBanById(id).queue(ban ->
				{
					//if user is banned
					if(ban != null)
					{
						e.getGuild().unban(id).queue(); //unban
						e.getChannel().sendMessage("User " + ban.getUser().getAsTag() + " was unbanned.").queue();
						GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " unbanned "
								+ ban.getUser().getAsTag());
					}
					else
						e.getChannel().sendMessage("User is not banned!").queue();
				});
				
				return;
			}
		}
	}
}
