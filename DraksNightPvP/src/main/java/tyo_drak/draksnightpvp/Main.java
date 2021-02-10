package tyo_drak.draksnightpvp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {
    public static Main plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new MainEvents(), this);
        config = getConfig();
        setupConfig();
        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void setupConfig() {
        //<editor-fold defaultstate="collapsed" desc="DEBUG">
        config.addDefault("SYSTEM_DEBUG", false);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="UNBOUND">
        config.addDefault("NIGHT_PVP", true);
        //</editor-fold>
        config.options().copyDefaults(true);
    }

}
