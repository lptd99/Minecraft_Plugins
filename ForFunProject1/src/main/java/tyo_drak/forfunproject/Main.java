package tyo_drak.forfunproject;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new MainEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
