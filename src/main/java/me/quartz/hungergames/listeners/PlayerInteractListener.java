package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = Hungergames.getInstance().getGameManager().getGame(player);
        if(game != null && (game.isDead(player))) event.setCancelled(true);
    }
}
