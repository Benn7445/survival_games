package me.quartz.hungergames.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener implements Listener {

    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent event) {
        event.setCancelled(true);
    }
}
