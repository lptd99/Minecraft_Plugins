package tyo_drak.drakslib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

public class Misc {
    public static final Random rand = new Random();
    public static final FileConfiguration config = null;

    public static int random(int min, int max) {
        return (rand.nextInt((max - min) + 1) + min);
    }

    public static String colorize(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void message(final CommandSender sender, final String message) {
        sender.sendMessage(colorize(message));
    }

    public static void log(final String message) {
        message(Bukkit.getConsoleSender(), "[" + DraksLibMain.getPlugin().getName() + "] " + message);
    }

    public static void dLog(final String message) {
        if (config.getBoolean("SYSTEM_DEBUG")) {
            message(Bukkit.getConsoleSender(), "[" + DraksLibMain.getPlugin().getName() + "] " + message + " [DEBUG LOG]");
        }
    }

}
