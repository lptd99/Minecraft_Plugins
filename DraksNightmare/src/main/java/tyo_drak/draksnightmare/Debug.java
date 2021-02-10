package tyo_drak.draksnightmare;

import org.bukkit.entity.Player;

public class Debug {

    public static void consoleMessage(String s) {
        if (Main.config.getBoolean("SYSTEM_DEBUG")) {
            System.out.println("[Drak's Nightmare]: " + s);
        }
    }

    public static void playerMessage(Player player, String s) {
        if (Main.config.getBoolean("SYSTEM_DEBUG")) {
            player.sendMessage("[Drak's Nightmare]: " + s);
        }
    }

}
