package com.forgestorm.lobby.player;

import com.forgestorm.lobby.Lobby;
import com.forgestorm.lobby.constants.LobbyMessages;
import com.forgestorm.lobby.display.PlayerBossBar;
import com.forgestorm.spigotcore.database.PlayerProfileData;
import com.forgestorm.spigotcore.database.ProfileLoadedEvent;
import com.forgestorm.spigotcore.player.DoubleJump;
import com.forgestorm.spigotcore.util.text.CenterChatText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    public void onPlayerQuit(PlayerQuitEvent event) { removePlayer(event.getPlayer()); }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) { removePlayer(event.getPlayer()); }
}
