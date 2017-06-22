package com.forgestorm.lobby.listeners;

import com.forgestorm.lobby.Lobby;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@AllArgsConstructor
public class FoodLevelChange implements Listener {

	private final Lobby plugin;
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        // Don't let peoples food level change in the tutorial.
        if (!plugin.getSpigotCore().getProfileManager().getProfile(player).isTutorialFinished()) {
            event.setCancelled(true);
            player.setFoodLevel(20);
        }
	}
}
