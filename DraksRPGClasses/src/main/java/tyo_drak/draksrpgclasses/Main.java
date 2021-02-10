package tyo_drak.draksrpgclasses;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    public static Main plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        plugin = this;

        config = getConfig();
        setupConfig();
        saveConfig();

        getServer().getPluginManager().registerEvents(new MainEvents(), this);

        Recipes.addRecipes();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupConfig() {
        config.addDefault("SYSTEM_DEBUG", false);

        //<editor-fold defaultstate="collapsed" desc="VIPS">
        List<String> vips = new ArrayList<>();
        vips.add("Tyo_Drak");
        Debug.consoleMessage("Added " + vips.size() + " VIPs.");
        Main.config.addDefault("VIPS", vips);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PERMISSIONS">
        //config.addDefault("COMMANDS_PERMISSION", "drakswork.main.commands");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PACKS">
        config.addDefault("RPG_CLASSES", true);
        config.addDefault("COMMANDS", false);
        config.addDefault("ENCHANTMENTS", false);
        //</editor-fold>

        config.addDefault("WORLD_NAME", "world");

        config.options().copyDefaults(true);
    }




}
