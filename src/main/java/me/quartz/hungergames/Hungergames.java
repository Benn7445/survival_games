package me.quartz.hungergames;

import me.quartz.hungergames.commands.MapCommand;
import me.quartz.hungergames.map.MapManager;
import me.quartz.hungergames.profile.ProfileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hungergames extends JavaPlugin {

    private static Hungergames instance;
    private MapManager mapManager;
    private ProfileManager profileManager;

    public static Hungergames getInstance() {
        return instance;
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
    }

    @Override
    public void onDisable() {
    }

    private void registerManagers() {
        instance = this;
        mapManager = new MapManager();
        profileManager = new ProfileManager();
    }

    private void registerCommands() {
        getCommand("map").setExecutor(new MapCommand());
    }
}
