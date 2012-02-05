package fr.crafter.tickleman.realjobs;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

//########################################################################## RealJobsPlayerListener
public class RealJobsPlayerListener extends PlayerListener
{

	RealJobsPlugin plugin;

	//------------------------------------------------------------------------ RealJobsPlayerListener
	public RealJobsPlayerListener(RealJobsPlugin plugin)
	{
		this.plugin = plugin;
	}

	//---------------------------------------------------------------------------------- onPlayerQuit
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		String playerName = event.getPlayer().getName();
		plugin.getPlayerJobs(playerName).save(plugin, playerName);
	}

}
