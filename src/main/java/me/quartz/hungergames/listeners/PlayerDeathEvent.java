package me.quartz.hungergames.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(org.bukkit.event.entity.PlayerDeathEvent event){
        event.setDeathMessage(null);
    }
}
