package tyo_drak.draksnightmare;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import tyo_drak.drakslib.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Misc.dLog("Added " + vips.size() + " VIPs.");
        Main.config.addDefault("VIPS", vips);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PERMISSIONS">
        config.addDefault("COMMANDS_PERMISSION", "drakswork.main.commands");
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PACKS AND SINGLES">
        config.addDefault("NIGHTMARE", true);
        config.addDefault("COMMANDS", false);
        config.addDefault("ENCHANTMENTS", false);
        config.addDefault("DELAY_RESPAWN", true);
        config.addDefault("DEBUG_RESPAWN_TIME", 10);
        config.addDefault("VIP_RESPAWN_TIME", 60);
        config.addDefault("REGULAR_RESPAWN_TIME", 300);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="NIGHTMARE MODULES">
        config.addDefault("HYDRA", false);
        config.addDefault("HYDRA_EGG", false);
        config.addDefault("BUFF_MOBS", false);
        config.addDefault("HURTS_LEGS", false);
        config.addDefault("HURT_LEGS_DURATION", 15);
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
}
