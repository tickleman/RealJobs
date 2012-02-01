package fr.crafter.tickleman.realjobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import fr.crafter.tickleman.realplugin.RealFileTools;

//###################################################################################### PlayerJobs
public class PlayerJobs
{

	private Set<Job> jobs = new HashSet<Job>();

	//---------------------------------------------------------------------------------------- addJob
	public boolean addJob(Job job)
	{
		if (job != null) {
			jobs.add(job);
			return true;
		} else {
			return false;
		}
	}

	//--------------------------------------------------------------------------------------- getJobs
	public Set<Job> getJobs()
	{
		return jobs;
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
					jobs.add(plugin.getJob(buffer));
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
			for (Job job : jobs) {
				writer.write(job.getName() + "\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------- setJob
	public boolean setJob(Job job)
	{
		jobs.clear();
		if (job != null) {
			jobs.add(job);
			return true;
		} else {
			return false;
		}
	}

}
