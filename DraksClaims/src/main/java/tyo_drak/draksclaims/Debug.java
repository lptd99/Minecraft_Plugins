package tyo_drak.draksclaims;

import org.bukkit.entity.Player;

public class Debug {


    public static void consoleMessage(String s) {
        if (Main.config.getBoolean("SYSTEM_DEBUG")) {
            System.out.println("[Drak's Work]: "+s);
        }
    }

    public static void playerMessage(Player player, String s) {
        if (Main.config.getBoolean("SYSTEM_DEBUG")) {
            player.sendMessage("[Drak's Work]: "+s);
        }
    }

}
