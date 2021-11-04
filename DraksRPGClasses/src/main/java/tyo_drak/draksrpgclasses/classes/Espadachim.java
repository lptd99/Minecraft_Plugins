package tyo_drak.draksrpgclasses.classes;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityMountEvent;
import tyo_drak.drakslib.Entities;
import tyo_drak.drakslib.Items;
import tyo_drak.drakslib.Misc;
import tyo_drak.drakslib.Players;
import tyo_drak.draksrpgclasses.Main;
import tyo_drak.draksrpgclasses.MainEvents;


public class Espadachim extends RPGClass {

    // CHARACTERISTICS
    public static final Enum<ChatColor> CLASS_COLOR = ChatColor.BLUE;
    public static final Integer BASE_HEALTH = 28;
    public static final PotionEffect[] CLASS_EFFECTS = { new PotionEffect(PotionEffectType.REGENERATION, 99999, 0),
            new PotionEffect(PotionEffectType.GLOWING, 99999, 0) };
    public static final PotionEffectType[] CLASS_IMMUNITIES = {PotionEffectType.WEAKNESS};

    // PERMISSIONS
    public static final String BASE_PERMISSION = "group.espadachim";

    // MAGIC NUMBERS
    public static final int ESPADACHIM_RESOLVE_COOLDOWN = 120;
    public static final int ESPADACHIM_RESOLVE_RESISTANCE_DURATION = ESPADACHIM_RESOLVE_COOLDOWN / 12;
    public static final int ESPADACHIM_RESOLVE_REGENERATION_DURATION = ESPADACHIM_RESOLVE_RESISTANCE_DURATION / 2;
    public static final int ESPADACHIM_RESOLVE_RESISTANCE_LEVEL = 4;
    public static final int ESPADACHIM_RESOLVE_REGENERATION_LEVEL = ESPADACHIM_RESOLVE_RESISTANCE_LEVEL * 2;

    public static final double HORSE_GENERIC_MOVEMENT_SPEED = 0.2;
    public static final double HORSE_JUMP_STRENGTH = 0.5;
    public static final int HORSE_REGENERATION_LEVEL = 1;
    public static final int HORSE_RESISTANCE_LEVEL = 1;

    public static final int ESPADACHIM_GUARDIAN_ANGEL_COOLDOWN = 1200;

    public static int ESPADACHIM_RESOLVE_MIN_LEVEL = 5;
    public static void espadachimResolve(EntityDamageByEntityEvent event) {
        Misc.dLog("Espadachim.espadachimResolve() START");
        if (!event.isCancelled()) {
            if (event.getEntity() instanceof Player) {
                Player playerDamagee = (Player) event.getEntity();
                String playerName = playerDamagee.getName();
                if (playerDamagee.hasPermission(Espadachim.BASE_PERMISSION) && playerDamagee.getLevel() >= ESPADACHIM_RESOLVE_MIN_LEVEL) {
                    if (playerDamagee.getHealth() < (playerDamagee.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 3)) {
                        if (!MainEvents.skillsCooldowns.containsKey(playerName + " Determinação do Espadachim") || MainEvents.hasPassedSince(ESPADACHIM_RESOLVE_COOLDOWN, MainEvents.skillsCooldowns.get(playerName + " Determinação do Espadachim")) || playerDamagee.hasPermission(BASE_PERMISSION + ".bypass")) {
                            MainEvents.skillsCooldowns.put(playerName + " Determinação do Espadachim", MainEvents.getTimeSeconds());
                            playerDamagee.sendMessage(CLASS_COLOR + "Determinação Inabalável ativada!");
                            playerDamagee.playSound(playerDamagee.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 2, 1);
                            playerDamagee.removePotionEffect(PotionEffectType.REGENERATION);
                            Entities.applyEffectShiny(playerDamagee, PotionEffectType.REGENERATION, ESPADACHIM_RESOLVE_REGENERATION_DURATION, ESPADACHIM_RESOLVE_REGENERATION_LEVEL);
                            Entities.applyEffectShiny(playerDamagee, PotionEffectType.DAMAGE_RESISTANCE, ESPADACHIM_RESOLVE_RESISTANCE_DURATION, ESPADACHIM_RESOLVE_RESISTANCE_LEVEL);
                        } else {
                            Players.denyPlayerAction(playerDamagee, "ESPADACHIM_RESOLVE_COOLDOWN", "Você deve esperar mais " +
                                    MainEvents.getFormattedTimeRemaining(300, MainEvents.skillsCooldowns.get(playerName + " Determinação do Espadachim"))
                                    + " para usar esta habilidade!");
                        }
                    }
                }
            }
        }
        Misc.dLog("Espadachim.espadachimResolve() END");
    }

    public static int ESPADACHIM_MOUNT_HORSE_MIN_LEVEL = 10;
    public static void mountHorse(EntityMountEvent event) {
        Misc.dLog("Espadachim.mountHorse() START");
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getMount() instanceof Horse) {
                Horse horse = (Horse) event.getMount();
                if (player.hasPermission(Espadachim.BASE_PERMISSION)) {
                    if (player.getLevel() >= ESPADACHIM_MOUNT_HORSE_MIN_LEVEL) {
                        Entities.setBaseMaxHealth(horse, 20);
                        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(HORSE_GENERIC_MOVEMENT_SPEED);
                        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(HORSE_JUMP_STRENGTH);
                        Entities.applyEffectShiny(horse, PotionEffectType.REGENERATION, 99999, HORSE_REGENERATION_LEVEL);
                        Entities.applyEffectShiny(horse, PotionEffectType.DAMAGE_RESISTANCE, 99999, HORSE_RESISTANCE_LEVEL);
                    } else {
                        event.setCancelled(true);
                        Players.denyPlayerAction(player, "NOT_ENOUGH_LEVEL", "Você não tem nível suficiente para montar um cavalo!");
                    }
                } else {
                    event.setCancelled(true);
                    Players.denyPlayerAction(player, "NOT_ESPADACHIM_HORSE_MOUNT", ChatColor.DARK_RED + "Apenas " + Espadachim.CLASS_COLOR + "Espadachins" + ChatColor.DARK_RED + " podem montar Cavalos!");
                }
            }
        }
        Misc.dLog("Espadachim.mountHorse() END");
    }

    public static int ESPADACHIM_GUARDIAN_ANGEL_MIN_LEVEL = 30;
    public static void espadachimGuardianAngel(EntityDamageEvent event){
        Misc.dLog("Espadachim.espadachimGuardianAngel() START");
        if (!event.isCancelled()) {
            if (event.getEntity() instanceof Player) {
                Player playerDamagee = (Player) event.getEntity();
                if (playerDamagee.getHealth() - event.getFinalDamage() <= 0) {
                    if (playerDamagee.hasPermission(Espadachim.BASE_PERMISSION) && playerDamagee.getLevel() >= ESPADACHIM_GUARDIAN_ANGEL_MIN_LEVEL) {
                        if (!MainEvents.skillsCooldowns.containsKey(playerDamagee.getName() + " Anjo Guardião") || MainEvents.hasPassedSince(ESPADACHIM_GUARDIAN_ANGEL_COOLDOWN, MainEvents.skillsCooldowns.get(playerDamagee.getName() + " Anjo Guardião")) || playerDamagee.hasPermission(BASE_PERMISSION + ".bypass")) {
                            MainEvents.skillsCooldowns.put(playerDamagee.getName() + " Anjo Guardião", MainEvents.getTimeSeconds());
                            event.setDamage(0);
                            playerDamagee.setHealth(playerDamagee.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                            Entities.applyEffectShiny(playerDamagee, PotionEffectType.DAMAGE_RESISTANCE, ESPADACHIM_GUARDIAN_ANGEL_COOLDOWN / 120, 4);
                            playerDamagee.playEffect(EntityEffect.TOTEM_RESURRECT);
                            playerDamagee.playEffect(EntityEffect.LOVE_HEARTS);
                            Players.acceptPlayerAction(playerDamagee, "GUARDIAN_ANGEL", ChatColor.GREEN + "Seu Anjo Guardião preveniu sua morte! Ele deverá descansar pelos próximos 10 minutos, tome cuidado!");
                        }
                    }
                }
            }
        }
        Misc.dLog("Espadachim.espadachimGuardianAngel() END");
    }

    public static void applyImmunities(EntityPotionEffectEvent event) {
        Misc.dLog("Espadachim.applyImmunities() START");
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getNewEffect() != null) {
                if (Main.config.getBoolean("RPG_CLASSES")) {
                    if (player.hasPermission(Espadachim.BASE_PERMISSION)) {
                        for (PotionEffectType immunity :
                                Espadachim.CLASS_IMMUNITIES) {
                            if (event.getNewEffect().getType().equals(immunity)) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
        Misc.dLog("Espadachim.applyImmunities() END");
    }

    public static void placeSpawner(Player player, Block blockPlaced, ItemStack itemMain, ItemStack itemOff) {
        Misc.dLog("Espadachim.placeSpawner() START");
        if (itemMain.getType().equals(Material.SPAWNER) || itemOff.getType().equals(Material.SPAWNER)) {
            CreatureSpawner creatureSpawner = (CreatureSpawner) blockPlaced.getState();
            if (player.getInventory().getItemInMainHand().getType().equals(Material.SPAWNER)) {
                if (itemMain.getItemMeta() != null) {
                    if (itemMain.getItemMeta().getLore() != null) {
                        if (itemMain.getItemMeta().getLore().get(0).replace("Spawned Type: ", "").equals("None")) {
                            creatureSpawner.setSpawnedType(EntityType.ARMOR_STAND);
                        } else {
                            creatureSpawner.setSpawnedType(Items.nameToEntityType(itemMain.getItemMeta().getLore().get(0).replace("Spawned Type: ", "")));
                        }
                        creatureSpawner.setSpawnCount(Integer.parseInt(itemMain.getItemMeta().getLore().get(1).replace("Spawn Count: ", "")));
                        creatureSpawner.setSpawnRange(Integer.parseInt(itemMain.getItemMeta().getLore().get(2).replace("Spawn Range: ", "")));
                        creatureSpawner.setRequiredPlayerRange(Integer.parseInt(itemMain.getItemMeta().getLore().get(3).replace("Required Player Range: ", "")));
                        creatureSpawner.setMaxNearbyEntities(Integer.parseInt(itemMain.getItemMeta().getLore().get(4).replace("Max Nearby Entities: ", "")));
                        creatureSpawner.setMinSpawnDelay(Integer.parseInt(itemMain.getItemMeta().getLore().get(5).replace("Min Spawn Delay: ", "")));
                        creatureSpawner.setMaxSpawnDelay(Integer.parseInt(itemMain.getItemMeta().getLore().get(6).replace("Max Spawn Delay: ", "")));
                        creatureSpawner.setDelay(creatureSpawner.getMinSpawnDelay());
                        creatureSpawner.update();
                    }
                }
            } else {
                if (itemOff.getItemMeta() != null) {
                    if (itemOff.getItemMeta().getLore() != null) {
                        creatureSpawner.setSpawnedType(Items.nameToEntityType(itemOff.getItemMeta().getLore().get(0).replace("Spawned Type: ", "")));
                        creatureSpawner.setSpawnCount(Integer.parseInt(itemOff.getItemMeta().getLore().get(1).replace("Spawn Count: ", "")));
                        creatureSpawner.setSpawnRange(Integer.parseInt(itemOff.getItemMeta().getLore().get(2).replace("Spawn Range: ", "")));
                        creatureSpawner.setRequiredPlayerRange(Integer.parseInt(itemOff.getItemMeta().getLore().get(3).replace("Required Player Range: ", "")));
                        creatureSpawner.setMaxNearbyEntities(Integer.parseInt(itemOff.getItemMeta().getLore().get(4).replace("Max Nearby Entities: ", "")));
                        creatureSpawner.setMinSpawnDelay(Integer.parseInt(itemOff.getItemMeta().getLore().get(5).replace("Min Spawn Delay: ", "")));
                        creatureSpawner.setMaxSpawnDelay(Integer.parseInt(itemOff.getItemMeta().getLore().get(6).replace("Max Spawn Delay: ", "")));
                        creatureSpawner.setDelay(creatureSpawner.getMinSpawnDelay());
                        creatureSpawner.update();
                    }
                }
            }
        }
        Misc.dLog("Espadachim.placeSpawner() END");
    }

    public static void editSpawner(PlayerInteractEvent event, Player player, ItemStack itemMain) {
        Misc.dLog("Espadachim.editSpawner() START");
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.SPAWNER) && player.hasPermission(Espadachim.BASE_PERMISSION)) {
                CreatureSpawner spawner = (CreatureSpawner) event.getClickedBlock().getState();
                String spawnedType = spawner.getSpawnedType().name().toLowerCase();
                String eggTypeName = itemMain.getType().toString().toLowerCase().replace("_spawn_egg", "");
                if (spawnedType.equals(eggTypeName)) {
                    if (spawner.getSpawnCount() < 16) {
                        event.setCancelled(true);
                        spawner.setSpawnCount(spawner.getSpawnCount() + 1);
                        itemMain.setAmount(itemMain.getAmount() - 1);
                        spawner.setMaxNearbyEntities(spawner.getSpawnCount() * 4);
                    } else {
                        event.setCancelled(true);
                        Players.denyPlayerAction(player, "MAX_ENTITIES_NUMBER", ChatColor.DARK_RED + "Este Spawner já atingiu o máximo de criaturas criadas.");
                    }
                } else if (itemMain.getType().toString().toLowerCase().contains("_spawn_egg")) {
                    spawner.setSpawnCount(4);
                    spawner.setSpawnRange(4);
                    spawner.setRequiredPlayerRange(16);
                    spawner.setMaxNearbyEntities(6);
                    spawner.setMinSpawnDelay(200);
                    spawner.setMaxSpawnDelay(800);
                    spawner.update(true);
                }
                if (Items.isCrystal(itemMain)) {
                    if (itemMain.getItemMeta().getLore().contains("Diminui o tempo mínimo do Spawner.")) {
                        if (spawner.getMinSpawnDelay() > 10) {
                            itemMain.setAmount(itemMain.getAmount() - 1);
                            spawner.setMinSpawnDelay(spawner.getMinSpawnDelay() - 10);
                        } else {
                            Players.denyPlayerAction(player, "ZERO_MIN_DELAY", ChatColor.DARK_RED + "Este Spawner não aceita mais este Cristal.");
                        }
                    } else if (itemMain.getItemMeta().getLore().contains("Diminui o tempo máximo do Spawner.")) {
                        if (spawner.getMaxSpawnDelay() > 10 && spawner.getMaxSpawnDelay() - 10 > spawner.getMinSpawnDelay()) {
                            itemMain.setAmount(itemMain.getAmount() - 1);
                            spawner.setMaxSpawnDelay(spawner.getMaxSpawnDelay() - 10);
                        } else {
                            Players.denyPlayerAction(player, "ZERO_MAX_DELAY", ChatColor.DARK_RED + "Este Spawner não aceita mais este Cristal.");
                        }
                    } else if (itemMain.getItemMeta().getLore().contains("Aumenta a distância mínima do Jogador pro Spawner.")) {
                        if (spawner.getRequiredPlayerRange() < 64) {
                            itemMain.setAmount(itemMain.getAmount() - 1);
                            spawner.setRequiredPlayerRange(spawner.getRequiredPlayerRange() + 1);
                        } else {
                            Players.denyPlayerAction(player, "MAX_PLAYER_REQUIRED_RANGE", ChatColor.DARK_RED + "Este Spawner não aceita mais este Cristal.");
                        }
                    }
                }
                spawner.update();
            }
        }
        Misc.dLog("Espadachim.editSpawner() END");
    }

    public static void combatExperience(EntityDamageByEntityEvent event) {
        Misc.dLog("Espadachim.combatExperience() START");
        if (!event.isCancelled()) {
            if (event.getEntity() instanceof Player) {
                Player playerDamagee = (Player) event.getEntity();
                if (playerDamagee.hasPermission(Espadachim.BASE_PERMISSION)) {
                    int xpGenerated = 0;
                    ExperienceOrb experienceOrb = (ExperienceOrb) playerDamagee.getWorld().spawnEntity(playerDamagee.getLocation(), EntityType.EXPERIENCE_ORB);
                    if (playerDamagee.isBlocking()) {
                        xpGenerated = (int) event.getFinalDamage() * 2;
                    } else {
                        xpGenerated = (int) event.getFinalDamage();
                    }
                    experienceOrb.setExperience(xpGenerated);
                }
            }
        }
        Misc.dLog("Espadachim.combatExperience() END");
    }

}