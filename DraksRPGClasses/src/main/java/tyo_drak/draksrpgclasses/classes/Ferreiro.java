package tyo_drak.draksrpgclasses.classes;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityMountEvent;
import tyo_drak.draksrpgclasses.Checks;
import tyo_drak.draksrpgclasses.Main;
import tyo_drak.draksrpgclasses.MainEvents;
import tyo_drak.draksrpgclasses.misc.DraksEntities;
import tyo_drak.draksrpgclasses.misc.DraksItems;
import tyo_drak.draksrpgclasses.misc.DraksPlayers;

public class Ferreiro {

    // CHARACTERISTICS
    public static final Enum<ChatColor> classColor = ChatColor.RED;
    public static final Integer BASE_HEALTH = 16;
    public static final PotionEffect[] CLASS_EFFECTS = {new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 0),
            new PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 1)};
    public static final PotionEffectType[] CLASS_IMMUNITIES = {PotionEffectType.SLOW_DIGGING};

    // PERMISSIONS
    public static final String BASE_PERMISSION = "group.ferreiro";
    public static final String MASTERY_PERMISSION = "group.mestre_ferreiro";

    public static int FERREIRO_MOUNT_CHESTED_HORSE_MIN_LEVEL = 15;
    public static void mountChestedHorse(EntityMountEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getMount() instanceof ChestedHorse) {
                ChestedHorse chestedHorse = (ChestedHorse) event.getMount();
                if (player.hasPermission(Ferreiro.BASE_PERMISSION)) {
                    if (player.getLevel() >= FERREIRO_MOUNT_CHESTED_HORSE_MIN_LEVEL) {
                        chestedHorse.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.8);
                        DraksEntities.setBaseMaxHealth(chestedHorse, 40);
                        chestedHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15);
                        chestedHorse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(0.5);
                    } else {
                        event.setCancelled(true);
                        DraksPlayers.denyPlayerAction(player, "NOT_ENOUGH_LEVEL", ChatColor.DARK_RED+"Você não tem nível suficiente para montar burros de carga!");
                    }
                } else {
                    event.setCancelled(true);
                    DraksPlayers.denyPlayerAction(player, "NOT_FERREIRO_CHESTED_HORSE_MOUNT", ChatColor.DARK_RED + "Apenas " + Ferreiro.classColor + "Ferreiros" + ChatColor.DARK_RED + " podem montar Animais de Carga!");
                }
            }
        }
    }

    public static void proficientCrafting(CraftItemEvent event) {
        if (!event.isCancelled()) {
            ItemStack item = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            if (item != null) {
                if ((Checks.IsActualItem(event.getWhoClicked().getItemOnCursor()) || event.getClick().isShiftClick() || event.getHotbarButton() != -1) && item.getMaxStackSize() == 1) {
                    event.setCancelled(true);
                    DraksPlayers.denyPlayerAction(player, "PREVENT_CRAFT_ITEM_BUG", ChatColor.DARK_RED + "Você deve construir um item de cada vez, seja colocando-o no seu inventário ou derrubando-o.");
                } else {

                    int itemQuality = 0;
                    if (player.hasPermission(BASE_PERMISSION)) {

                        //<editor-fold defaultstate="collapsed" desc="DICES">
                        int d1 = MainEvents.random(0, 1);
                        int d01 = MainEvents.random(0, 1);
                        int d2 = MainEvents.random(0, 2);
                        int d02 = MainEvents.random(0, 2);
                        int d3 = MainEvents.random(0, 3);
                        int d03 = MainEvents.random(0, 3);
                        int d04 = MainEvents.random(0, 4);
                        int d4 = MainEvents.random(0, 4);
                        int d5 = MainEvents.random(0, 5);
                        int d05 = MainEvents.random(0, 5);
                        int d06 = MainEvents.random(0, 6);
                        int d6 = MainEvents.random(0, 6);
                        int d7 = MainEvents.random(0, 7);
                        int d07 = MainEvents.random(0, 7);
                        int d08 = MainEvents.random(0, 8);
                        int d8 = MainEvents.random(0, 8);
                        int d9 = MainEvents.random(0, 9);
                        int d09 = MainEvents.random(0, 9);
                        int d10 = MainEvents.random(0, 10);
                        int d010 = MainEvents.random(0, 10);
                        int d11 = MainEvents.random(0, 11);
                        int d011 = MainEvents.random(0, 11);
                        int d12 = MainEvents.random(0, 12);
                        int d012 = MainEvents.random(0, 12);
                        int d13 = MainEvents.random(0, 13);
                        int d013 = MainEvents.random(0, 13);
                        int d14 = MainEvents.random(0, 14);
                        int d014 = MainEvents.random(0, 14);
                        int d15 = MainEvents.random(0, 15);
                        int d015 = MainEvents.random(0, 15);
                        int d16 = MainEvents.random(0, 16);
                        int d016 = MainEvents.random(0, 16);
                        int d17 = MainEvents.random(0, 17);
                        int d017 = MainEvents.random(0, 17);
                        int d18 = MainEvents.random(0, 18);
                        int d018 = MainEvents.random(0, 18);
                        int d19 = MainEvents.random(0, 19);
                        int d019 = MainEvents.random(0, 19);
                        int d20 = MainEvents.random(0, 20);
                        int d020 = MainEvents.random(0, 20);
                        int d100 = MainEvents.random(1, 100);
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="WEAPONS">
                        // CROSSBOW
                        if (d100 < 40) {
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.QUICK_CHARGE, d8 / 4 + 1);
                        } else if (d100 < 70) {
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.DURABILITY, d12 / 6 + 1);
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.QUICK_CHARGE, d6 / 3 + 1);
                        } else if (d100 < 90) {
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.DURABILITY, d4 / 2 + 1);
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.QUICK_CHARGE, d4 / 2 + 1);
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.MULTISHOT, d10 / 10 + 1);
                        } else {
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.QUICK_CHARGE, d2 + 1);
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.DURABILITY, d2 + 1);
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CROSSBOW, Enchantment.PIERCING, d6 / 3 + 1);  // 9
                        }

                        // SHIELD
                        if (d100 < 50) {
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.SHIELD, Enchantment.DURABILITY, d12 / 6 + 1);
                        } else if (d100 < 85) {
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.SHIELD, Enchantment.DURABILITY, d4 / 2 + 1);
                        } else {
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.SHIELD, Enchantment.DURABILITY, d2 + 1);
                            itemQuality += DraksItems.addEnchantmentsIfType(item, Material.SHIELD, Enchantment.MENDING, d8 / 4 + 1);  // 6
                        }
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="TOOLS">
                        //<editor-fold defaultstate="collapsed" desc="STONE">
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.STONE_SWORD, Enchantment.KNOCKBACK, d1 + 1);     // 2
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.STONE_PICKAXE, Enchantment.DURABILITY, d1 + 1);  // 2
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.STONE_SHOVEL, Enchantment.DIG_SPEED, d1 + 1);    // 2
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.STONE_AXE, Enchantment.DAMAGE_ALL, d1 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.STONE_AXE, Enchantment.DIG_SPEED, d1 + 1);       // 4
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.STONE_HOE, Enchantment.DURABILITY, d1 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.STONE_HOE, Enchantment.DAMAGE_ALL, d2 + 1);      // 5
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="IRON">
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_SWORD, Enchantment.DAMAGE_ALL, d2 + 1);     // 3
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_PICKAXE, Enchantment.DIG_SPEED, d2 + 1);    // 3
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_SHOVEL, Enchantment.DIG_SPEED, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_SHOVEL, Enchantment.DURABILITY, d1 + 1);    // 5
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_AXE, Enchantment.DIG_SPEED, d2 + 1);        // 3
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_HOE, Enchantment.DAMAGE_ALL, d5 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_HOE, Enchantment.LOOT_BONUS_MOBS, d1 + 1);  // 8
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="GOLDEN">                                                      // 14
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_SWORD, Enchantment.MENDING, d10 / 10);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_SWORD, Enchantment.KNOCKBACK, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_SWORD, Enchantment.FIRE_ASPECT, d02 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_SWORD, Enchantment.DAMAGE_UNDEAD, d6 + 1);

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_PICKAXE, Enchantment.DIG_SPEED, d6 + 2);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_PICKAXE, Enchantment.LOOT_BONUS_BLOCKS, d10 / 10);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_PICKAXE, Enchantment.MENDING, d010 / 10 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_PICKAXE, Enchantment.DURABILITY, d4 / 2 + 1);

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_SHOVEL, Enchantment.DIG_SPEED, d5 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_SHOVEL, Enchantment.SILK_TOUCH, d10 / 10 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_SHOVEL, Enchantment.MENDING, d20 / 10 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_PICKAXE, Enchantment.DURABILITY, d4 / 2 + 1);

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_AXE, Enchantment.DAMAGE_UNDEAD, d6 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_AXE, Enchantment.FIRE_ASPECT, d3);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_AXE, Enchantment.KNOCKBACK, d03);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_AXE, Enchantment.MENDING, d10 / 10);

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_HOE, Enchantment.DAMAGE_UNDEAD, d6);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_HOE, Enchantment.KNOCKBACK, d1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_HOE, Enchantment.FIRE_ASPECT, d1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_HOE, Enchantment.MENDING, d10 / 10);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_HOE, Enchantment.LOOT_BONUS_MOBS, d10 / 2);
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="DIAMOND">
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, d3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_SWORD, Enchantment.KNOCKBACK, d1 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_SWORD, Enchantment.DURABILITY, d2 + 1);  // 9

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_PICKAXE, Enchantment.DIG_SPEED, d3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_PICKAXE, Enchantment.DURABILITY, d2 + 1);// 7

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_SHOVEL, Enchantment.DIG_SPEED, d3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_SHOVEL, Enchantment.DURABILITY, d3 + 1); // 8

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_AXE, Enchantment.DIG_SPEED, d3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_AXE, Enchantment.DURABILITY, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_AXE, Enchantment.DAMAGE_ALL, d02 + 1);   // 10

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_HOE, Enchantment.DAMAGE_ALL, d10 + 1);   // 14
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_HOE, Enchantment.LOOT_BONUS_MOBS, d6 / 2);
                        //</editor-fold>
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="ARMOR">

                        //<editor-fold defaultstate="collapsed" desc="SETUP ARMOR ENCHANTMENTS">
                        Enchantment helmetEnchantment = null;
                        Enchantment helmetEnchantment2 = null;
                        Enchantment genericEnchantment = null;
                        Enchantment genericEnchantment2 = null;
                        Enchantment protectionEnchantment = null;
                        Enchantment protectionEnchantment2 = null;
                        Enchantment bootsEnchantment = null;
                        Enchantment bootsEnchantment2 = null;

                        switch (MainEvents.random(1, 2)) { // HELMET ENCHANTMENTS
                            case 1:
                                helmetEnchantment = Enchantment.WATER_WORKER;
                                helmetEnchantment2 = Enchantment.OXYGEN;
                                break;
                            case 2:
                                helmetEnchantment = Enchantment.OXYGEN;
                                helmetEnchantment2 = Enchantment.WATER_WORKER;
                                break;
                        } // HELMET ENCHANTMENTS

                        switch (MainEvents.random(1, 2)) { // GENERIC ENCHANTMENTS
                            case 1:
                                genericEnchantment = Enchantment.THORNS;
                                genericEnchantment2 = Enchantment.DURABILITY;
                                break;
                            case 2:
                                genericEnchantment = Enchantment.DURABILITY;
                                genericEnchantment2 = Enchantment.THORNS;
                                break;
                        } // GENERIC ENCHANTMENTS

                        switch (MainEvents.random(1, 4)) { // PROTECTION ENCHANTMENTS
                            case 1:
                                protectionEnchantment = Enchantment.PROTECTION_ENVIRONMENTAL;
                                break;
                            case 2:
                                protectionEnchantment = Enchantment.PROTECTION_EXPLOSIONS;
                                break;
                            case 3:
                                protectionEnchantment = Enchantment.PROTECTION_FIRE;
                                break;
                            case 4:
                                protectionEnchantment = Enchantment.PROTECTION_PROJECTILE;
                                break;
                        } // PROTECTION ENCHANTMENTS
                        protectionEnchantment2 = protectionEnchantment;
                        while (protectionEnchantment2 == protectionEnchantment) {
                            switch (MainEvents.random(1, 4)) { // PROTECTION ENCHANTMENTS
                                case 1:
                                    protectionEnchantment2 = Enchantment.PROTECTION_ENVIRONMENTAL;
                                    break;
                                case 2:
                                    protectionEnchantment2 = Enchantment.PROTECTION_EXPLOSIONS;
                                    break;
                                case 3:
                                    protectionEnchantment2 = Enchantment.PROTECTION_FIRE;
                                    break;
                                case 4:
                                    protectionEnchantment2 = Enchantment.PROTECTION_PROJECTILE;
                                    break;
                            } // PROTECTION ENCHANTMENTS
                        } // PROTECTION ENCHANTMENTS

                        switch (MainEvents.random(1, 4)) { // BOOT ENCHANTMENTS
                            case 1:
                                bootsEnchantment = Enchantment.SOUL_SPEED;
                                break;
                            case 2:
                                bootsEnchantment = Enchantment.FROST_WALKER;
                                break;
                            case 3:
                                bootsEnchantment = Enchantment.DEPTH_STRIDER;
                                break;
                            case 4:
                                bootsEnchantment = Enchantment.PROTECTION_FALL;
                                break;
                        } // BOOT ENCHANTMENTS
                        bootsEnchantment2 = bootsEnchantment;
                        while (bootsEnchantment2 == bootsEnchantment || (bootsEnchantment2 == Enchantment.FROST_WALKER && bootsEnchantment == Enchantment.DEPTH_STRIDER) || (bootsEnchantment == Enchantment.FROST_WALKER && bootsEnchantment2 == Enchantment.DEPTH_STRIDER)) {
                            switch (MainEvents.random(1, 4)) { // BOOT ENCHANTMENTS
                                case 1:
                                    bootsEnchantment2 = Enchantment.SOUL_SPEED;
                                    break;
                                case 2:
                                    bootsEnchantment2 = Enchantment.FROST_WALKER;
                                    break;
                                case 3:
                                    bootsEnchantment2 = Enchantment.DEPTH_STRIDER;
                                    break;
                                case 4:
                                    bootsEnchantment2 = Enchantment.PROTECTION_FALL;
                                    break;
                            } // BOOT ENCHANTMENTS
                        } // BOOT ENCHANTMENTS
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="CHAINMAIL">
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_HELMET, protectionEnchantment, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_HELMET, helmetEnchantment, d1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_HELMET, helmetEnchantment, d01);     // 5

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_CHESTPLATE, protectionEnchantment, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_CHESTPLATE, protectionEnchantment2, d2 + 1);// 6

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_LEGGINGS, protectionEnchantment, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_LEGGINGS, protectionEnchantment2, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_LEGGINGS, bootsEnchantment, d3 / 3);// 7

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_BOOTS, protectionEnchantment, d2);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.CHAINMAIL_BOOTS, bootsEnchantment, d8 / 2 + 1);       // 7
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="IRON">
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_HELMET, protectionEnchantment, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_HELMET, protectionEnchantment2, d1 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_HELMET, helmetEnchantment, d8 / 3);          // 7

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_CHESTPLATE, protectionEnchantment, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_CHESTPLATE, protectionEnchantment, d1 + 2);    // 6

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_LEGGINGS, protectionEnchantment, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_LEGGINGS, genericEnchantment, d4 / 2 + 1);       // 6

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_BOOTS, protectionEnchantment, d2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_BOOTS, protectionEnchantment, d02 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.IRON_BOOTS, bootsEnchantment, d4 / 2);            // 8
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="GOLDEN">
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_HELMET, helmetEnchantment, d20 / 20);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_HELMET, Enchantment.DURABILITY, d14 / 2);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_HELMET, Enchantment.MENDING, d10 / 2 + 1);   // 14

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_CHESTPLATE, protectionEnchantment, d20 / 4 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_CHESTPLATE, Enchantment.MENDING, d20 / 5 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_CHESTPLATE, Enchantment.DURABILITY, d10 / 10 + 2); // 14

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_LEGGINGS, bootsEnchantment, d9 / 3 + 2);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_LEGGINGS, protectionEnchantment, d20 / 10);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_LEGGINGS, Enchantment.MENDING, d15 / 3 + 2);   // 14

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_BOOTS, bootsEnchantment, d10 / 3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_BOOTS, bootsEnchantment2, d9 / 3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_BOOTS, Enchantment.DURABILITY, d15 / 5);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.GOLDEN_BOOTS, Enchantment.MENDING, d10 / 5 + 1);     // 14
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="DIAMOND">
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_HELMET, protectionEnchantment, d6 / 3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_HELMET, protectionEnchantment2, d06 / 3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_HELMET, genericEnchantment, d15 / 3);            // 11

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_CHESTPLATE, protectionEnchantment, d6 / 3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_CHESTPLATE, protectionEnchantment2, d06 / 3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_CHESTPLATE, genericEnchantment, d12 / 6 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_CHESTPLATE, genericEnchantment2, d012 / 4);      // 12

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_LEGGINGS, protectionEnchantment, d7 / 2);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_LEGGINGS, protectionEnchantment2, d8 / 3 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_LEGGINGS, genericEnchantment, d8 / 4 + 2);       // 10

                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_BOOTS, protectionEnchantment, d6 / 2 + 1);
                        itemQuality += DraksItems.addEnchantmentsIfType(item, Material.DIAMOND_BOOTS, genericEnchantment, d4 / 2 + 1);          // 7
                        //</editor-fold>
                        //</editor-fold>

                        if (DraksItems.determineMaxQuality(item) >= 0) {
                            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, MainEvents.random(1, 3) + 1);
                            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, MainEvents.random(1, 3) + 1);
                            ExperienceOrb experienceOrb = (ExperienceOrb) event.getWhoClicked().getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
                            experienceOrb.setExperience(itemQuality * 4);
                        }

                    }

                    if (DraksItems.applyRarity(item, itemQuality) == 14) {
                        player.getServer().broadcastMessage("" + ChatColor.GOLD + ChatColor.BOLD + "O jogador " + Ferreiro.classColor + ChatColor.BOLD + player.getName() + ChatColor.GOLD + ChatColor.BOLD + " acabou de criar uma RELÍQUIA! Parabéns!");
                        shootRelicFirework((Player) event.getWhoClicked());
                    }


                }
            }
        }
    }

    public static void betterSmelt(FurnaceSmeltEvent event, int d100, Material resultType) {
        if (Checks.isIngot(resultType)) {
            if (resultType.equals(Material.IRON_NUGGET) || resultType.equals(Material.GOLD_NUGGET)) {
                if (d100 > 40) {
                    if (d100 <= 70) {
                        event.getResult().setAmount(event.getResult().getAmount() + 3);
                    } else if (d100 <= 90) {
                        event.getResult().setAmount(event.getResult().getAmount() + 7);
                    } else if (d100 <= 100) {
                        event.getResult().setAmount(event.getResult().getAmount() + 15);
                    }
                }
            } else {
                if (d100 > 60) {
                    if (d100 <= 85) {
                        event.getResult().setAmount(event.getResult().getAmount() + 1);
                    } else if (d100 <= 100) {
                        event.getResult().setAmount(event.getResult().getAmount() + 3);
                    }
                }
            }
        }
    }

    public static void shootRelicFirework(Player player) {

        Color color1 = null;
        Color color2 = null;
        Color color3 = null;

        Color color4 = null;
        Color color5 = null;
        Color color6 = null;

        switch (MainEvents.random(1, 6)) {
            case 1:
                color1 = Color.ORANGE;
                color2 = Color.YELLOW;
                color3 = Color.RED;

                color4 = Color.ORANGE;
                color5 = Color.WHITE;
                color6 = Color.BLACK;
                break;
            case 2:
                color1 = Color.ORANGE;
                color2 = Color.YELLOW;
                color3 = Color.WHITE;

                color4 = Color.ORANGE;
                color5 = Color.RED;
                color6 = Color.BLACK;
                break;
            case 3:
                color1 = Color.ORANGE;
                color2 = Color.YELLOW;
                color3 = Color.BLACK;

                color4 = Color.ORANGE;
                color5 = Color.WHITE;
                color6 = Color.RED;
                break;
            case 4:
                color1 = Color.ORANGE;
                color2 = Color.YELLOW;
                color3 = Color.RED;

                color4 = Color.ORANGE;
                color5 = Color.WHITE;
                color6 = Color.BLACK;
                break;
            case 5:
                color1 = Color.ORANGE;
                color2 = Color.RED;
                color3 = Color.BLACK;

                color4 = Color.ORANGE;
                color5 = Color.YELLOW;
                color6 = Color.WHITE;
                break;
            case 6:
                color1 = Color.ORANGE;
                color2 = Color.YELLOW;
                color3 = Color.BLACK;

                color4 = Color.ORANGE;
                color5 = Color.WHITE;
                color6 = Color.RED;
                break;
        }

        Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(color1, color2, color3).withFade(color4, color5, color6).with(FireworkEffect.Type.STAR).trail(true).build();
        fwm.addEffect(effect);
        fwm.setPower(1);
        fw.setFireworkMeta(fwm);

    }

    public static void applyImmunities(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getNewEffect() != null) {
                if (Main.config.getBoolean("RPG_CLASSES")) {
                    if (player.hasPermission(Ferreiro.BASE_PERMISSION)) {
                        for (PotionEffectType immunity :
                                Ferreiro.CLASS_IMMUNITIES) {
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
