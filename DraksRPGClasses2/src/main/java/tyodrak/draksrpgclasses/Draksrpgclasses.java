package tyodrak.draksrpgclasses;

import org.bukkit.plugin.java.JavaPlugin;

public final class Draksrpgclasses extends JavaPlugin {

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
}
