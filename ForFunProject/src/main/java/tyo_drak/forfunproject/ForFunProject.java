package tyo_drak.forfunproject;

import org.bukkit.plugin.java.JavaPlugin;

public final class ForFunProject extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Claims(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
