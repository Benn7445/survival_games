package me.quartz.hungergames;

import me.quartz.hungergames.commands.GetWorldCommand;
import me.quartz.hungergames.commands.JoinCommand;
import me.quartz.hungergames.commands.MapCommand;
import me.quartz.hungergames.commands.SetSpawnCommand;
import me.quartz.hungergames.files.FileManager;
import me.quartz.hungergames.game.Game;
import me.quartz.hungergames.game.GameManager;
import me.quartz.hungergames.listeners.*;
import me.quartz.hungergames.map.MapManager;
import me.quartz.hungergames.map.WorldManager;
import me.quartz.hungergames.profile.ProfileManager;
import me.quartz.hungergames.queue.QueueManager;
import me.quartz.hungergames.score.ScoreHelper;
import me.quartz.hungergames.score.ScoreManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hungergames extends JavaPlugin {

    private static Hungergames instance;

    private FileManager fileManager;
    private QueueManager queueManager;
    private GameManager gameManager;
    private MapManager mapManager;
    private ProfileManager profileManager;
    private ScoreManager scoreManager;
    private WorldManager worldManager;

    public static Hungergames getInstance() {
        return instance;
    }

    public FileManager getFileManager() {
        return fileManager;
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

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    @Override
    public void onEnable() {
        registerManagers();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(ScoreHelper.hasScore(player)) ScoreHelper.removeScore(player);
        }
        for(Game game : getGameManager().getGames()) game.endGame();
    }

    private void registerManagers() {
        instance = this;
        saveDefaultConfig();

        fileManager = new FileManager();
        queueManager = new QueueManager();
        gameManager = new GameManager();
        mapManager = new MapManager();
        profileManager = new ProfileManager();
        scoreManager = new ScoreManager();
        worldManager = new WorldManager();
    }

    private void registerCommands() {
        getCommand("map").setExecutor(new MapCommand());
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("getworld").setExecutor(new GetWorldCommand());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntitySpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }
}
