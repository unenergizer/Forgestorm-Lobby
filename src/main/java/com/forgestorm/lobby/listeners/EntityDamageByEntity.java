package com.forgestorm.lobby.listeners;

import com.forgestorm.lobby.Lobby;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@AllArgsConstructor
public class EntityDamageByEntity implements Listener {

	private final Lobby plugin;

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

		// Update hp under name tag
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			//Prevent NPC's from getting updates.
			if (player.hasMetadata("NPC")) return;

			// Update the players health under their name.
            int health = (int) (player.getHealth() - event.getDamage());
			plugin.getSpigotCore().getScoreboardManager().updatePlayerHP(player, health);
		}
	}
}
