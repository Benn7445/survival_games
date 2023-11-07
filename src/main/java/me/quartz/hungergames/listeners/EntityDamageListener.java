package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import me.quartz.hungergames.game.GameStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Game game = Hungergames.getInstance().getGameManager().getGame(player);
            if(game != null) {
                if (game.getGameStatus() == GameStatus.PRE_GAME || game.isGrace())
                    event.setCancelled(true);
            } else event.setCancelled(true);
        }
    }
}
