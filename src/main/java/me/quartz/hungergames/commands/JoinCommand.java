package me.quartz.hungergames.commands;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Game game = Hungergames.getInstance().getGameManager().getGame(player);
            if(game == null) {
                if (!Hungergames.getInstance().getQueueManager().isQueued(player.getUniqueId())) {
                    Hungergames.getInstance().getQueueManager().queuePlayer(player);
                    player.sendMessage(ChatColor.GREEN + "Added to the queue.");
                } else player.sendMessage(ChatColor.RED + "You are already queued.");
            } else player.sendMessage(ChatColor.RED + "You are already in game.");
        } else commandSender.sendMessage(ChatColor.RED + "You do not have permissions.");
        return true;
    }
}
