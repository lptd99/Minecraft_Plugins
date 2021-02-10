package tyo_drak.draksnightpvp;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class MainEvents implements Listener {

    public static final String WORLD_NAME = "world";
    public static final String MESSAGE_FLAG_NIGHTPVP = "NIGHTPVP";
    public static final String MESSAGE_FLAG_NIGHTPVP_PROTECT_TAMED = MESSAGE_FLAG_NIGHTPVP + "_PROTECT_TAMED";
    public static final Map<String, Long> messagesCooldowns = new HashMap<>();

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event){
        messagesCooldowns.put(event.getPlayer().getName() + " " + MESSAGE_FLAG_NIGHTPVP, getTimeSeconds());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event){
        Entity entityDamager = event.getDamager();
        Entity entityDamagee = event.getEntity();
        nightPVP(event, entityDamager, entityDamagee);
    }

    public void nightPVP(EntityDamageByEntityEvent event, Entity entityDamager, Entity entityDamagee) {
        if (Main.config.getBoolean("NIGHT_PVP")) {
            if (day()) {
                if (!event.isCancelled()) { // PLAYER ATTACK PLAYER
                    if (entityDamager instanceof Player && entityDamagee instanceof Player) {
                        Player playerDamager = (Player) entityDamager;
                        Player playerDamagee = (Player) entityDamagee;
                        String playerDamagerName = playerDamager.getName();
                        String playerDamageeName = playerDamagee.getName();
                        pvpPreventMessageAndSound(playerDamager, playerDamagee, playerDamagerName, playerDamageeName);
                        event.setCancelled(true);
                    } else if (entityDamager instanceof Projectile) {
                        Projectile projectileDamager = (Projectile) entityDamager;
                        if (projectileDamager.getShooter() instanceof Player && entityDamagee instanceof Player) {
                            Player playerDamager = (Player) projectileDamager.getShooter();
                            Player playerDamagee = (Player) entityDamagee;
                            String playerDamagerName = playerDamager.getName();
                            String playerDamageeName = playerDamagee.getName();
                            pvpPreventMessageAndSound(playerDamager, playerDamagee, playerDamagerName, playerDamageeName);
                            event.setCancelled(true);
                        }
                    }
                } // PLAYER ATTACK PLAYER

                if (!event.isCancelled()) { // PLAYER ATTACK TAMED
                    if (entityDamagee instanceof Tameable) {
                        Tameable tameableDamagee = (Tameable) entityDamagee;
                        if (tameableDamagee.isTamed() && tameableDamagee.getOwner() != null) {
                            if (entityDamager instanceof Player && (tameableDamagee.getOwner() instanceof Player || tameableDamagee.getOwner() instanceof OfflinePlayer)) {
                                Player playerDamager = (Player) entityDamager;
                                Player playerDamagee = (Player) tameableDamagee.getOwner();
                                String playerDamagerName = playerDamager.getName();
                                String playerDamageeName = playerDamagee.getName();
                                pvpProtectTamedMessageAndSound(playerDamager, playerDamagee, playerDamagerName, playerDamageeName);
                                event.setCancelled(true);
                            } else if (entityDamager instanceof Projectile) {
                                Projectile projectileDamager = (Projectile) entityDamager;
                                if (projectileDamager.getShooter() instanceof Player && tameableDamagee.getOwner() instanceof Player) {
                                    Player playerDamager = (Player) projectileDamager.getShooter();
                                    Player playerDamagee = (Player) tameableDamagee.getOwner();
                                    String playerDamagerName = playerDamager.getName();
                                    String playerDamageeName = playerDamagee.getName();
                                    pvpProtectTamedMessageAndSound(playerDamager, playerDamagee, playerDamagerName, playerDamageeName);
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                } // PLAYER ATTACK TAMED
            }
        }
    }

    public void pvpPreventMessageAndSound(Player playerDamager, Player playerDamagee, String playerDamagerName, String playerDamageeName) {
        if (!messagesCooldowns.containsKey(playerDamagerName + " " + MESSAGE_FLAG_NIGHTPVP) ||
                !messagesCooldowns.containsKey(playerDamageeName + " " + MESSAGE_FLAG_NIGHTPVP) ||
                hasPassedSince(5, messagesCooldowns.get(playerDamagerName + " " + MESSAGE_FLAG_NIGHTPVP)) ||
                hasPassedSince(5, messagesCooldowns.get(playerDamageeName + " " + MESSAGE_FLAG_NIGHTPVP))) {
            playerDamager.sendMessage(ChatColor.DARK_RED + "O PvP só é liberado à noite!");
            playerDamager.sendMessage(ChatColor.DARK_RED + "Até lá, jogadores e criaturas domesticadas estão protegidas.");
            playerDamager.playSound(playerDamager.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);
            playerDamagee.sendMessage(ChatColor.DARK_RED + "Um jogador tentou te atacar!");
            playerDamagee.sendMessage(ChatColor.DARK_RED + "A proteção para jogadores e criaturas acaba quando ficar de noite!");
            playerDamagee.playSound(playerDamagee.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);
            messagesCooldowns.put(playerDamagerName + " " + MESSAGE_FLAG_NIGHTPVP, getTimeSeconds());
            messagesCooldowns.put(playerDamageeName + " " + MESSAGE_FLAG_NIGHTPVP, getTimeSeconds());
        }
    }

    public void pvpProtectTamedMessageAndSound(Player playerDamager, Player damageeOwner, String playerDamagerName, String damageeOwnerName) {
        if (!messagesCooldowns.containsKey(playerDamagerName + " " + MESSAGE_FLAG_NIGHTPVP_PROTECT_TAMED) ||
                !messagesCooldowns.containsKey(damageeOwnerName + " " + MESSAGE_FLAG_NIGHTPVP_PROTECT_TAMED) ||
                hasPassedSince(5, messagesCooldowns.get(playerDamagerName + " " + MESSAGE_FLAG_NIGHTPVP_PROTECT_TAMED)) ||
                hasPassedSince(5, messagesCooldowns.get(damageeOwnerName + " " + MESSAGE_FLAG_NIGHTPVP_PROTECT_TAMED))) {
            messagesCooldowns.put(playerDamagerName + " " + MESSAGE_FLAG_NIGHTPVP_PROTECT_TAMED, getTimeSeconds());
            messagesCooldowns.put(damageeOwnerName + " " + MESSAGE_FLAG_NIGHTPVP_PROTECT_TAMED, getTimeSeconds());
            playerDamager.sendMessage(ChatColor.DARK_RED + "O PvP só é liberado à noite!");
            playerDamager.sendMessage(ChatColor.DARK_RED + "Até lá, jogadores e criaturas domesticadas estão protegidas.");
            playerDamager.playSound(playerDamager.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);
            damageeOwner.sendMessage(ChatColor.DARK_RED + "Um jogador tentou atacar uma de suas criaturas domesticadas! ");
            damageeOwner.sendMessage(ChatColor.DARK_RED + "A proteção para jogadores e criaturas acaba quando ficar de noite!");
            damageeOwner.playSound(damageeOwner.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);
        }
    }











    public static boolean day() {
        Server server = getServer();
        long time = Objects.requireNonNull(server.getWorld(WORLD_NAME)).getTime();
        return (time > 0 && time < 13000);
    }

    public static boolean hasPassedSince(int seconds, long initialTime) {
        return getTimeRemaining(seconds, initialTime) <= 0;
    }

    public static String getFormattedTimeRemaining(int seconds, Long timePrevious) {
        return formatTime(getTimeRemaining(seconds, timePrevious));
    }

    public static String formatTime(long time) {
        String formattedTime;
        if (time >= 3600) {
            formattedTime = time / 3600 + " hora(s), ";
            formattedTime = formattedTime + (time % 3600) / 60 + " minuto(s) e ";
            formattedTime = formattedTime + time % 60 + " segundo(s)";
        } else if (time >= 60) {
            formattedTime = time / 60 + " minuto(s) e ";
            formattedTime = formattedTime + time % 60 + " segundo(s)";
        } else if (time > 0) {
            formattedTime = time + " segundos";
        } else {
            formattedTime = "0 segundos";
        }
        return formattedTime;
    }

    public static long getTimeRemaining(long seconds, long timePrevious) {
        return seconds - (getTimeSeconds() - timePrevious);
    }

    public static long getTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

}
