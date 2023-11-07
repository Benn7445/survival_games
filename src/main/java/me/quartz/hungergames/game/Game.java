package me.quartz.hungergames.game;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.map.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {

    private final UUID id;
    private final Map map;

    private GameStatus gameStatus;
    private int timer;

    private final List<Player> teamA;
    private final List<Player> teamB;
    private final List<Player> spectators;

    public Game(List<Player> teamA, List<Player> teamB) {
        this.id = UUID.randomUUID();
        this.map = Hungergames.getInstance().getMapManager().getRandomMap();
        this.gameStatus = GameStatus.PRE_GAME;
        this.timer = 11;
        this.teamA = teamA;
        this.teamB = teamB;
        this.spectators = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public Map getMap() {
        return map;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void nextGameStatus() {
        if(gameStatus == GameStatus.PRE_GAME) gameStatus = GameStatus.LIVE_GAME;
        else if(gameStatus == GameStatus.LIVE_GAME) gameStatus = GameStatus.DEATHMATCH;
        else if(gameStatus == GameStatus.DEATHMATCH) gameStatus = GameStatus.FINISHED;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void reduceTimer() {
        this.timer--;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        players.addAll(teamA);
        players.addAll(teamB);
        return players;
    }

    public List<Player> getTeamA() {
        return teamA;
    }

    public void addTeamA(Player player) {
        teamA.add(player);
    }

    public List<Player> getTeamB() {
        return teamB;
    }

    public void addTeamB(Player player) {
        teamB.add(player);
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public void killPlayer(Player player) {
        spectators.add(player);
    }

    public void start() {
        int index = 0;
        for(Player player : getPlayers()){
            player.teleport(map.getSpawnLocations().get(index));
            player.setAllowFlight(true);
            final int finalIndex = index;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Hungergames.getInstance(), () -> {
                player.teleport(map.getSpawnLocations().get(finalIndex));
                player.setAllowFlight(false);
            }, 400L);
            index++;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                reduceTimer();
                if(timer == 10 || timer <= 5)
                    getPlayers().forEach(player -> player.sendMessage(timer + "s..."));
                if(timer == 0) cancel();
            }
        }.runTaskTimer(Hungergames.getInstance(), 0, 20);
    }
}
