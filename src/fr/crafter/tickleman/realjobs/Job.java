package fr.crafter.tickleman.realjobs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

import fr.crafter.tickleman.realstats.RealPlayerStats;

//############################################################################################# Job
public class Job
{

	private String name;

	private Set<Integer> targets = new HashSet<Integer>();

	private Set<Integer> tools = new HashSet<Integer>();

	private Map<Integer, List<Integer>> xp = new HashMap<Integer, List<Integer>>();

	//------------------------------------------------------------------------------------------- Job
	public Job(String name, Object jobYmlObject)
	{
		this.name = name;
		@SuppressWarnings("unchecked")
		Map<String, Object> jobYml = (Map<String, Object>)jobYmlObject;
		if (jobYml.containsKey("targets")) {
			@SuppressWarnings("unchecked")
			List<Integer> targetValues = (List<Integer>)jobYml.get("targets");
			for (Integer targetValue : targetValues) {
				targets.add(targetValue);
			}
		}
		if (jobYml.containsKey("tools")) {
			@SuppressWarnings("unchecked")
			List<Integer> toolValues = (List<Integer>)jobYml.get("tools");
			for (Integer toolValue : toolValues) {
				tools.add(toolValue);
			}
		}
		if (jobYml.containsKey("xp")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> xpYml = (Map<String, Object>)jobYml.get("xp");
			for (String xpActionName : xpYml.keySet()) {
				Integer action = RealPlayerStats.actionFromString(xpActionName);
				if (action > 0) {
					@SuppressWarnings("unchecked")
					List<Integer> xpValues = (List<Integer>)xpYml.get(xpActionName);
					xp.put(action, xpValues);
					if (action.equals(RealPlayerStats.BREAK)) {
						for (Integer xpValue : xpValues) {
							targets.add(xpValue);
						}
					}
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

	//----------------------------------------------------------------------------- hasTargetMaterial
	public boolean hasTargetMaterial(Material material)
	{
		return targets.contains(material.getId());
	}

	//------------------------------------------------------------------------------- hasTargetEntity
	public boolean hasTargetEntity(Entity entity)
	{
		return targets.contains(entity.getEntityId());
	}

	//--------------------------------------------------------------------------------------- hasTool
	public boolean hasTool(Material material)
	{
		return tools.contains(material.getId());
	}

	//-------------------------------------------------------------------------------------- toString
	@Override
	public String toString()
	{
		StringBuffer string = new StringBuffer(name + " :");
		for (Integer action : xp.keySet()) {
			string.append(" [").append(action).append("]");
			for (Integer xpValue : xp.get(action)) {
				string.append(" ").append(xpValue.toString());
			}
		}
		string.append("\n   targets =");
		for (Integer target : targets) {
			string.append(" ").append(target.toString());
		}
		return string.toString();
	}

}
