package fr.crafter.tickleman.realjobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.crafter.tickleman.realstats.RealPlayerStats;

//############################################################################################# Job
public class Job
{

	private String name;
	private Map<Integer, List<Integer>> xp = new HashMap<Integer, List<Integer>>();

	//------------------------------------------------------------------------------------------- Job
	public Job(String name, Object jobYmlObject)
	{
		this.name = name;
		@SuppressWarnings("unchecked")
		Map<String, Object> jobYml = (Map<String, Object>)jobYmlObject;
		if (jobYml.containsKey("xp")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> xpYml = (Map<String, Object>)jobYml.get("xp");
			for (String xpActionName : xpYml.keySet()) {
				int action = RealPlayerStats.actionFromString(xpActionName);
				if (action > 0) {
					@SuppressWarnings("unchecked")
					List<Integer> xpValues = (List<Integer>)xpYml.get(xpActionName);
					xp.put(action, xpValues);
				}
			}
		}
	}

	//--------------------------------------------------------------------------------------- getName
	public String getName()
	{
		return name;
	}

	//----------------------------------------------------------------------------------------- getXp
	public long getXp(RealJobsPlugin plugin, String playerName)
	{
		long totalXp = 0;
		RealPlayerStats stats = plugin.getRealStatsPlugin().getPlayerStats(playerName);
		for (Integer action : xp.keySet()) {
			for (Integer xpValue : xp.get(action)) {
				totalXp += stats.getXp(action, xpValue);
			}
		}
		return totalXp;
	}

	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString()
	{
		String string = name + " :";
		for (Integer action : xp.keySet()) {
			string += " [" + action + "]";
			for (Integer xpValue : xp.get(action)) {
				string += " " + xpValue;
			}
		}
		return string;
	}

}
