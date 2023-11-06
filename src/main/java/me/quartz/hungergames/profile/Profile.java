package me.quartz.hungergames.profile;

import java.util.UUID;

public class Profile {

    private final UUID uuid;

    private int elo;
    private int played;
    private int wins;
    private int mvps;
    private int worsts;
    private int kills;
    private int deaths;
    private int assists;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.elo = 1000;
        this.played = 0;
        this.wins = 0;
        this.mvps = 0;
        this.worsts = 0;
        this.kills = 0;
        this.deaths = 0;
        this.assists = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getElo() {
        return elo;
    }

    public void addElo(int elo) {
        this.elo += elo;
    }

    public void removeElo(int elo) {
        this.elo -= elo;
    }

    public int getPlayed() {
        return played;
    }

    public void addPlayed() {
        this.played++;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        this.wins++;
    }

    public int getMvps() {
        return mvps;
    }

    public void addMvp() {
        this.mvps++;
    }

    public int getWorsts() {
        return worsts;
    }

    public void addWorst() {
        this.worsts++;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        this.kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        this.deaths++;
    }

    public int getAssists() {
        return assists;
    }

    public void addAssist() {
        this.assists++;
    }
}
