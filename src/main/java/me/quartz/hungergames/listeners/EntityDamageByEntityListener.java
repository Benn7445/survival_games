package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import me.quartz.hungergames.game.GameStatus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Game game = Hungergames.getInstance().getGameManager().getGame(player);
            if(game != null) {
                if (game.getGameStatus() == GameStatus.PRE_GAME || game.isGrace())
                    event.setCancelled(true);
                if(event.getDamager() instanceof Player) {
                    Player killer = (Player) event.getDamager();
                    if(game.isDead(killer)) event.setCancelled(true);
                }

                if(player.getHealth() - event.getFinalDamage() <= 0) {
                    event.setCancelled(true);
                    if(event.getDamager() instanceof Player) {
                        Player killer = (Player) event.getDamager();
                        game.killPlayer(player, killer);
                    } else if(event.getDamager() instanceof Arrow) {
                        Player killer = (Player) ((Arrow) event.getDamager()).getShooter();
                        game.killPlayer(player, killer);
                    } else {
                        player.sendMessage("EntityDamageByEntity: " + event.getDamager());
                        game.killPlayer(player);
                    }
                }
            } else event.setCancelled(true);
        }
    }
}
