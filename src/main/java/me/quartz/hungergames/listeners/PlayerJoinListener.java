package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Hungergames.getInstance().getScoreManager().createScoreboard(player);
    }
}
