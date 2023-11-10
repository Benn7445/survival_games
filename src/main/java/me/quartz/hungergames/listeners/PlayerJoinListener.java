package me.quartz.hungergames.listeners;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.score.ScoreHelper;
import me.quartz.hungergames.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.stream.Collectors;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Hungergames.getInstance().getScoreManager().createScoreboard(player);
        player.teleport(FileUtils.stringToLocation(Hungergames.getInstance().getConfig().getString("spawn")));
        ScoreHelper.getByPlayer(player).setPlayersTag(player);
    }
}
