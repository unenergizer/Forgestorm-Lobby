package com.forgestorm.lobby;

import com.forgestorm.lobby.commands.Spawn;
import com.forgestorm.lobby.commands.Tutorial;
import com.forgestorm.lobby.display.TarkanScoreboard;
import com.forgestorm.lobby.listeners.EntityDamage;
import com.forgestorm.lobby.listeners.EntityDamageByEntity;
import com.forgestorm.lobby.listeners.EntityRegainHealth;
import com.forgestorm.lobby.listeners.FoodLevelChange;
import com.forgestorm.lobby.player.PlayerManager;
import com.forgestorm.lobby.player.Teleports;
import com.forgestorm.spigotcore.SpigotCore;
import com.forgestorm.spigotcore.database.MongoDatabaseManager;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

@Getter
public class Lobby extends JavaPlugin {

    private TitleManagerAPI titleManagerAPI = (TitleManagerAPI) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
    private SpigotCore spigotCore;
    private MongoDatabaseManager profileManager;
    private PlayerManager playerManager;
    private TarkanScoreboard tarkanScoreboard;
    private Teleports teleports;
    private Spawn spawn;

    @Override
    public void onEnable() {
        spigotCore = (SpigotCore) Bukkit.getServer().getPluginManager().getPlugin("FS-SpigotCore");
        profileManager = spigotCore.getProfileManager();
        playerManager = new PlayerManager(this);
        tarkanScoreboard = new TarkanScoreboard(this);
        teleports = new Teleports(this);
        spawn = new Spawn(this);

        // Start Bukkit Tasks
        spawn.runTaskTimer(this, 0, 20);

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        playerManager.onDisable();
    }

    private void registerCommands() {
        getCommand("spawn").setExecutor(spawn);
        getCommand("tutorial").setExecutor(new Tutorial(this));
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EntityDamage(this), this);
        pm.registerEvents(new EntityDamageByEntity(this), this);
        pm.registerEvents(new EntityRegainHealth(this), this);
        pm.registerEvents(new FoodLevelChange(this), this);
    }
}
