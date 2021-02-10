package me.tyodrak.tyodraksrpgplugin;

import net.braincraft.tyo_drak.main.rpg.classes.arqueiro.Cacador;
import net.braincraft.tyo_drak.main.rpg.classes.druida.Druida;
import net.braincraft.tyo_drak.main.rpg.classes.mercador.Ferreiro;
import net.braincraft.tyo_drak.main.rpg.classes.gatuno.Gatuno;
import net.braincraft.tyo_drak.main.rpg.classes.espadachim.Espadachim;
import net.braincraft.tyo_drak.main.rpg.classes.mago.Mago;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public final class Main extends JavaPlugin {

    public static Cacador cacador;
    public static Gatuno gatuno;
    public static Espadachim espadachim;
    public static Ferreiro ferreiro;
    public static Druida druida;
    public static Mago mago;
    public static MainEvents mainEvents;

    @Override
    public void onEnable() {
        initialize();

        Server s = this.getServer();
        PluginManager pm = s.getPluginManager();

        pm.registerEvents(gatuno, this);
        pm.registerEvents(espadachim, this);
        pm.registerEvents(druida, this);
        pm.registerEvents(mago, this);
        pm.registerEvents(ferreiro, this);

        pm.registerEvents(cacador, this);

        pm.registerEvents(mainEvents, this);
    }

    private void initialize() {
        //plugin = new Main();
        mainEvents = new MainEvents();

        espadachim = new Espadachim();
        gatuno = new Gatuno();
        druida = new Druida();
        mago = new Mago();
        ferreiro = new Ferreiro();
        cacador = new Cacador();

        braincraftEnchantments = new ArrayList<>();

        registerEnchantments(braincraftEnchantments);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void end() {
        plugin = null;
        mainEvents = null;

        gatuno = null;
        espadachim = null;

        braincraftEnchantments = null;

        unregisterEnchantments(braincraftEnchantments);
    }

    public static void registerEnchantment(Enchantment enchantment){
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if(registered){
            //It`s been registered!
        }
    }
    public static void registerEnchantments(ArrayList<Enchantment> enchantments){
        for (Enchantment enchantment :
                enchantments) {
            registerEnchantment(enchantment);
        }
    }
    public static void unregisterEnchantment(Enchantment enchantment){

        try {
            // Remove Key
            Field keyField = Enchantment.class.getDeclaredField("byKey");
            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);
            if(byKey.containsKey(enchantment.getKey())) {
                byKey.remove(enchantment.getKey());
            }

            // Remove Name
            Field nameField = Enchantment.class.getDeclaredField("byName");
            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);
            if (byName.containsKey(enchantment.getName())){
                byName.remove(enchantment.getName());
            }
        } catch (Exception ignored) { }
    }
    public static void unregisterEnchantments(ArrayList<Enchantment> enchantments){
        for (Enchantment enchantment :
                enchantments) {
            unregisterEnchantment(enchantment);
        }
    }

    public static Main getPlugin(){
        return plugin;
    }

}
