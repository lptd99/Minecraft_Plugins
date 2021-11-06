package tyo_drak.drakslib;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Players {
    public static String VIP_BASIC_KIT_FLAG = "VIP_BASIC_KIT";
    public static int VIP_BASIC_KIT_COOLDOWN_SECONDS = 1800;

    //<editor-fold defaultstate="collapsed" desc="MISC">
    public static void denyPlayerAction(Player player, String messageKey, String string) {
        String cooldownKey = player.getName() + " " + messageKey;
        if (!MainEvents.messagesCooldowns.containsKey(cooldownKey) || Time.hasPassedSince(1, MainEvents.messagesCooldowns.get(cooldownKey))) {
            MainEvents.messagesCooldowns.put(cooldownKey, Time.getTimeSeconds());
            player.sendMessage(string);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    public static void acceptPlayerAction(Player player, String messageKey, String string) {
        String cooldownKey = player.getName() + " " + messageKey;
        if (!MainEvents.messagesCooldowns.containsKey(cooldownKey) || Time.hasPassedSince(1, MainEvents.messagesCooldowns.get(cooldownKey))) {
            MainEvents.messagesCooldowns.put(cooldownKey, Time.getTimeSeconds());
            player.sendMessage(string);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
        }
    }
    //</editor-fold>

    public static boolean isVIP(String playerName) {
        return DraksLibMain.config.getStringList("VIPS").contains(playerName);
    }

    public static void giveVIPKitBasic(Player p){
        p.getInventory().addItem(new ItemStack(Material.STONE_SWORD, Misc.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE, Misc.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL, Misc.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.STONE_AXE, Misc.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.STONE_HOE, Misc.random(0,3) / 2));
        p.getInventory().addItem(new ItemStack(Material.APPLE, 6 + Misc.random(0, 6)));
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
