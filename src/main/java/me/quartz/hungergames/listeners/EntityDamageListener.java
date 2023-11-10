package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import me.quartz.hungergames.game.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
                if (game.getGameStatus() == GameStatus.PRE_GAME || game.isGrace() || game.isDead(player))
                    event.setCancelled(true);

                if(player.getHealth() - event.getFinalDamage() <= 0) {
                    event.setCancelled(true);
                    if(player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
                        player.sendMessage("EntityDamage: " + player.getLastDamageCause().getCause().toString());
                        game.killPlayer(player);
                    }
                }
            } else event.setCancelled(true);
        }
    }
}
