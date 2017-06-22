package com.forgestorm.lobby.display;

import com.forgestorm.spigotcore.util.display.BossBarAnnouncer;
import org.bukkit.entity.Player;

public class PlayerBossBar {

    private final BossBarAnnouncer bossBarAnnouncer;

    public PlayerBossBar() {
        bossBarAnnouncer = new BossBarAnnouncer("&e&lFORGESTORM &7&l- &a&lLOBBY");
    }

    /**
     * This will display the boss bar to the player.
     *
     * @param player The player to give the boss bar to.
     */
    public void addPlayerBar(Player player) {
        bossBarAnnouncer.showBossBar(player);
    }

    /**
     * This will remove the boss bar from the player.
     *
     * @param player The player to remove the boss bar from.
     */
    public void removePlayerBar(Player player) {
        bossBarAnnouncer.removeBossBar(player);
    }
}
