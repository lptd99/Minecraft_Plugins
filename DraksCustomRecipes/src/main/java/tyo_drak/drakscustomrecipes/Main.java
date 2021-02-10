package tyo_drak.drakscustomrecipes;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Recipes.addRecipes();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
