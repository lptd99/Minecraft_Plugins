package tyo_drak.draksclaims;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        getServer().getPluginManager().registerEvents(new Claims(), this);

        config = getConfig();
        setupConfig();
        saveConfig();
    }

    public void setupConfig() {
        config.addDefault("SYSTEM_DEBUG", true);
        config.options().copyDefaults(true);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
