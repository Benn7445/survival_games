package me.quartz.hungergames.queue;

import me.quartz.hungergames.Hungergames;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class QueueManager {

    private List<Player> players;

    public QueueManager() {
        this.players = new ArrayList<>();
    }

    public void queuePlayer(Player player) {
        players.add(player);
        if(players.size() == Hungergames.getInstance().getMapManager().getMinimum() && Hungergames.getInstance().getMapManager().isMinimum()) {
            Hungergames.getInstance().getGameManager().createGame(players);
            players.clear();
        }
    }

    public boolean isQueued(UUID uuid) {
        return players.stream().anyMatch(player -> player.getUniqueId().toString().equals(uuid.toString()));
    }

    public List<Player> getPlayers() {
        return players;
    }
}
