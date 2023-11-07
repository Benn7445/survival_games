package me.quartz.hungergames.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final List<Game> games;

    public GameManager() {
        this.games = new ArrayList<>();
    }

    public void createGame(List<Player> players) {
        Game game = new Game(players.subList(0, 5), players.subList(5, 10));
        games.add(game);
        game.start();
    }

    public Game getGame(Player player) {
        for(Game game : games) {
            if(game.getPlayers().contains(player)) return game;
        }
        return null;
    }
}
