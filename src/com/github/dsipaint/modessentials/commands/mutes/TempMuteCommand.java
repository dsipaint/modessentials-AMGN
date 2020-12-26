package com.github.dsipaint.modessentials.commands.mutes;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class TempMuteCommand extends Command
{
	public TempMuteCommand(Plugin plugin)
	{
		super(plugin, "tempmute");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^tempmute
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()) && this.hasPermission(e.getMember()))
		{
			if(args.length == 1)
			{
				e.getChannel().sendMessage("Please specify a user to mute").queue();
				return;
			}
			
			if(args.length == 2)
			{
				e.getChannel().sendMessage("Please specify a time to tempute for").queue();
				return;
			}
			
			//^tempmute {id}
			if(args[1].matches("\\d{18}"))
			{
				//if user is in server
				if(e.getGuild().getMemberById(args[1]) != null)
				{
					String name = e.getGuild().getMemberById(args[1]).getUser().getAsTag();
					
					if(MuteTask.memberIsMuted(e.getMember()))
					{
						e.getChannel().sendMessage("Member is already muted!").queue();
						return;
					}
					
					if(args[2].matches("\\d+(s|m|h|d)"))
					{
						MuteTask.mute(e.getGuild().getMemberById(args[1]));
						e.getChannel().sendMessage("Muted user " + name).queue();
						GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " muted "
								+ name);
					}
					else
					{
						e.getChannel().sendMessage("Invalid time").queue();
						return;
					}
					
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
						e.getChannel().sendMessage("Member is already muted!").queue();
						return;
					}
					
					if(args[2].matches("\\d+(s|m|h|d)"))
					{
						MuteTask.mute(e.getGuild().getMemberById(id));
						e.getChannel().sendMessage("Muted user " + name).queue();
						GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " muted "
								+ name);
					}
					else
					{
						e.getChannel().sendMessage("Invalid time").queue();
						return;
					}
				}
				else
					e.getChannel().sendMessage("User is not in server").queue();
				
				return;
			}
		}
	}
}
