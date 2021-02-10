package tyo_drak.draksrpgclasses.classes;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tyo_drak.draksrpgclasses.Checks;
import tyo_drak.draksrpgclasses.Main;
import tyo_drak.draksrpgclasses.MainEvents;
import tyo_drak.draksrpgclasses.misc.DraksEntities;
import tyo_drak.draksrpgclasses.misc.DraksPlayers;

public class Cacador {

    // CHARACTERISTICS
    public static final Enum<ChatColor> CLASS_COLOR = ChatColor.GRAY;
    public static final Color CACADOR_ARROW_COLOR = Color.GRAY;
    public static final Integer BASE_HEALTH = 8;
    public static final PotionEffect[] CLASS_EFFECTS = {};
    public static final PotionEffectType[] CLASS_IMMUNITIES = {PotionEffectType.BLINDNESS};

    // PERMISSIONS
    public static final String BASE_PERMISSION = "group.cacador";
    public static final String CACADOR_SILVER_ARROW_NAME = "Flecha de Prata";

    public static int CACADOR_ARROW_BUFF_MIN_LEVEL = 2;

    public static void applyCacadorArrowBuff(Arrow arrow) {
        if (!arrow.hasCustomEffect(PotionEffectType.HARM)) {
            arrow.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 1), false);
            arrow.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 30, 1), false);
            arrow.setColor(CACADOR_ARROW_COLOR);
            arrow.setCustomName(CACADOR_SILVER_ARROW_NAME);
        }
    }

    public static int CACADOR_FAT_QUIVER_MIN_LEVEL = 8;

    public static void fatQuiver(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getLevel() >= CACADOR_FAT_QUIVER_MIN_LEVEL) {
                int d100 = MainEvents.random(1, 100);
                if (d100 >= 51 && d100 < 90) {
                    ((Player) event.getEntity()).getInventory().addItem(new ItemStack(Material.ARROW, 1));
                } else if (d100 >= 91) {
                    ((Player) event.getEntity()).getInventory().addItem(new ItemStack(Material.ARROW, 2));
                }
            }
        }
    }

    public static int CACADOR_WOLF_TARGET_MIN_LEVEL = 15;

    public static void wolfTarget(EntityTargetLivingEntityEvent event, Entity entity) {
        if (!event.isCancelled()) {
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                if (wolf.isTamed() && wolf.getOwner() instanceof Player) {
                    Player playerOwner = (Player) wolf.getOwner();
                    if (playerOwner.hasPermission(Cacador.BASE_PERMISSION)) {
                        if (playerOwner.getLevel() >= CACADOR_WOLF_TARGET_MIN_LEVEL) {
                            wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.45);
                            wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8);
                            wolf.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.5);
                            DraksEntities.setBaseMaxHealth(wolf, 10);
                            wolf.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
                            DraksEntities.applyEffectShiny(wolf, PotionEffectType.REGENERATION, 99999, 1);
                        } else {
                            event.setCancelled(true);
                            DraksPlayers.denyPlayerAction(playerOwner, "NOT_ENOUGH_LEVEL", "Você não tem nível suficiente para usar lobos em combate!");
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public static void shootBow(EntityShootBowEvent event) {
        if (!event.isCancelled()) {
            if (!event.getBow().getType().equals(Material.BOW)) {
                if (event.getEntity() instanceof Player) {
                    Player player = (Player) event.getEntity();
                    if (event.getProjectile() instanceof Arrow) {
                        Arrow arrow = (Arrow) event.getProjectile();
                        if (player.hasPermission(Cacador.BASE_PERMISSION) && player.getLevel() >= CACADOR_ARROW_BUFF_MIN_LEVEL) {
                            applyCacadorArrowBuff(arrow);
                        }
                    }
                }
            }
        }
    }

    public static void invertArrowDamageEffect(ProjectileHitEvent event) {
        if (event != null) {
            if (event.getEntity() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getEntity();
                if (arrow.getCustomName() != null && event.getHitEntity() != null) {
                    if (arrow.getCustomName().equals(CACADOR_SILVER_ARROW_NAME) && event.getHitEntity() instanceof LivingEntity) {
                        LivingEntity entity = (LivingEntity) event.getHitEntity();
                        if (Checks.isUndead(entity.getType())) {
                            arrow.removeCustomEffect(PotionEffectType.HARM);
                            if (!arrow.hasCustomEffect(PotionEffectType.HEAL)) {
                                arrow.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1), false);
                                arrow.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1), false);
                            }
                            arrow.setColor(CACADOR_ARROW_COLOR);
                        }
                    }
                }
            }
        }
    }

    public static void uninvertArrowDamageEffect(ProjectileHitEvent event) {
        if (event != null) {
            if (event.getEntity() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getEntity();
                if (arrow.getCustomName() != null && event.getHitEntity() != null) {
                    if (arrow.getCustomName().equals(CACADOR_SILVER_ARROW_NAME) && event.getHitEntity() instanceof LivingEntity) {
                        LivingEntity entity = (LivingEntity) event.getHitEntity();
                        if (!Checks.isUndead(entity.getType()) && arrow.hasCustomEffect(PotionEffectType.HEAL)) {
                            arrow.removeCustomEffect(PotionEffectType.HEAL);
                            if (!arrow.hasCustomEffect(PotionEffectType.HARM)) {
                                arrow.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 1), false);
                                arrow.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1), false);
                            }
                            arrow.setColor(CACADOR_ARROW_COLOR);
                        }
                    }
                }
            }
        }
    }

    public static void applyImmunities(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getNewEffect() != null) {
                if (Main.config.getBoolean("RPG_CLASSES")) {
                    if (player.hasPermission(Cacador.BASE_PERMISSION)) {
                        for (PotionEffectType immunity :
                                Cacador.CLASS_IMMUNITIES) {
                            if (event.getNewEffect().getType().equals(immunity)) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void arrowHitProficiency(EntityDamageByEntityEvent event, Entity entityDamagee, ItemStack itemMain) {
        if (!event.isCancelled()) {
            if (Checks.isEvil(entityDamagee.getType())) {
                int xpGenerated = 4;
                for (Enchantment enchantment :
                        itemMain.getEnchantments().keySet()) {
                    xpGenerated += itemMain.getEnchantmentLevel(enchantment) * 2;
                }
                ExperienceOrb experienceOrb = (ExperienceOrb) entityDamagee.getWorld().spawnEntity(entityDamagee.getLocation(), EntityType.EXPERIENCE_ORB);
                experienceOrb.setExperience(xpGenerated);
            }
        }
    }

}
