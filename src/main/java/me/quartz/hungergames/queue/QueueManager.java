package me.quartz.hungergames.queue;

import me.quartz.hungergames.Hungergames;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {

    private List<Player> players;

    public QueueManager() {
        this.players = new ArrayList<>();
    }

    public void queuePlayer(Player player) {
        players.add(player);
        if(players.size() == 10) {
            Hungergames.getInstance().getGameManager().createGame(players);
            players.clear();
        }
    }

    public boolean isQueued(Player player) {
        return players.contains(player);
    }

    public List<Player> getPlayers() {
        return players;
    }
}
