package fr.crafter.tickleman.realjobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import fr.crafter.tickleman.realplugin.RealFileTools;

//############################################################################################ Jobs
public class Jobs
{

	private RealJobsPlugin plugin;
	private String fileName = "jobs.yml";
	private HashMap<String, Job> jobs = new HashMap<String, Job>();

	//------------------------------------------------------------------------------------------ Jobs
	public Jobs(RealJobsPlugin plugin)
	{
		this.plugin = plugin;
		fileName = plugin.getDataFolder().getPath() + "/" + fileName;
	}

	//---------------------------------------------------------------------------------------- getJob
	public Job getJob(String jobName)
	{
		return jobs.get(jobName);
	}

	//---------------------------------------------------------------------------------- loadYamlFile
	public Jobs loadYamlFile()
	{
		jobs.clear();
		if (!RealFileTools.fileExists(fileName)) {
			RealFileTools.extractDefaultFile(plugin, fileName);
		}
		try {
	    InputStream input = new FileInputStream(new File(fileName));
	    Yaml yaml = new Yaml(new SafeConstructor());
			@SuppressWarnings("unchecked")
			Map<String, Object> jobsYml = (Map<String, Object>)yaml.load(input);
			for (String jobName : jobsYml.keySet()) {
				if (jobsYml.get(jobName) != null) {
					Job job = new Job(jobName, jobsYml.get(jobName));
					jobs.put(jobName, job);
				}
			}
		} catch (IOException e) {
			plugin.getLog().severe("Could not load " + fileName);
		}
		return this;
	}

	//-------------------------------------------------------------------------------------- toString
	public String toString()
	{
		StringBuffer string = new StringBuffer();
		for (String jobName : jobs.keySet()) {
			string.append(jobs.get(jobName).toString()).append("\n");
		}
		return string.toString();
	}

}
