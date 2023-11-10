package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import me.quartz.hungergames.game.GameStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Game game = Hungergames.getInstance().getGameManager().getGame(player);
        if(game != null) {
            if(game.getGameStatus() == GameStatus.PRE_GAME || game.isDead(player)) {
                event.setCancelled(true);
            } else {
                Material material = event.getBlock().getType();
                if(material != Material.VINE && material != Material.LONG_GRASS && material != Material.LEAVES && material != Material.LEAVES_2 &&
                material != Material.YELLOW_FLOWER && material != Material.RED_ROSE && material != Material.DOUBLE_PLANT) {
                    event.setCancelled(true);
                }
            }
        } else event.setCancelled(true);
    }
}
