package tyo_drak.draksrpgclasses.classes;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tyo_drak.drakslib.Entities;
import tyo_drak.drakslib.Misc;

public abstract class RPGClass {
    public static Enum<ChatColor> CLASS_COLOR;
    public static Integer BASE_HEALTH;
    public static PotionEffect[] CLASS_EFFECTS;
    public static PotionEffectType[] CLASS_IMMUNITIES;

    // PERMISSIONS
    public static String BASE_PERMISSION;

    public static Enum<ChatColor> getPlayerClassColor(Player player){
        if (player.hasPermission(Druida.BASE_PERMISSION)){
            return Druida.CLASS_COLOR;
        } else if (player.hasPermission(Cacador.BASE_PERMISSION)){
            return Cacador.CLASS_COLOR;
        } else if (player.hasPermission(Ferreiro.BASE_PERMISSION)){
            return Ferreiro.CLASS_COLOR;
        } else if (player.hasPermission(Gatuno.BASE_PERMISSION)){
            return Gatuno.CLASS_COLOR;
        } else if (player.hasPermission(Espadachim.BASE_PERMISSION)){
            return Espadachim.CLASS_COLOR;
        } else {
            return Aprendiz.CLASS_COLOR;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="EFFECTS AND ATTRIBUTES">
    public static void applyClassEffects(Player player) {
        if (player.hasPermission(Druida.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Druida.CLASS_EFFECTS) {
                Entities.applyEffectInvisible(player, effect);
            }
            Misc.dLog("Applied DRUIDA effects to player: " + player.getName() + ".");
        } else if (player.hasPermission(Ferreiro.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Ferreiro.CLASS_EFFECTS) {
                if (player.hasPermission(Ferreiro.MASTERY_PERMISSION) && effect.getType().equals(PotionEffectType.FAST_DIGGING)) {
                    Entities.applyEffectInvisible(player, PotionEffectType.FAST_DIGGING, 99999, 4);
                } else {
                    Entities.applyEffectInvisible(player, effect);
                }
            }
            Misc.dLog("Applied FERREIRO effects to player: " + player.getName() + ".");
        } else if (player.hasPermission(Gatuno.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Gatuno.CLASS_EFFECTS) {
                Entities.applyEffectInvisible(player, effect);
            }
            Misc.dLog("Applied GATUNO effects to player: " + player.getName() + ".");
        } else if (player.hasPermission(Espadachim.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Espadachim.CLASS_EFFECTS) {
                if (effect.getType().equals(PotionEffectType.REGENERATION)) {
                    if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                        Entities.applyEffectInvisible(player, effect);
                    }
                } else {
                    Entities.applyEffectInvisible(player, effect);
                }
            }
            Misc.dLog("Applied ESPADACHIM effects to player: " + player.getName() + ".");
        } /* else if (player.hasPermission(Cacador.BASE_PERMISSION)){
            // NONE TO BE ADDED
            //Config.sout("Applied CACADOR effects to player: " +player.getName()+ ".");
        } */
    }

    public static void removeWrongClassEffects(Player player) {
        if (!player.hasPermission(Druida.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Druida.CLASS_EFFECTS) {
                if (Entities.hasPotionEffect(player, effect.getDuration(), effect.getType())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
        if (!player.hasPermission(Ferreiro.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Ferreiro.CLASS_EFFECTS) {
                if (Entities.hasPotionEffect(player, effect.getDuration(), effect.getType())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
        if (!player.hasPermission(Gatuno.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Gatuno.CLASS_EFFECTS) {
                if (Entities.hasPotionEffect(player, effect.getDuration(), effect.getType())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
        if (!player.hasPermission(Espadachim.BASE_PERMISSION)) {
            for (PotionEffect effect :
                    Espadachim.CLASS_EFFECTS) {
                if (Entities.hasPotionEffect(player, effect.getDuration(), effect.getType())) {
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
            Entities.setBaseMaxHealth(player, Cacador.BASE_HEALTH); // 8
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.115);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.05);
        } else if (player.hasPermission(Druida.BASE_PERMISSION)) {      // DRUIDA
            Entities.setBaseMaxHealth(player, Druida.BASE_HEALTH); // 6
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.11);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
        } else if (player.hasPermission(Espadachim.BASE_PERMISSION)) {  // ESPADACHIM
            Entities.setBaseMaxHealth(player, Espadachim.BASE_HEALTH); // 28
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.08);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(3.5);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.6);
        } else if (player.hasPermission(Ferreiro.BASE_PERMISSION)) {    // FERREIRO
            Entities.setBaseMaxHealth(player, Ferreiro.BASE_HEALTH); // 16
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.095);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.5);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.2);
        } else if (player.hasPermission(Gatuno.BASE_PERMISSION)) {      // GATUNO
            Entities.setBaseMaxHealth(player, Gatuno.BASE_HEALTH); // 12
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(6);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.13);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(5);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.1);
        } else {                                                        // APRENDIZ
            Entities.setBaseMaxHealth(player, 10);
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.09);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(2);
            player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.1);
        }
        Misc.dLog("Set Class attributes for " + player.getName() + ".");

    }
    //</editor-fold>
}
