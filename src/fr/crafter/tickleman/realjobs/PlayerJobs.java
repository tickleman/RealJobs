package fr.crafter.tickleman.realjobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.crafter.tickleman.realplugin.RealFileTools;

//###################################################################################### PlayerJobs
public class PlayerJobs
{

	private Map<Job, Long> jobs = new HashMap<Job, Long>();

	//---------------------------------------------------------------------------------------- addJob
	public boolean addJob(Job job, Long xp)
	{
		if (job != null) {
			jobs.put(job, xp);
			return true;
		} else {
			return false;
		}
	}

	//--------------------------------------------------------------------------------------- getJobs
	public Set<Job> getJobs()
	{
		return jobs.keySet();
	}

	//------------------------------------------------------------------------------------ playerJobs
	public PlayerJobs(RealJobsPlugin plugin, String playerName)
	{
		load(plugin, playerName);
	}

	//------------------------------------------------------------------------------------------ load
	private void load(RealJobsPlugin plugin, String playerName)
	{
		jobs.clear();
		if (RealFileTools.fileExists(
			plugin.getDataFolder().getPath() + "/" + playerName.toLowerCase() + ".txt"
		)) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(
					plugin.getDataFolder().getPath() + "/" + playerName.toLowerCase() + ".txt"
				));
				String buffer;
				while ((buffer = reader.readLine()) != null) {
					String[] data = buffer.split(";");
					Job job = plugin.getJob(data[0]); 
					addJob(job, job.getXp(plugin, playerName));
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//---------------------------------------------------------------------------------------- remJob
	public boolean remJob(Job job)
	{
		if (job != null) {
			jobs.remove(job);
			return true;
		}
		return false;
	}

	//------------------------------------------------------------------------------------------ save
	public void save(RealJobsPlugin plugin, String playerName)
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
				plugin.getDataFolder().getPath() + "/" + playerName.toLowerCase() + ".txt"
			));
			for (Job job : jobs.keySet()) {
				writer.write(job.getName() + ";" + job.getXp(plugin, playerName) + "\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------- setJob
	public boolean setJob(Job job, Long xp)
	{
		jobs.clear();
		if (job != null) {
			addJob(job, xp);
			return true;
		} else {
			return false;
		}
	}

}
