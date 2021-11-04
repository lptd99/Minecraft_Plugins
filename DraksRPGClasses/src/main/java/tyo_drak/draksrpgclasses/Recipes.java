package tyo_drak.draksrpgclasses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import tyo_drak.drakslib.Items;

import java.util.ArrayList;

public class Recipes {

    //<editor-fold defaultstate="collapsed" desc="EDITOR-FOLDING">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VANILLA'S UNCRAFTABLES">
    public static void craftingSaddle() {
            ItemStack saddle = new ItemStack(Material.SADDLE, 1);
            NamespacedKey key = new NamespacedKey(Main.plugin, "saddle");
            ShapedRecipe recipe = new ShapedRecipe(key, saddle);
            recipe.shape("LLL", "LNL", "N N");
            recipe.setIngredient('L', Material.LEATHER);
            recipe.setIngredient('N', Material.IRON_NUGGET);
            Bukkit.addRecipe(recipe);
        }

        public static void craftingChainmailArmor() {
            ItemStack chaimmailHelmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
            NamespacedKey helmetKey = new NamespacedKey(Main.plugin, "chainmail_helmet");
            ShapedRecipe helmetRecipe = new ShapedRecipe(helmetKey, chaimmailHelmet);
            helmetRecipe.shape("NIN", "N N", "   ");
            helmetRecipe.setIngredient('I', Material.IRON_INGOT);
            helmetRecipe.setIngredient('N', Material.IRON_NUGGET);
            Bukkit.addRecipe(helmetRecipe);

            ItemStack chaimmailChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
            NamespacedKey chestplateKey = new NamespacedKey(Main.plugin, "chainmail_chestplate");
            ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, chaimmailChestplate);
            chestplateRecipe.shape("N N", "NIN", "NIN");
            chestplateRecipe.setIngredient('I', Material.IRON_INGOT);
            chestplateRecipe.setIngredient('N', Material.IRON_NUGGET);
            Bukkit.addRecipe(chestplateRecipe);

            ItemStack chaimmailLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
            NamespacedKey leggingsKey = new NamespacedKey(Main.plugin, "chainmail_leggings");
            ShapedRecipe leggingsRecipe = new ShapedRecipe(leggingsKey, chaimmailLeggings);
            leggingsRecipe.shape("NIN", "N N", "N N");
            leggingsRecipe.setIngredient('I', Material.IRON_INGOT);
            leggingsRecipe.setIngredient('N', Material.IRON_NUGGET);
            Bukkit.addRecipe(leggingsRecipe);

            ItemStack chaimmailBoots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
            NamespacedKey bootsKey = new NamespacedKey(Main.plugin, "chainmail_boots");
            ShapedRecipe bootsRecipe = new ShapedRecipe(bootsKey, chaimmailBoots);
            bootsRecipe.shape("N N", "N N", "   ");
            bootsRecipe.setIngredient('N', Material.IRON_NUGGET);
            Bukkit.addRecipe(bootsRecipe);
        }

        public static void craftingEnchantedGoldenApple() {
            ItemStack enchantedGoldenApple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
            NamespacedKey key = new NamespacedKey(Main.plugin, "enchantedGoldenApple");
            ShapedRecipe recipe = new ShapedRecipe(key, enchantedGoldenApple);
            recipe.shape("BBB", "BAB", "BBB");
            recipe.setIngredient('A', Material.GOLDEN_APPLE);
            recipe.setIngredient('B', Material.GOLD_BLOCK);
            Bukkit.addRecipe(recipe);
        }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SPAWNER">
    public static void craftingMinDelayCrystal() {
        ItemStack minDelayCrystal = Items.getMinDelayCrystal();
        NamespacedKey key = new NamespacedKey(Main.plugin, "minDelayCrystal");
        ShapedRecipe recipe = new ShapedRecipe(key, minDelayCrystal);
        recipe.shape("FEF", "EEE", "FEF");
        recipe.setIngredient('F', Material.FEATHER);
        recipe.setIngredient('E', Material.EMERALD);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingMaxDelayCrystal() {
        ItemStack maxDelayCrystal = Items.getMaxDelayCrystal();
        NamespacedKey key = new NamespacedKey(Main.plugin, "maxDelayCrystal");
        ShapedRecipe recipe = new ShapedRecipe(key, maxDelayCrystal);
        recipe.shape("EFE", "FEF", "EFE");
        recipe.setIngredient('F', Material.FEATHER);
        recipe.setIngredient('E', Material.EMERALD);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingPlayerRangeCrystal() {
        ItemStack playerRangeCrystal = Items.getPlayerRangeCrystal();
        NamespacedKey key = new NamespacedKey(Main.plugin, "playerRangeCrystal");
        ShapedRecipe recipe = new ShapedRecipe(key, playerRangeCrystal);
        recipe.shape("BEB", "EEE", "BEB");
        recipe.setIngredient('B', Material.BOW);
        recipe.setIngredient('E', Material.EMERALD);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingCondensedEmeraldGem() {
        ItemStack condensedEmeraldGem = Items.getCondensedEmeraldGem();
        NamespacedKey key = new NamespacedKey(Main.plugin, "condensedEmeraldGem");
        ShapedRecipe recipe = new ShapedRecipe(key, condensedEmeraldGem);
        recipe.shape("EEE", "EEE", "EEE");
        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingSpiritualIronBars() {
        ItemStack spiritualIronBars = Items.getSpiritualIronBars();
        spiritualIronBars.setAmount(4);
        NamespacedKey key = new NamespacedKey(Main.plugin, "spiritualIronBars");
        ShapedRecipe recipe = new ShapedRecipe(key, spiritualIronBars);
        recipe.shape("BSB", "STS", "BSB");
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('S', Material.SOUL_SAND);
        recipe.setIngredient('T', Material.GHAST_TEAR);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingSparkingCondensedSoulSand() {
        ItemStack sparkingCondensedSoulSand = Items.getSparkingCondensedSoulSand();
        NamespacedKey key = new NamespacedKey(Main.plugin, "sparkingCondensedSoulSand");
        ShapedRecipe recipe = new ShapedRecipe(key, sparkingCondensedSoulSand);
        recipe.shape("SSS", "SPS", "SSS");
        recipe.setIngredient('S', Material.SOUL_SAND);
        recipe.setIngredient('P', Material.BLAZE_POWDER);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingSpawner() {

        ItemStack spawner = Items.getSpawnerItemStack(null);
        NamespacedKey key = new NamespacedKey(Main.plugin, "spawner");
        ShapedRecipe recipe = new ShapedRecipe(key, spawner);
        recipe.shape("BCB", "CSC", "BGB");
        recipe.setIngredient('B', Material.IRON_BARS); // SPIRITUAL IRON BARS
        recipe.setIngredient('C', Material.EMERALD); // CRYSTALS
        recipe.setIngredient('S', Material.SOUL_SAND); // SPARKING CONDENSED SOUL SAND
        recipe.setIngredient('G', Material.EMERALD); // CONDENSED EMERALD GEM
        Bukkit.addRecipe(recipe);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SCROLLS">
    public static void craftingBlankScroll(){
        ItemStack blankScroll = Items.getBlankScroll();
        NamespacedKey key = new NamespacedKey(Main.plugin, "blankScroll");
        ShapedRecipe recipe = new ShapedRecipe(key, blankScroll);
        recipe.shape(" E ", " P ", " P ");
        recipe.setIngredient('E', Material.EXPERIENCE_BOTTLE);
        recipe.setIngredient('P', Material.PAPER);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingTeleportationPearl(){
        ItemStack teleportationPearl = Items.getTeleportationPearl();
        NamespacedKey key = new NamespacedKey(Main.plugin, "teleportationPearl");
        ShapedRecipe recipe = new ShapedRecipe(key, teleportationPearl);
        recipe.shape("PPP", "PNP", "PPP");
        recipe.setIngredient('N', Material.GOLD_NUGGET);
        recipe.setIngredient('P', Material.ENDER_PEARL);
        Bukkit.addRecipe(recipe);
    }
    public static void craftingTeleportScroll(){
        ItemStack teleportScroll = Items.getTeleportScroll();
        NamespacedKey key = new NamespacedKey(Main.plugin, "teleportScroll");
        ShapedRecipe recipe = new ShapedRecipe(key, teleportScroll);
        recipe.shape(" P ", " S ", "   ");
        recipe.setIngredient('S', Material.PAPER); // BLANK SCROLL
        recipe.setIngredient('P', Material.ENDER_PEARL); // CONDENSED ENDER PEARL
        Bukkit.addRecipe(recipe);
    }

    public static void craftingScrollOfResurrection() {
        ItemStack scrollOfResurrection = Items.getScrollOfResurrection();
    }
    //</editor-fold>

    public static void furnaceRottenFleshToLeather() {
        ItemStack leather = new ItemStack(Material.LEATHER, 2);
        NamespacedKey key = new NamespacedKey(Main.plugin, "rottenFleshToLeather");
        FurnaceRecipe recipe = new FurnaceRecipe(key, leather, Material.ROTTEN_FLESH, 5, 400);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingPlayerSpawnPointApple() {
        ItemStack playerSpawnPointApple = Items.getPlayerSpawnPointApple();
        NamespacedKey key = new NamespacedKey(Main.plugin, "playerSpawnPointApple");
        ShapedRecipe recipe = new ShapedRecipe(key, playerSpawnPointApple);
        recipe.shape("DED", "EAE", "DED");
        recipe.setIngredient('A', Material.ENCHANTED_GOLDEN_APPLE);
        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        Bukkit.addRecipe(recipe);
    }

    public static void craftingCondensedGoldNugget() {
        ItemStack condensedGoldNugget = Items.getCondensedGoldNugget();
        NamespacedKey key = new NamespacedKey(Main.plugin, "condensedGoldNugget");
        ShapedRecipe recipe = new ShapedRecipe(key, condensedGoldNugget);
        recipe.shape("GGG", "GGG", "GGG");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        Bukkit.addRecipe(recipe);
    }

    public static void addRecipes() {
        furnaceRottenFleshToLeather();

        // VANILLA'S UNCRAFTABLES
        craftingSaddle();
        craftingChainmailArmor();

        craftingMinDelayCrystal();
        craftingMaxDelayCrystal();
        craftingPlayerRangeCrystal();

        craftingCondensedGoldNugget();
        craftingCondensedEmeraldGem();

        craftingSpiritualIronBars();
        craftingSparkingCondensedSoulSand();
        craftingSpawner();

        craftingEnchantedGoldenApple();
        //craftingPlayerSpawnPointApple();
    }

}
