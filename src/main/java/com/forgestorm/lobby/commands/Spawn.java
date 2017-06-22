package com.forgestorm.lobby.commands;

import com.forgestorm.lobby.Lobby;
import com.forgestorm.spigotcore.constants.CommonSounds;
import com.forgestorm.spigotcore.database.PlayerProfileData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Spawn extends BukkitRunnable implements CommandExecutor, Listener {

    private final Lobby plugin;
    private final int maxWaitTime = 7;
    private Queue<PlayerTeleport> playerTeleports = new ConcurrentLinkedQueue<>();

    public Spawn (Lobby plugin) {
        this.plugin = plugin;

        PluginManager pm = Bukkit.getPluginManager();

        // Registering Bukkit Events
        pm.registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerProfileData profile = plugin.getProfileManager().getProfile(player);

            if (!profile.isInTutorial()) {
                if (profile.isAdmin() || profile.isModerator()) {
                    // Admins have no wait time for
                    plugin.getTeleports().teleportSpawn(player);
                    profile.setTutorialFinished(true);
                } else {
                    // Wait 7 seconds. Stop TP if move or if in combat.
                    if (!containsPlayer(player)) {
                        player.sendMessage(ChatColor.GOLD + "Please wait 7 seconds before you can teleport.");
                        playerTeleports.add(new PlayerTeleport(player));
                    } else {
                        player.sendMessage(ChatColor.RED + "You have already started teleporting...");
                        CommonSounds.ACTION_FAILED.playSound(player);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void run() {

        Iterator<PlayerTeleport> iterator = playerTeleports.iterator();
        while (iterator.hasNext()) {
            PlayerTeleport playerTeleport = iterator.next();
            int timeLeft = playerTeleport.getTimeLeft() + 1;
            Player player = playerTeleport.getPlayer();

            if (timeLeft >= maxWaitTime) {
                iterator.remove();
                plugin.getTeleports().teleportSpawn(player);
                player.sendMessage(ChatColor.GREEN + "You have been teleported.");
                CommonSounds.ACTION_SUCCESS.playSound(player);
            } else {
                playerTeleport.setTimeLeft(timeLeft);
                player.sendMessage(ChatColor.GOLD + "Teleporting in " + (maxWaitTime - timeLeft) + " seconds.");
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();

            if (containsPlayer(player)) {
                playerTeleports.remove();
                player.sendMessage(ChatColor.RED + "Teleport canceled because you took damage.");
                CommonSounds.ACTION_FAILED.playSound(player);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (containsPlayer(player)) {
            double moveX = event.getFrom().getX();
            double moveToX = event.getTo().getX();

            double moveY = event.getFrom().getY();
            double moveToY = event.getTo().getY();

            double moveZ = event.getFrom().getZ();
            double moveToZ = event.getTo().getZ();

            //If the teleport has started, then let the player look around, but not jump, fly, walk, or run.
            if (moveX != moveToX || moveY != moveToY || moveZ != moveToZ) {
                playerTeleports.remove();
                player.sendMessage(ChatColor.RED + "Teleport canceled because you moved.");
                CommonSounds.ACTION_FAILED.playSound(player);
            }
        }
    }

    private boolean containsPlayer(Player player) {
        for (PlayerTeleport playerTeleport : playerTeleports) {
            if (playerTeleport.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    @Getter
    @Setter
    private class PlayerTeleport {
        private final Player player;
        private int timeLeft = 0;
        private PlayerTeleport(Player player) {this.player = player;}
    }
}
