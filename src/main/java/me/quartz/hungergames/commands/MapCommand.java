package me.quartz.hungergames.commands;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.map.Map;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("hungergames.map")) {
            if(strings.length > 1) {
                if(strings[0].equalsIgnoreCase("create")) {
                    if(Hungergames.getInstance().getMapManager().createMap(strings[1]))
                        commandSender.sendMessage(ChatColor.GREEN + "The map " + strings[1] + " has been created.");
                    else commandSender.sendMessage(ChatColor.RED + "The map " + strings[1] + " already exists.");
                } else if(strings[0].equalsIgnoreCase("add")) {
                    if(commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        Map map = Hungergames.getInstance().getMapManager().getMap(strings[1]);
                        if(map != null) {
                            map.addSpawnLocation(player.getLocation());
                            commandSender.sendMessage(ChatColor.GREEN + "Added location for " + map.getName() + ".");
                        } else commandSender.sendMessage(ChatColor.RED + "This map wasn't found.");
                    } else commandSender.sendMessage(ChatColor.RED + "Only players can execute this command.");
                } else if(strings[0].equalsIgnoreCase("center")) {
                    if(commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        Map map = Hungergames.getInstance().getMapManager().getMap(strings[1]);
                        if(map != null) {
                            map.setCenter(player.getLocation());
                            commandSender.sendMessage(ChatColor.GREEN + "Center location for " + map.getName() + " has been set.");
                        } else commandSender.sendMessage(ChatColor.RED + "This map wasn't found.");
                    } else commandSender.sendMessage(ChatColor.RED + "Only players can execute this command.");
                } else if(strings[0].equalsIgnoreCase("load")) {
                    World world = Bukkit.getWorld(strings[1]);
                    if(world == null) {
                        world = new WorldCreator(strings[1]).createWorld();
                    }
                    if(commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        Location location = player.getLocation();
                        location.setWorld(world);
                        player.teleport(location);
                    }
                } else if(strings[0].equalsIgnoreCase("remove")) {
                    if(Hungergames.getInstance().getMapManager().removeMap(strings[1]))
                        commandSender.sendMessage(ChatColor.GREEN + "The map " + strings[1] + " has been removed.");
                    else commandSender.sendMessage(ChatColor.RED + "This map wasn't found.");
                } else sendHelp(commandSender);
            } else sendHelp(commandSender);
        } else commandSender.sendMessage(ChatColor.RED + "You do not have permissions.");
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------");
        sender.sendMessage(ChatColor.YELLOW + "/map create" + ChatColor.GOLD + " <name>");
        sender.sendMessage(ChatColor.YELLOW + "/map remove" + ChatColor.GOLD + " <name>");
        sender.sendMessage(ChatColor.YELLOW + "/map info" + ChatColor.GOLD + " <name>");
        sender.sendMessage(ChatColor.YELLOW + "/map add" + ChatColor.GOLD + " <name>");
        sender.sendMessage(ChatColor.YELLOW + "/map center" + ChatColor.GOLD + " <name>");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------");
    }
}
