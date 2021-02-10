package tyo_drak.draksrpgclasses.misc;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tyo_drak.draksrpgclasses.Debug;
import tyo_drak.draksrpgclasses.Main;
import tyo_drak.draksrpgclasses.MainEvents;
import tyo_drak.draksrpgclasses.classes.*;

public class DraksPlayers {
    public static final int REGULAR_REVIVE_TIME_SECONDS = 600;
    public static final int DEBUG_REVIVE_TIME_SECONDS = 10;
    public static final String PLAYER_SPAWNPOINT_TAG = "Spawn";

    public static long getPlayerDeathTime(Player player) {
        String playerName = player.getName();
        if (MainEvents.PLAYER_DEATH_TIME.containsKey(playerName + MainEvents.PLAYER_DEATH_TAG)) {
            return MainEvents.PLAYER_DEATH_TIME.get(playerName + MainEvents.PLAYER_DEATH_TAG);
        } else {
            return Main.config.getLong(playerName + MainEvents.PLAYER_DEATH_TAG);
        }
    }

    public static void forceSpectatorToDeadPlayers(Player player) {
        if (!playerCanRevive(player.getName()) && !player.isOp()) {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    public static void playerWarnUnrevivable(Player player) {
        denyPlayerAction(player, "CANT_REVIVE_YET", "Espere mais " + getFormattedRespawnTimeRemaining(player) + " para reviver.");
    }

    public static void addPlayerDeathTime(Player player) {
        String playerName = player.getName();
        //MainEvents.spectatorJoinTime.put(playerName, MainEvents.getTimeSeconds());
        Main.config.set(playerName + MainEvents.PLAYER_DEATH_TAG, MainEvents.getTimeSeconds());
        Main.config.options().copyDefaults(true);
        Main.plugin.saveConfig();
    }

    public static void playerRevive(Player player) {
        String playerName = player.getName();
        if (playerCanRevive(playerName)) {
            forcePlayerRevive(player);
        }
    }

    public static void forcePlayerRevive(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        setClassAttributes(player);
        applyPlayerPotionEffects(player);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setFoodLevel(20);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        player.sendMessage(ChatColor.YELLOW + "Você foi revivido!");
        Debug.consoleMessage("Revived player " + player.getName() + "." + (isVIP(player.getName()) ? " (VIP)" : ""));
    }

    public static boolean playerCanRevive(String playerName) {

        if (Main.config.getBoolean("SYSTEM_DEBUG")) {
            if (MainEvents.PLAYER_DEATH_TIME.containsKey(playerName + MainEvents.PLAYER_DEATH_TAG)) {
                return MainEvents.hasPassedSince(DEBUG_REVIVE_TIME_SECONDS, MainEvents.PLAYER_DEATH_TIME.get(playerName + MainEvents.PLAYER_DEATH_TAG));
            } else if (Main.config.contains(playerName + MainEvents.PLAYER_DEATH_TAG)) {
                return MainEvents.hasPassedSince(DEBUG_REVIVE_TIME_SECONDS, Main.config.getLong(playerName + MainEvents.PLAYER_DEATH_TAG));
            } else {
                return true;
            }
        } else if (isVIP(playerName)) {
            if (MainEvents.PLAYER_DEATH_TIME.containsKey(playerName + MainEvents.PLAYER_DEATH_TAG)) {
                return MainEvents.hasPassedSince(VIP_REVIVE_TIME_SECONDS, MainEvents.PLAYER_DEATH_TIME.get(playerName + MainEvents.PLAYER_DEATH_TAG));
            } else if (Main.config.contains(playerName + MainEvents.PLAYER_DEATH_TAG)) {
                return MainEvents.hasPassedSince(VIP_REVIVE_TIME_SECONDS, Main.config.getLong(playerName + MainEvents.PLAYER_DEATH_TAG));
            } else {
                return true;
            }
        } else {
            if (MainEvents.PLAYER_DEATH_TIME.containsKey(playerName + MainEvents.PLAYER_DEATH_TAG)) {
                return MainEvents.hasPassedSince(REGULAR_REVIVE_TIME_SECONDS, MainEvents.PLAYER_DEATH_TIME.get(playerName + MainEvents.PLAYER_DEATH_TAG));
            } else if (Main.config.contains(playerName + MainEvents.PLAYER_DEATH_TAG)) {
                return MainEvents.hasPassedSince(REGULAR_REVIVE_TIME_SECONDS, Main.config.getLong(playerName + MainEvents.PLAYER_DEATH_TAG));
            } else {
                return true;
            }
        }

    }

    public static void applyPlayerPotionEffects(Player player) {
        DraksEntities.applyNightmareHunger(player);
        resetClassEffects(player);
    }

    public static String getFormattedRespawnTimeRemaining(Player player) {
        if (isVIP(player.getName())) {
            return DraksTime.formatTime(DraksTime.getTimeRemaining(VIP_REVIVE_TIME_SECONDS, getPlayerDeathTime(player)));
        } else {
            return DraksTime.formatTime(DraksTime.getTimeRemaining(REGULAR_REVIVE_TIME_SECONDS, getPlayerDeathTime(player)));
        }
    }


    //<editor-fold defaultstate="collapsed" desc="EFFECTS AND ATTRIBUTES">
    public static void applyClassEffects(Player player) {
        DraksEntities.applyNightmareHunger(player);
        if (player.hasPermission(Druida.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Druida.CLASS_EFFECTS) {
                DraksEntities.applyEffectInvisible(player, effect);
            }
            Debug.consoleMessage("Applied DRUIDA effects to player: " + player.getName() + ".");
        } else if (player.hasPermission(Ferreiro.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Ferreiro.CLASS_EFFECTS) {
                if (player.hasPermission(Ferreiro.MASTERY_PERMISSION) && effect.getType().equals(PotionEffectType.FAST_DIGGING)) {
                    DraksEntities.applyEffectInvisible(player, PotionEffectType.FAST_DIGGING, 99999, 4);
                } else {
                    DraksEntities.applyEffectInvisible(player, effect);
                }
            }
            Debug.consoleMessage("Applied FERREIRO effects to player: " + player.getName() + ".");
        } else if (player.hasPermission(Gatuno.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Gatuno.CLASS_EFFECTS) {
                DraksEntities.applyEffectInvisible(player, effect);
            }
            Debug.consoleMessage("Applied GATUNO effects to player: " + player.getName() + ".");
        } else if (player.hasPermission(Espadachim.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Espadachim.CLASS_EFFECTS) {
                if (effect.getType().equals(PotionEffectType.REGENERATION)) {
                    if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                        DraksEntities.applyEffectInvisible(player, effect);
                    }
                } else {
                    DraksEntities.applyEffectInvisible(player, effect);
                }
            }
            Debug.consoleMessage("Applied ESPADACHIM effects to player: " + player.getName() + ".");
        } /* else if (player.hasPermission(Cacador.BASE_PERMISSION)){
            // NONE TO BE ADDED
            //Config.sout("Applied CACADOR effects to player: " +player.getName()+ ".");
        } */
    }

    public static void removeWrongClassEffects(Player player) {
        if (!player.hasPermission(Druida.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Druida.CLASS_EFFECTS) {
                if (DraksEntities.hasPotionEffect(player, effect.getDuration(), effect.getType())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
        if (!player.hasPermission(Ferreiro.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Ferreiro.CLASS_EFFECTS) {
                if (DraksEntities.hasPotionEffect(player, effect.getDuration(), effect.getType())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
        if (!player.hasPermission(Gatuno.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Gatuno.CLASS_EFFECTS) {
                if (DraksEntities.hasPotionEffect(player, effect.getDuration(), effect.getType())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
        if (!player.hasPermission(Espadachim.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Espadachim.CLASS_EFFECTS) {
                if (DraksEntities.hasPotionEffect(player, effect.getDuration(), effect.getType())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        } /* else if (player.hasPermission(Cacador.BASE_PERMISSION)){
            // NONE TO BE ADDED
            //Config.sout("Applied CACADOR effects to player: " +player.getName()+ ".");
        } */
    }

    public static void resetClassEffects(Player player) {
        removeWrongClassEffects(player);
        applyClassEffects(player);
    } // FINE

    public static void setClassAttributes(Player player) {
        if (player.hasPermission(Cacador.BASE_PERMISSION)) {            // CAÇADOR
            DraksEntities.setBaseMaxHealth(player, Cacador.BASE_HEALTH); // 8
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.115);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.05);
        } else if (player.hasPermission(Druida.BASE_PERMISSION)) {      // DRUIDA
            DraksEntities.setBaseMaxHealth(player, Druida.BASE_HEALTH); // 6
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.11);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
        } else if (player.hasPermission(Espadachim.BASE_PERMISSION)) {  // ESPADACHIM
            DraksEntities.setBaseMaxHealth(player, Espadachim.BASE_HEALTH); // 28
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.08);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(3.5);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.6);
        } else if (player.hasPermission(Ferreiro.BASE_PERMISSION)) {    // FERREIRO
            DraksEntities.setBaseMaxHealth(player, Ferreiro.BASE_HEALTH); // 16
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.095);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.5);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.2);
        } else if (player.hasPermission(Gatuno.BASE_PERMISSION)) {      // GATUNO
            DraksEntities.setBaseMaxHealth(player, Gatuno.BASE_HEALTH); // 12
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(6);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.13);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(5);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.1);
        } else {                                                        // APRENDIZ
            DraksEntities.setBaseMaxHealth(player, 10);
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.09);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(2);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.1);
        }
        Debug.consoleMessage("Set Class attributes for " + player.getName() + ".");

    } // FINE
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="MISC">
    public static void denyPlayerAction(Player player, String messageKey, String string) {
        String cooldownKey = player.getName() + " " + messageKey;
        if (!MainEvents.messagesCooldowns.containsKey(cooldownKey) || MainEvents.hasPassedSince(1, MainEvents.messagesCooldowns.get(cooldownKey))) {
            MainEvents.messagesCooldowns.put(cooldownKey, MainEvents.getTimeSeconds());
            player.sendMessage(string);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    public static void acceptPlayerAction(Player player, String messageKey, String string) {
        String cooldownKey = player.getName() + " " + messageKey;
        if (!MainEvents.messagesCooldowns.containsKey(cooldownKey) || MainEvents.hasPassedSince(1, MainEvents.messagesCooldowns.get(cooldownKey))) {
            MainEvents.messagesCooldowns.put(cooldownKey, MainEvents.getTimeSeconds());
            player.sendMessage(string);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
        }
    }
    //</editor-fold>



    public static boolean isVIP(String playerName) {
        return Main.config.getStringList("VIPS").contains(playerName);
    }

    public static final String VIP_BASIC_KIT_FLAG = ".basic";
    public static final int VIP_BASIC_KIT_COOLDOWN_SECONDS = 1800;
    public static final int VIP_REVIVE_TIME_SECONDS = 150;

    public static void giveVIPKitBasic(Player p){
        p.getInventory().addItem(new ItemStack(Material.STONE_SWORD, MainEvents.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE, MainEvents.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL, MainEvents.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.STONE_AXE, MainEvents.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.STONE_HOE, MainEvents.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.APPLE, 6 + MainEvents.random(0, 6)));
    }
    //</editor-fold>



    //<editor-fold defaultstate="collapsed" desc="EXPERIENCE">
    // Calculate amount of EXP needed to level up
    public static int getExpToLevelUp(int level){
        if(level <= 15){
            return 2*level+7;
        } else if(level <= 30){
            return 5*level-38;
        } else {
            return 9*level-158;
        }
    }

    // Calculate total experience up to a level
    public static int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }

    // Calculate player's current EXP amount
    public static int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();

        // Get the amount of XP in past levels
        exp += getExpAtLevel(level);

        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.getExp());

        return exp;
    }
    //</editor-fold>

}
