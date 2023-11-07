package me.quartz.hungergames.scoreHelper;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import me.quartz.hungergames.game.GameStatus;
import me.quartz.hungergames.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreManager {

    public ScoreManager() {
        for(Player player : Bukkit.getOnlinePlayers()) createScoreboard(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) updateScoreboard(player);
            }
        }.runTaskTimer(Hungergames.getInstance(), 20L, 20L);
    }

    public void createScoreboard(Player player) {
        Profile profile = Hungergames.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Game game = Hungergames.getInstance().getGameManager().getGame(player);
        ScoreHelper helper = ScoreHelper.createScore(player);
        helper.setTitle(Hungergames.getInstance().getConfig().getString(game != null ? (game.getGameStatus() == GameStatus.PRE_GAME ? "scoreboard-pre-title" : "scoreboard-live-title") : "scoreboard-title"));
        updateConfigScore(profile, game, helper);
    }

    public void updateScoreboard(Player player) {
        Profile profile = Hungergames.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Game game = Hungergames.getInstance().getGameManager().getGame(player);
        if(ScoreHelper.hasScore(player)) {
            ScoreHelper helper = ScoreHelper.getByPlayer(player);
            updateConfigScore(profile, game, helper);
        }
    }

    private void updateConfigScore(Profile profile, Game game, ScoreHelper helper) {
        int i = Hungergames.getInstance().getConfig().getStringList(game != null ? (game.getGameStatus() == GameStatus.PRE_GAME ? "scoreboard-pre" : "scoreboard-live") : "scoreboard").size();
        for(String s : Hungergames.getInstance().getConfig().getStringList(game != null ? (game.getGameStatus() == GameStatus.PRE_GAME ? "scoreboard-pre" : "scoreboard-live") : "scoreboard")) {
            if((!s.startsWith("%ifq%:") || Hungergames.getInstance().getQueueManager().isQueued(profile.getUuid())) &&
                    (!s.startsWith("%ifg%:") || (game != null && game.isGrace())) &&
                    (!s.startsWith("%ifng%:") || (game != null && !game.isGrace()))) {
                helper.setSlot(i, s.
                        replace("%ifq%:", "").
                        replace("%ifg%:", "").
                        replace("%ifng%:", "").
                        replace("%status%", game != null ? game.getGameStatus().toString() : "").
                        replace("%map%", game != null ? game.getMap().getName() + "" : "").
                        replace("%timer%", game != null ? game.getTimer() + "s" : "").
                        replace("%online%", Bukkit.getOnlinePlayers().size() + "").
                        replace("%max%", Bukkit.getMaxPlayers() + "").
                        replace("%spectators%", game != null ? game.getSpectators().size() + "" : "")
                );
            }
            i = i-1;
        }
    }
}