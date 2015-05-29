package com.connorlinfoot.playerpings.Commands;

import com.connorlinfoot.playerpings.PlayerPings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.AQUA + "----- PlayerPings V" + PlayerPings.getPlugin().getDescription().getVersion() + " -----");
            sender.sendMessage(ChatColor.AQUA + "/ping help - View this menu");
            if( sender.hasPermission("playerpings.ping.me" ) ) sender.sendMessage(ChatColor.AQUA + "/ping - View your ping");
            if( sender.hasPermission("playerpings.ping.other" ) ) sender.sendMessage(ChatColor.AQUA + "/ping <player> - Get a players ping");
            if( sender.hasPermission("playerpings.ping.gui" ) ) sender.sendMessage(ChatColor.AQUA + "/ping gui - View all players pings in a GUI");
            return true;
        }

        if( args.length == 1 ) {
            if( args[0].equalsIgnoreCase("gui") ) {
                if( sender instanceof Player && !sender.hasPermission("playerpings.ping.gui")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to run this command");
                    return false;
                }

                sender.sendMessage(ChatColor.RED + "Soon...");
                return true;
            }

            if( sender instanceof Player && !sender.hasPermission("playerpings.ping.other")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to run this command");
                return false;
            }

            Player player = Bukkit.getPlayer(args[0]);
            if( player == null ) {
                sender.sendMessage(ChatColor.RED + "The player \"" + args[0] + "\" could not be found");
                return false;
            }

            try {
                sender.sendMessage(ChatColor.GREEN + player.getName() + "'s ping is " + PlayerPings.getPingReflection(player) + "ms");
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Failed to get ping :(");
                e.printStackTrace();
            }
            return true;
        }

        if( sender instanceof Player ) {
            Player player = (Player) sender;
            if( !player.hasPermission("playerpings.ping.me")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to run this command");
                return false;
            }

            try {
                sender.sendMessage(ChatColor.GREEN + "Your ping is " + PlayerPings.getPingReflection(player) + "ms");
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Failed to get ping :(");
                e.printStackTrace();
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Command must be ran as a player");
        }

        return true;
    }

}
