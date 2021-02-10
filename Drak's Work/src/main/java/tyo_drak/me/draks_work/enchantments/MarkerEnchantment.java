package tyo_drak.me.draks_work.enchantments;

import tyo_drak.me.draks_work.Main;
import tyo_drak.me.draks_work.tasks.BleedoutTask;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class MarkerEnchantment extends Enchantment implements Listener {

    public MarkerEnchantment(String namespace) {
        super(new NamespacedKey(Main.getPlugin(), namespace));
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        try {
            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                if (p.getEquipment().getChestplate().getEnchantments().containsKey(Enchantment.getByKey(Main.markerEnchantment.getKey()))) {
                    e.getEntity().setGlowing(true);
                    LivingEntity enemy = (LivingEntity) e.getEntity();
                    if (enemy.getHealth() - e.getDamage() <= 2*p.getEquipment().getChestplate().getEnchantmentLevel(getByKey(Main.markerEnchantment.getKey()))) {
                        p.sendMessage(ChatColor.DARK_RED + "The enemy is bleeding out!");
                        new BleedoutTask(enemy).runTaskTimer(Main.getPlugin(), 0L, 10L);
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    @Override
    public String getName() {
        return "Marker";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }
}
