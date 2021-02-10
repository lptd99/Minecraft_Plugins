package tyo_drak.draksrpgclasses;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityMountEvent;
import tyo_drak.draksrpgclasses.classes.*;
import tyo_drak.draksrpgclasses.misc.DraksItems;
import tyo_drak.draksrpgclasses.misc.DraksPlayers;

import java.util.*;

public class MainEvents implements Listener {

    public static final Map<String, Long> messagesCooldowns = new HashMap<>();
    public static final Map<String, Long> skillsCooldowns = new HashMap<>();
    public static final Map<String, Long> PLAYER_DEATH_TIME = new HashMap<>();
    public static final String PLAYER_DEATH_TAG = "'s DEATH: ";
    //<editor-fold defaultstate="collapsed" desc="MAGIC NUMBERS">
    public static final Random rand = new Random();
    //</editor-fold>

    //<editor-fold defaultstate="" desc="EDITOR FOLD EXAMPLE">
    //</editor-fold>

    // MISC

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

    // EVENTS

    //<editor-fold defaultstate="collapsed" desc="PLAYER EVENTS FUNCTIONS">
    public void animalSacrifice(Player playerInteracting, Entity entity, Animals animal) {
        if (Checks.isSacrificeable(animal) && playerInteracting.hasPermission(Druida.BASE_PERMISSION) && playerInteracting.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            Druida.sacrificeAnimal(playerInteracting, entity, animal);
        }
    }

    public void playerInteractEventRefreshClassEffectsAndAttributes(PlayerInteractEvent event, Player player) {
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            DraksPlayers.resetClassEffects(player);
            DraksPlayers.setClassAttributes(player);
        }
    }

    public void givePlayerEXPFromPlayerEXPBottle(PlayerInteractEvent event, Player player, ItemStack itemMain) {
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (itemMain.getType().equals(Material.EXPERIENCE_BOTTLE) && itemMain.getItemMeta() != null && itemMain.getItemMeta().getLore() != null && itemMain.getItemMeta().getLore().get(0).contains(" pontos de experiência ao ser consumida.")) {
                    event.setCancelled(true);
                    int bottleEXP = Integer.parseInt(itemMain.getItemMeta().getLore().get(0).replace("Concede ", "").replace(" pontos de experiência ao ser consumida.", ""));
                    ItemStack emptyBottle = new ItemStack(Material.GLASS_BOTTLE, 1);
                    itemMain.setAmount(itemMain.getAmount() - 1);
                    player.getInventory().addItem(emptyBottle);
                    player.giveExp(bottleEXP);
                }
            }
        }
    }

    public void getPlayerEXPBottleFromGlassBottle(PlayerInteractEvent event, Player player, ItemStack itemMain) {
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (itemMain.getType().equals(Material.GLASS_BOTTLE) && (DraksPlayers.getPlayerExp(player) > 0 || player.getLevel() > 0)) {
                    int playerEXP = DraksPlayers.getPlayerExp(player);
                    ItemStack playerEXPBottle = DraksItems.getPlayerEXPBottle(player.getLevel(), playerEXP);
                    itemMain.setAmount(itemMain.getAmount() - 1);
                    player.getInventory().addItem(playerEXPBottle);
                    player.setExp(0);
                    player.setLevel(0);
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BLOCK EVENTS FUNCTIONS">
    public void forDruidasHarvest(BlockBreakEvent event, Player player, Block block, Material blockType, World world) {
        if (Checks.isForDruidasHarvest(blockType)) {
            // CROPS
            Druida.druida3x3Harvest(event, player, block, world);
            if (!player.hasPermission(Druida.BASE_PERMISSION)) {
                event.setCancelled(true);
                DraksPlayers.denyPlayerAction(player, "IS_FOR_DRUIDAS_HARVEST", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " sabem colher!");
            }
        }
    }

    public void forDruidas(BlockBreakEvent event, Player player, Material blockType) {
        if (Checks.isForDruidasBreak(blockType) && !player.hasPermission(Druida.BASE_PERMISSION)) {
            event.setCancelled(true);
            DraksPlayers.denyPlayerAction(player, "IS_FOR_DRUIDAS_BREAK", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " podem quebrar isto!");
        }
    }

    public void forFerreiros(BlockBreakEvent event, Player player, Material blockType) {
        if (Checks.isForFerreirosBreak(blockType) && !player.hasPermission(Ferreiro.BASE_PERMISSION)) {
            event.setCancelled(true);
            DraksPlayers.denyPlayerAction(player, "IS_FOR_FERREIROS_BREAK", ChatColor.DARK_RED + "Apenas " + Ferreiro.classColor + "Ferreiros" + ChatColor.DARK_RED + " podem quebrar isto!");
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
            block.getWorld().dropItem(block.getLocation(), DraksItems.getSpawnerItemStack(spawnerBlock));
            event.setExpToDrop(0);
        }
    }

    public void preventNotEspadachimBreakSpawner(BlockBreakEvent event, Player player) {
        event.setCancelled(true);
        DraksPlayers.denyPlayerAction(player, "NOT_ESPADACHIM_BREAK_SPAWNER", ChatColor.DARK_RED + "Apenas " + Espadachim.CLASS_COLOR + "Espadachins" + ChatColor.DARK_RED + " sabem desativar Spawners!");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MISC EVENTS FUNCTIONS">
    public void forceSpawnerRecipeUseCustomItems(PrepareItemCraftEvent event, List<ItemStack> matrix, ShapedRecipe shapedRecipe) {
        if (shapedRecipe.getKey().getKey().equals("spawner")) {
            if (!DraksItems.compareLore(matrix.get(0), DraksItems.getSpiritualIronBars()) ||
                    !DraksItems.compareLore(matrix.get(1), DraksItems.getPlayerRangeCrystal()) ||
                    !DraksItems.compareLore(matrix.get(2), DraksItems.getSpiritualIronBars()) ||
                    !DraksItems.compareLore(matrix.get(3), DraksItems.getMinDelayCrystal()) ||
                    !DraksItems.compareLore(matrix.get(4), DraksItems.getSparkingCondensedSoulSand()) ||
                    !DraksItems.compareLore(matrix.get(5), DraksItems.getMaxDelayCrystal()) ||
                    !DraksItems.compareLore(matrix.get(6), DraksItems.getSpiritualIronBars()) ||
                    !DraksItems.compareLore(matrix.get(7), DraksItems.getCondensedEmeraldGem()) ||
                    !DraksItems.compareLore(matrix.get(8), DraksItems.getSpiritualIronBars())) {
                event.getInventory().setResult(null);
            }
        }
    }

    public void preventNewPlayerPortals(PortalCreateEvent event) {
        if (event.getReason().equals(PortalCreateEvent.CreateReason.FIRE) || event.getReason().equals(PortalCreateEvent.CreateReason.NETHER_PAIR)) {
            event.setCancelled(true);
        }
    }

    public void preventEmptySpawnersSpawn(SpawnerSpawnEvent event) {
        if (!event.isCancelled()) {
            event.setCancelled(event.getSpawner().getSpawnedType().equals(EntityType.ARMOR_STAND));
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
        DraksPlayers.resetClassEffects(player);
    }

    //@EventHandler
    //public void playerMoveEvent(PlayerMoveEvent event) {
        // USEFUL VARIABLES
        //Player player = event.getPlayer();

        // ACTIONS
        //DraksPlayers.forceSpectatorToDeadPlayers(player);
    //}

    @EventHandler
    public void playerShearEntityEvent(PlayerShearEntityEvent event) {
        // USEFUL VARIABLES
        Player player = event.getPlayer();

        // ACTIONS
        Preventions.preventNotDruidaShear(event, player);
    }

    @EventHandler
    public void playerPickupArrowEvent(PlayerPickupArrowEvent event) {
        if (event.getArrow().getCustomName() != null) {
            event.setCancelled(event.getArrow().getCustomName().equals(Cacador.CACADOR_SILVER_ARROW_NAME));
        }
    }

    @EventHandler
    public void playerItemConsumeEvent(PlayerItemConsumeEvent event) {
        // USEFUL VARIABLES
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // ACTIONS
        Preventions.preventMilkDrink(event, player, item);
        Preventions.preventDruidaMeatEating(event, player, item);
    }

    @EventHandler
    public void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player playerInteracting = event.getPlayer();
        Entity entity = event.getRightClicked();

        Druida.druidaBlessing(playerInteracting, entity);

        Preventions.preventNotDruidaMilkCow(event);
        Preventions.preventNotDruidaFishBucket(event);
        Preventions.preventNotCacadorNotDruidaWolfTame(event);

        if (entity instanceof Animals) {
            Animals animal = (Animals) entity;

            animalSacrifice(playerInteracting, entity, animal);
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        // Variables
        Player player = event.getPlayer();
        ItemStack itemMain = event.getPlayer().getInventory().getItemInMainHand();
        ItemStack itemOff = event.getPlayer().getInventory().getItemInOffHand();

        Preventions.preventNotFerreirosInteract(event);
        Preventions.preventNotDruidaInteract(event);

        Preventions.preventNotDruidaPlow(event);

        Preventions.preventNotDruidaThrowPotion(event, player, itemMain, itemOff);

        Druida.preventFarmlandTrample(event, player);
        Gatuno.nightVeil(event, player, itemMain.getType(), itemOff.getType());


        getPlayerEXPBottleFromGlassBottle(event, player, itemMain);
        givePlayerEXPFromPlayerEXPBottle(event, player, itemMain);

        Espadachim.editSpawner(event, player, itemMain);

        playerInteractEventRefreshClassEffectsAndAttributes(event, player);

    }

    @EventHandler
    public void expBottleEvent(ExpBottleEvent event) {
        ItemStack thrownItem = event.getEntity().getItem();
        if (thrownItem.getItemMeta() != null && thrownItem.getItemMeta().getLore() != null) {
            int bottleEXP = Integer.parseInt(thrownItem.getItemMeta().getLore().get(0).replace("Concede ", "").replace(" pontos de experiência ao ser consumida.", ""));
            event.setExperience(bottleEXP);
        }
    }

    @EventHandler
    public void playerLeashEntityEvent(PlayerLeashEntityEvent event) {
        Player player = event.getPlayer();
        Preventions.preventNotDruidaLeashEntity(event, player);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ENTITY">
    @EventHandler
    public void entityResurrectEvent(EntityResurrectEvent event) {
        Preventions.preventNotEspadachimTotemUse(event);
    }

    @EventHandler
    public void entityBreedEvent(EntityBreedEvent event) {
        Druida.natureFertility(event, random(0, 6));
    }

    @EventHandler
    public void entityMountEvent(EntityMountEvent event) {
        Espadachim.mountHorse(event);
        Ferreiro.mountChestedHorse(event);
        Druida.mountStrider(event);
    }

    @EventHandler
    public void entityPotionEffectEvent(EntityPotionEffectEvent event) {
        classImmunities(event);
    }

    @EventHandler
    public void entityTargetEvent(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        Druida.greenBlood(event);
        Cacador.wolfTarget(event, entity);
        preventTargetInvisible(event);
    }

    @EventHandler
    public void entityShootBowEvent(EntityShootBowEvent event) {
        Cacador.shootBow(event);
        Cacador.fatQuiver(event);
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        Druida.preventPlantDamage(event, entity);
        if (entity instanceof Player) {
            Preventions.warnNotEspadachimTotemHeld(event);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entityDamager = event.getDamager();
        Entity entityDamagee = event.getEntity();

        Druida.preventDruidaHurtAnimals(event);
        Preventions.preventCombatWithoutProperWeapon(event, entityDamager);

        Gatuno.gatunoPoison(event);
        Gatuno.endNightVeilBonusDamage(event);
        Gatuno.backstab(event);

        if (entityDamager instanceof Projectile && !event.isCancelled()) {
            Projectile projectile = (Projectile) entityDamager;
            if (projectile.getShooter() instanceof Player) {
                Player playerShooter = (Player) projectile.getShooter();
                ItemStack itemMain = playerShooter.getInventory().getItemInMainHand();
                Cacador.arrowHitProficiency(event, entityDamagee, itemMain);
            }
        }
        Espadachim.combatExperience(event);
        Espadachim.espadachimResolve(event);
        arrowCleanup(event);
        Espadachim.espadachimGuardianAngel(event);
    }

    //</editor-fold>

    public static void classImmunities(EntityPotionEffectEvent event) {
        Druida.applyImmunities(event);
        Ferreiro.applyImmunities(event);
        Gatuno.applyImmunities(event);
        Espadachim.applyImmunities(event);
        Cacador.applyImmunities(event);
    }

    public static void preventTargetInvisible(EntityTargetLivingEntityEvent event) {
        if (!event.isCancelled()) {
            if (event.getTarget() instanceof Player) {
                Player player = (Player) event.getTarget();
                event.setCancelled(player.hasPotionEffect(PotionEffectType.INVISIBILITY));
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="BLOCK">
    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent event) {
        preventUnplaceableItemPlace(event);
        Player player = event.getPlayer();
        Block blockPlaced = event.getBlockPlaced();
        ItemStack itemMain = player.getInventory().getItemInMainHand();
        ItemStack itemOff = player.getInventory().getItemInOffHand();
        Espadachim.placeSpawner(player, blockPlaced, itemMain, itemOff);
        if (event.getBlockPlaced() instanceof ItemFrame)
            if (Checks.isForFerreirosPlace(blockPlaced.getType()) && !player.hasPermission(Ferreiro.BASE_PERMISSION)) {
                event.setCancelled(true);
                DraksPlayers.denyPlayerAction(player, "IS_FOR_FERREIROS_PLACE", ChatColor.DARK_RED + "Apenas " + Ferreiro.classColor + "Ferreiros" + ChatColor.DARK_RED + " podem posicionar isto!");
            } else if (Checks.isForDruidasPlace(blockPlaced.getType()) && !player.hasPermission(Druida.BASE_PERMISSION)) {
                event.setCancelled(true);
                DraksPlayers.denyPlayerAction(player, "IS_FOR_DRUIDAS_PLACE", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " podem posicionar isto!");
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
                DraksPlayers.denyPlayerAction(player, "IS_FOR_DRUIDAS_PLANT", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " sabem plantar!");
            }
    }

    @EventHandler
    public void blockFertilizeEvent(BlockFertilizeEvent event) {
        if (event.getPlayer() != null && event.getPlayer().hasPermission(Druida.BASE_PERMISSION) && event.getBlock() instanceof org.bukkit.block.data.Ageable) {
            org.bukkit.block.data.Ageable ageableBlock = (org.bukkit.block.data.Ageable) event.getBlock().getBlockData();
            if (ageableBlock.getAge() < ageableBlock.getMaximumAge() / 2) {
                ageableBlock.setAge(ageableBlock.getMaximumAge() / 2);
            } else if (ageableBlock.getAge() >= ageableBlock.getMaximumAge() / 2) {
                ageableBlock.setAge(ageableBlock.getMaximumAge());
            }
            event.getBlock().setBlockData(ageableBlock);
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = event.getBlock().getType();
        World world = block.getWorld();

        spawnerBreak(event, player, block, blockType);
        forFerreiros(event, player, blockType);
        forDruidas(event, player, blockType);
        forDruidasHarvest(event, player, block, blockType, world);
    }

    @EventHandler
    public void blockDamageEvent(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = event.getBlock().getType();
        Druida.instaHarvest(event, player, block, blockType);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MISC EVENTS">
    @EventHandler
    public void portalCreateEvent(PortalCreateEvent event) {
        preventNewPlayerPortals(event);
    }

    @EventHandler
    public void explosionPrimeEvent(ExplosionPrimeEvent event) {
        if (event.getEntityType().equals(EntityType.PRIMED_TNT) || event.getEntityType().equals(EntityType.MINECART_TNT)) {
            event.setRadius(8);
        }
    }

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

    @EventHandler
    public void prepareItemCraftEvent(PrepareItemCraftEvent event) {
        Debug.consoleMessage("prepareItemCraftEvent");
        List<ItemStack> matrix = new ArrayList<>(Arrays.asList(event.getInventory().getMatrix()));
        if (event.getRecipe() instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) event.getRecipe();

            forceSpawnerRecipeUseCustomItems(event, matrix, shapedRecipe);
        }
        /*
        if (event.getRecipe() == null) {
            Debug.consoleMessage("event.getRecipe() == null");
            ItemStack firstItem = matrix.get(0);
            int itemsIdenticalToFirstItem = 0;
            for (ItemStack item :
                    matrix) {
                Debug.consoleMessage("ItemStack item : matrix");
                if (item.equals(firstItem)) {
                    itemsIdenticalToFirstItem++;
                    Debug.consoleMessage("item.equals(firstItem)");
                    Debug.consoleMessage("itemsIdenticalToFirstItem: " + itemsIdenticalToFirstItem);
                }
            }
            if (itemsIdenticalToFirstItem == 8 && matrix.get(4).equals(new ItemStack(Material.CHEST, 1))) {
                Debug.consoleMessage("itemsIdenticalToFirstItem == 8 && matrix.get(4).equals(new ItemStack(Material.CHEST, 1))");
                event.getInventory().setResult(DraksItems.getSimpleItemSack(firstItem));
            }
        }*/
    }

    @EventHandler
    public void prepareItemEnchantEvent(PrepareItemEnchantEvent event) {

    }

    @EventHandler
    public void spawnerSpawnEvent(SpawnerSpawnEvent event) {
        preventEmptySpawnersSpawn(event);
    }
    //</editor-fold>

    //<editor-fold defaultstate="" desc="GENERAL FUNCTIONS">
    public static int random(int min, int max) {
        return (rand.nextInt((max - min) + 1) + min);
    }

    //<editor-fold defaultstate="" desc="EVENTS">
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
    //</editor-fold>

    //</editor-fold>

    public static void preventUnplaceableItemPlace(BlockPlaceEvent event) {
        if (DraksItems.isUnplaceable(event.getItemInHand())) {
            try {
                DraksPlayers.denyPlayerAction(event.getPlayer(), "UNPLACEABLE_PLACE", "Você não pode posicionar este item.");
                event.setCancelled(true);
            } catch (Exception ignored) {
            }
        }
    }

    public static void preventUndroppableItemDrop(PlayerDropItemEvent event) {
        if (DraksItems.isUndroppable(event.getItemDrop().getItemStack())) {
            try {
                DraksPlayers.denyPlayerAction(event.getPlayer(), "UNDROPPABLE_DROP", "Você não pode derrubar este item.");
                event.setCancelled(true);
            } catch (Exception ignored) {
            }
        }
    }
}