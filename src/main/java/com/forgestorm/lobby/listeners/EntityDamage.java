package com.forgestorm.lobby.listeners;

import com.forgestorm.lobby.Lobby;
import com.forgestorm.spigotcore.constants.CommonSounds;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class EntityDamage implements Listener {

    private final Lobby plugin;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();

            // Update the players health under their name.
            plugin.getSpigotCore().getScoreboardManager().updatePlayerHP(player);

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
                return;
            }

            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                // Cancel annoying void damage.
                event.setCancelled(true);

                // Wait one tick and respawn the player..
                new BukkitRunnable() {
                    public void run() {

                        player.setFallDistance(0F);
                        CommonSounds.ACTION_FAILED.playSound(player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 4 * 20, 100));

                        // If the player is in the main world, tp them to spawn.
                        if (Bukkit.getWorlds().get(0).equals(player.getWorld())) {
                            // Teleport the player.
                            if (plugin.getSpigotCore().getProfileManager().getProfile(player).isTutorialFinished()) {
                                plugin.getTeleports().teleportSpawn(player);
                            } else {
                                plugin.getTeleports().teleportTutorial(player);
                            }
                        } else {
                            // If the player was in a realm, make them leave the realm.
                            plugin.getSpigotCore().getRealmManager().playerExitRealm(player);
                        }

                        cancel();
                    }
                }.runTaskLater(plugin, 1L);
            }
        }
    }
}
