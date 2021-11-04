package tyo_drak.draksnightmare;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import tyo_drak.drakslib.Misc;
import tyo_drak.drakslib.Players;
import tyo_drak.drakslib.Time;

public class Preventions {
    public static final String PLAYER_DEATH_TIMESTAMPS_TAG = "PLAYER_DEATH_TIMESTAMPS";
    public static final String PLAYER_LAST_DEATH_TIME_TAG = "LAST_DEATH_TIME";
    public static final String PLAYER_WARN_DEATH_TIMER_TAG = "WARN_DEATH_TIMER";

    //<editor-fold defaultstate="collapsed" desc="EDITOR-FOLDING">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DEATH TIMER">
    public static boolean playerCanRevive(String playerName){
        switch (debugVipRegular(playerName)) {
            case 'D':
                return Time.hasPassedSince(Main.config.getInt("DEBUG_RESPAWN_TIME"), Main.config.getLong(playerName + " " + PLAYER_LAST_DEATH_TIME_TAG));
            case 'V':
                return Time.hasPassedSince(Main.config.getInt("VIP_RESPAWN_TIME"), Main.config.getLong(playerName + " " + PLAYER_LAST_DEATH_TIME_TAG));
            default:
                return Time.hasPassedSince(Main.config.getInt("REGULAR_RESPAWN_TIME"), Main.config.getLong(playerName + " " + PLAYER_LAST_DEATH_TIME_TAG));
        }
    }

    public static char debugVipRegular(String playerName) {
        if (Main.config.getBoolean("SYSTEM_DEBUG")) {
            return 'D';
        }
        if (Players.isVIP(playerName)) {
            return 'V';
        } else {
            return 'R';
        }
    }

    public static void preventDeadPlayerMove(PlayerMoveEvent event, Player player) {
        if (!player.hasPermission(MainEvents.ALLOW_GAMEMODE) && !player.getGameMode().equals(GameMode.SURVIVAL) && !player.isOp()) {
            if (MainEvents.spectatorJoinTime.containsKey(player.getName()) && Time.getTimeSeconds() - MainEvents.spectatorJoinTime.get(player.getName()) >= 3) {
                if (!Main.config.getBoolean("DELAY_RESPAWN") || playerCanRevive(player.getName())) {
                    forcePlayerRevive(player.getName());
                } else {
                    playerWarnUnrevivable(player);
                }
            }
            event.setCancelled(true);
        }
    }

    public static void playerWarnUnrevivable(Player player) {
        String path = "";
        switch (debugVipRegular(player.getName())){
            case 'D':
                path = "DEBUG_RESPAWN_TIME";
                break;
            case 'V':
                path = "VIP_RESPAWN_TIME";
                break;
            default:
                path = "REGULAR_RESPAWN_TIME";
                break;
        }
        Players.denyPlayerAction(player, PLAYER_WARN_DEATH_TIMER_TAG, ChatColor.RED + "Você deve esperar mais " + Time.getFormattedTimeRemaining(Main.config.getInt(path), Main.config.getLong(player.getName() + " " + PLAYER_LAST_DEATH_TIME_TAG)) + " para reviver!");
    }

    public static void forcePlayerRevive(String playerName) {
        Main.config.set(playerName + " " + PLAYER_LAST_DEATH_TIME_TAG, 0);
    }

    public static void preventPlayerSpectateOthers(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            event.getPlayer().setSpectatorTarget(null);
            event.setCancelled(true);
        }
    }
    //</editor-fold>

}
