package tyo_drak.draksnightmare.misc;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tyo_drak.draksrpgclasses.Debug;
import tyo_drak.draksrpgclasses.Main;

public class DraksEntities {

    //<editor-fold defaultstate="" desc="ENTITY">
    public static Entity duplicate(Entity entity) {
        return spawnEntity(entity.getType(), entity.getWorld(), entity.getLocation());
    }

    public static Entity spawnEntity(EntityType entityType, World world, Location location) {
        return world.spawnEntity(location, entityType);
    }
    //</editor-fold>

    //<editor-fold defaultstate="" desc="EFFECTS">
    public static void setBaseMaxHealth(LivingEntity livingEntity, double baseMaxHealth) {
        double currentHealthPercentage = livingEntity.getHealth() / livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseMaxHealth);
        livingEntity.setHealth(currentHealthPercentage * livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        if (livingEntity.getHealth() < 0) {
            livingEntity.setHealth(1);
        } else if (livingEntity.getHealth() > livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }
        Debug.consoleMessage("LivingEntity " + livingEntity.getName() + "'s Base Max Health set.");
    } // PERFECT

    public static boolean hasPotionEffect(LivingEntity livingEntity, PotionEffectType potionEffectType, int remainingDuration, int level) {
        for (PotionEffect potionEffect :
                livingEntity.getActivePotionEffects()) {
            if (potionEffect.getType().equals(potionEffectType)) {
                if (potionEffect.getAmplifier() + 1 == level && potionEffect.getDuration() >= remainingDuration) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void applyNightmareHunger(Entity entity) {
        if (entity instanceof Player) {
            if (Main.config.getBoolean("DONT_STARVE") || Main.config.getBoolean("NIGHTMARE")) {
                applyEffectInvisible((Player) entity, PotionEffectType.HUNGER, 99999, 1);
            }
        }
    }

    public static boolean hasPotionEffect(LivingEntity livingEntity, int remainingDuration, PotionEffectType potionEffectType) {
        for (PotionEffect potionEffect :
                livingEntity.getActivePotionEffects()) {
            if (potionEffect.getType().equals(potionEffectType)) {
                if (potionEffect.getDuration() >= remainingDuration) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void applyEffectShiny(LivingEntity livingEntity, PotionEffectType potionEffectType, int seconds, int level) {

        livingEntity.addPotionEffect(new PotionEffect(potionEffectType, seconds * 20, level - 1, true, true, true));

    }

    public static void applyEffectVisible(LivingEntity livingEntity, PotionEffectType potionEffectType, int seconds, int level) {

        livingEntity.addPotionEffect(new PotionEffect(potionEffectType, seconds * 20, level - 1, false, true, true));

    }

    public static void applyEffectSubtle(LivingEntity livingEntity, PotionEffectType potionEffectType, int seconds, int level) {

        livingEntity.addPotionEffect(new PotionEffect(potionEffectType, seconds * 20, level - 1, false, false, true));

    }

    public static void applyEffectInvisible(LivingEntity livingEntity, PotionEffectType potionEffectType, int seconds, int level) {

        livingEntity.addPotionEffect(new PotionEffect(potionEffectType, seconds * 20, level - 1, false, false, false));

    }

    public static void applyEffectInvisible(LivingEntity livingEntity, PotionEffect potionEffect) {

        livingEntity.addPotionEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration(), potionEffect.getAmplifier(), false, false, false));

    }
    //</editor-fold>

}
