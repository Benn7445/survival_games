package me.quartz.hungergames.game;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.map.Map;
import me.quartz.hungergames.score.ScoreHelper;
import me.quartz.hungergames.utils.FileUtils;
import me.quartz.hungergames.utils.TierUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {

    private final UUID id;
    private final Map map;

    private GameStatus gameStatus;
    private int timer;
    private boolean grace;

    private final List<Player> teamA;
    private final List<Player> teamB;
    private final List<Player> spectators;

    private boolean finished;

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
        this.finished = false;
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
        for (Player player : getPlayers()) {
            if (ScoreHelper.hasScore(player)) ScoreHelper.removeScore(player);
            Hungergames.getInstance().getScoreManager().createScoreboard(player);
        }
    }

    public int getTimer() {
        return timer;
    }

    public String getStringTimer() {
        if ((gameStatus == GameStatus.LIVE_GAME && !isGrace()) || gameStatus == GameStatus.DEATHMATCH) {
            int minAndSec = timer % 3600;
            int min = minAndSec / 60;
            int sec = minAndSec % 60;

            return (min > 9 ? min : "0" + min) + ":" + (sec > 9 ? sec : "0" + sec);
        }
        return timer + "s";
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
        for (Player player : getPlayers()) {
            if (ScoreHelper.hasScore(player)) ScoreHelper.removeScore(player);
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

    public boolean isTeamA(Player player) {return teamA.contains(player);}

    public List<Player> getSpectators() {
        return spectators;
    }

    public boolean isDead(Player player) {
        return spectators.contains(player);
    }

    public boolean isFinished() {
        return finished;
    }

    public void killPlayer(Player player, Player killer) {
        if (!isDead(player)) {
            getPlayers().forEach(player1 -> player1.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Hungergames.getInstance().getConfig().getString("messages.killed")
                            .replace("%dead%", player.getName())
                            .replace("%killer%", killer.getName())
            )));
            killed(player);
        }
    }

    public void killPlayer(Player player) {
        if (!isDead(player)) {
            getPlayers().forEach(player1 -> player1.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Hungergames.getInstance().getConfig().getString("messages.dead").replace("%dead%", player.getName())
            )));
            killed(player);
        }
    }

    public void quitPlayer(Player player) {
        if (!isDead(player) && getGameStatus() != GameStatus.PRE_GAME) {
            killPlayer(player);
        } else if (!isDead(player) && getGameStatus() == GameStatus.PRE_GAME) {
            Hungergames.getInstance().getGameManager().removeGame(id);
            getPlayers().forEach(player1 -> {
                lobbyPlayer(player1);
                player1.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Hungergames.getInstance().getConfig().getString("messages.discontinued")
                ));
            });
        } else lobbyPlayer(player);
    }

    private void lobbyPlayer(Player player) {
        for(Player targets : getPlayers()) {
            player.showPlayer(targets);
            targets.showPlayer(player);
        }
        getTeamA().remove(player);
        getTeamB().remove(player);
        getSpectators().remove(player);
        player.teleport(FileUtils.stringToLocation(Hungergames.getInstance().getConfig().getString("spawn")));
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.setHealth(20);
        player.setLevel(0);

        if (ScoreHelper.hasScore(player)) ScoreHelper.removeScore(player);
        Hungergames.getInstance().getScoreManager().createScoreboard(player);
    }

    private void killed(Player player) {
        spectators.add(player);
        player.setGameMode(GameMode.CREATIVE);
        player.getWorld().strikeLightningEffect(player.getLocation());

        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if(itemStack != null && itemStack.getType() != Material.AIR) {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                player.getInventory().removeItem(itemStack);
            }
        }

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if(itemStack != null && itemStack.getType() != Material.AIR) {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                player.getInventory().removeItem(itemStack);
            }
        }

        for (Player players : getPlayers()) players.hidePlayer(player);
        if (getTeamA().stream().allMatch(this::isDead)) winGame(false);
        if (getTeamB().stream().allMatch(this::isDead)) winGame(true);
    }

    private void winGame(boolean a) {
        finished = true;
        getPlayers().forEach(player -> {
            if (ScoreHelper.hasScore(player)) ScoreHelper.removeScore(player);
            Hungergames.getInstance().getScoreManager().createScoreboard(player);
        });
        getPlayers().forEach(players -> players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Hungergames.getInstance().getConfig().getString("messages.won")
                        .replace("%won%", a ? "Team A" : "Team B")
                        .replace("%loss%", !a ? "Team A" : "Team B")
        )));

        Bukkit.getScheduler().scheduleSyncDelayedTask(Hungergames.getInstance(), this::endGame, 200L);
    }

    public void endGame() {
        for(Player player : getPlayers()) lobbyPlayer(player);
        for(Player player : getSpectators().stream().filter(player -> !getPlayers().contains(player)).collect(Collectors.toList())) lobbyPlayer(player);
        Hungergames.getInstance().getWorldManager().unloadWorld(Bukkit.getWorld(id.toString()));
        Hungergames.getInstance().getGameManager().removeGame(id);
    }

    private final List<Player> striked = new ArrayList<>();

    public void start() {
        World world = Hungergames.getInstance().getWorldManager().copyWorld(this);
        List<Location> locations = new ArrayList<>(map.getSpawnLocations()).stream().peek(location -> location.setWorld(world)).collect(Collectors.toList());

        Location center = map.getCenter().clone();
        center.setWorld(world);

        for (Player player : getPlayers()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Hungergames.getInstance().getConfig().getString("messages.map").replace("%map%", map.getName())));
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.teleport(center);
            player.setHealth(20);
            player.getInventory().clear();
            player.getInventory().setBoots(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setHelmet(null);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if(Hungergames.getInstance().getGameManager().getGame(id) == null) cancel();
                if (timer > 0 && !finished) {
                    reduceTimer();
                    getPlayers().forEach(player -> player.setLevel(timer));
                }
                if (timer == 10) {
                    int index = 0;
                    for (Player player : getPlayers()) {
                        player.teleport(locations.get(index));
                        player.setAllowFlight(false);
                        index++;
                    }
                }
                if (timer == 10 || (timer <= 5 && timer > 0))
                    getPlayers().forEach(player -> {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Hungergames.getInstance().getConfig().getString("messages.starting-cooldown").replace("%timer%", timer != 1 ? timer + " seconds" : timer + " second")));
                        player.playSound(player.getLocation(), Sound.valueOf(Hungergames.getInstance().getConfig().getString("sounds.cooldown")), 1, 1);
                    });
                if (timer == 0) {
                    TierUtils.fillChests(center.getBlock(), 150);
                    getPlayers().forEach(player -> {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Hungergames.getInstance().getConfig().getString("messages.starting")));
                        player.playSound(player.getLocation(), Sound.valueOf(Hungergames.getInstance().getConfig().getString("sounds.start")), 1, 1);
                    });
                    nextGameStatus();
                    setTimer(61);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(Hungergames.getInstance().getGameManager().getGame(id) == null) cancel();
                            if (timer > 0 && !finished) reduceTimer();
                            else {
                                setGrace(false);
                                setTimer(601);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(Hungergames.getInstance().getGameManager().getGame(id) == null) cancel();
                                        if (timer > 0 && !finished) reduceTimer();
                                        else if(!finished) {
                                            nextGameStatus();
                                            int index = 0;
                                            for (Player player : getPlayers()) {
                                                player.teleport(locations.get(index));
                                                index++;
                                            }
                                            setTimer(191);

                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    if(Hungergames.getInstance().getGameManager().getGame(id) == null) cancel();
                                                    if (timer > 0 && !finished) reduceTimer();
                                                    else if (finished) cancel();
                                                    if (timer == 190 || (timer > 180 && timer <= 185)) {
                                                        getPlayers().forEach(player -> {
                                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Hungergames.getInstance().getConfig().getString("messages.dm-cooldown").replace("%timer%", (timer - 180) != 1 ? (timer - 180) + " seconds" : (timer - 180) + " second")));
                                                            player.playSound(player.getLocation(), Sound.valueOf(Hungergames.getInstance().getConfig().getString("sounds.cooldown")), 1, 1);
                                                        });
                                                    }
                                                    else if (timer == 180) {
                                                        getPlayers().forEach(player -> {
                                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Hungergames.getInstance().getConfig().getString("messages.dm-starting")));
                                                            player.playSound(player.getLocation(), Sound.valueOf(Hungergames.getInstance().getConfig().getString("sounds.start")), 1, 1);
                                                        });
                                                    }

                                                    for(Player player : getPlayers()) {
                                                        if(!isDead(player)) {
                                                            final Location center = getMap().getCenter().clone();
                                                            center.setY(player.getLocation().getY());
                                                            center.setWorld(Bukkit.getWorld(getId().toString()));
                                                            final Location locationPlayer = player.getLocation();
                                                            if(locationPlayer.distance(center) > ((double) (getTimer() / 6)) && !striked.contains(player)) {
                                                                player.getLocation().getWorld().strikeLightning(player.getLocation());
                                                                striked.add(player);

                                                                Bukkit.getScheduler().scheduleSyncDelayedTask(Hungergames.getInstance(), () -> striked.remove(player), 100);
                                                            }
                                                        }
                                                    }
                                                }
                                            }.runTaskTimer(Hungergames.getInstance(), 0, 20);
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
            }
        }.runTaskTimer(Hungergames.getInstance(), 0, 20);
    }
}
