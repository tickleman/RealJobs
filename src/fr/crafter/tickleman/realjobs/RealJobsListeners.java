package fr.crafter.tickleman.realjobs;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

//############################################################################### RealJobsListeners
public class RealJobsListeners implements Listener
{

	RealJobsPlugin plugin;

	//----------------------------------------------------------------------------- RealJobsListeners
	public RealJobsListeners(RealJobsPlugin plugin)
	{
		this.plugin = plugin;
	}

	//---------------------------------------------------------------------------- onBlockDamageEvent
	// @EventHandler
	public void onBlockDamageEvent(BlockDamageEvent event)
	{
		Player player = event.getPlayer();
		if (player instanceof Player) {
			String playerName = player.getName();
			Set<Job> jobs = plugin.getPlayerJobs(playerName).getJobs();
			for (Job job : jobs) {
				ItemStack itemInHand = player.getItemInHand();
				if (
					(
						((itemInHand == null) && job.hasTool(Material.AIR))
						|| (itemInHand != null) && job.hasTool(itemInHand.getType())
					) && job.hasTargetMaterial(event.getBlock().getType())
				) {
					player.sendMessage("++ job effect for " + job.getName());
				}
			}
		}
	}

	//---------------------------------------------------------------------------------- onPlayerQuit
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		String playerName = event.getPlayer().getName();
		plugin.getPlayerJobs(playerName).save(plugin, playerName);
	}

}
