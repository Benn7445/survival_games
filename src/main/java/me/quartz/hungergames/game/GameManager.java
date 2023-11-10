package me.quartz.hungergames.game;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.score.ScoreHelper;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {
    private final List<Game> games;

    public GameManager() {
        this.games = new ArrayList<>();
    }

    public void createGame(List<Player> players) {
        Game game = new Game(new ArrayList<>(players));
        games.add(game);
        game.start();
        for(Player player : players) {
            if(ScoreHelper.hasScore(player)) ScoreHelper.removeScore(player);
            Hungergames.getInstance().getScoreManager().createScoreboard(player);
        }
    }

    public Game getGame(Player player) {
        return games.stream().filter(game ->
                        game.getPlayers().stream().anyMatch(player1 -> player1.getUniqueId().toString().equals(player.getUniqueId().toString())))
                .findAny().orElse(null);
    }

    public Game getGame(UUID uuid) {
        return games.stream().filter(game -> game.getId().toString().equals(uuid.toString())).findAny().orElse(null);
    }

    public void removeGame(UUID uuid) {
        games.removeIf(game -> game.getId().toString().equals(uuid.toString()));
    }

    public List<Game> getGames() {
        return games;
    }
}
