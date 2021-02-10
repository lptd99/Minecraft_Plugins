package tyo_drak.draksrpgclasses;

import org.bukkit.entity.Player;

public class Debug {

    public static void consoleMessage(String s) {
        if (Main.config.getBoolean("SYSTEM_DEBUG")) {
            System.out.println("[Drak's RPG Classes]: "+s);
        }
    }

    public static void playerMessage(Player player, String s) {
        if (Main.config.getBoolean("SYSTEM_DEBUG")) {
            player.sendMessage("[Drak's RPG Classes]: "+s);
        }
    }

}
