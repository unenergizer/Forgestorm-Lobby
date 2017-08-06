package com.forgestorm.lobby.player;

import com.forgestorm.lobby.Lobby;
import com.forgestorm.lobby.constants.LobbyMessages;
import com.forgestorm.lobby.display.PlayerBossBar;
import com.forgestorm.spigotcore.constants.ItemTypes;
import com.forgestorm.spigotcore.database.PlayerProfileData;
import com.forgestorm.spigotcore.database.ProfileLoadedEvent;
import com.forgestorm.spigotcore.player.DoubleJump;
import com.forgestorm.spigotcore.util.item.ItemGenerator;
import com.forgestorm.spigotcore.util.text.CenterChatText;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/*********************************************************************************
 *
 * OWNER: Robert Andrew Brown & Joseph Rugh
 * PROGRAMMER: Robert Andrew Brown & Joseph Rugh
 * PROJECT: forgestorm-lobby
 * DATE: 6/13/2017
 * _______________________________________________________________________________
 *
 * Copyright © 2017 ForgeStorm.com. All Rights Reserved.
 *
 * No part of this project and/or code and/or source code and/or source may be 
 * reproduced, distributed, or transmitted in any form or by any means, 
 * including photocopying, recording, or other electronic or mechanical methods, 
 * without the prior written permission of the owner.
 */

public class PlayerManager implements Listener {

    private final Lobby plugin;
    private final PlayerBossBar playerBossBar;
    private final DoubleJump doubleJump;

    public PlayerManager(Lobby plugin) {
        this.plugin = plugin;
        this.playerBossBar = new PlayerBossBar();
        this.doubleJump = new DoubleJump(plugin.getSpigotCore());

        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Add all online players. Good for server reloads.
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayer(player);
        }
    }

    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removePlayer(player);
        }
    }

    private void addPlayer(Player player) {
        PlayerProfileData playerProfileData = plugin.getSpigotCore().getProfileManager().getProfile(player);

        // Send Welcome Message
        player.sendMessage("");
        player.sendMessage(CenterChatText.centerChatMessage(LobbyMessages.PLAYER_WELCOME_1.toString().replace("%s", "1.0")));
        player.sendMessage("");
        player.sendMessage(CenterChatText.centerChatMessage(LobbyMessages.PLAYER_WELCOME_2.toString()));
        player.sendMessage("");
        player.sendMessage(CenterChatText.centerChatMessage(LobbyMessages.PLAYER_WELCOME_3.toString()));
        player.sendMessage("");

        // Give them the boss bar.
        playerBossBar.addPlayerBar(player);

        // Spawn player on spawn pad.
        if (playerProfileData.isTutorialFinished()) {
            plugin.getTeleports().teleportSpawn(player);
        } else {
            plugin.getTeleports().teleportTutorial(player);
        }

        // Enable double jumping.
        doubleJump.setupPlayer(player);

        //Reset the players compass.
        player.setCompassTarget(new Location(Bukkit.getWorlds().get(0), 0.5, 108, -24.5));

        //Put the player in adventure mode.
        player.setGameMode(GameMode.SURVIVAL);

        //Set player health and food level.
        player.setHealth(20);
        player.setFoodLevel(20);

        //Give player starting items.
        if (plugin.getSpigotCore().getProfileManager().getProfile(player).getSerializedInventory().equals("")) {
            giveStartingItems(player);
        }
    }

    private void giveStartingItems(final Player player) {
        ItemGenerator itemGen = plugin.getSpigotCore().getItemGen();
        List<ItemStack> beginnerItems = new ArrayList<>();
        int timePerItem = 1;
        int delayTime = 3;

        //Generate Items
        beginnerItems.add(itemGen.generateItem("main_menu", ItemTypes.MENU));
        beginnerItems.add(itemGen.generateItem("beginner_boots", ItemTypes.ARMOR));
        beginnerItems.add(itemGen.generateItem("beginner_leggings", ItemTypes.ARMOR));
        beginnerItems.add(itemGen.generateItem("beginner_chestplate", ItemTypes.ARMOR));
        beginnerItems.add(itemGen.generateItem("beginner_helmet", ItemTypes.ARMOR));
        beginnerItems.add(itemGen.generateItem("beginner_sword", ItemTypes.WEAPON));
        beginnerItems.add(itemGen.generateItem("beginner_bread", ItemTypes.FOOD, 6));

        //Set Items
        new BukkitRunnable() {

            private int i;

            @Override
            public void run() {
                Material material = beginnerItems.get(i).getType();
                player.getInventory().setItem(i, beginnerItems.get(i));

                if (material == Material.BREAD) {
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, .6f);
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, .6f);
                }

                i++;
                if (i == beginnerItems.size()) cancel();
            }
        }.runTaskTimer(plugin, 20 * delayTime, 20 * timePerItem);
    }

    private void removePlayer(Player player) {

        playerBossBar.removePlayerBar(player);

        // Disable double jumping.
        doubleJump.removePlayer(player);
    }

    /***
     * Setup the player once their profile is loaded.
     * @param event
     */
    @EventHandler
    public void onProfileLoad(ProfileLoadedEvent event) { addPlayer(event.getPlayer()); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {event.setJoinMessage("");}

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) { removePlayer(event.getPlayer()); }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) { removePlayer(event.getPlayer()); }
}
