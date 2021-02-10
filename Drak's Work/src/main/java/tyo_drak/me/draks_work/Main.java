package tyo_drak.me.draks_work;

import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tyo_drak.me.draks_work.commands.GiveTest;
import tyo_drak.me.draks_work.commands.RawInfo;
import tyo_drak.me.draks_work.enchantments.MarkerEnchantment;
import tyo_drak.me.draks_work.enchantments.OfferingEnchantment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin {

    // MAIN
    //<editor-fold defaultstate="collapsed" desc="GENERAL">
    public static Main plugin;
    public static FileConfiguration config;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ENCHANTMENTS">
    public static ArrayList<Enchantment> customEnchants = new ArrayList<>();
    public static MarkerEnchantment markerEnchantment;
    public static OfferingEnchantment offeringEnchantment;
    //</editor-fold>


    @Override
    public void onEnable() {
        //<editor-fold defaultstate="collapsed" desc="CORE">
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        plugin = this;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CONFIG">
        config = getConfig();
        setupConfig();
        saveConfig();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="EVENTS">
        getServer().getPluginManager().registerEvents(new MainEvents(), this);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="ENCHANTMENTS AND COMMANDS">
        if (config.getBoolean("ENCHANTMENTS") && config.getBoolean("COMMANDS")) {
            Objects.requireNonNull(getCommand("givetest")).setExecutor(new GiveTest());
        }

        if (config.getBoolean("ENCHANTMENTS")) {
            markerEnchantment = new MarkerEnchantment("marker");
            offeringEnchantment = new OfferingEnchantment("offering");

            customEnchants.add(markerEnchantment);
            customEnchants.add(offeringEnchantment);

            registerEnchantments(customEnchants);

            pluginManager.registerEvents(markerEnchantment, this);
            pluginManager.registerEvents(offeringEnchantment, this);
        }

        if (config.getBoolean("COMMANDS")) {
            Objects.requireNonNull(getCommand("rawinfo")).setExecutor(new RawInfo());
        }
        //</editor-fold>
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (config.getBoolean("ENCHANTMENTS")) {
            unregisterEnchantments(customEnchants);
        }
    }


    // METHODS
    //<editor-fold defaultstate="collapsed" desc="GENERAL">
    public static Main getPlugin() {
        return plugin;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONFIG">
    public void setupConfig() {
        //<editor-fold defaultstate="collapsed" desc="VIPS">
        List<String> vips = new ArrayList<>();
        vips.add("MarceMagik");
        vips.add("Tyo_Drak");
        Debug.consoleMessage("Added " + vips.size() + " VIPs.");
        Main.config.addDefault("VIPS", vips);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PERMISSIONS">
        config.addDefault("COMMANDS_PERMISSION", "drakswork.main.commands");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="DEBUG">
        config.addDefault("SYSTEM_DEBUG", false);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PACKS">
        config.addDefault("NIGHTMARE", true);
        config.addDefault("RPG_CLASSES", true);
        config.addDefault("COMMANDS", false);
        config.addDefault("ENCHANTMENTS", false);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="NIGHTMARE MODULES">
        config.addDefault("HYDRA", false);
        config.addDefault("HYDRA_EGG", false);
        config.addDefault("BUFF_MOBS", false);
        config.addDefault("HURTS_LEGS", false);
        config.addDefault("BUFF_DROPS", false);
        config.addDefault("DONT_STARVE", false);
        config.addDefault("WARN_HUNGER", false);
        config.addDefault("WITHER_BUFF", false);
        config.addDefault("CREEPER_BOMB", false);
        config.addDefault("BUFF_DROWNING", false);
        config.addDefault("BETTER_POTIONS", false);
        config.addDefault("CRUMBLING_TOOLS", false);
        config.addDefault("SILVERFISH_HELL", false);
        config.addDefault("HALF_MAX_HEALTH", false);
        config.addDefault("STARVE_TO_DEATH", false);
        config.addDefault("WITHER_BUFF_DROPS", false);
        config.addDefault("FLESH_BURNS_HOTTER", false);
        config.addDefault("RAW_FOOD_POISONING", false);
        config.addDefault("BONES_BREAK_FURTHER", false);
        config.addDefault("PREVENT_UNARMED_COMBAT", false);
        config.addDefault("BLIND_FROM_PROJECTILE_HIT", false);
        //</editor-fold>

        config.options().copyDefaults(true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ENCHANTMENTS">
    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {
            //It's been registered!
        }
    }

    public static void registerEnchantments(ArrayList<Enchantment> enchantments) {
        for (Enchantment enchantment :
                enchantments) {
            registerEnchantment(enchantment);
        }
    }

    public static void unregisterEnchantment(Enchantment enchantment) {

        try {
            // Remove Key
            Field keyField = Enchantment.class.getDeclaredField("byKey");
            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);
            if (byKey.containsKey(enchantment.getKey())) {
                byKey.remove(enchantment.getKey());
            }

            // Remove Name
            Field nameField = Enchantment.class.getDeclaredField("byName");
            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);
            if (byName.containsKey(enchantment.getName())) {
                byName.remove(enchantment.getName());
            }
        } catch (Exception ignored) {
        }
    }

    public static void unregisterEnchantments(ArrayList<Enchantment> enchantments) {
        for (Enchantment enchantment :
                enchantments) {
            unregisterEnchantment(enchantment);
        }
    }
    //</editor-fold>


}
