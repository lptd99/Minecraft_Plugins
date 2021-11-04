package tyo_drak.draksrpgclasses;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import tyo_drak.drakslib.Checks;
import tyo_drak.drakslib.Players;
import tyo_drak.draksrpgclasses.classes.*;

public class Preventions {

    //<editor-fold defaultstate="collapsed" desc="EDITOR-FOLDING">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RPG">
    public static void warnNotEspadachimTotemHeld(EntityDamageEvent event) {
        Player player = (Player) event.getEntity();
        if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING) || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
            if (!player.hasPermission(Espadachim.BASE_PERMISSION) && !event.isCancelled()) {
                Players.denyPlayerAction(player, "NOT_ESPADACHIM_TOTEM_USE", ChatColor.DARK_RED + "Apenas " + Espadachim.CLASS_COLOR + "Espadachins" + ChatColor.DARK_RED + " podem usar Totens de Ressurreição!");
            }
        }
    }

    public static void preventNotEspadachimTotemUse(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player && !event.isCancelled()) {
            if (!event.getEntity().hasPermission(Espadachim.BASE_PERMISSION)) {
                event.setCancelled(true);
                Players.denyPlayerAction((Player) event.getEntity(), "NOT_ESPADACHIM_TOTEM_USE", ChatColor.DARK_RED + "Apenas " + Espadachim.CLASS_COLOR + "Espadachins" + ChatColor.DARK_RED + " podem usar Totens de Ressurreição!");
            }
        }
    }

    public static void preventNotDruidaMilkCow(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Cow) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BUCKET) || event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.BUCKET)) {
                if (!event.getPlayer().hasPermission(Druida.BASE_PERMISSION)) {
                    event.setCancelled(true);
                    Players.denyPlayerAction(event.getPlayer(), "COW_MILK_EXTRACT", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " sabem ordenhar vacas!");
                }
            }
        }
    }

    public static void preventNotDruidaFishBucket(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Fish) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET) || event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.WATER_BUCKET)) {
                if (!event.getPlayer().hasPermission(Druida.BASE_PERMISSION)) {
                    event.setCancelled(true);
                    Players.denyPlayerAction(event.getPlayer(), "FISH_TRY", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " sabem pescar!");
                }
            }
        }
    }

    public static void preventNotCacadorNotDruidaWolfTame(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Wolf) {
            Wolf wolf = (Wolf) event.getRightClicked();
            if (!wolf.isTamed() && (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BONE) || event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.BONE)) && !event.getPlayer().hasPermission(Cacador.BASE_PERMISSION) && !event.getPlayer().hasPermission(Druida.BASE_PERMISSION)) {
                event.setCancelled(true);
                Players.denyPlayerAction(event.getPlayer(), "WOLF_TAME", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " e " + Cacador.CLASS_COLOR + "Caçadores" + ChatColor.DARK_RED + " sabem domesticar Lobos!");
            }
        }
    }

    public static void preventNotDruidaLeashEntity(PlayerLeashEntityEvent event, Player player) {
        if (!player.hasPermission(Druida.BASE_PERMISSION)) {
            event.setCancelled(true);
            Players.denyPlayerAction(player, "NOT_DRUIDA_LEASH_ENTITY", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " sabem laçar animais!");
        }
    }

    public static void preventMilkDrink(PlayerItemConsumeEvent event, Player player, ItemStack item) {
        if (item.getType().equals(Material.MILK_BUCKET) && !event.isCancelled()) {
            Players.denyPlayerAction(player, "MILK_DRINK", ChatColor.DARK_RED + "Não é possível beber leite de vacas.");
            event.setCancelled(true);
        }
    }

    public static void preventNotDruidaShear(PlayerShearEntityEvent event, Player player) {
        if (!player.hasPermission(Druida.BASE_PERMISSION)) {
            event.setCancelled(true);
            Players.denyPlayerAction(event.getPlayer(), "NOT_DRUIDA_SHEAR", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " sabem cortar lã!");
        }
    }

    public static void preventCombatWithoutProperWeapon(EntityDamageByEntityEvent event, Entity entityDamager) {
        if (!event.isCancelled()) {
            if (entityDamager instanceof Player) {
                Player playerDamager = (Player) entityDamager;
                Material itemInHandType = playerDamager.getInventory().getItemInMainHand().getType();
                if (Main.config.getBoolean("RPG_CLASSES") && Main.config.getBoolean("PREVENT_UNARMED_COMBAT")) {
                    if (!Checks.isHoe(itemInHandType) &&
                            !Checks.isSword(itemInHandType) &&
                            !Checks.isAxe(itemInHandType) &&
                            !itemInHandType.equals(Material.AIR)) {
                        event.setCancelled(true);
                        Players.denyPlayerAction(playerDamager, "PREVENT_COMBAT_WITHOUT_PROPER_WEAPON", ChatColor.DARK_RED + "Use uma arma para bater!");
                    }
                    if (Checks.isHoe(itemInHandType)) {
                        if (!playerDamager.hasPermission(Gatuno.BASE_PERMISSION)) {
                            event.setCancelled(true);
                            Players.denyPlayerAction(playerDamager, "PREVENT_COMBAT_WITHOUT_PROPER_WEAPON", ChatColor.DARK_RED + "Apenas " + Gatuno.CLASS_COLOR + "Gatunos" + ChatColor.DARK_RED + " sabem usar isto!");
                        }
                    } else if (Checks.isAxe(itemInHandType)) {
                        if (!playerDamager.hasPermission(Ferreiro.BASE_PERMISSION)) {
                            event.setCancelled(true);
                            Players.denyPlayerAction(playerDamager, "PREVENT_COMBAT_WITHOUT_PROPER_WEAPON", ChatColor.DARK_RED + "Apenas " + Ferreiro.CLASS_COLOR + "Ferreiros" + ChatColor.DARK_RED + " sabem usar isto!");
                        }
                    } else if (Checks.isSword(itemInHandType) || itemInHandType.equals(Material.AIR)) {
                        if (!itemInHandType.equals(Material.WOODEN_SWORD)){
                            if (!playerDamager.hasPermission(Espadachim.BASE_PERMISSION)) {
                                if (itemInHandType.equals(Material.AIR)) {
                                    event.setCancelled(true);
                                    Players.denyPlayerAction(playerDamager, "PREVENT_COMBAT_WITHOUT_PROPER_WEAPON", ChatColor.DARK_RED + "Apenas " + Espadachim.CLASS_COLOR + "Espadachins" + ChatColor.DARK_RED + " podem lutar com as mãos!");
                                } else {
                                    event.setCancelled(true);
                                    Players.denyPlayerAction(playerDamager, "PREVENT_COMBAT_WITHOUT_PROPER_WEAPON", ChatColor.DARK_RED + "Apenas " + Espadachim.CLASS_COLOR + "Espadachins" + ChatColor.DARK_RED + " sabem usar isto!");
                                }
                            }
                        }
                    }
                } else if (Main.config.getBoolean("PREVENT_UNARMED_COMBAT") || Main.config.getBoolean("NIGHTMARE")) {
                    if (!(Checks.isSword(itemInHandType) || Checks.isAxe(itemInHandType))) {
                        event.setCancelled(true);
                        Players.denyPlayerAction(playerDamager, "PREVENT_COMBAT_WITHOUT_PROPER_WEAPON", ChatColor.DARK_RED + "Use uma arma para bater!");
                    }
                }

            }
        }
    }

    public static void preventDruidaMeatEating(PlayerItemConsumeEvent event, Player player, ItemStack item) {
        if (Checks.isMeat(item.getType()) && !event.isCancelled()) {
            if (player.hasPermission(Druida.BASE_PERMISSION)) {
                event.setCancelled(true);
                Players.denyPlayerAction(player, "DRUIDA_MEAT_EAT", Druida.CLASS_COLOR + "" + ChatColor.ITALIC + "Você sente que se alimentar de outras criaturas é desnecessário.");
            }
        }
    }

    public static void preventNotDruidaInteract(PlayerInteractEvent event) {
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (event.hasBlock()) {
                    //noinspection ConstantConditions
                    if (Checks.isForDruidasInteract(event.getClickedBlock().getType()) && !event.getPlayer().hasPermission(Druida.BASE_PERMISSION)) {
                        event.setCancelled(true);
                        Players.denyPlayerAction(event.getPlayer(), "IS_FOR_DRUIDAS_INTERACT", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " podem usar isto!");
                    }
                }
            }
        }
    }

    public static void preventNotDruidaPlow(PlayerInteractEvent event) {
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (event.hasBlock() && event.getClickedBlock() != null) {
                    Player player = event.getPlayer();
                    Block block = event.getClickedBlock();
                    ItemStack itemMain = player.getInventory().getItemInMainHand();
                    if (Checks.isHoe(itemMain.getType()) && !player.hasPermission(Druida.BASE_PERMISSION) && (block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS_BLOCK)) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        event.setCancelled(true);
                        Players.denyPlayerAction(event.getPlayer(), "NOT_DRUIDA_PLOW", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " podem arar a terra!");
                    }
                }
            }
        }
    }

    public static void preventNotFerreirosInteract(PlayerInteractEvent event) {
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
                    if (event.hasBlock()) {
                        //noinspection ConstantConditions
                        if (Checks.isForFerreirosInteract(event.getClickedBlock().getType()) && !event.getPlayer().hasPermission(Ferreiro.BASE_PERMISSION)) {
                            event.setCancelled(true);
                            Players.denyPlayerAction(event.getPlayer(), "IS_FOR_FERREIROS_INTERACT", ChatColor.DARK_RED + "Apenas " + Ferreiro.CLASS_COLOR + "Ferreiros" + ChatColor.DARK_RED + " podem usar isto!");
                        }
                    }
                }
            }
        }
    }

    public static void preventNotDruidaThrowPotion(PlayerInteractEvent event, Player player, ItemStack itemMain, ItemStack itemOff) {
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            if (itemMain.getType().equals(Material.SPLASH_POTION) || itemOff.getType().equals(Material.SPLASH_POTION)) {
                if (!player.hasPermission(Druida.BASE_PERMISSION)) {
                    Players.denyPlayerAction(player, "NOT_DRUIDA_THROW_POTION", ChatColor.DARK_RED+"Apenas "+Druida.CLASS_COLOR+"Druidas" +" podem arremessar poções.");
                }
            }
        }
    }
    //</editor-fold>

}
