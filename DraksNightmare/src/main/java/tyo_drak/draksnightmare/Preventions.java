package tyo_drak.draksnightmare;

import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import tyo_drak.draksnightmare.misc.DraksPlayers;
import tyo_drak.draksnightmare.misc.DraksTime;

public class Preventions {

    //<editor-fold defaultstate="collapsed" desc="EDITOR-FOLDING">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DEATH TIMER">
    public static void preventDeadPlayerMove(PlayerMoveEvent event, Player player) {
        if (!player.hasPermission(MainEvents.ALLOW_GAMEMODE) && !player.getGameMode().equals(GameMode.SURVIVAL) && !player.isOp()) {
            if (MainEvents.spectatorJoinTime.containsKey(player.getName()) && DraksTime.getTimeSeconds() - MainEvents.spectatorJoinTime.get(player.getName()) >= 3) {
                if (DraksPlayers.playerCanRevive(player.getName())) {
                    DraksPlayers.forcePlayerRevive(player);
                } else {
                    DraksPlayers.playerWarnUnrevivable(player);
                }
            }
            event.setCancelled(true);
        }
    }

    public static void preventPlayerSpectateOthers(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            event.getPlayer().setSpectatorTarget(null);
            event.setCancelled(true);
        }
    }
    //</editor-fold>

}
