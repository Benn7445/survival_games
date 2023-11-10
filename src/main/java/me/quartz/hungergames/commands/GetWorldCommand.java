package me.quartz.hungergames.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("hungergames.setspawn")) {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if(strings.length > 0) {
                    Location location = player.getLocation();
                    World world = Bukkit.getWorld(strings[0]);
                    if(world == null) {
                        world = new WorldCreator(strings[0]).createWorld();
                    }
                    location.setWorld(world);
                    player.teleport(location);
                    player.sendMessage("Teleporting");
                } else {
                    player.sendMessage(player.getWorld().getName());
                }
            } else commandSender.sendMessage(ChatColor.RED + "Only players can execute this command.");
        } else commandSender.sendMessage(ChatColor.RED + "You do not have permissions.");
        return true;
    }
}
