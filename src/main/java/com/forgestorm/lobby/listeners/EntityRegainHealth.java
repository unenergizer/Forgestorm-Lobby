package com.forgestorm.lobby.listeners;

import com.forgestorm.lobby.Lobby;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

@AllArgsConstructor
public class EntityRegainHealth implements Listener {

	private final Lobby plugin;

	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {

		if (event.getEntity() instanceof Player) {
		    Player player = (Player) event.getEntity();
			// Update the players health under their name.
            int health = (int) (player.getHealth() + event.getAmount());
            plugin.getSpigotCore().getScoreboardManager().updatePlayerHP(player, health);
		}
	}
}
