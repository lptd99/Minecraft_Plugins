package tyo_drak.me.draks_work;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import tyo_drak.me.draks_work.classes.*;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class MainEvents implements Listener {

    public static final Map<String, Long> spectatorJoinTime = new HashMap<>();
    public static final Map<String, Long> PLAYER_DEATH_TIME = new HashMap<>();
    public static final Map<String, Long> kitsCooldowns = new HashMap<>();
    public static final Map<String, Long> messagesCooldowns = new HashMap<>();
    public static final Map<String, Long> skillsCooldowns = new HashMap<>();
    public static final Map<String, Integer> playerKillers = new HashMap<>();
    public static final String ALLOW_GAMEMODE = "spectator.allow";
    public static final List<String> PLAYERS_TO_REVIVE = new ArrayList<>();

    //<editor-fold defaultstate="collapsed" desc="MAGIC NUMBERS">
    public static final Random rand = new Random();
    public static final int REGULAR_REVIVE_TIME_SECONDS = 600;
    public static final int VIP_REVIVE_TIME_SECONDS = 150;
    public static final int DEBUG_REVIVE_TIME_SECONDS = 10;

    public static final double WITHER_TOTAL_DAMAGE = 1.5;
    public static final double FLESH_BONUS_DAMAGE_FACTOR = 2;
    public static final double BONES_BONUS_DAMAGE_FACTOR = 4;
    public static final String PLAYER_DEATH_TAG = "'s DEATH: ";
    public static final int BETTER_POTIONS_HASTE_FACTOR = 2;
    public static final double WITHER_BUFF_EFFECT_DAMAGE_FACTOR = 1.5;
    //</editor-fold>

    public static VIP vipClass = new VIP();

    //<editor-fold defaultstate="" desc="EDITOR FOLD EXAMPLE">
    //</editor-fold>

    // EVENTS

    //<editor-fold defaultstate="collapsed" desc="PLAYER EVENTS FUNCTIONS">
    public static void crumblingTools(PlayerItemDamageEvent event) {
        if (Main.config.getBoolean("CRUMBLING_TOOLS") || Main.config.getBoolean("NIGHTMARE")) {
            int d3 = random(0, 2);
            event.setDamage(event.getDamage() + d3);
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

    public void teleportSpectatorToWorldSpawn(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() == GameMode.SPECTATOR) {
            event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        }
    }

    public void addJoinCooldowns(String playerName) {
        spectatorJoinTime.put(playerName, getTimeSeconds());
    }

    public void forceSpectatorToDeadPlayers(Player player) {
        if (!DraksPlayer.playerCanRevive(player.getName()) && !player.isOp()) {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    public void preventDeadPlayerMove(PlayerMoveEvent event, Player player) {
        if (!player.hasPermission(ALLOW_GAMEMODE) && !player.getGameMode().equals(GameMode.SURVIVAL) && !player.isOp()) {
            if (spectatorJoinTime.containsKey(player.getName()) && getTimeSeconds() - spectatorJoinTime.get(player.getName()) >= 3) {
                if (DraksPlayer.playerCanRevive(player.getName())) {
                    DraksPlayer.forcePlayerRevive(player);
                } else {
                    DraksPlayer.playerWarnUnrevivable(player);
                }
            }
            event.setCancelled(true);
        }
    }

    public void rawMeatPoisoning(PlayerItemConsumeEvent event) {
        if (Checks.isRawMeat(event.getItem().getType()) && !event.isCancelled()) {
            if (Main.config.getBoolean("RAW_FOOD_POISONING") || Main.config.getBoolean("NIGHTMARE")) {
                applyEffectShiny(event.getPlayer(), PotionEffectType.POISON, 30, 1);
                applyEffectShiny(event.getPlayer(), PotionEffectType.CONFUSION, 60, 1);
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

    public void Tyo_DrakCreateNuke(Player playerInteracting, Creeper creeper) {
        if (playerInteracting.getName().equals("Tyo_Drak") && playerInteracting.getInventory().getItemInMainHand().getType().equals(Material.NETHER_STAR)) {
            creeper.setPowered(true);
            creeper.setExplosionRadius(100);
            getServer().broadcastMessage(ChatColor.RED + "A NUKE HAS BEEN CREATED AT " + playerInteracting.getLocation().toString());
        }
    }

    public void animalSacrifice(Player playerInteracting, Entity entity, Animals animal) {
        if (Checks.isSacrificeable(animal) && playerInteracting.hasPermission(Druida.BASE_PERMISSION) && playerInteracting.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            Druida.sacrificeAnimal(playerInteracting, entity, animal);
        }
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ENTITY EVENTS FUNCTIONS">
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
                    int d4 = random(1, 4);
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
                    if (!((Zombie) entity).isBaby()) {
                        Zombie baby = (Zombie) duplicate(entity);
                        baby.setBaby(true);
                    }
                }
            }
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

    public static void buffCreeper(Entity entity, int d100) {
        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            if (Main.config.getBoolean("BUFF_MOBS") || Main.config.getBoolean("NIGHTMARE")) {
                if (d100 <= 25) {
                    creeper.setPowered(true);
                    creeper.setExplosionRadius(10);
                    creeper.setMaxFuseTicks(10);
                } else {
                    creeper.setExplosionRadius(6);
                    creeper.setMaxFuseTicks(20);
                }
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

    public static void betterPotions(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getNewEffect() != null) {
                if (Main.config.getBoolean("BETTER_POTIONS") || Main.config.getBoolean("NIGHTMARE")) {
                    if (event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_DRINK) || event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_SPLASH)) {
                        if (event.getNewEffect().getType().equals(PotionEffectType.SPEED)) {
                            if (!hasPotionEffect(player, PotionEffectType.FAST_DIGGING, 99999, event.getNewEffect().getAmplifier() * BETTER_POTIONS_HASTE_FACTOR)) {
                                applyEffectShiny(player, PotionEffectType.FAST_DIGGING, event.getNewEffect().getDuration(), event.getNewEffect().getAmplifier() * BETTER_POTIONS_HASTE_FACTOR);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void classImmunities(EntityPotionEffectEvent event) {
        applyImmunitiesDruida(event);
        applyImmunitiesFerreiro(event);
        applyImmunitiesGatuno(event);
        applyImmunitiesEspadachim(event);
        applyImmunitiesCacador(event);
    }

    public static void preventTargetInvisible(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            event.setCancelled(player.hasPotionEffect(PotionEffectType.INVISIBILITY));
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
        if (event.getCause().equals(EntityDamageEvent.DamageCause.STARVATION) && !event.isCancelled()) {
            if (Main.config.getBoolean("STARVE_TO_DEATH") || Main.config.getBoolean("NIGHTMARE")) {
                event.setDamage(200);
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
        if (event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && !event.isCancelled()) {
                if (Main.config.getBoolean("HURTS_LEGS") || Main.config.getBoolean("NIGHTMARE")) {
                    applyEffectVisible((LivingEntity) event.getEntity(), PotionEffectType.SLOW, 30 * (int) event.getDamage(), 5);
                }
            }
        }
    }

    public static void blindnessFromProjectileHit(EntityDamageEvent event) {
        if (Main.config.getBoolean("BLIND_FROM_PROJECTILE_HIT") || Main.config.getBoolean("NIGHTMARE")) {
            if (event.getEntity() instanceof Player) {
                applyEffectSubtle((Player) event.getEntity(), PotionEffectType.BLINDNESS, 4, 5);
            }
        }
    }

    public static void silverfishDoesNotSuffocate(EntityDamageEvent event) {
        if (Main.config.getBoolean("SILVERFISH_HELL") || Main.config.getBoolean("NIGHTMARE")) {
            if (event.getEntity() instanceof Silverfish && event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                event.setDamage(0);
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

    public static void witherBuff(EntityDamageByEntityEvent event, Entity entityDamager, double eventDamage) {
        if (entityDamager instanceof WitherSkull) {
            if (Main.config.getBoolean("WITHER_BUFF") || Main.config.getBoolean("NIGHTMARE")) {
                event.setDamage(eventDamage * WITHER_TOTAL_DAMAGE);
            }
        }
    }

    public static void safeDropItem(Location entityLocation, ItemStack itemStack) {
        if (!itemStack.getType().equals(Material.AIR)) {
            entityLocation.getWorld().dropItem(entityLocation, itemStack);
        }
    }

    public void fleshMeleeBonusDamage(EntityDamageByEntityEvent event) {
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

    public void bonesMeleeBonusDamage(EntityDamageByEntityEvent event) {
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

    public void balrogHitSpawnLavafish(EntityDamageByEntityEvent event, Entity entityDamagee) {
        if (entityDamagee instanceof Silverfish && !event.isCancelled()) {
            if (entityDamagee.getCustomName() != null && entityDamagee.getCustomName().contains("Balrog")) {
                // BALROG HIT BY ENTITY
                Silverfish balrog = (Silverfish) entityDamagee;
                createAncientLavafish(2, balrog.getWorld(), balrog.getLocation());
            }
        }
    }


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PLAYER">
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        // USEFUL VARIABLES
        Player player = event.getPlayer();
        String playerName = player.getName();

        // ACTIONS
        addJoinCooldowns(playerName);
        resetClassEffects(player);
        claims.loadConfigClaimAndTrust(playerName);
    }

    @EventHandler
    public void playerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
        teleportSpectatorToWorldSpawn(event);
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        // USEFUL VARIABLES
        Player player = event.getPlayer();

        // ACTIONS
        forceSpectatorToDeadPlayers(player);
        preventDeadPlayerMove(event, player);
    }

    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent event) {
        preventUndroppableItemDrop(event);
    }

    @EventHandler
    public void playerShearEntityEvent(PlayerShearEntityEvent event) {
        // USEFUL VARIABLES
        Entity entity = event.getEntity();
        Player player = event.getPlayer();

        // ACTIONS
        preventNotDruidaShear(event, player);
    }

    @EventHandler
    public void playerPickupArrowEvent(PlayerPickupArrowEvent event) {
        if (event.getArrow().getCustomName() != null) {
            event.setCancelled(event.getArrow().getCustomName().equals("Flecha de Prata"));
        }
    }

    @EventHandler
    public void playerItemConsumeEvent(PlayerItemConsumeEvent event) {
        // USEFUL VARIABLES
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Material itemType = item.getType();

        // ACTIONS
        preventMilkDrink(event);
        rawMeatPoisoning(event);
        preventDruidaMeatEating(event, player);
    }

    @EventHandler
    public void playerTeleportEvent(PlayerTeleportEvent event) {
        preventPlayerSpectateOthers(event);
    }

    @EventHandler
    public void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player playerInteracting = event.getPlayer();
        Entity entity = event.getRightClicked();

        Druida.druidasBlessing(playerInteracting, entity);

        /*      WEAKER CLASSES RIDE SWORDSMEN
        if (event.getRightClicked() instanceof Player){
            Player playerInteracted = (Player) event.getRightClicked();
            if (playerInteracted.isSneaking() && playerInteracting.getInventory().getItemInMainHand().getType().equals(Material.AIR) &&
                    !playerInteracting.hasPermission(PREVENT_PLAYER_MOUNT) && !playerInteracted.hasPermission(PREVENT_PLAYER_MOUNT) &&
                    (playerInteracting.hasPermission(Cacador.BASE_PERMISSION) || playerInteracting.hasPermission(Druida.BASE_PERMISSION)) && playerInteracted.hasPermission(Espadachim.BASE_PERMISSION)){
                if (playerInteracted.getPassengers().size() == 0) {
                    playerInteracted.getPassengers().add(playerInteracting);
                } else {
                    if (playerInteracted.getPassengers().get(0) instanceof Player){
                        Player playerMounted = (Player) playerInteracted.getPassengers().get(0);
                        denyPlayerAction(playerInteracting, Espadachim.classColor+ playerInteracted.getName() +ChatColor.DARK_RED+" já está carregando alguém!");
                        playerMounted.sendMessage(playerInteracting.getName()+ ChatColor.YELLOW+" gostaria de subir neste jogador.");
                        playerMounted.playSound(playerMounted.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                        playerInteracted.sendMessage(playerInteracting.getName()+ ChatColor.GREEN+" gostaria de subir em você.");
                        playerInteracted.playSound(playerInteracted.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                    }
                }
            }
        }
        */

        if (entity instanceof Animals) {
            Animals animal = (Animals) entity;
            animalSacrifice(playerInteracting, entity, animal);
            preventNotDruidasCowMilk(event);
            preventNotDruidaFishBucket(event);
            preventNotCacadorNotDruidaWolfTame(event);
        }
        if (Checks.isEvil(entity.getType())) {
            if (entity instanceof Creeper) {
                Creeper creeper = (Creeper) entity;
                if (!creeper.isPowered()) {
                    Tyo_DrakCreateNuke(playerInteracting, creeper);
                    chargeCreeper(playerInteracting, creeper);
                }
            } else if (entity instanceof Silverfish) {
                Silverfish silverfish = (Silverfish) entity;
                TyoDrakSpawnBalrog(event, playerInteracting, silverfish);
            }
        }
    } // GRIEF PREVENTED

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        // Variables
        Player player = event.getPlayer();
        ItemStack itemMain = event.getPlayer().getInventory().getItemInMainHand();
        ItemStack itemOff = event.getPlayer().getInventory().getItemInOffHand();

        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            Druida.preventFarmlandTrample(event, player);
            Gatuno.nightVeil(player, itemMain.getType(), itemOff.getType());
            Tyo_DrakForceStuff(player);

            // BanItem
            preventNotFerreirosInteract(event);
            preventNotDruidasInteract(event);
            preventNotDruidasPlow(event);
        /*
        if (!skillsCooldowns.containsKey(player.getName() + " " + Cacador.HUNTER_MOUNT_PERMISSION) || hasPassedSince(3, skillsCooldowns.get(player.getName() + " " + Cacador.HUNTER_MOUNT_PERMISSION))) {
            Cacador.phanty(player);
        }
        */

        /*if (Checks.isForGatunosInteract(event.getClickedBlock().getType()) && !player.hasPermission(Gatuno.BASE_PERMISSION)){
            event.setCancelled(true);
            denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Gatuno.classColor + "Gatunos" + ChatColor.DARK_RED + " podem usar isto!");
        } else if (Checks.isForCacadoresInteract(event.getClickedBlock().getType()) && !player.hasPermission(Cacador.BASE_PERMISSION)){
            event.setCancelled(true);
            denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Cacador.classColor + "Cacadores" + ChatColor.DARK_RED + " podem usar isto!");
        } else if (Checks.isForEspadachinsInteract(event.getClickedBlock().getType()) && !player.hasPermission(Espadachim.BASE_PERMISSION)){
            event.setCancelled(true);
            denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Espadachim.classColor + "Espadachins" + ChatColor.DARK_RED + " podem usar isto!");
        }*/
        }

    }

    @EventHandler
    public void playerLeashEntityEvent(PlayerLeashEntityEvent event) {
        Player player = event.getPlayer();
        preventNotDruidaLeashEntity(event, player);
    }

    @EventHandler
    public void playerItemDamageEvent(PlayerItemDamageEvent event) {
        crumblingTools(event);
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        DraksPlayer.addPlayerDeathTime(event.getEntity());
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent event) {
        forceSpectatorToDeadPlayers(event.getPlayer());
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
    public void entityResurrectEvent(EntityResurrectEvent event) {
        preventNotEspadachimTotemUse(event);
    }

    @EventHandler
    public void entitySpawnEvent(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        int d100 = random(1, 100);
        preventZombieNPCSpawn(event, entity);
        buffCreeper(entity, d100);
        buffZombie(entity, d100);
        buffSkeleton(entity, d100);
        buffWither(entity);
        preventMobDropDuping(entity);
    }

    @EventHandler
    public void entityBreedEvent(EntityBreedEvent event) {
        Druida.natureFertility(event, random(0, 3));
    }

    @EventHandler
    public void entityMountEvent(EntityMountEvent event) {
        Espadachim.mountHorse(event);
        Ferreiro.mountChestedHorse(event);
        Druida.mountStrider(event);
    }

    @EventHandler
    public void entityDismountEvent(EntityDismountEvent event) {
        /*
        if (event.getDismounted() instanceof Phantom) {
            Phantom phantom = (Phantom) event.getDismounted();
            if (event.getEntity() instanceof Player) {
                if (phantom.getCustomName().contains("Phanty")) {
                    phantom.remove();
                }
            }
        }
        */
        removeBalrogWallClimber(event);
    }

    @EventHandler
    public void entityPotionEffectEvent(EntityPotionEffectEvent event) {
        betterPotions(event);
        classImmunities(event);
    }

    @EventHandler
    public void entityTargetEvent(EntityTargetLivingEntityEvent event) {
        /*
        if (entity instanceof Phantom) {
            Phantom phantom = (Phantom) entity;
            if (phantom.getCustomName()!=null){
                if (phantom.getCustomName().contains("Phanty")) {
                    event.setCancelled(true);
                }
            }
        }
        */
        Entity entity = event.getEntity();
        Druida.greenBlood(event);
        wolfTarget(event, entity);
        preventTargetInvisible(event);
    }

    @EventHandler
    public void entityPoseChangeEvent(EntityPoseChangeEvent event) {
    }

    @EventHandler
    public void entityExplodeEvent(EntityExplodeEvent event) {
        event.blockList().removeIf(claims::isClaimedChunkBlock);
        event.setYield((50F + random(1, 25)) / 100F);
        for (Block block :
                event.blockList()) {
            silverfishHell(block, random(1, 100) * 4);
        }
    } // GRIEF PREVENTED

    @EventHandler
    public void entityShootBowEvent(EntityShootBowEvent event) {
        Cacador.shootCrossbow(event);
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        Druida.preventPlantDamage(event, entity);
        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            creeperBomb(event, creeper);
        }
        if (entity instanceof Player) {
            warnNotEspadachimTotemHeld(event);
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
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entityDamager = event.getDamager();
        Entity entityDamagee = event.getEntity();

        Druida.preventDruidaHurtAnimals(event);
        preventCombatWithoutProperWeapon(event, entityDamager);
        claims.preventClaimedChunkAnimalsHurt(event, entityDamager, entityDamagee);

        bonesMeleeBonusDamage(event);
        fleshMeleeBonusDamage(event);

        Gatuno.roguePoison(event);
        Gatuno.endNightVeilBonusDamage(event);
        Gatuno.backstab(event);

        if (entityDamager instanceof Projectile && !event.isCancelled()) {
            Projectile projectile = (Projectile) entityDamager;
            blindnessFromProjectileHit(event);
            witherBuff(event, entityDamager, eventDamage);
            if (projectile.getShooter() instanceof Player) {
                Player playerShooter = (Player) projectile.getShooter();
                ItemStack itemMain = playerShooter.getInventory().getItemInMainHand();
                claims.preventClaimedChunksAnimalsShoot(event, playerShooter);
                fleshBowBonusDamage(event, entityDamagee, itemMain);
                boneBowBonusDamage(event, entityDamagee, itemMain);
            }
        }

        spiderBuff(event);
        Espadachim.swordsmanResolve(event);
        if (){

        }
        silverfishNonFerreiroLesserDamage(event, entityDamager, entityDamagee);
        balrogHitSpawnLavafish(event, entityDamagee);
        arrowCleanup(event);
    } // GRIEF PREVENTED MELEE + RANGED

    @EventHandler
    public void entityDeathEvent(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Player playerEntityKiller = event.getEntity().getKiller();
        World entityWorld = event.getEntity().getWorld();
        Location entityLocation = event.getEntity().getLocation();
        int d100 = random(1, 100);
        if (Checks.isEvil(entity.getType())/*&& !entity.getName().contains("Phanty")*/) {
            hydra(d100, entity, playerEntityKiller);
            if (playerEntityKiller != null) {
                if (playerEntityKiller.hasPermission(Gatuno.BASE_PERMISSION)) {
                    event.getDrops().addAll(event.getDrops());
                    event.getDrops().addAll(event.getDrops());
                    event.setDroppedExp(event.getDroppedExp() * 4);
                    Gatuno.givePlayerMonsterRareLoot(playerEntityKiller, entity, random(1, 100));
                } else {
                    event.getDrops().addAll(event.getDrops());
                    event.setDroppedExp(event.getDroppedExp() * 2);
                }
                if (entity instanceof Silverfish) {
                    if (playerEntityKiller.hasPermission(Ferreiro.BASE_PERMISSION)) {
                        safeDropItem(entityLocation, silverfishLootTable(d100));
                    } else {
                        safeDropItem(entityLocation, silverfishLootTable(d100 - 15));
                    }
                }
            }
            if (event.getEntity() instanceof Wither) {
                if (Main.config.getBoolean("WITHER_BUFF_DROPS") || Main.config.getBoolean("WITHER_BUFF") || Main.config.getBoolean("NIGHTMARE")) {
                    witherBuffDrops(event);
                }
            } // WITHER DROPS
            if (event.getEntity() instanceof Silverfish && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains("Balrog")) {
                balrogDrops(event);
            } // BALROG DROPS
        }
        /*
        if (event.getEntity() instanceof Phantom) {
            if (event.getEntity().getName().contains("Phanty")) {
                event.getDrops().removeAll(event.getDrops());
            }
        }
        */ // Prevent Phanty's drops
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BLOCK">
    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent event) {
        preventUnplaceableItemPlace(event);
        Player player = event.getPlayer();
        Block blockPlaced = event.getBlockPlaced();
        ItemStack itemMain = player.getInventory().getItemInMainHand();
        ItemStack itemOff = player.getInventory().getItemInOffHand();
        Block block = event.getBlock();
        if (!claims.chunkClaimedAndNotTrusted(block.getLocation(), player.getName())) {
            if (itemMain.getType().equals(Material.SPAWNER) || itemOff.getType().equals(Material.SPAWNER)) {
                CreatureSpawner creatureSpawner = (CreatureSpawner) blockPlaced.getState();
                if (player.getInventory().getItemInMainHand().getType().equals(Material.SPAWNER)) {
                    if (itemMain.getItemMeta() != null) {
                        if (itemMain.getItemMeta().getLore() != null) {
                            creatureSpawner.setSpawnedType(Ferreiro.nameToEntityType(itemMain.getItemMeta().getLore().get(0).replace("Spawned Type: ", "")));
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
                            creatureSpawner.setSpawnedType(Ferreiro.nameToEntityType(itemOff.getItemMeta().getLore().get(0).replace("Spawned Type: ", "")));
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
            if (Checks.isForFerreirosPlace(blockPlaced.getType()) && !player.hasPermission(Ferreiro.BASE_PERMISSION)) {
                event.setCancelled(true);
                DraksPlayer.denyPlayerAction(player, "IS_FOR_FERREIROS_PLACE", ChatColor.DARK_RED + "Apenas " + Ferreiro.classColor + "Ferreiros" + ChatColor.DARK_RED + " podem posicionar isto!");
            } else if (Checks.isForDruidasPlace(blockPlaced.getType()) && !player.hasPermission(Druida.BASE_PERMISSION)) {
                event.setCancelled(true);
                DraksPlayer.denyPlayerAction(player, "IS_FOR_DRUIDAS_PLACE", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " podem posicionar isto!");
            } /* else if (Checks.isForGatunosPlace(blockPlaced.getType()) && !player.hasPermission(Gatuno.BASE_PERMISSION)){
            event.setCancelled(true);
            denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Gatuno.classColor + "Gatunos" + ChatColor.DARK_RED + " podem posicionar isto!");
        } else if (Checks.isForCacadoresPlace(blockPlaced.getType()) && !player.hasPermission(Cacador.BASE_PERMISSION)){
            event.setCancelled(true);
            denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Cacador.classColor + "Cacadores" + ChatColor.DARK_RED + " podem posicionar isto!");
        } else if (Checks.isForEspadachinsPlace(blockPlaced.getType()) && !player.hasPermission(Espadachim.BASE_PERMISSION)){
            event.setCancelled(true);
            denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Espadachim.classColor + "Espadachins" + ChatColor.DARK_RED + " podem posicionar isto!");
        } */ else if (Checks.isForDruidasPlant(blockPlaced.getType()) && !player.hasPermission(Druida.BASE_PERMISSION)) {
                event.setCancelled(true);
                DraksPlayer.denyPlayerAction(player, "IS_FOR_DRUIDAS_PLANT", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " sabem plantar!");
            }
        } else {
            event.setCancelled(true);
            DraksPlayer.denyPlayerAction(player, "GRIEF_PREVENTION", "Esta Chunk pertence a outro jogador!");
        }
    } // GRIEF PREVENTED

    @EventHandler
    public void blockFertilizeEvent(BlockFertilizeEvent event) {
        if (event.getPlayer() != null && event.getPlayer().hasPermission(Druida.BASE_PERMISSION) && event.getBlock() instanceof Ageable) {
            Ageable ageableBlock = (Ageable) event.getBlock().getBlockData();
            if (ageableBlock.getAge() < ageableBlock.getMaximumAge() / 2) {
                ageableBlock.setAge(ageableBlock.getMaximumAge() / 2);
            } else if (ageableBlock.getAge() >= ageableBlock.getMaximumAge() / 2) {
                ageableBlock.setAge(ageableBlock.getMaximumAge());
            }
            event.getBlock().setBlockData(ageableBlock);
        }
    }

    /*
    @EventHandler
    public void blockDamageEvent(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
    }
    */

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        int d100 = random(1, 100);
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = event.getBlock().getType();
        World world = block.getWorld();
        if (!claims.chunkClaimedAndNotTrusted(player.getLocation(), player.getName())) {
            spawnerBreak(event, player, block, blockType);
            forFerreiros(event, player, blockType);
            forDruidas(event, player, blockType);
            forDruidasHarvest(event, player, block, blockType, world);
            oreOrInfestableBreak(d100, player, block, blockType);
            ancientDebrisBreak(event, player, block);
        } else {
            event.setCancelled(true);
            DraksPlayer.denyPlayerAction(player, "GRIEF_PREVENTION", "Esta Chunk pertence a outro jogador!");
        }
    } // GRIEF PREVENTED

    public void ancientDebrisBreak(BlockBreakEvent event, Player player, Block block) {
        if (!event.isCancelled() && event.getBlock().getType().equals(Material.ANCIENT_DEBRIS) && player.hasPermission(Ferreiro.MASTERY_PERMISSION)) {
            int d3 = random(1, 3);
            if (d3 == 3) {
                spawnBalrog(player, block);
            } else {
                createAncientLavafish(20, block.getWorld(), block.getLocation(), player);
            }
        }
    }

    public void oreOrInfestableBreak(int d100, Player player, Block block, Material blockType) {
        if (Checks.isOre(blockType) || Checks.isInfestableStone(blockType)) {
            if (player.hasPermission(Ferreiro.BASE_PERMISSION)) {
                silverfishHell(block, d100 * 4);
            } else {
                silverfishHell(block, d100);
            }
        }
    }

    public void forDruidasHarvest(BlockBreakEvent event, Player player, Block block, Material blockType, World world) {
        if (Checks.isForDruidasHarvest(blockType)) {
            // CROPS
            if (player.hasPermission(Druida.BASE_PERMISSION)) {
                druida3x3Harvest(event, player, block, world);
            } else {
                // NÃO É DRUIDA
                event.setCancelled(true);
                DraksPlayer.denyPlayerAction(player, "IS_FOR_DRUIDAS_HARVEST", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " sabem colher!");
            }
        }
    }

    public void druida3x3Harvest(BlockBreakEvent event, Player player, Block block, World world) {
        if (Checks.isFarmlandCrop(block.getType())) {
            ExperienceOrb experienceOrb = (ExperienceOrb) world.spawnEntity(block.getLocation(), EntityType.EXPERIENCE_ORB);
            experienceOrb.setExperience(2);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Block currentBlock = world.getBlockAt(block.getX() - i, block.getY(), block.getZ() - j);
                    if (Checks.isMatureCrop(currentBlock) && !currentBlock.getType().equals(Material.MELON_STEM) && !currentBlock.getType().equals(Material.PUMPKIN_STEM)) {
                        for (ItemStack item :
                                currentBlock.getDrops(player.getInventory().getItemInMainHand())) {
                            world.dropItem(currentBlock.getLocation(), item);
                            ((ExperienceOrb) duplicate(experienceOrb)).setExperience(item.getAmount());
                            world.spawnEntity(currentBlock.getLocation(), EntityType.EXPERIENCE_ORB);
                        }
                        Ageable ageableCurrentBlock = ((Ageable) currentBlock.getBlockData());
                        ageableCurrentBlock.setAge(0);
                        currentBlock.setBlockData(ageableCurrentBlock);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public void forDruidas(BlockBreakEvent event, Player player, Material blockType) {
        if (Checks.isForDruidasBreak(blockType) && !player.hasPermission(Druida.BASE_PERMISSION)) {
            event.setCancelled(true);
            DraksPlayer.denyPlayerAction(player, "IS_FOR_DRUIDAS_BREAK", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " podem quebrar isto!");
        } /* else if (Checks.isForGatunosInteract(event.getClickedBlock().getType()) && !player.hasPermission(Gatuno.BASE_PERMISSION)){
        event.setCancelled(true);
        denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Gatuno.classColor + "Gatunos" + ChatColor.DARK_RED + " podem usar isto!");
    } else if (Checks.isForCacadoresInteract(event.getClickedBlock().getType()) && !player.hasPermission(Cacador.BASE_PERMISSION)){
        event.setCancelled(true);
        denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Cacador.classColor + "Cacadores" + ChatColor.DARK_RED + " podem usar isto!");
    } else if (Checks.isForEspadachinsInteract(event.getClickedBlock().getType()) && !player.hasPermission(Espadachim.BASE_PERMISSION)){
        event.setCancelled(true);
        denyPlayerAction(player, ChatColor.DARK_RED + "Apenas " + Espadachim.classColor + "Espadachins" + ChatColor.DARK_RED + " podem usar isto!");
    } */
    }

    public void forFerreiros(BlockBreakEvent event, Player player, Material blockType) {
        if (Checks.isForFerreirosBreak(blockType) && !player.hasPermission(Ferreiro.BASE_PERMISSION)) {
            event.setCancelled(true);
            DraksPlayer.denyPlayerAction(player, "IS_FOR_FERREIROS_BREAK", ChatColor.DARK_RED + "Apenas " + Ferreiro.classColor + "Ferreiros" + ChatColor.DARK_RED + " podem quebrar isto!");
        }
    }

    public void spawnerBreak(BlockBreakEvent event, Player player, Block block, Material blockType) {
        if (blockType == Material.SPAWNER) {
            if (player.hasPermission(Espadachim.BASE_PERMISSION)) {
                spawnerEspadachimSilkHarvest(event, player, block);
            } else {
                preventNotEspadachimBreakSpawner(event, player);
            }
        }
    }

    public void spawnerEspadachimSilkHarvest(BlockBreakEvent event, Player player, Block block) {
        if (Checks.playerMainHasEnchantment(player, Enchantment.SILK_TOUCH)) {
            CreatureSpawner spawnerBlock = (CreatureSpawner) block.getState();
            ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
            ItemMeta spawnerMeta = spawner.getItemMeta();
            ArrayList<String> spawnerLore = new ArrayList<>();
            Objects.requireNonNull(spawnerMeta).setDisplayName(
                    ChatColor.YELLOW + Ferreiro.keyToName(spawnerBlock.getSpawnedType().toString()) +
                            ChatColor.DARK_AQUA + " Spawner");
            spawnerLore.add("Spawned Type: " + Ferreiro.keyToName(spawnerBlock.getSpawnedType().toString()));
            spawnerLore.add("Spawn Count: " + spawnerBlock.getSpawnCount());
            spawnerLore.add("Spawn Range: " + spawnerBlock.getSpawnRange());
            spawnerLore.add("Required Player Range: " + spawnerBlock.getRequiredPlayerRange());
            spawnerLore.add("Max Nearby Entities: " + spawnerBlock.getMaxNearbyEntities());
            spawnerLore.add("Min Spawn Delay: " + spawnerBlock.getMinSpawnDelay());
            spawnerLore.add("Max Spawn Delay: " + spawnerBlock.getMaxSpawnDelay());
            Objects.requireNonNull(spawnerMeta).setLore(spawnerLore);
            spawner.setItemMeta(spawnerMeta);
            addUndroppableTag(spawner);
            block.getWorld().dropItem(block.getLocation(), spawner);
            event.setExpToDrop(0);
        }
    }

    public void preventNotEspadachimBreakSpawner(BlockBreakEvent event, Player player) {
        event.setCancelled(true);
        DraksPlayer.denyPlayerAction(player, "NOT_ESPADACHIM_BREAK_SPAWNER", ChatColor.DARK_RED + "Apenas " + Espadachim.CLASS_COLOR + "Espadachins" + ChatColor.DARK_RED + " sabem desativar Spawners!");
    }

    @EventHandler
    public void blockExplodeEvent(BlockExplodeEvent event) {
        event.blockList().removeIf(claims::isClaimedChunkBlock);
        event.setYield((90F + random(1, 10)) / 100F);
        for (Block block :
                event.blockList()) {
            silverfishHell(block, random(1, 100) * 4);
        }
    } // GRIEF PREVENTED

    /*
    @EventHandler
    public void blockDamageEvent(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = event.getBlock().getType();
        int d100 = random(1, 100);
        if (Checks.isLeaves(blockType)){
            if (player.hasPermission(Druida.BASE_PERMISSION)){
                if (player.getFoodLevel() > 0){
                    block.breakNaturally(player.getInventory().getItemInMainHand());
                    player.getInventory().addItem(new ItemStack(Material.BONE_MEAL, random(1,100)/20));
                    player.setFoodLevel(player.getFoodLevel()-1);
                }
            }
        }
    }
    */
    //</editor-fold>

    @EventHandler
    public void projectileHitEvent(ProjectileHitEvent event) {
        Cacador.invertArrowDamageEffect(event);
        Cacador.uninvertArrowDamageEffect(event);
    }

    @EventHandler
    public void craftItemEvent(CraftItemEvent event) {
        Ferreiro.proficientCrafting(event);
    }

    @EventHandler
    public void furnaceSmeltEvent(FurnaceSmeltEvent event) {
        int d100 = random(1, 100);
        ItemStack result = event.getResult();
        Material resultType = result.getType();
        Ferreiro.betterSmelt(event, d100, resultType);
    }

    // METHODS

    //<editor-fold defaultstate="collapsed" desc="NIGHTMARE">
    public static void hydra(int d100, Entity entity, Player player) {
        if (Main.config.getBoolean("HYDRA") || Main.config.getBoolean("NIGHTMARE")) {
            if (!(entity instanceof Slime)) {
                Entity hydra1 = null;
                Entity hydra2 = null;
                Entity hydra3 = null;
                Entity hydra4;
                if (d100 >= 67) {
                    hydra1 = duplicate(entity);
                    ((LivingEntity) hydra1).setCanPickupItems(false);
                    if (hydra1 instanceof Mob) {
                        ((Mob) hydra1).setTarget(player);
                    }
                }
                if (d100 >= 85) {
                    hydra2 = duplicate(entity);
                    ((LivingEntity) hydra2).setCanPickupItems(false);
                    if (hydra2 instanceof Mob) {
                        ((Mob) hydra2).setTarget(player);
                    }
                }
                if (d100 >= 94) {
                    hydra3 = duplicate(entity);
                    ((LivingEntity) hydra3).setCanPickupItems(false);
                    if (hydra3 instanceof Mob) {
                        ((Mob) hydra3).setTarget(player);
                    }
                }
                if (d100 >= 98) {
                    hydra4 = duplicate(entity);
                    if (hydra4 instanceof Mob) {
                        ((Mob) hydra4).setTarget(player);
                    }
                    ((LivingEntity) hydra4).setCanPickupItems(false);
                    if (player != null && d100 == 100) {
                        if (Main.config.getBoolean("HYDRA_EGG") || Main.config.getBoolean("BUFF_DROPS") || Main.config.getBoolean("NIGHTMARE")) {
                            entity.getWorld().dropItem(entity.getLocation(), getSpawnEgg(entity));
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
                                setBaseMaxHealth(hoglin, 40);
                                hoglin.setTarget(player);
                                hoglin.addPassenger(hydra4);
                            } else {
                                SkeletonHorse skeletonHorse = (SkeletonHorse) hydra4.getWorld().spawnEntity(hydra4.getLocation(), EntityType.SKELETON_HORSE);
                                skeletonHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
                                setBaseMaxHealth(skeletonHorse, 20);
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

    public static void silverfishHell(Block block, int d100) {
        if (Main.config.getBoolean("SILVERFISH_HELL") || Main.config.getBoolean("NIGHTMARE")) {
            Material type = block.getType();
            if (block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() != Material.FIRE) {
                if (Checks.isInfestableStone(type) || Checks.isCobblestone(type) || Checks.isSandstone(type) ||
                        Checks.isCrackedBricks(type) || Checks.isMossy(type)) {
                    if (d100 <= 25) {
                        spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                    }
                } else if (Checks.isOre(type)) {
                    if (Checks.isT0Ore(type)) {
                        if (d100 <= 33) {
                            Silverfish s1 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s1.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                        }
                    }
                    if (Checks.isT1Ore(type)) {
                        if (d100 <= 50) {
                            Silverfish s1 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s1.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                            Silverfish s2 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s2.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                        }
                    }
                    if (Checks.isT2Ore(type)) {
                        if (d100 <= 75) {
                            Silverfish s1 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s1.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                            Silverfish s2 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s2.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                            Silverfish s3 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s3.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                        }
                    }
                    if (Checks.isT3Ore(type)) {
                        if (d100 <= 100) {
                            Silverfish s1 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s1.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                            Silverfish s2 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s2.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                            Silverfish s3 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s3.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                            Silverfish s4 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s4.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
                            Silverfish s5 = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
                            s5.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);

                            s1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999, 0));
                            s2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999, 0));
                            s3.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999, 1));
                            s4.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999, 1));
                            s5.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 9999, 0));
                        }
                    }
                }
            }
        }
    }

    public static void createAncientLavafish(int count, World world, Location location) {
        for (int i = 0; i < count; i++) {
            Silverfish lavafish = (Silverfish) spawnEntity(EntityType.SILVERFISH, world, location);
            applyEffectInvisible(lavafish, PotionEffectType.FIRE_RESISTANCE, 99999, 1);
            applyEffectInvisible(lavafish, PotionEffectType.SPEED, 99999, 2);

        }
    }

    public static void createAncientLavafish(int count, World world, Location location, LivingEntity target) {
        for (int i = 0; i < count; i++) {
            Silverfish lavafish = (Silverfish) spawnEntity(EntityType.SILVERFISH, world, location);
            applyEffectInvisible(lavafish, PotionEffectType.FIRE_RESISTANCE, 99999, 1);
            applyEffectInvisible(lavafish, PotionEffectType.SPEED, 99999, 2);
            lavafish.setTarget(target);
        }
    }

    public static Silverfish spawnBalrog(Player player, Block block) {
        Silverfish balrog = (Silverfish) spawnEntity(EntityType.SILVERFISH, block.getWorld(), block.getLocation());
        balrog.setCustomName("" + ChatColor.DARK_RED + ChatColor.BOLD + "Balrog");
        balrog.setFireTicks(99999);
        applyEffectInvisible(balrog, PotionEffectType.FIRE_RESISTANCE, 99999, 9);
        applyEffectInvisible(balrog, PotionEffectType.REGENERATION, 99999, 1);
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
        applyEffectInvisible(wallClimber, PotionEffectType.INVISIBILITY, 99999, 9);
        player.getServer().broadcastMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "Um Demônio das profundezas infernais foi despertado!");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 10, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 10, 1);
        balrog.setTarget(player);
        return balrog;
    }

    //<editor-fold defaultstate="" desc="MOB BUFFS">
    public static void zombieBuff(Zombie entity) {
    }

    public static void skeletonBuff(Skeleton skeleton) {
    }

    public static void spiderBuff(EntityDamageByEntityEvent event) {
        if (Main.config.getBoolean("BUFF_MOBS") || Main.config.getBoolean("NIGHTMARE")) {
            if (event.getDamager() instanceof Spider && !event.isCancelled()) {
                if (Checks.isWebbable(event.getEntity().getLocation().getBlock().getType())) {
                    event.getEntity().getLocation().getBlock().setType(Material.COBWEB);
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EFFECTS">
    public static void addNightmareMaxHealthModifier(Player player) {
        if (Main.config.getBoolean("HALF_MAX_HEALTH") || Main.config.getBoolean("NIGHTMARE")) {
            setBaseMaxHealth(player, 10);
        }
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

    //</editor-fold>

    //<editor-fold defaultstate="" desc="WITHER BUFF">
    public static void preventUnplaceableItemPlace(BlockPlaceEvent event) {
        if (Checks.isUnplaceable(event.getItemInHand())) {
            try {
                DraksPlayer.denyPlayerAction(event.getPlayer(), "UNPLACEABLE_PLACE", "Você não pode posicionar este item.");
                event.setCancelled(true);
            } catch (Exception ignored) {
            }
        }
    }

    public static void preventUndroppableItemDrop(PlayerDropItemEvent event) {
        if (Checks.isUndroppable(event.getItemDrop().getItemStack())) {
            try {
                DraksPlayer.denyPlayerAction(event.getPlayer(), "UNDROPPABLE_DROP", "Você não pode derrubar este item.");
                event.setCancelled(true);
            } catch (Exception ignored) {
            }
        }
    }

    public static void witherBuffHealth(Wither wither) {
        Objects.requireNonNull(wither.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(600);
        wither.setHealth(600);
    }

    public static void witherBuffDrops(EntityDeathEvent event) {
        event.getDrops().add(getWitherHead());
        event.setDroppedExp(850);
    }

    public static void balrogDrops(EntityDeathEvent event) {
        event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), getBalrogSkull());
        event.setDroppedExp(400);
    }
    //</editor-fold>

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

    public static void starveToDeath(EntityDamageEvent event) {
    }

    public static void bonesBreakFurther(EntityDamageEvent event) {
        if (Checks.isMadeOfBones(event.getEntity().getType())) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                if (Main.config.getBoolean("BONES_BREAK_FURTHER") || Main.config.getBoolean("NIGHTMARE")) {
                    event.setDamage(event.getDamage() * 2);
                }
            }
        }
    }

    public static void fleshBurnsHotter(EntityDamageEvent event) {
        if (Checks.isMadeOfFlesh(event.getEntity().getType())) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.HOT_FLOOR) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                if (Main.config.getBoolean("FLESH_BURNS_HOTTER") || Main.config.getBoolean("NIGHTMARE")) {
                    event.setDamage(event.getDamage() * 2);
                }
            }
        }
    }
    //</editor-fold>

    public static void setPowered(Creeper creeper) {
        creeper.setPowered(true);
        creeper.setMaxFuseTicks(10);
    }

    //<editor-fold defaultstate="" desc="SERVER">
    public static void pseudoCommands(AsyncPlayerChatEvent event, Player player) {
        String playerName = player.getName();
        ItemStack itemMain = player.getInventory().getItemInMainHand();
        Location playerLocation = player.getLocation();

        switch (event.getMessage().toLowerCase()) {
            case "..kit basico":
            case "..kit basic":
            case "..vkb":
            case "..kb":
                if (DraksPlayer.isVIP(playerName)) {
                    if (!kitsCooldowns.containsKey(player.getName() + VIP.BASIC_FLAG) || getTimeSeconds() - kitsCooldowns.get(player.getName() + VIP.BASIC_FLAG) >= 1800) {
                        kitsCooldowns.put(player.getName() + VIP.BASIC_FLAG, getTimeSeconds());
                        vipClass.giveKitBasic(event.getPlayer());
                        player.sendMessage(ChatColor.GREEN + "Você recebeu o Kit Básico!");
                        Debug.consoleMessage("Give BASIC KIT to " + player.getName());
                    } else {
                        player.sendMessage("Espere mais " + getFormattedTimeRemaining(VIP.BASIC_COOLDOWN_SECONDS, kitsCooldowns.get(player.getName() + VIP.BASIC_FLAG)) + " para receber este Kit!");
                    }
                } else {
                    DraksPlayer.denyPlayerAction(player, "NOT_VIP_KIT_BASIC", "Apenas VIPs podem usar este comando!");
                }
                event.setCancelled(true);
                break;
            case ".draksworkresetclasseffects.":
            case ".resetclasseffects.":
            case ".dwrce.":
            case ".rce.":
                if (playerName.equals("Tyo_Drak")) {
                    player.sendMessage("Para atualizar os Efeitos de Classe de todos os jogadores, voce deve segurar uma Nether Star na mao principal, " +
                            "uma Splash Potion na outra mao e estar agachado.");
                    for (Player onlinePlayer : event.getPlayer().getServer().getOnlinePlayers()) {
                        setClassAttributes(onlinePlayer);
                    }
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
                /*Double[] claimBorder = {player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getX() + PLAYER_CLAIM_SIZE, player.getLocation().getX() + PLAYER_CLAIM_SIZE};
                if (!isClaimedBlock(player.getWorld().getBlockAt(player.getLocation()))) {
                    if (!isClaimedArea(player.getWorld().getBlockAt(player.getLocation())) {

                    }
                }*/
        }
        if (event.getMessage().contains("..revive ")) {
            String targetName = event.getMessage().replace("..revive ", "");
            PLAYERS_TO_REVIVE.add(targetName);
            event.setCancelled(true);
        }
    }

    public static void preventPlayerSpectateOthers(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            event.getPlayer().setSpectatorTarget(null);
            event.setCancelled(true);
        }
    }

    public static void Tyo_DrakForceStuff(Player player) {
        if (player.getName().equals("Tyo_Drak")) {
            if (player.isSneaking()) {
                for (Player onlinePlayer :
                        player.getServer().getOnlinePlayers()) {
                    resetClassEffects(onlinePlayer);
                    setClassAttributes(onlinePlayer);
                    for (String playerName :
                            PLAYERS_TO_REVIVE) {
                        Player serverPlayer = getServer().getPlayer(playerName);
                        if (serverPlayer != null) {
                            PLAYER_DEATH_TIME.remove(playerName);
                            Main.config.addDefault(playerName + PLAYER_DEATH_TAG, 0);
                            serverPlayer.setGameMode(GameMode.SURVIVAL);
                            resetClassEffects(onlinePlayer);
                            setClassAttributes(onlinePlayer);
                        }
                    }
                }
            }
        }
    }

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
                applyEffectInvisible((Player) entity, PotionEffectType.HUNGER, 99999, 2);
            }
        }
    }

    public static void arrowCleanup(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    projectile.remove();
                }
            }
        }
    }

    public static ItemStack silverfishLootTable(int d100) {
        ItemStack item = new ItemStack(Material.AIR, 0);
        if (d100 >= 53) {
            switch (random(1, 3)) {
                case 1:
                    item = new ItemStack(Material.GRAVEL, 1);
                    break;
                case 2:
                    item = new ItemStack(Material.COBBLESTONE, 1);
                    break;
                case 3:
                    item = new ItemStack(Material.STONE, 1);
                    break;
            } // GRAVEL, COBBLESTONE, STONE
        }
        if (d100 >= 85) {
            switch (random(1, 2)) {
                case 1:
                    item = new ItemStack(Material.IRON_NUGGET, 1);
                    break;
                case 2:
                    item = new ItemStack(Material.GOLD_NUGGET, 1);
                    break;
            } // IRON NUGGET, GOLD NUGGET
        }
        if (d100 >= 93) {
            switch (random(1, 3)) {
                case 1:
                    item = new ItemStack(Material.IRON_INGOT, 1);
                    break;
                case 2:
                    item = new ItemStack(Material.REDSTONE, 1);
                    break;
                case 3:
                    item = new ItemStack(Material.QUARTZ, 1);
                    break;
            } // IRON INGOT, REDSTONE, QUARTZ
        }
        if (d100 >= 97) {
            switch (random(1, 3)) {
                case 1:
                    item = new ItemStack(Material.GOLD_INGOT);
                    break;
                case 2:
                    item = new ItemStack(Material.LAPIS_LAZULI);
                    break;
                case 3:
                    item = new ItemStack(Material.EMERALD);
                    break;
            } // GOLD INGOT, LAPIS LAZULI, EMERALD
        }
        if (d100 == 100) { // DIAMOND
            item = new ItemStack(Material.DIAMOND);
        }
        return item;
    }

    /*
    public void removeClassEffects(Player player) {
        for (PotionEffect effect :
                player.getActivePotionEffects()) {
            if (effect.getDuration() > 99999 && !effect.getType().equals(PotionEffectType.HUNGER)) {
                player.removePotionEffect(effect.getType());
            }
        }
    }
    */

    //<editor-fold defaultstate="" desc="GENERAL FUNCTIONS">

    public static int random(int min, int max) {
        return (rand.nextInt((max - min) + 1) + min);
    }

    //<editor-fold defaultstate="" desc="ITEM">
    public static ItemStack addUndroppableTag(ItemStack item) {
        ArrayList<String> itemLore;
        Debug.consoleMessage("addUndroppable");
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            Debug.consoleMessage("not nulls");
            ItemMeta itemMeta = item.getItemMeta();
            itemLore = (ArrayList<String>) item.getItemMeta().getLore();
            itemLore.add("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Item não derrubável");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            Debug.consoleMessage("lore: " + itemLore);
        } else if (item.getItemMeta() != null && item.getItemMeta().getLore() == null) {
            Debug.consoleMessage("lore null");
            ItemMeta itemMeta = item.getItemMeta();
            itemLore = new ArrayList<>();
            itemLore.add("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Item não derrubável");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            Debug.consoleMessage("lore: " + itemLore);
        }
        return item;
    }

    public static ItemStack addUnplaceableTag(ItemStack item) {
        ArrayList<String> itemLore;
        Debug.consoleMessage("addUnplaceable");
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            Debug.consoleMessage("not nulls");
            ItemMeta itemMeta = item.getItemMeta();
            itemLore = (ArrayList<String>) item.getItemMeta().getLore();
            itemLore.add("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Item não posicionável");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            Debug.consoleMessage("lore: " + itemLore);
        } else if (item.getItemMeta() != null && item.getItemMeta().getLore() == null) {
            Debug.consoleMessage("lore null");
            ItemMeta itemMeta = item.getItemMeta();
            itemLore = new ArrayList<>();
            itemLore.add("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Item não posicionável");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            Debug.consoleMessage("isUndroppable");
        }
        return item;
    }

    public static ItemStack getSpawnEgg(Entity entity) {
        ItemStack egg = new ItemStack(Material.EGG);
        switch (entity.getType()) {
            case VEX:
                egg = new ItemStack(Material.VEX_SPAWN_EGG);
                break;
            case HUSK:
                egg = new ItemStack(Material.HUSK_SPAWN_EGG);
                break;
            case SLIME:
                egg = new ItemStack(Material.SLIME_SPAWN_EGG);
                break;
            case STRAY:
                egg = new ItemStack(Material.STRAY_SPAWN_EGG);
                break;
            case WITCH:
                egg = new ItemStack(Material.WITCH_SPAWN_EGG);
                break;
            case BLAZE:
                egg = new ItemStack(Material.BLAZE_SPAWN_EGG);
                break;
            case GHAST:
                egg = new ItemStack(Material.GHAST_SPAWN_EGG);
                break;
            case SPIDER:
                egg = new ItemStack(Material.SPIDER_SPAWN_EGG);
                break;
            case PIGLIN:
                egg = new ItemStack(Material.PIGLIN_SPAWN_EGG);
                break;
            case EVOKER:
                egg = new ItemStack(Material.EVOKER_SPAWN_EGG);
                break;
            case HOGLIN:
                egg = new ItemStack(Material.HOGLIN_SPAWN_EGG);
                break;
            case ZOGLIN:
                egg = new ItemStack(Material.ZOGLIN_SPAWN_EGG);
                break;
            case ZOMBIE:
                egg = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
                break;
            case CREEPER:
                egg = new ItemStack(Material.CREEPER_SPAWN_EGG);
                break;
            case DROWNED:
                egg = new ItemStack(Material.DROWNED_SPAWN_EGG);
                break;
            case PHANTOM:
                egg = new ItemStack(Material.PHANTOM_SPAWN_EGG);
                break;
            case RAVAGER:
                egg = new ItemStack(Material.RAVAGER_SPAWN_EGG);
                break;
            case SHULKER:
                egg = new ItemStack(Material.SHULKER_SPAWN_EGG);
                break;
            case GUARDIAN:
                egg = new ItemStack(Material.GUARDIAN_SPAWN_EGG);
                break;
            case PILLAGER:
                egg = new ItemStack(Material.PILLAGER_SPAWN_EGG);
                break;
            case SKELETON:
                egg = new ItemStack(Material.SKELETON_SPAWN_EGG);
                break;
            case ENDERMAN:
                egg = new ItemStack(Material.ENDERMAN_SPAWN_EGG);
                break;
            case ENDERMITE:
                egg = new ItemStack(Material.ENDERMITE_SPAWN_EGG);
                break;
            case MAGMA_CUBE:
                egg = new ItemStack(Material.MAGMA_CUBE_SPAWN_EGG);
                break;
            case SILVERFISH:
                egg = new ItemStack(Material.SILVERFISH_SPAWN_EGG);
                break;
            case VINDICATOR:
                egg = new ItemStack(Material.VINDICATOR_SPAWN_EGG);
                break;
            case CAVE_SPIDER:
                egg = new ItemStack(Material.CAVE_SPIDER_SPAWN_EGG);
                break;
        /*  case PIGLIN_BRUTE:
                egg = new ItemStack(Material.PIGLIN_BRUTE_SPAWN_EGG);
                break; */
            case ELDER_GUARDIAN:
                egg = new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG);
                break;
            case WITHER_SKELETON:
                egg = new ItemStack(Material.WITHER_SKELETON_SPAWN_EGG);
                break;
            case ZOMBIE_VILLAGER:
                egg = new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG);
                break;
            case ZOMBIFIED_PIGLIN:
                egg = new ItemStack(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG);
                break;
        }
        return egg;
    }

    public static ItemStack getWitherHead() {
        //<editor-fold defaultstate="" desc="CHANGING ITEM NAME">
        /*
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(NEW_NAME);
        item.setItemMeta(itemMeta);
        */
        //</editor-fold>
        //<editor-fold defaultstate="" desc="CHANGING ITEM LORE">
        /*
        ArrayList<String> witherHeadLore = new ArrayList<>();
        witherHeadLore.add(LINE_1);
        witherHeadLore.add(LINE_2);
        witherHeadLore.add(LINE_3);
        Objects.requireNonNull(item).setLore(itemLore);
        */
        //</editor-fold>

        ItemStack witherHead = new ItemStack(Material.WITHER_SKELETON_SKULL, 3);
        ItemMeta witherHeadMeta = witherHead.getItemMeta();
        ArrayList<String> witherHeadLore = new ArrayList<>();
        Objects.requireNonNull(witherHeadMeta).setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Crânio do Decaimento");
        witherHeadLore.add(ChatColor.DARK_GRAY + "A prova de que a lendária");
        witherHeadLore.add(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Ameaça do Decaimento");
        witherHeadLore.add(ChatColor.DARK_GRAY + "foi derrotada por um valoroso guerreiro.");
        Objects.requireNonNull(witherHeadMeta).setLore(witherHeadLore);
        witherHead.setItemMeta(witherHeadMeta);
        addUnplaceableTag(witherHead);
        addUndroppableTag(witherHead);
        return witherHead;
    }

    public static ItemStack getBalrogSkull() {
        //<editor-fold defaultstate="" desc="CHANGING ITEM NAME">
        /*
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(NEW_NAME);
        item.setItemMeta(itemMeta);
        */
        //</editor-fold>
        //<editor-fold defaultstate="" desc="CHANGING ITEM LORE">
        /*
        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add(LINE_1);
        itemLore.add(LINE_2);
        itemLore.add(LINE_3);
        item.getItemMeta().setLore(itemLore);
        */
        //</editor-fold>

        ItemStack balrogSkull = new ItemStack(Material.SKELETON_SKULL, 1);
        ItemMeta balrogSkullMeta = balrogSkull.getItemMeta();
        ArrayList<String> balrogSkullLore = new ArrayList<>();
        Objects.requireNonNull(balrogSkullMeta).setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Crânio de Balrog");
        balrogSkullLore.add(ChatColor.RED + "O Crânio de um");
        balrogSkullLore.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Balrog");
        balrogSkullLore.add(ChatColor.RED + "das profundezas do inferno.");
        Objects.requireNonNull(balrogSkullMeta).setLore(balrogSkullLore);
        balrogSkull.setItemMeta(balrogSkullMeta);
        addUnplaceableTag(balrogSkull);
        addUndroppableTag(balrogSkull);
        return balrogSkull;
    }
    //</editor-fold>

    //<editor-fold defaultstate="" desc="TIME">
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
    //</editor-fold>

    //<editor-fold defaultstate="" desc="ENTITY">
    public static Entity duplicate(Entity entity) {
        return spawnEntity(entity.getType(), entity.getWorld(), entity.getLocation());
    }

    public static Entity spawnEntity(EntityType entityType, World world, Location location) {
        return world.spawnEntity(location, entityType);
    }

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
    //</editor-fold>

    //<editor-fold defaultstate="" desc="CLASSES">
    //</editor-fold>

    //</editor-fold>

}
