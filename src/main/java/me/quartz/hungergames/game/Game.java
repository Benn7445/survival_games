package me.quartz.hungergames.game;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.map.Map;
import me.quartz.hungergames.scoreHelper.ScoreHelper;
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
    private boolean grace;

    private final List<Player> teamA;
    private final List<Player> teamB;
    private final List<Player> spectators;

    public Game(List<Player> players) {
        this.id = UUID.randomUUID();
        this.map = Hungergames.getInstance().getMapManager().getRandomMap();
        this.gameStatus = GameStatus.PRE_GAME;
        this.timer = 30;
        this.grace = true;
        this.teamA = new ArrayList<>();
        this.teamB = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if ((players.size() / 2) > i) teamA.add(players.get(i));
            else teamB.add(players.get(i));
        }
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
        if (gameStatus == GameStatus.PRE_GAME) gameStatus = GameStatus.LIVE_GAME;
        else if (gameStatus == GameStatus.LIVE_GAME) gameStatus = GameStatus.DEATHMATCH;
        else if (gameStatus == GameStatus.DEATHMATCH) gameStatus = GameStatus.FINISHED;
        for(Player player : getPlayers()) {
            if(ScoreHelper.hasScore(player)) ScoreHelper.removeScore(player);
            Hungergames.getInstance().getScoreManager().createScoreboard(player);
        }
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

    public boolean isGrace() {
        return grace;
    }

    public void setGrace(boolean grace) {
        this.grace = grace;
        for(Player player : getPlayers()) {
            if(ScoreHelper.hasScore(player)) ScoreHelper.removeScore(player);
            Hungergames.getInstance().getScoreManager().createScoreboard(player);
        }
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
        for (Player player : getPlayers()) {
            player.teleport(map.getSpawnLocations().get(index));
            player.setAllowFlight(true);
            index++;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if(timer == 10) {
                    int index = 0;
                    for (Player player : getPlayers()) {
                        player.teleport(map.getSpawnLocations().get(index));
                        player.setAllowFlight(false);
                        index++;
                    }
                }
                if (timer == 10 || (timer <= 5 && timer > 0))
                    getPlayers().forEach(player -> player.sendMessage(timer + "s..."));
                if (timer == 0) {
                    getPlayers().forEach(player -> player.sendMessage("Starting..."));
                    nextGameStatus();
                    setTimer(60);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(timer > 0) reduceTimer();
                            else {
                                setGrace(false);
                                setTimer(600);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(timer > 0) reduceTimer();
                                        else {
                                            nextGameStatus();
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(Hungergames.getInstance(), 0, 20);
                                cancel();
                            }
                        }
                    }.runTaskTimer(Hungergames.getInstance(), 0, 20);
                    cancel();
                }
                if(timer > 0) reduceTimer();
            }
        }.runTaskTimer(Hungergames.getInstance(), 0, 20);
    }
}
