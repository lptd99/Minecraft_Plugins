package tyo_drak.draksrpgclasses;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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


        //<editor-fold defaultstate="collapsed" desc="PACKS">
        config.addDefault("RPG_CLASSES", true);
        config.addDefault("COMMANDS", false);
        config.addDefault("ENCHANTMENTS", false);
        //</editor-fold>

        config.addDefault("PREVENT_UNARMED_COMBAT", true);
        config.addDefault("WORLD_NAME", "world");

        config.options().copyDefaults(true);
    }




}
