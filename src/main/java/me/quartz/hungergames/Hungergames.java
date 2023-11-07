package me.quartz.hungergames;

import me.quartz.hungergames.commands.JoinCommand;
import me.quartz.hungergames.commands.MapCommand;
import me.quartz.hungergames.game.GameManager;
import me.quartz.hungergames.listeners.EntityDamageListener;
import me.quartz.hungergames.map.MapManager;
import me.quartz.hungergames.profile.ProfileManager;
import me.quartz.hungergames.queue.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hungergames extends JavaPlugin {

    private static Hungergames instance;
    private QueueManager queueManager;
    private GameManager gameManager;
    private MapManager mapManager;
    private ProfileManager profileManager;

    public static Hungergames getInstance() {
        return instance;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    @Override
    public void onEnable() {
        registerManagers();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
    }

    private void registerManagers() {
        instance = this;
        queueManager = new QueueManager();
        gameManager = new GameManager();
        mapManager = new MapManager();
        profileManager = new ProfileManager();
    }

    private void registerCommands() {
        getCommand("map").setExecutor(new MapCommand());
        getCommand("join").setExecutor(new JoinCommand());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
    }
}
