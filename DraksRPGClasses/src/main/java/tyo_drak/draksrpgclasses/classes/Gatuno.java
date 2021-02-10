package tyo_drak.draksrpgclasses.classes;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import tyo_drak.draksrpgclasses.Checks;
import tyo_drak.draksrpgclasses.Debug;
import tyo_drak.draksrpgclasses.Main;
import tyo_drak.draksrpgclasses.MainEvents;
import tyo_drak.draksrpgclasses.misc.DraksEntities;
import tyo_drak.draksrpgclasses.misc.DraksItems;
import tyo_drak.draksrpgclasses.misc.DraksPlayers;
import tyo_drak.draksrpgclasses.misc.DraksTime;

public class Gatuno implements Listener {

    // CHARACTERISTICS
    public static final Enum<ChatColor> CLASS_COLOR = ChatColor.DARK_GRAY;
    public static final Integer BASE_HEALTH = 12;
    public static final PotionEffect[] CLASS_EFFECTS = {new PotionEffect(PotionEffectType.JUMP, 99999, 1),
            new PotionEffect(PotionEffectType.LUCK, 99999, 2)};
    public static final PotionEffectType[] CLASS_IMMUNITIES = {PotionEffectType.SLOW};

    // PERMISSIONS
    public static final String BASE_PERMISSION = "group.gatuno";

    // MAGIC NUMBERS
    public static final int BACKSTAB_DEGREES = 60;
    public static final double BACKSTAB_DAMAGE_FACTOR = 1.5;

    public static final int NIGHT_VEIL_COOLDOWN = 120;
    public static final int NIGHT_VEIL_DURATION_FACTOR = 2;
    public static final int NIGHT_VEIL_BONUS_SPEED_DURATION_FACTOR = 3;
    public static final int NIGHT_VEIL_BONUS_DAMAGE_LEVEL = 2;

    public static final int RARE_ITEM_DROP_PERCENTAGE = 5;
    public static final int UNCOMMON_ITEM_DROP_PERCENTAGE = 15;
    public static final int COMMMON_ITEM_DROP_PERCENTAGE = 40;

    public static int GATUNO_POISON_MIN_LEVEL = 4;
    public static void gatunoPoison(EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamage() > 0) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                if (player.hasPermission(BASE_PERMISSION) && player.getLevel() >= GATUNO_POISON_MIN_LEVEL) {
                    if (event.getEntity() instanceof Mob) {
                        ((Mob) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 2));
                    } else if (event.getEntity() instanceof Player && !((Player) event.getEntity()).isBlocking()) {
                        if (!DraksTime.isDayOnWorld()) {
                            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 2));
                        }
                    }
                }
            }
        }
    }

    public static int GATUNO_NIGHT_VEIL_MIN_LEVEL = 10;
    public static void nightVeil(PlayerInteractEvent event, Player player, Material itemMainType, Material itemOffType) {
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                String playerName = player.getName();
                if (player.hasPermission(BASE_PERMISSION) && player.getLevel() >= GATUNO_NIGHT_VEIL_MIN_LEVEL) {
                    if (Checks.isHoe(itemMainType)) {
                        if (!MainEvents.skillsCooldowns.containsKey(playerName + " Véu da Noite") || MainEvents.hasPassedSince(NIGHT_VEIL_COOLDOWN, MainEvents.skillsCooldowns.get(playerName + " Véu da Noite")) || player.hasPermission(BASE_PERMISSION + ".bypass")) {
                            int skillDurationSeconds = player.getLevel();
                            if (!itemOffType.equals(Material.PHANTOM_MEMBRANE)) {
                                skillDurationSeconds = skillDurationSeconds / NIGHT_VEIL_DURATION_FACTOR;
                            }
                            player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                            Debug.consoleMessage(playerName + " triggered Night Veil.");
                            MainEvents.skillsCooldowns.put(playerName + " Véu da Noite", MainEvents.getTimeSeconds());
                            DraksEntities.applyEffectSubtle(player, PotionEffectType.SPEED, skillDurationSeconds / NIGHT_VEIL_BONUS_SPEED_DURATION_FACTOR, NIGHT_VEIL_BONUS_DAMAGE_LEVEL + 1);
                            DraksEntities.applyEffectSubtle(player, PotionEffectType.INCREASE_DAMAGE, skillDurationSeconds, NIGHT_VEIL_BONUS_DAMAGE_LEVEL);
                            DraksEntities.applyEffectSubtle(player, PotionEffectType.INVISIBILITY, skillDurationSeconds, NIGHT_VEIL_BONUS_DAMAGE_LEVEL - 1);
                        } else {
                            DraksPlayers.denyPlayerAction(player, "GATUNO_NIGHT_VEIL_COOLDOWN", "Você deve esperar mais " + MainEvents.getFormattedTimeRemaining(NIGHT_VEIL_COOLDOWN, MainEvents.skillsCooldowns.get(playerName + " Véu da Noite")) + " para usar esta habilidade!");
                        }
                    }
                }
            }
        }
    }

    public static void backstab(EntityDamageByEntityEvent event) {
        if (!event.isCancelled()) {
            if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player) {
                LivingEntity livingEntityDamagee = (LivingEntity) event.getEntity();
                Player playerDamager = (Player) event.getDamager();
                if (playerDamager.hasPermission(BASE_PERMISSION) && isBackstab(playerDamager, livingEntityDamagee)) {
                    event.setDamage((event.getDamage() + Math.sqrt(playerDamager.getLevel())) * BACKSTAB_DAMAGE_FACTOR);
                    ExperienceOrb experienceOrb = (ExperienceOrb) livingEntityDamagee.getWorld().spawnEntity(livingEntityDamagee.getLocation(), EntityType.EXPERIENCE_ORB);
                    String expGeneratedString = "" + Math.sqrt(playerDamager.getLevel()) * BACKSTAB_DAMAGE_FACTOR;
                    int expGenerated = Integer.parseInt(expGeneratedString.split(".")[0]);
                    experienceOrb.setExperience(expGenerated);
                }
            }
        }
    }

    public static boolean isBackstab(Player backstabber, LivingEntity backstabbed) {
        Location backstabberLocation = backstabber.getLocation();
        Location backstabbedLocation = backstabbed.getLocation();

        Vector toBackstabber = backstabberLocation.toVector().subtract(backstabbedLocation.toVector());
        Vector backstabbedLineOfSight = backstabbed.getEyeLocation().getDirection();

        Vector toBackstabberNormalized = toBackstabber.normalize();

        double cos = toBackstabberNormalized.dot(backstabbedLineOfSight);
        double degrees = Math.toDegrees(Math.acos(cos));

        return degrees >= BACKSTAB_DEGREES;
    }

    public static boolean isUnderNightVeil(Player player) {
        boolean b = false;
        if (MainEvents.skillsCooldowns.containsKey(player.getName() + " Véu da Noite")) {
            if (!MainEvents.hasPassedSince(NIGHT_VEIL_DURATION_FACTOR, MainEvents.skillsCooldowns.get(player.getName() + " Véu da Noite"))) {
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY) && player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                    b = true;
                }
            }
        }
        return b;
    }

    public static void endNightVeilBonusDamage(EntityDamageByEntityEvent event) {
        if (!event.isCancelled() || event.getDamage() == 0) {
            if (event.getDamager() instanceof Player) {
                Player playerDamager = (Player) event.getDamager();
                if (isUnderNightVeil(playerDamager)) {
                    playerDamager.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    Debug.consoleMessage(playerDamager.getName() + " ended Night Veil on hit.");
                }
            }
        }
    }

    public static void givePlayerMonsterRareLoot(Player player, Entity entity, int d100) {
        if (player.hasPermission(BASE_PERMISSION)) {
            int playerLevel = player.getLevel();
            switch (entity.getType()) {
                case ELDER_GUARDIAN:
                    safeDropRareItem(entity.getLocation(), Material.SPONGE, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case WITHER_SKELETON:
                    safeDropRareItem(entity.getLocation(), Material.WITHER_SKELETON_SKULL, d100, UNCOMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    safeDropRareItem(entity.getLocation(), Material.WITHER_ROSE, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case VEX:
                    safeDropRareItem(entity.getLocation(), Material.PHANTOM_MEMBRANE, d100, COMMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case CREEPER:
                    if (((Creeper) entity).isPowered()) {
                        safeDropRareItem(entity.getLocation(), Material.TNT, d100, COMMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    } else {
                        safeDropRareItem(entity.getLocation(), Material.TNT, d100, UNCOMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    }
                    break;
                case SKELETON:
                    safeDropRareItem(entity.getLocation(), Material.BONE_BLOCK, d100, COMMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case SPIDER:
                    safeDropRareItem(entity.getLocation(), Material.FERMENTED_SPIDER_EYE, d100, UNCOMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case ZOMBIE:
                    safeDropRareItem(entity.getLocation(), Material.LEATHER, d100, COMMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case GHAST:
                    safeDropRareItem(entity.getLocation(), Material.FIRE_CHARGE, d100, COMMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case ZOMBIFIED_PIGLIN:
                    safeDropRareItem(entity.getLocation(), Material.GOLD_INGOT, d100, UNCOMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case ENDERMAN:
                    safeDropRareItem(entity.getLocation(), Material.EXPERIENCE_BOTTLE, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case CAVE_SPIDER:
                    safeDropRareItem(entity.getLocation(), Material.FERMENTED_SPIDER_EYE, d100, COMMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case BLAZE:
                    safeDropRareItem(entity.getLocation(), Material.GOLDEN_CARROT, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case MAGMA_CUBE:
                    safeDropRareItem(entity.getLocation(), Material.SLIME_BALL, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case ENDER_DRAGON:
                    safeDropRareItem(entity.getLocation(), Material.DRAGON_EGG, d100, UNCOMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    safeDropRareItem(entity.getLocation(), Material.ELYTRA, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case WITHER:
                    safeDropRareItem(entity.getLocation(), Material.WITHER_SKELETON_SKULL, d100, COMMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    safeDropRareItem(entity.getLocation(), Material.NETHER_STAR, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case WITCH:
                    safeDropRareItem(entity.getLocation(), Material.BREWING_STAND, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case SHULKER:
                    safeDropRareItem(entity.getLocation(), Material.SHULKER_BOX, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
                case PILLAGER:
                    safeDropRareItem(entity.getLocation(), Material.BELL, d100, RARE_ITEM_DROP_PERCENTAGE, playerLevel);
                    safeDropRareItem(entity.getLocation(), Material.CROSSBOW, d100, COMMMON_ITEM_DROP_PERCENTAGE, playerLevel);
                    break;
            }
        }
    }

    public static void safeDropRareItem(Location location, Material material, int dice, int chancePercentage, int playerLevel) {
        DraksItems.safeDropItem(location, new ItemStack(material, dice / (100 - (chancePercentage + playerLevel / 10))));
    }

    public static void applyImmunities(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getNewEffect() != null) {
                if (Main.config.getBoolean("RPG_CLASSES")) {
                    if (player.hasPermission(BASE_PERMISSION)) {
                        for (PotionEffectType immunity :
                                CLASS_IMMUNITIES) {
                            if (event.getNewEffect().getType().equals(immunity)) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

}
