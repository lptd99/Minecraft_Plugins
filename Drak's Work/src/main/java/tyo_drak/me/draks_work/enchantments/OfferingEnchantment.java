package tyo_drak.me.draks_work.enchantments;

import tyo_drak.me.draks_work.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class OfferingEnchantment extends Enchantment implements Listener {

    public OfferingEnchantment(String namespace) {
        super(new NamespacedKey(Main.getPlugin(), namespace));
    }

    @EventHandler
    public void entityDeathEvent(EntityDeathEvent event){
        Debug.consoleMessage("\n+ entityDeathEvent +\n");
        if (event.getEntity().getKiller() instanceof Player) {
            System.out.println("\n+ 1 +\n");
            Player p = (Player) event.getEntity().getKiller();
            if (event.getEntity().getType() == EntityType.ZOMBIE) {
                System.out.println("\n+ 2 +\n");
                EntityType z = event.getEntity().getType();
                if (p.getEquipment().getItemInMainHand().getEnchantments().containsKey(Enchantment.getByKey(Main.offeringEnchantment.getKey()))) { // Se o item equipado tem esse encantamento
                    System.out.println("\n+ 3 +\n");
                    ItemStack weaponOffering = p.getEquipment().getItemInMainHand();
                    Map<Enchantment, Integer> enchantments = weaponOffering.getEnchantments();
                    Integer enchLevel = enchantments.get(Enchantment.getByKey(Main.offeringEnchantment.getKey()));
                    System.out.println(enchLevel);
                    List<ItemStack> drops = event.getDrops();
                    boolean hasRottenFlesh = false;
                    int index = 0;
                    for (ItemStack item : drops) {
                        if (item.getType() == Material.ROTTEN_FLESH) {
                            hasRottenFlesh = true;
                            index = drops.indexOf(item);
                        }
                    }
                    if (hasRottenFlesh) {
                        System.out.println("\n+ 4 +\n");
                        ItemStack rottenFlesh = drops.get(index);
                        rottenFlesh.setType(Material.GOLD_NUGGET);
                        Integer amount = rottenFlesh.getAmount() * enchLevel;
                        System.out.println("\nRF amount: "+rottenFlesh.getAmount());
                        drops.remove(rottenFlesh);
                        drops.add(new ItemStack(Material.GOLD_NUGGET, amount % 9));
                        drops.add(new ItemStack(Material.GOLD_INGOT, amount / 9));
                        System.out.println("\namount: " + amount +
                                "\namount % 9: " + (amount % 9) +
                                "\namount / 9: " + (amount / 9) +
                                "\ndrops.size(): " + drops.size());
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Offering";
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
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
