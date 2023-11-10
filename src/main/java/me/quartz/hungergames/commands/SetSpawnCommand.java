package me.quartz.hungergames.commands;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.map.Map;
import me.quartz.hungergames.utils.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("hungergames.setspawn")) {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Hungergames.getInstance().getConfig().set("spawn", FileUtils.locationToString(player.getLocation()));
                Hungergames.getInstance().saveConfig();
                player.sendMessage(ChatColor.GREEN + "Spawn has been set.");
            } else commandSender.sendMessage(ChatColor.RED + "Only players can execute this command.");
        } else commandSender.sendMessage(ChatColor.RED + "You do not have permissions.");
        return true;
    }
}
