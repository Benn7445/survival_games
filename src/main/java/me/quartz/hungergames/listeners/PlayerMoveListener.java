package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import me.quartz.hungergames.game.GameStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = Hungergames.getInstance().getGameManager().getGame(player);
        if(game != null) {
            if(game.getGameStatus() == GameStatus.PRE_GAME && game.getTimer() != 0 && game.getTimer() < 10) {
                if(event.getTo().getX() != event.getFrom().getX()) player.teleport(event.getFrom());
                if(event.getTo().getZ() != event.getFrom().getZ()) player.teleport(event.getFrom());
            }
        }
    }
}
