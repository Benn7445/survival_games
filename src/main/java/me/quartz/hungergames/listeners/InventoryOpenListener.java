package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import me.quartz.hungergames.game.GameStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryOpenListener implements Listener {

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getType().equals(InventoryType.CHEST)) {
            Game game = Hungergames.getInstance().getGameManager().getGame(player);
            if (game != null && game.getGameStatus() == GameStatus.PRE_GAME) {
                event.setCancelled(true);
            }
        }
    }
}
