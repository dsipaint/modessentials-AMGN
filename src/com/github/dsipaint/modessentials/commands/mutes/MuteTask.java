package com.github.dsipaint.modessentials.commands.mutes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.dsipaint.AMGN.entities.GuildNetwork;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class MuteTask
{
	public static void mute(Member m)
	{
		getMuteRoles().forEach(role -> 
		{
			if(m.getGuild().getRoleById(role)!= null) //if role exists in server (valid mute role)
			{
				m.getGuild().addRoleToMember(m, m.getGuild().getRoleById(role)).queue();
				return;
			}
		});
		
		makeMuteRole(m.getGuild()); //register a new muted role if one did not exist
		mute(m); //try to mute them again
	}
	
	public static void unmute(Member m)
	{
		getMuteRoles().forEach(role -> 
		{
			if(m.getGuild().getRoleById(role)!= null) //if role exists in server (valid mute role)
			{
				m.getGuild().removeRoleFromMember(m, m.getGuild().getRoleById(role)).queue();
				return;
			}
		});
	}
	
	public static void tempmute(Member m, String time)
	{
		String unit = time.replaceAll("\\d", "");
		String num = time.replaceAll("[^\\d+]", "");
		
		TimeUnit formal_unit;
		switch(unit)
		{
			case "s":
				formal_unit = TimeUnit.SECONDS;
				break;
				
			case "m":
				formal_unit = TimeUnit.MINUTES;
				break;
				
			case "h":
				formal_unit = TimeUnit.HOURS;
				break;
	
			case "d":
				formal_unit = TimeUnit.DAYS;
				break;
				
			default:
				return;
		}
		
		int formal_num;
		try
		{
			formal_num = Integer.parseInt(num);
		}
		catch(Exception e)
		{
			return;
		}
		
		//actually mute them here (stole and edited code from mute())
		getMuteRoles().forEach(role -> 
		{
			if(m.getGuild().getRoleById(role)!= null) //if role exists in server (valid mute role)
			{
				m.getGuild().addRoleToMember(m, m.getGuild().getRoleById(role)).queue(); //mute them
				/*
				 * can make more efficient by taking the generated scheduledfuture object, storing it in a
				 * list, and when the unmute method is used, have it check the list and cancel any relevant
				 * scheduled unmutes
				 */
				m.getGuild().removeRoleFromMember(m, m.getGuild().getRoleById(role)).queueAfter(formal_num, formal_unit); //schedule unmute
				return;
			}
		});
		
		makeMuteRole(m.getGuild()); //register a new muted role if one did not exist
		tempmute(m, time); //try to mute them again
	}
	
	public static List<String> getMuteRoles()
	{
		List<String> roles = new ArrayList<String>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File("./modessentials/muteroles.dat")));
			String line = "";
			
			while((line = br.readLine()) != null)
			{
				roles.add(line);
			}
			
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return (List<String>) roles;
	}
	
	public static void makeMuteRole(Guild g)
	{
		g.createRole().queue(role ->
		{
			role.getManager()
			.setName("muted")
			.setColor(11447982) //grey
			.revokePermissions(Permission.MESSAGE_WRITE, Permission.VOICE_CONNECT)
			.queue();
			
			List<String> roles = getMuteRoles();
			roles.add(role.getId());
			//write roles
			try
			{
				PrintWriter pw = new PrintWriter(new FileWriter(new File("./modessentials/muteroles.dat")));
				roles.forEach(id ->
				{
					pw.println(id);
				});
				
				pw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			GuildNetwork.sendToModlogs(g.getIdLong(), "Created new muted role.");
		});
	}
	
	public static boolean memberIsMuted(Member m)
	{
		for(String id : getMuteRoles())
		{
			if(m.getGuild().getRoles().contains(m.getGuild().getRoleById(id)))
				return true;
		}
		
		return false;
	}
}
