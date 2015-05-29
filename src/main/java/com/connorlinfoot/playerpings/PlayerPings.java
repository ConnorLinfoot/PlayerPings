package com.connorlinfoot.playerpings;

import com.connorlinfoot.playerpings.Commands.PingCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class PlayerPings extends JavaPlugin implements Listener {
    private static PlayerPings instance;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("ping").setExecutor(new PingCommand());
        Bukkit.getPluginManager().registerEvents(this, this);
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "PlayerPings V" + getDescription().getVersion() + " has been enabled!");

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                for( Player player : Bukkit.getOnlinePlayers() ) {
                    try {
                        player.setPlayerListName(player.getDisplayName() + ChatColor.GOLD + " [" + getPingReflection(player) + "]");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 40L, 40L);
    }

    public static PlayerPings getPlugin() {
        return instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            player.setPlayerListName(player.getDisplayName() + ChatColor.GOLD + " [" + getPingReflection(player) + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getPingReflection(Player player) throws Exception {
        int ping = 0;
        Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + "entity.CraftPlayer");
        Object converted = craftPlayer.cast(player);
        Method handle = converted.getClass().getMethod("getHandle");
        Object entityPlayer = handle.invoke(converted);
        Field pingField = entityPlayer.getClass().getField("ping");
        ping = pingField.getInt(entityPlayer);
        return ping;
    }

    public static String getServerVersion() {
        Pattern brand = Pattern.compile("(v|)[0-9][_.][0-9][_.][R0-9]*");
        String version = null;
        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        String version0 = pkg.substring(pkg.lastIndexOf('.') + 1);
        if (!brand.matcher(version0).matches()) {
            version0 = "";
        }
        version = version0;
        return !"".equals(version) ? version + "." : "";
    }

}
