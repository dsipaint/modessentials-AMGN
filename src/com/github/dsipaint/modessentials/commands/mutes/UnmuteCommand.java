package com.github.dsipaint.modessentials.commands.mutes;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UnmuteCommand extends Command
{
	public UnmuteCommand(Plugin plugin)
	{
		super(plugin, "unmute");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()) && this.hasPermission(e.getMember()))
		{
			//^unmute
			if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()) && this.hasPermission(e.getMember()))
			{
				if(args.length == 1)
				{
					e.getChannel().sendMessage("Please specify a user to unmute").queue();
					return;
				}
				
				//^unmute {id}
				if(args[1].matches("\\d{18}"))
				{
					//if user is in server
					if(e.getGuild().getMemberById(args[1]) != null)
					{
						String name = e.getGuild().getMemberById(args[1]).getUser().getAsTag();
						
						if(!MuteTask.memberIsMuted(e.getMember()))
						{
							e.getChannel().sendMessage("Member is already unmuted!").queue();
							return;
						}
						
						MuteTask.unmute(e.getGuild().getMemberById(args[1]));
						e.getChannel().sendMessage("Unmuted user " + name).queue();
						GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " unmuted "
								+ name);
					}
					else
						e.getChannel().sendMessage("User is not in server").queue();
					
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
						
						if(MuteTask.memberIsMuted(e.getMember()))
						{
							e.getChannel().sendMessage("Member is already unmuted!").queue();
							return;
						}
						
						MuteTask.unmute(e.getGuild().getMemberById(id));
						e.getChannel().sendMessage("Unmuted user " + name).queue();
						GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " unmuted "
								+ name);
					}
					else
						e.getChannel().sendMessage("User is not in server").queue();
					
					return;
				}
			}
		}
	}
}
