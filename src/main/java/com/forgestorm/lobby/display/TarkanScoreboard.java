package com.forgestorm.lobby.display;

import com.forgestorm.lobby.Lobby;
import com.forgestorm.spigotcore.SpigotCore;
import com.forgestorm.spigotcore.constants.SpigotCoreMessages;
import com.forgestorm.spigotcore.database.PlayerProfileData;
import com.forgestorm.spigotcore.database.ProfileLoadedEvent;
import com.forgestorm.spigotcore.events.UpdateScoreboardEvent;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TarkanScoreboard implements Listener {

    private final Lobby plugin;
    private final SpigotCore spigotCore;
    private final TitleManagerAPI titleManagerAPI;

    public TarkanScoreboard(Lobby plugin) {
        this.plugin = plugin;
        spigotCore = plugin.getSpigotCore();
        titleManagerAPI = plugin.getTitleManagerAPI();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void giveScoreboard(Player player) {
        titleManagerAPI.giveScoreboard(player);
        updateScoreboard(player);
    }

    public void updateScoreboard(Player player) {
        PlayerProfileData playerProfileData = spigotCore.getProfileManager().getProfile(player);

        //Set Title
        titleManagerAPI.setScoreboardTitle(player, SpigotCoreMessages.SCOREBOARD_TITLE.toString());

        // Blank line 1
        titleManagerAPI.setScoreboardValue(player, 1, SpigotCoreMessages.SCOREBOARD_BLANK_LINE_1.toString());

        // GEMS
        titleManagerAPI.setScoreboardValue(player, 2, SpigotCoreMessages.SCOREBOARD_GEMS.toString() +
                playerProfileData.getCurrency());

        // LEVEL
        titleManagerAPI.setScoreboardValue(player, 3, SpigotCoreMessages.SCOREBOARD_LEVEL.toString() +
                playerProfileData.getPlayerLevel());

        // XP
        titleManagerAPI.setScoreboardValue(player, 4, SpigotCoreMessages.SCOREBOARD_XP.toString() +
                playerProfileData.getExpPercent() + "%");

        // BLANK LINE 2
        titleManagerAPI.setScoreboardValue(player, 5, SpigotCoreMessages.SCOREBOARD_BLANK_LINE_2.toString());

        // SERVER
        titleManagerAPI.setScoreboardValue(player, 6, SpigotCoreMessages.SCOREBOARD_SERVER.toString());
        titleManagerAPI.setScoreboardValue(player, 7, plugin.getServer().getServerName());
    }

    @EventHandler
    public void onProfileLoadEvent(ProfileLoadedEvent event){
        giveScoreboard(event.getPlayer());
    }

    @EventHandler
    public void onUpdateScoreboard(UpdateScoreboardEvent event) {
        updateScoreboard(event.getPlayer());
    }
}
