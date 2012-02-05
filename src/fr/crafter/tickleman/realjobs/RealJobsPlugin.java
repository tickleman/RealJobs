package fr.crafter.tickleman.realjobs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;

import fr.crafter.tickleman.realplugin.RealPlugin;
import fr.crafter.tickleman.realstats.RealStatsPlugin;

//################################################################################## RealJobsPlugin
public class RealJobsPlugin extends RealPlugin
{

	private Jobs jobs;
	private Map<String, PlayerJobs> playersJobs = new HashMap<String, PlayerJobs>();
	private RealStatsPlugin realStatsPlugin = null;

	//---------------------------------------------------------------------------------------- getJob
	public Job getJob(String jobName)
	{
		return jobs.getJob(jobName);
	}

	//--------------------------------------------------------------------------------- getPlayerJobs
	public PlayerJobs getPlayerJobs(String playerName)
	{
		playerName = playerName.toLowerCase();
		PlayerJobs playerJobs = playersJobs.get(playerName);
		if (playerJobs == null) {
			playerJobs = new PlayerJobs(this, playerName);
			playersJobs.put(playerName, playerJobs);
		}
		return playerJobs;
	}

	//---------------------------------------------------------------------------- getRealStatsPlugin
	public RealStatsPlugin getRealStatsPlugin()
	{
		if (realStatsPlugin == null) {
			realStatsPlugin = (RealStatsPlugin)getServer().getPluginManager().getPlugin("RealStats");
		}
		return realStatsPlugin;
	}

	//------------------------------------------------------------------------------------- onCommand
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("jobxp")) {
			RealStatsPlugin realStats = getRealStatsPlugin();
			if (realStats == null) {
				sender.sendMessage(tr("You need RealStats plugin to make this work"));
			} else {
				if (args.length == 1) {
					if (sender.isOp()) {
						for (Job job : getPlayerJobs(args[0]).getJobs()) {
							sender.sendMessage(
								tr("+player +job : +xp xp")
								.replace("+player", args[0])
								.replace("+xp", "" + job.getXp(this, args[0]))
								.replace("+job", tr(job.getName()))
							);
						}
					}
				} else if (!(sender instanceof Player)) {
					sender.sendMessage(tr("Only players can /jobxp"));
				} else {
					Player player = (Player)sender;
					for (Job job : getPlayerJobs(player.getName()).getJobs()) {
						sender.sendMessage(
							tr("+job : +xp xp")
							.replace("+xp", "" + job.getXp(this, ((Player)sender).getName()))
							.replace("+job", tr(job.getName()))
						);
					}
				}
			}
		} else if (command.getName().equalsIgnoreCase("jobreload") && sender.isOp()) {
			jobs = new Jobs(this).loadYamlFile();
			playersJobs = new HashMap<String, PlayerJobs>();
			realStatsPlugin = null;
			sender.sendMessage(tr("Reloaded jobs configuration"));
		} else if (
			command.getName().equalsIgnoreCase("addjob")
			&& (args.length >= 2)
			&& sender.isOp()
		) {
			for (int i = 1; i < args.length; i++) {
				Job job = getJob(args[i]);
				if (getPlayerJobs(args[0]).addJob(job, job.getXp(this, args[0]))) {
					sender.sendMessage(tr("+player is now +job").replace("+job", tr(args[i])).replace("+player", args[0]));
				} else {
					sender.sendMessage(tr("Unknown job +job").replace("+job", args[i]));
				}
			}
			getPlayerJobs(args[0]).save(this, args[0]);
		} else if (
			command.getName().equalsIgnoreCase("remjob")
			&& (args.length >= 2)
			&& sender.isOp()
		) {
			for (int i = 1; i < args.length; i++) {
				if (getPlayerJobs(args[0]).remJob(getJob(args[i]))) {
					sender.sendMessage(tr("+player job +job removed").replace("+job", tr(args[i])).replace("+player", args[0]));
				} else {
					sender.sendMessage(tr("Unknown job +job").replace("+job", args[i]));
				}
			}
			getPlayerJobs(args[0]).save(this, args[0]);
		} else if (
			command.getName().equalsIgnoreCase("setjob")
			&& (args.length >= 2)
			&& sender.isOp()
		) {
			Job job = getJob(args[1]);
			if (getPlayerJobs(args[0]).setJob(job, job.getXp(this, args[0]))) {
				sender.sendMessage(tr("+player is now +job").replace("+job", tr(args[1])).replace("+player", args[0]));
			} else {
				sender.sendMessage(tr("Unknown job +job").replace("+job", args[1]));
			}
			if (args.length >= 3) {
				for (int i = 2; i < args.length; i++) {
					job = getJob(args[i]);
					if (getPlayerJobs(args[0]).addJob(job, job.getXp(this, args[0]))) {
						sender.sendMessage(tr("+player is now +job").replace("+job", tr(args[i])).replace("+player", args[0]));
					} else {
						sender.sendMessage(tr("Unknown job +job").replace("+job", args[i]));
					}
				}
			}
			getPlayerJobs(args[0]).save(this, args[0]);
		} else {
			return false;
		}
		return true;
	}

	//------------------------------------------------------------------------------------- onDisable
	@Override
	public void onDisable()
	{
		realStatsPlugin = null;
		jobs = null;
		super.onDisable();
	}

	//-------------------------------------------------------------------------------------- onEnable
	@Override
	public void onEnable()
	{
		super.onEnable();
		jobs = new Jobs(this).loadYamlFile();
		RealJobsPlayerListener playerListener  = new RealJobsPlayerListener(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Lowest, this);
	}

}
