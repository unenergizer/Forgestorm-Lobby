package com.forgestorm.lobby.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Teleports {

	private static final Location SPAWN_LOCATION = new Location(Bukkit.getWorlds().get(0), 0.5, 108, -24.5);
	private static final Location TUTORIAL_LOCATION = new Location(Bukkit.getWorlds().get(0), -522.5, 96, -64.5);
	private List<Player> players = new ArrayList<>();

	public void teleportSpawn(Player player) {

		if (!players.contains(player)) {
			players.add(player);

			//Teleport the player.
			player.teleport(SPAWN_LOCATION);

			//Remove them from the array.
			players.remove(player);
		}
	}

	public void teleportTutorial(Player player) {

		if (!players.contains(player)) {
			players.add(player);

			//Teleport the player.
			player.teleport(TUTORIAL_LOCATION);

			//Remove them from the array.
			players.remove(player);
		}
	}
}
