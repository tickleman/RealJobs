package fr.crafter.tickleman.realjobs;

import org.bukkit.entity.Player;

//####################################################################################### PlayerJob
public class PlayerJob
{

	//------------------------------------------------------------------------------------- PlayerJob
	public PlayerJob(Player player)
	{
		
	}

	//---------------------------------------------------------------------------------------- create
	public static PlayerJob create(Player player)
	{
		return new PlayerJob(player);
	}

	//----------------------------------------------------------------------------------------- getXp
	public Integer getXp()
	{
		return 0;
	}

}
