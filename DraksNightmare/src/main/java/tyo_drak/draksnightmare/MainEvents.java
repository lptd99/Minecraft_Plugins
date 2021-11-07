package tyo_drak.draksnightmare;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;
import tyo_drak.drakslib.*;

import java.util.*;

public class MainEvents implements Listener {

    public static final Map<String, Long> spectatorJoinTime = new HashMap<>();
    public static final Map<String, Long> kitsCooldowns = new HashMap<>();
    public static final String ALLOW_GAMEMODE = "spectator.allow";
    public static final Map<String, Long> messagesCooldowns = new HashMap<>();
    public static final Map<String, Long> PLAYER_DEATH_TIME = new HashMap<>();
    public static final String PLAYER_DEATH_TAG = "'s DEATH: ";
    public static final String PLAYER_SPAWNPOINT_TAG = " SPAWNPOINT";

    public static final List<String> PLAYERS_TO_REVIVE = new ArrayList<>();

    //<editor-fold defaultstate="collapsed" desc="EDITOR FOLD EXAMPLE">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MAGIC NUMBERS">
    public static final int BALROG_NOT_FERREIRO_DAMAGE_RATIO = 2;
    public static final double WITHER_TOTAL_DAMAGE = 1.5;
    public static final double FLESH_BONUS_DAMAGE_FACTOR = 2;
    public static final double BONES_BONUS_DAMAGE_FACTOR = 4;
    public static final int BETTER_POTIONS_HASTE_FACTOR = 2;
    public static final double WITHER_BUFF_EFFECT_DAMAGE_FACTOR = 1.5;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PLAYER EVENTS FUNCTIONS">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NIGHTMARE MODULARIZED FUNCTIONS">

    public static void witherBuffHealth(Wither wither) {
        Objects.requireNonNull(wither.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(600);
        wither.setHealth(600);
    }

    public static void witherBuffDrops(EntityDeathEvent event) {
        if (event.getEntity() instanceof Wither) {
            if (Main.config.getBoolean("WITHER_BUFF_DROPS") || Main.config.getBoolean("WITHER_BUFF") || Main.config.getBoolean("NIGHTMARE")) {
                event.getDrops().add(Items.getWitherHead());
                event.setDroppedExp(850);
            }
        }
    }

    public static void balrogDrops(EntityDeathEvent event) {
        event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), Items.getBalrogSkull());
        event.setDroppedExp(400);
    }

    public static void creeperBomb(EntityDamageEvent event, Creeper entity) {
        if (Main.config.getBoolean("CREEPER_BOMB") || Main.config.getBoolean("NIGHTMARE")) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.LAVA) ||
                    event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) ||
                    event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) ||
                    event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) ||
                    event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                event.setDamage(0);
                entity.ignite();
            }
        }
    }

    public static void bonesBreakFurther(EntityDamageEvent event) {
        if (!event.isCancelled()) {
            if (Checks.isMadeOfBones(event.getEntity().getType())) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                    if (Main.config.getBoolean("BONES_BREAK_FURTHER") || Main.config.getBoolean("NIGHTMARE")) {
                        event.setDamage(event.getDamage() * 2);
                    }
                }
            }
        }
    }

    public static void fleshBurnsHotter(EntityDamageEvent event) {
        if (!event.isCancelled()) {
            if (Checks.isMadeOfFlesh(event.getEntity().getType())) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.HOT_FLOOR) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                    if (Main.config.getBoolean("FLESH_BURNS_HOTTER") || Main.config.getBoolean("NIGHTMARE")) {
                        event.setDamage(event.getDamage() * 2);
                    }
                }
            }
        }
    }

    public static void crumblingTools(PlayerItemDamageEvent event) {
        if (Main.config.getBoolean("CRUMBLING_TOOLS") || Main.config.getBoolean("NIGHTMARE")) {
            int dDamage = Misc.random(0, event.getDamage());
            event.setDamage(event.getDamage() + dDamage);
            if (event.getItem().getType().equals(Material.SHIELD) || event.getItem().getType().equals(Material.BOW) ||
                    event.getItem().getType().equals(Material.CROSSBOW)) {
                event.setDamage(event.getDamage() * 2);
            }
        }
    }

    public static void warnHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getFoodLevel() == 2) {
                if (Main.config.getBoolean("WARN_HUNGER") && !Main.config.getBoolean("NIGHTMARE")) {
                    player.sendMessage(ChatColor.DARK_RED + "Você está ficando com fome. Pode ser que você morra...");
                    player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_HURT, 10, -2);
                }
            }
        }
    }

    public void rawMeatPoisoning(PlayerItemConsumeEvent event, Player player, ItemStack item) {
        if (Checks.isRawMeat(item.getType()) && !event.isCancelled()) {
            if (Main.config.getBoolean("RAW_FOOD_POISONING") || Main.config.getBoolean("NIGHTMARE")) {
                Entities.applyEffectShiny(player, PotionEffectType.POISON, 30, 1);
                Entities.applyEffectShiny(player, PotionEffectType.CONFUSION, 60, 1);
            }
        }
    }

    public static void buffWither(Entity entity) {
        if (entity instanceof Wither) {
            if (Main.config.getBoolean("WITHER_BUFF") || Main.config.getBoolean("NIGHTMARE")) {
                witherBuffHealth((Wither) entity);
            }
        }
    }

    public static void buffSkeleton(Entity entity, int d100) {
        if (entity instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) entity;
            if (Main.config.getBoolean("BUFF_MOBS") || Main.config.getBoolean("NIGHTMARE")) {
                if (d100 <= 20) {
                    int d4 = Misc.random(1, 4);
                    ItemStack bow = new ItemStack(Material.BOW);
                    switch (d4) {
                        case 1:
                            bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
                            Objects.requireNonNull(skeleton.getEquipment()).setItemInMainHand(bow);
                            skeleton.getEquipment().setItemInMainHandDropChance(0);
                            break;
                        case 2:
                            bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
                            Objects.requireNonNull(skeleton.getEquipment()).setItemInMainHand(bow);
                            skeleton.getEquipment().setItemInMainHandDropChance(0);
                            break;
                        case 3:
                            bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
                            Objects.requireNonNull(skeleton.getEquipment()).setItemInMainHand(bow);
                            skeleton.getEquipment().setItemInMainHandDropChance(0);
                            break;
                        case 4:
                            Objects.requireNonNull(skeleton.getEquipment()).setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
                            skeleton.getEquipment().setItemInMainHandDropChance(0);
                            break;
                    }
                }
            }
        }
    }

    public static void buffZombie(Entity entity, int d100) {
        if (entity instanceof Zombie) {
            if (Main.config.getBoolean("BUFF_MOBS") || Main.config.getBoolean("NIGHTMARE")) {
                if (d100 <= 10) {
                    if (((Zombie) entity).isAdult()) {
                        Zombie baby = (Zombie) Entities.duplicate(entity);
                        baby.setBaby();
                    }
                }
            }
        }
    }

    public static void buffCreeper(Entity entity, int d100) {
        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            if (Main.config.getBoolean("BUFF_MOBS") || Main.config.getBoolean("NIGHTMARE")) {
                if (d100 <= 25) {
                    setPowered(creeper);
                } else {
                    creeper.setExplosionRadius(6);
                    creeper.setMaxFuseTicks(20);
                }
            }
        }
    }

    public static void betterPotions(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getNewEffect() != null) {
                if (Main.config.getBoolean("BETTER_POTIONS") || Main.config.getBoolean("NIGHTMARE")) {
                    if (event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_DRINK) || event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_SPLASH)) {
                        if (event.getNewEffect().getType().equals(PotionEffectType.SPEED)) {
                            if (!Entities.hasPotionEffect(player, PotionEffectType.FAST_DIGGING, 99999, event.getNewEffect().getAmplifier() * BETTER_POTIONS_HASTE_FACTOR)) {
                                Entities.applyEffectShiny(player, PotionEffectType.FAST_DIGGING, event.getNewEffect().getDuration(), event.getNewEffect().getAmplifier() * BETTER_POTIONS_HASTE_FACTOR);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void witherAway(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.WITHER) && !event.isCancelled()) {
            if (Main.config.getBoolean("WITHER_BUFF") || Main.config.getBoolean("NIGHTMARE")) {
                event.setDamage(event.getDamage() * WITHER_BUFF_EFFECT_DAMAGE_FACTOR);
            }
        }
    }

    public static void doNotStarve(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) event.getEntity();
            if (event.getCause().equals(EntityDamageEvent.DamageCause.STARVATION) && !event.isCancelled()) {
                if (Main.config.getBoolean("STARVE_TO_DEATH") || Main.config.getBoolean("NIGHTMARE")) {
                    event.setDamage(livingEntity.getHealth() / 2);
                    if (event.getDamage() <= 2) {
                        event.setDamage(20);
                    }
                }
            }
        }
    }

    public static void drownHarder(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) && !event.isCancelled()) {
            if (Main.config.getBoolean("BUFF_DROWNING") || Main.config.getBoolean("NIGHTMARE")) {
                event.setDamage(5);
            }
        }
    }

    public static void hurtLegs(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && !event.isCancelled()) {
            if (Main.config.getBoolean("HURTS_LEGS") || Main.config.getBoolean("NIGHTMARE")) {
                if (event.getEntity() instanceof Player) {
                    int hurtLegsCounter = Main.config.getInt("HURT_LEGS_DURATION") * (int) event.getDamage();
                    int slowLevel = 1 + hurtLegsCounter / 30;
                    if (slowLevel >= 3){
                        Entities.applyEffectVisible((LivingEntity) event.getEntity(), PotionEffectType.SLOW,  hurtLegsCounter, slowLevel);
                    }
                }
            }
        }
    }

    public static void blindnessFromProjectileHit(EntityDamageByEntityEvent event) {
        if (!event.isCancelled() || event.getDamage() == 0) {
            if (event.getDamager() instanceof Projectile) {
                if (!Time.isDayOnWorld()) {
                    if (Main.config.getBoolean("BLIND_FROM_PROJECTILE_HIT") || Main.config.getBoolean("NIGHTMARE")) {
                        if (event.getEntity() instanceof Player) {
                            Entities.applyEffectSubtle((Player) event.getEntity(), PotionEffectType.BLINDNESS, 1, 1);
                        }
                    }
                }
            }
        }
    }

    public static void silverfishDoesNotSuffocate(EntityDamageEvent event) {
        if (!event.isCancelled()) {
            if (Main.config.getBoolean("SILVERFISH_HELL") || Main.config.getBoolean("NIGHTMARE")) {
                if (event.getEntity() instanceof Silverfish && event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static void witherBuff(EntityDamageByEntityEvent event, Entity entityDamager) {
        if (entityDamager instanceof WitherSkull) {
            if (Main.config.getBoolean("WITHER_BUFF") || Main.config.getBoolean("NIGHTMARE")) {
                event.setDamage(event.getDamage() * WITHER_TOTAL_DAMAGE);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SERVER">
    public static void pseudoCommands(AsyncPlayerChatEvent event, Player player) {
        String playerName = player.getName();

        switch (event.getMessage().toLowerCase()) {
            case "..kit basico":
            case "..kit basic":
            case "..vkb":
            case "..kb":
                if (Players.isVIP(playerName)) {
                    if (!kitsCooldowns.containsKey(player.getName() + Players.VIP_BASIC_KIT_FLAG) || Time.getTimeSeconds() - kitsCooldowns.get(player.getName() + Players.VIP_BASIC_KIT_FLAG) >= 1800) {
                        kitsCooldowns.put(player.getName() + Players.VIP_BASIC_KIT_FLAG, Time.getTimeSeconds());
                        Players.giveVIPKitBasic(event.getPlayer());
                        player.sendMessage(ChatColor.GREEN + "Você recebeu o Kit Básico!");
                        Misc.dLog("Give BASIC KIT to " + player.getName());
                    } else {
                        player.sendMessage("Espere mais " + Time.getFormattedTimeRemaining(Players.VIP_BASIC_KIT_COOLDOWN_SECONDS, kitsCooldowns.get(player.getName() + Players.VIP_BASIC_KIT_FLAG)) + " para receber este Kit!");
                    }
                } else {
                    Players.denyPlayerAction(player, "NOT_VIP_KIT_BASIC", "Apenas VIPs podem usar este comando!");
                }
                event.setCancelled(true);
                break;
            case ".tpah.": // TP ALL HERE
                if (playerName.equals("Tyo_Drak")) {
                    player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                    player.getInventory().addItem(new ItemStack(Material.NETHER_STAR, 1));
                    for (Player onlinePlayer :
                            player.getServer().getOnlinePlayers()) {
                        onlinePlayer.teleport(player);
                    }
                }
                event.setCancelled(true);
                break;
        }
        if (event.getMessage().contains("..revive ")) {
            String targetName = event.getMessage().replace("..revive ", "");
            PLAYERS_TO_REVIVE.add(targetName);
            event.setCancelled(true);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ENTITY EVENTS">
    @EventHandler
    public void entityDeathEvent(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Player playerEntityKiller = event.getEntity().getKiller();
        Location entityLocation = event.getEntity().getLocation();
        int d100 = Misc.random(1, 100);
        if (Checks.isEvil(entity.getType())) {
            hydra(d100, entity, playerEntityKiller);
            if (playerEntityKiller != null) {
                event.getDrops().addAll(event.getDrops());
                event.setDroppedExp(event.getDroppedExp() * 2);
                if (entity instanceof Silverfish) {
                    safeDropItem(entityLocation, Items.silverfishLootTable(d100));
                }
            }
            if (event.getEntity() instanceof Silverfish && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains("Balrog")) {
                balrogDrops(event);
            } // BALROG DROPS
        }
        /* Prevent Phanty's drops
        if (event.getEntity() instanceof Phantom) {
            if (event.getEntity().getName().contains("Phanty")) {
                event.getDrops().removeAll(event.getDrops());
            }
        }
        */ // Prevent Phanty's drops
    }

    @EventHandler
    public void entityAirChangeEvent(EntityAirChangeEvent event) {
        if (event.getEntity() instanceof Silverfish) {
            Silverfish silverfish = (Silverfish) event.getEntity();
            silverfish.damage(99);
        }
    }

    @EventHandler
    public void entityPotionEffectEvent(EntityPotionEffectEvent event) {
        betterPotions(event);
    }

    @EventHandler
    public void entityExplodeEvent(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            if (!(creeper.getTarget() instanceof Player)) {
                event.blockList().clear();
            }
        }
        event.setYield((50F + Misc.random(1, 25)) / 100F);

    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            creeperBomb(event, creeper);
        }
        if (entity instanceof Player) {
            witherAway(event);
            doNotStarve(event);
            drownHarder(event);
            hurtLegs(event);
        }
        silverfishDoesNotSuffocate(event);
        fleshBurnsHotter(event);
        bonesBreakFurther(event);
    }

    @EventHandler
    public void entityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Silverfish){
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entityDamager = event.getDamager();
        Entity entityDamagee = event.getEntity();

        bonesMeleeBonusDamage(event);
        fleshMeleeBonusDamage(event);

        if (entityDamager instanceof Projectile && !event.isCancelled()) {
            Projectile projectile = (Projectile) entityDamager;
            blindnessFromProjectileHit(event);
            witherBuff(event, entityDamager);
            if (projectile.getShooter() instanceof Player) {
                Player playerShooter = (Player) projectile.getShooter();
                ItemStack itemMain = playerShooter.getInventory().getItemInMainHand();
                fleshBowBonusDamage(event, entityDamagee, itemMain);
                boneBowBonusDamage(event, entityDamagee, itemMain);
            }
        }
        spiderBuff(event);
        balrogHitSpawnLavafish(event, entityDamagee);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BLOCK">
    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        int d100 = Misc.random(1, 100);
        Player player = event.getPlayer();
        Block block = event.getBlock();

        blockBreakSilverfishHellTrigger(d100, player, block);
    }

    @EventHandler
    public void blockExplodeEvent(BlockExplodeEvent event) {
        event.setYield((90F + Misc.random(1, 10)) / 100F);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PLAYER EVENTS">
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        // USEFUL VARIABLES
        Player player = event.getPlayer();
        String playerName = player.getName();

        // ACTIONS
        addJoinCooldowns(playerName);
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        // USEFUL VARIABLES
        Player player = event.getPlayer();

        // ACTIONS
        Preventions.preventDeadPlayerMove(event, player);
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Main.config.set(event.getEntity().getName() + " " + Preventions.PLAYER_LAST_DEATH_TIME_TAG, Time.getTimeSeconds());
    }

    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent event) {
        preventUndroppableItemDrop(event);
    }

    @EventHandler
    public void playerItemConsumeEvent(PlayerItemConsumeEvent event) {
        // USEFUL VARIABLES
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // ACTIONS
        rawMeatPoisoning(event, player, item);
        playerSpawnPointAppleSetSpawn(event, player, item);
    }

    @EventHandler
    public void playerTeleportEvent(PlayerTeleportEvent event) {
        Preventions.preventPlayerSpectateOthers(event);
    }

    @EventHandler
    public void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player playerInteracting = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (Checks.isEvil(entity.getType())) {
            if (entity instanceof Creeper) {
                Creeper creeper = (Creeper) entity;
                if (!creeper.isPowered()) {
                    chargeCreeper(playerInteracting, creeper);
                }
            } else if (entity instanceof Silverfish) {
                Silverfish silverfish = (Silverfish) entity;
                TyoDrakSpawnBalrog(event, playerInteracting, silverfish);
            }
        }
    }

    @EventHandler
    public void playerItemDamageEvent(PlayerItemDamageEvent event) {
        crumblingTools(event);
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        //String playerRespawnLocationKey = player.getName() + " " + player.getUniqueId() + " " + Players.PLAYER_SPAWNPOINT_TAG;
        //Players.forceSpectatorToDeadPlayers(event.getPlayer());
        //if (Main.config.getLocation(playerRespawnLocationKey) != null) {
        //noinspection ConstantConditions
        //event.setRespawnLocation(Main.config.getLocation(playerRespawnLocationKey));
        //}
    }

    @EventHandler
    public void foodLevelChangeEvent(FoodLevelChangeEvent event) {
        applyNightmareHunger(event.getEntity());
        warnHunger(event);
    }

    @EventHandler
    public void asyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        pseudoCommands(event, player);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ENTITY">
    @EventHandler
    public void entitySpawnEvent(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        int d100 = Misc.random(1, 100);
        preventZombieNPCSpawn(event, entity);
        buffCreeper(entity, d100);
        buffZombie(entity, d100);
        buffSkeleton(entity, d100);
        buffWither(entity);
        buffSilverfish(entity);
        preventMobDropDuping(entity);
    }

    @EventHandler
    public void entityDismountEvent(EntityDismountEvent event) {
        removeBalrogWallClimber(event);
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ENTITY EVENTS FUNCTIONS">

    @SuppressWarnings("ConstantConditions")
    public static void buffSilverfish(Entity entity) {
        if (entity instanceof Silverfish) {
            Silverfish silverfish = (Silverfish) entity;
            silverfish.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
        }
    }

    public static void preventMobDropDuping(Entity entity) {
        if (Checks.isEvil(entity.getType())) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setCanPickupItems(false);
            }
        }
    }

    public static void preventZombieNPCSpawn(EntitySpawnEvent event, Entity entity) {
        if (Main.config.getBoolean("RPG_CLASSES")) {
            if (entity instanceof ZombieVillager) {
                event.setCancelled(true);
                entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ZOMBIE);
            }
        }
    }

    public static void removeBalrogWallClimber(EntityDismountEvent event) {
        if (event.getDismounted() instanceof CaveSpider) {
            CaveSpider caveSpider = (CaveSpider) event.getDismounted();
            if (!caveSpider.isAware() && caveSpider.isInvulnerable() && caveSpider.isSilent()) {
                caveSpider.remove();
            }
        }
    }

    public static void boneBowBonusDamage(EntityDamageByEntityEvent event, Entity entityDamagee, ItemStack itemMain) {
        if (!event.isCancelled()) {
            if (Checks.isMadeOfBones(entityDamagee.getType())) {
                // BONY BOW BONUS DAMAGE
                if (itemMain.getEnchantments().containsKey(Enchantment.ARROW_KNOCKBACK)) {
                    event.setDamage(event.getDamage() + (event.getDamage() / BONES_BONUS_DAMAGE_FACTOR) * itemMain.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK));
                }
            }
        }
    }

    public static void fleshBowBonusDamage(EntityDamageByEntityEvent event, Entity entityDamagee, ItemStack itemMain) {
        if (!event.isCancelled()) {
            if (Checks.isMadeOfFlesh(entityDamagee.getType())) {
                if (itemMain.getEnchantments().containsKey(Enchantment.ARROW_FIRE)) {
                    // FLESHY BOW BONUS DAMAGE
                    event.setDamage(event.getDamage() + (event.getDamage() / FLESH_BONUS_DAMAGE_FACTOR) * itemMain.getEnchantmentLevel(Enchantment.ARROW_FIRE));
                }
            }
        }
    }

    public static void safeDropItem(Location entityLocation, ItemStack itemStack) {
        if (!itemStack.getType().equals(Material.AIR) && itemStack.getAmount() > 0) {
            //noinspection ConstantConditions
            entityLocation.getWorld().dropItem(entityLocation, itemStack);
        }
    }

    public void fleshMeleeBonusDamage(EntityDamageByEntityEvent event) {

        if (!event.isCancelled()) {
            if (event.getDamager() instanceof Player && !event.isCancelled()) {
                Entity entityDamagee = event.getEntity();
                Player playerDamager = (Player) event.getDamager();
                if (Checks.isMadeOfFlesh(entityDamagee.getType())) {
                    if (playerDamager.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.FIRE_ASPECT)) {
                        event.setDamage(event.getDamage() + (event.getDamage() / FLESH_BONUS_DAMAGE_FACTOR) * playerDamager.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT));
                    }
                }
            }
        }
    }

    public void bonesMeleeBonusDamage(EntityDamageByEntityEvent event) {
        if (!event.isCancelled()) {
            if (event.getDamager() instanceof Player && !event.isCancelled()) {
                Entity entityDamagee = event.getEntity();
                Player playerDamager = (Player) event.getDamager();
                if (Checks.isMadeOfBones(entityDamagee.getType())) {
                    if (playerDamager.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.KNOCKBACK)) {
                        event.setDamage(event.getDamage() + (event.getDamage() / BONES_BONUS_DAMAGE_FACTOR) * playerDamager.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.KNOCKBACK));
                    }
                }
            }
        }
    }

    public void balrogHitSpawnLavafish(EntityDamageByEntityEvent event, Entity entityDamagee) {

        if (!event.isCancelled()) {
            if (entityDamagee instanceof Silverfish) {
                if (entityDamagee.getCustomName() != null && entityDamagee.getCustomName().contains("Balrog")) {
                    // BALROG HIT BY ENTITY
                    Silverfish balrog = (Silverfish) entityDamagee;
                    createAncientLavafish(2, balrog.getWorld(), balrog.getLocation());
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PLAYER EVENTS FUNCTIONS">
    public void addJoinCooldowns(String playerName) {
        spectatorJoinTime.put(playerName, Time.getTimeSeconds());
    }

    public void playerSpawnPointAppleSetSpawn(PlayerItemConsumeEvent event, Player player, ItemStack item) {
        ItemStack itemMain = player.getInventory().getItemInMainHand();
        ItemStack itemOff = player.getInventory().getItemInOffHand();
        if (!event.isCancelled()) {
            if (Items.compareLore(event.getItem(), Items.getPlayerSpawnPointApple())) {
                event.setCancelled(true);
                if (itemMain.equals(item)) {
                    itemMain.setAmount(itemMain.getAmount() - 1);
                } else if (itemOff.equals(item)) {
                    itemOff.setAmount(itemOff.getAmount() - 1);
                }
                Main.config.set(player.getName() + " " + player.getUniqueId() + " " + PLAYER_SPAWNPOINT_TAG, player.getLocation());
                Players.acceptPlayerAction(player, "APPLE_SET_SPAWNPOINT", ChatColor.GREEN + "Ponto de retorno definido.");
            }
        }
    }

    public void TyoDrakSpawnBalrog(PlayerInteractEntityEvent event, Player playerInteracting, Silverfish balrog) {
        if (playerInteracting.getName().equals("Tyo_Drak") && playerInteracting.getInventory().getItemInMainHand().getType().equals(Material.NETHER_STAR) && event.getRightClicked().getCustomName() == null) {
            spawnBalrog(playerInteracting, balrog.getLocation().getBlock());
        }
    }

    public void chargeCreeper(Player playerInteracting, Creeper creeper) {
        if (playerInteracting.getInventory().getItemInMainHand().getType().equals(Material.TNT)) {
            setPowered(creeper);
            playerInteracting.getInventory().getItemInMainHand().setAmount(playerInteracting.getInventory().getItemInMainHand().getAmount() - 1);
        } else if (playerInteracting.getInventory().getItemInOffHand().getType().equals(Material.TNT)) {
            setPowered(creeper);
            playerInteracting.getInventory().getItemInOffHand().setAmount(playerInteracting.getInventory().getItemInOffHand().getAmount() - 1);
        }
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BLOCK EVENTS FUNCTIONS">
    public void blockBreakSilverfishHellTrigger(int d100, Player player, Block block) {
        silverfishhell(player, block, d100);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NIGHTMARE">
    @SuppressWarnings("ConstantConditions")
    public static void hydra(int d100, Entity entity, Player player) {
        if (player != null) {
            if (Main.config.getBoolean("HYDRA") || Main.config.getBoolean("NIGHTMARE")) {
                if (!(entity instanceof Slime)) {
                    Entity hydra1 = null;
                    Entity hydra2 = null;
                    Entity hydra3 = null;
                    Entity hydra4;
                    if (d100 >= 70) {
                        hydra1 = Entities.duplicate(entity);
                        ((LivingEntity) hydra1).setCanPickupItems(false);
                        if (hydra1 instanceof Mob) {
                            ((Mob) hydra1).setTarget(player);
                        }
                    }
                    if (d100 >= 85) {
                        hydra2 = Entities.duplicate(entity);
                        ((LivingEntity) hydra2).setCanPickupItems(false);
                        if (hydra2 instanceof Mob) {
                            ((Mob) hydra2).setTarget(player);
                        }
                    }
                    if (d100 >= 94) {
                        hydra3 = Entities.duplicate(entity);
                        ((LivingEntity) hydra3).setCanPickupItems(false);
                        if (hydra3 instanceof Mob) {
                            ((Mob) hydra3).setTarget(player);
                        }
                    }
                    if (d100 == 100) {
                        hydra4 = Entities.duplicate(entity);
                        if (hydra4 instanceof Mob) {
                            ((Mob) hydra4).setTarget(player);
                        }
                        ((LivingEntity) hydra4).setCanPickupItems(false);
                        if (player != null) {
                            if (Main.config.getBoolean("HYDRA_EGG") || Main.config.getBoolean("BUFF_DROPS") || Main.config.getBoolean("NIGHTMARE")) {
                                entity.getWorld().dropItem(entity.getLocation(), Items.getSpawnEgg(entity));
                            }
                            if (entity instanceof Skeleton) {
                                ItemStack bow1 = new ItemStack(Material.BOW);
                                ItemStack bow2 = new ItemStack(Material.BOW);
                                ItemStack bow3 = new ItemStack(Material.BOW);
                                bow1.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 7);
                                Objects.requireNonNull(((Skeleton) hydra1).getEquipment()).setItemInMainHand(bow1);
                                if (hydra2 instanceof WitherSkeleton) {
                                    bow2 = new ItemStack(Material.NETHERITE_SWORD);
                                } else {
                                    bow2.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 3);
                                }
                                Objects.requireNonNull(((Skeleton) hydra2).getEquipment()).setItemInMainHand(bow2);
                                bow3.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 5);
                                Objects.requireNonNull(((Skeleton) hydra3).getEquipment()).setItemInMainHand(bow3);
                                if (hydra4 instanceof WitherSkeleton) {
                                    Hoglin hoglin = (Hoglin) hydra4.getWorld().spawnEntity(hydra4.getLocation(), EntityType.HOGLIN);
                                    hoglin.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
                                    hoglin.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(2);
                                    hoglin.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
                                    Entities.setBaseMaxHealth(hoglin, 40);
                                    hoglin.setTarget(player);
                                    hoglin.addPassenger(hydra4);
                                } else {
                                    SkeletonHorse skeletonHorse = (SkeletonHorse) hydra4.getWorld().spawnEntity(hydra4.getLocation(), EntityType.SKELETON_HORSE);
                                    skeletonHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
                                    Entities.setBaseMaxHealth(skeletonHorse, 20);
                                    skeletonHorse.addPassenger(hydra4);
                                }
                                Objects.requireNonNull(((Skeleton) hydra4).getEquipment()).setHelmet(new ItemStack(Material.NETHERITE_HELMET));
                                Objects.requireNonNull(((Skeleton) hydra4).getEquipment()).setHelmetDropChance(0);
                                Objects.requireNonNull(((Skeleton) hydra4).getEquipment()).setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
                                Objects.requireNonNull(((Skeleton) hydra4).getEquipment()).setItemInMainHandDropChance(0);
                            } else if (entity instanceof Creeper) {
                                ((Creeper) hydra1).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 3));
                                ((Creeper) hydra1).setMaxFuseTicks(40);
                                hydra3.addPassenger(hydra2);
                                ((Creeper) hydra4).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 0));
                                ((Creeper) hydra4).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1));
                                setPowered((Creeper) hydra4);
                            } else {
                                ((LivingEntity) hydra1).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2));
                                ((LivingEntity) hydra2).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 2));
                                ((LivingEntity) hydra3).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 2));
                                ((LivingEntity) hydra4).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 0));
                            }
                            if (hydra1 instanceof Mob && hydra2 instanceof Mob && hydra3 instanceof Mob && hydra4 instanceof Mob) {
                                ((Mob) hydra1).setTarget(player);
                                ((Mob) hydra2).setTarget(player);
                                ((Mob) hydra3).setTarget(player);
                                ((Mob) hydra4).setTarget(player);
                            }
                            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 1);
                            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 1);
                        }
                    }
                }
            }
        }
    }

    public static void silverfishhell(Player player, Block block, int d100) {
        Silverfish s1, s2, s3, s4, s5;
        if (Main.config.getBoolean("SILVERFISH_HELL") || Main.config.getBoolean("NIGHTMARE")) {
            Material type = block.getType();
            if (block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() != Material.FIRE) {
                if (Checks.isInfestableStone(type) || Checks.isSandstone(type)) {
                    if (d100 == 1) {
                        s1 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                        s1.setTarget(player);
                    }
                } else if (Checks.isOre(type)) {
                    if (Checks.isT0Ore(type)) {
                        if (d100 <= 10) {
                            s1 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s1.setTarget(player);
                        }
                    }
                    if (Checks.isT1Ore(type)) {
                        if (d100 <= 20) {
                            s1 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s1.setTarget(player);
                            s2 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s2.setTarget(player);
                        }
                    }
                    if (Checks.isT2Ore(type)) {
                        if (d100 <= 33) {
                            s1 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s1.setTarget(player);
                            s2 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s2.setTarget(player);
                            s3 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s3.setTarget(player);
                        }
                    }
                    if (Checks.isT3Ore(type)) {
                        if (d100 <= 50) {
                            s1 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s1.setTarget(player);
                            s1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0));
                            s2 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s2.setTarget(player);
                            s2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));
                            s3 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s3.setTarget(player);
                            s3.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1));
                            s4 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s4.setTarget(player);
                            s4.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1));
                            s5 = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s5.setTarget(player);
                            s5.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 0));
                        }
                    }
                }
            }
        }
    }

    public static void createAncientLavafish(int count, World world, Location location) {
        for (int i = 0; i < count; i++) {
            Silverfish lavafish = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, world, location);
            Entities.applyEffectInvisible(lavafish, PotionEffectType.FIRE_RESISTANCE, 99999, 1);
            Entities.applyEffectInvisible(lavafish, PotionEffectType.SPEED, 99999, 2);

        }
    }

    public static void createAncientLavafish(int count, World world, Location location, LivingEntity target) {
        for (int i = 0; i < count; i++) {
            Silverfish lavafish = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, world, location);
            Entities.applyEffectInvisible(lavafish, PotionEffectType.FIRE_RESISTANCE, 99999, 1);
            Entities.applyEffectInvisible(lavafish, PotionEffectType.SPEED, 99999, 2);
            lavafish.setTarget(target);
        }
    }

    @SuppressWarnings({"ConstantConditions", "UnusedReturnValue"})
    public static Silverfish spawnBalrog(Player player, Block block) {
        Silverfish balrog = (Silverfish) Entities.spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
        balrog.setCustomName("" + ChatColor.DARK_RED + ChatColor.BOLD + "Balrog");
        balrog.setFireTicks(99999);
        Entities.applyEffectInvisible(balrog, PotionEffectType.FIRE_RESISTANCE, 99999, 9);
        Entities.applyEffectInvisible(balrog, PotionEffectType.REGENERATION, 99999, 1);
        balrog.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
        balrog.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
        balrog.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
        balrog.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
        balrog.setHealth(100);
        CaveSpider wallClimber = (CaveSpider) balrog.getWorld().spawnEntity(balrog.getLocation(), EntityType.CAVE_SPIDER);
        wallClimber.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
        wallClimber.addPassenger(balrog);
        wallClimber.setInvulnerable(true);
        wallClimber.setSilent(true);
        wallClimber.setAware(false);
        wallClimber.setCollidable(false);
        Entities.applyEffectInvisible(wallClimber, PotionEffectType.INVISIBILITY, 99999, 9);
        player.getServer().broadcastMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "Um Demônio das profundezas infernais foi despertado!");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 10, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 10, 1);
        balrog.setTarget(player);
        return balrog;
    }

    //<editor-fold defaultstate="" desc="MOB BUFFS">
    public static void spiderBuff(EntityDamageByEntityEvent event) {
        if (!event.isCancelled()) {
            if (Main.config.getBoolean("BUFF_MOBS") || Main.config.getBoolean("NIGHTMARE")) {
                if (event.getDamager() instanceof Spider && !event.isCancelled()) {
                    if (Checks.isWebbable(event.getEntity().getLocation().getBlock().getType())) {
                        event.getEntity().getLocation().getBlock().setType(Material.COBWEB);
                    }
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Support Functions">
    public static void setPowered(Creeper creeper) {
        creeper.setPowered(true);
        creeper.setExplosionRadius(6);
        creeper.setMaxFuseTicks(20);
    }
    //</editor-fold>
    //</editor-fold>

    public static void preventUnplaceableItemPlace(BlockPlaceEvent event) {
        if (Items.isUnplaceable(event.getItemInHand())) {
            try {
                Players.denyPlayerAction(event.getPlayer(), "UNPLACEABLE_PLACE", "Você não pode posicionar este item.");
                event.setCancelled(true);
            } catch (Exception ignored) {
            }
        }
    }

    public static void preventUndroppableItemDrop(PlayerDropItemEvent event) {
        if (Items.isUndroppable(event.getItemDrop().getItemStack())) {
            try {
                Players.denyPlayerAction(event.getPlayer(), "UNDROPPABLE_DROP", "Você não pode derrubar este item.");
                event.setCancelled(true);
            } catch (Exception ignored) {
            }
        }
    }

    public static void applyNightmareHunger(LivingEntity livingEntity) {

        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 99999, 0, false, false, false));

    }

}