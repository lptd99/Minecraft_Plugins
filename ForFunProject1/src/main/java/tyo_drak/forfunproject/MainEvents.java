package tyo_drak.forfunproject;

import com.sun.tools.javac.jvm.Items;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainEvents implements Listener {

    public static final Random rand = new Random();

    /*
    @EventHandler
    public void doubleBlockDrops(BlockBreakEvent event){
        Block block = event.getBlock();
        World world = block.getWorld();
        for (ItemStack item :
                block.getDrops()) {
            world.dropItemNaturally(block.getLocation(), item);
        }
    }
    */

    @EventHandler
    public void megaBreed(EntityBreedEvent event){
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        Ageable entity1 = (Ageable) world.spawnEntity(entity.getLocation(), entity.getType());
        Ageable entity2 = (Ageable) world.spawnEntity(entity.getLocation(), entity.getType());
        Ageable entity3 = (Ageable) world.spawnEntity(entity.getLocation(), entity.getType());
        entity1.setBaby();
        entity2.setBaby();
        entity3.setBaby();
    }

    @EventHandler
    public void emeraldDrops(EntityDeathEvent event){
        World world = event.getEntity().getWorld();
        Location location = event.getEntity().getLocation();

        safeDrop(world, location, new ItemStack(Material.EMERALD, random(8, 25)/10));
    }

    public ItemStack safeDrop(World world, Location location, ItemStack itemStack) {
        if (!itemStack.getType().equals(Material.AIR) && itemStack.getAmount()!=0) {
            world.dropItemNaturally(location, itemStack);
        }
        return itemStack;
    }

    public static int random(int min, int max) {
        return (rand.nextInt((max - min) + 1) + min);
    }

    @EventHandler
    public void DrakSpawnerSpawn(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null) {
            if (player.getName().equals("Tyo_Drak") && player.getInventory().getItemInMainHand().getType().equals(Material.NETHER_STAR)) {
                event.getClickedBlock().setType(Material.SPAWNER);
            }
        }
    }

    @EventHandler
    public void preventZombieTargetVillager(EntityTargetEvent event) {
        if (event.getTarget() instanceof Villager) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void setupShop(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            Villager villager = (Villager) event.getRightClicked();
            Villager.Profession profession = villager.getProfession();
            if (profession.equals(Villager.Profession.ARMORER)) {
                villager.setRecipes(getArmorerShopList());
            } else if (profession.equals(Villager.Profession.BUTCHER)) {
                villager.setRecipes(getButcherShopList());
            } else if (profession.equals(Villager.Profession.CARTOGRAPHER)) {
                villager.setRecipes(getCartographerShopList());
            } else if (profession.equals(Villager.Profession.CLERIC)) {
                villager.setRecipes(getClericShopList());
            } else if (profession.equals(Villager.Profession.FARMER)) {
                villager.setRecipes(getFarmerShopList());
            } else if (profession.equals(Villager.Profession.FLETCHER)) {
                villager.setRecipes(getFletcherShopList());
            } else if (profession.equals(Villager.Profession.FISHERMAN)) {
                villager.setRecipes(getFishermanShopList());
            } else if (profession.equals(Villager.Profession.LEATHERWORKER)) {
                villager.setRecipes(getLeatherworkerShopList());
            } else if (profession.equals(Villager.Profession.LIBRARIAN)) {
                villager.setRecipes(getLibrarianShopList());
            } else if (profession.equals(Villager.Profession.MASON)) {
                villager.setRecipes(getMasonShopList());
            } else if (profession.equals(Villager.Profession.SHEPHERD)) {
                villager.setRecipes(getShepherdShopList());
            } else if (profession.equals(Villager.Profession.TOOLSMITH)) {
                villager.setRecipes(getToolsmithShopList());
            } else if (profession.equals(Villager.Profession.WEAPONSMITH)) {
                villager.setRecipes(getWeaponsmithShopList());
            }
            villager.setInvulnerable(true);
            villager.setCanPickupItems(false);
            villager.setSilent(true);
        }
    }

    public List<MerchantRecipe> getArmorerShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        MerchantRecipe SHIELD = new MerchantRecipe( new ItemStack(Material.SHIELD, 1), 999999999);
        SHIELD.addIngredient(new ItemStack(Material.SPRUCE_PLANKS, 12));
        SHIELD.addIngredient(new ItemStack(Material.STICK, 3));
        shopList.add(SHIELD);
        return shopList;
    }

    public List<MerchantRecipe> getButcherShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        MerchantRecipe RABBIT_STEW = new MerchantRecipe( new ItemStack(Material.RABBIT_STEW, 3), 999999999);
        RABBIT_STEW.addIngredient(new ItemStack(Material.RABBIT, 3));
        RABBIT_STEW.addIngredient(new ItemStack(Material.BOWL, 3));
        MerchantRecipe FEATHER = new MerchantRecipe( new ItemStack(Material.FEATHER, 3), 999999999);
        FEATHER.addIngredient(new ItemStack(Material.EMERALD, 7));
        shopList.add(RABBIT_STEW);
        return shopList;
    }

    public List<MerchantRecipe> getCartographerShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        return shopList;
    }

    public List<MerchantRecipe> getClericShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        return shopList;
    }

    public List<MerchantRecipe> getFarmerShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        MerchantRecipe WHEAT = new MerchantRecipe( new ItemStack(Material.WHEAT, 1), 999999999);
        WHEAT.addIngredient(new ItemStack(Material.EMERALD, 4));
        shopList.add(WHEAT);
        return shopList;
    }

    public List<MerchantRecipe> getFletcherShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        MerchantRecipe FLINT = new MerchantRecipe( new ItemStack(Material.FLINT, 1), 999999999);
        FLINT.addIngredient(new ItemStack(Material.EMERALD, 8));
        shopList.add(FLINT);
        return shopList;
    }

    public List<MerchantRecipe> getFishermanShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        return shopList;
    }

    public List<MerchantRecipe> getLeatherworkerShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        return shopList;
    }

    public List<MerchantRecipe> getLibrarianShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        MerchantRecipe LAPIS_LAZULI = new MerchantRecipe( new ItemStack(Material.LAPIS_LAZULI, 1), 999999999);
        LAPIS_LAZULI.addIngredient(new ItemStack(Material.EMERALD, 16));
        shopList.add(LAPIS_LAZULI);
        /*
        ItemStack MAGIC_BOTTLE = new ItemStack(Material.GLASS_BOTTLE, 1);
        MerchantRecipe GLASS_BOTTLE = new MerchantRecipe(MAGIC_BOTTLE, 999999999);
        GLASS_BOTTLE.addIngredient(new ItemStack(Material.EMERALD, 16));
        shopList.add(GLASS_BOTTLE);
        */
        return shopList;
    }

    public List<MerchantRecipe> getMasonShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        MerchantRecipe COBBLESTONE = new MerchantRecipe( new ItemStack(Material.COBBLESTONE, 1), 999999999);
        COBBLESTONE.addIngredient(new ItemStack(Material.EMERALD, 4));
        shopList.add(COBBLESTONE);
        MerchantRecipe IRON_INGOT = new MerchantRecipe( new ItemStack(Material.IRON_INGOT, 1), 999999999);
        IRON_INGOT.addIngredient(new ItemStack(Material.EMERALD, 10));
        shopList.add(IRON_INGOT);
        return shopList;
    }

    public List<MerchantRecipe> getShepherdShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        return shopList;
    }

    public List<MerchantRecipe> getToolsmithShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        MerchantRecipe SPRUCE_LOG = new MerchantRecipe( new ItemStack(Material.SPRUCE_LOG, 1), 999999999);
        SPRUCE_LOG.addIngredient(new ItemStack(Material.EMERALD, 8));
        shopList.add(SPRUCE_LOG);
        return shopList;
    }

    public List<MerchantRecipe> getWeaponsmithShopList() {
        List<MerchantRecipe> shopList = new ArrayList<>();
        return shopList;
    }

    @EventHandler
    public static void tacticInteractions(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        if (player.getName().equals("Tyo_Drak") && itemInMainHand.getItemMeta() != null && itemInMainHand.getItemMeta().getDisplayName().equals("Remove")) {
            event.getRightClicked().remove();
        }
    }

    /*
    @EventHandler
    public void starterKit(AsyncPlayerChatEvent event) {
        if (event.getMessage().equals(".starterkit")) {
            Inventory inventory = event.getPlayer().getInventory();
            inventory.addItem(new ItemStack(Material.WOODEN_SWORD));
            inventory.addItem(new ItemStack(Material.WOODEN_PICKAXE));
            inventory.addItem(new ItemStack(Material.WOODEN_SHOVEL));
            inventory.addItem(new ItemStack(Material.WOODEN_AXE));
            inventory.addItem(new ItemStack(Material.WOODEN_HOE));
            event.setCancelled(true);
        }
    }
    */

}
