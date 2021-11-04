package tyo_drak.drakslib;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    private static JavaPlugin plugin;
    public static FileConfiguration config;

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    // This method must not be used any where in the library!
    public static void setPlugin(final JavaPlugin plugin) {
        Main.plugin = plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Main.setPlugin(this);

        config = getConfig();
        setupConfig();
        saveConfig();
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
        Misc.log("Added " + vips.size() + " VIPs.");
        Main.config.addDefault("VIPS", vips);
        //</editor-fold>

        config.addDefault("WORLD_NAME", "world");

        config.options().copyDefaults(true);
    }

}
