package tyo_drak.me.draks_work.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tyo_drak.me.draks_work.Main;

import java.util.ArrayList;

public class GiveTest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("main.givetest")) {

                // MARKER CHESTPLATE
                ItemStack cpMarker = new ItemStack(Material.IRON_CHESTPLATE, 1);
                cpMarker.addUnsafeEnchantment(Main.markerEnchantment, 1);
                ItemMeta metaMarker = cpMarker.getItemMeta();
                ArrayList<String> loreMarker = new ArrayList<>();
                loreMarker.add(ChatColor.LIGHT_PURPLE + "Marker");
                metaMarker.setLore(loreMarker);
                cpMarker.setItemMeta(metaMarker);

                // OFFERING AXE
                ItemStack axeOffering = new ItemStack(Material.GOLDEN_AXE, 1);
                axeOffering.addUnsafeEnchantment(Main.offeringEnchantment, 3);
                axeOffering.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
                ItemMeta metaOffering = axeOffering.getItemMeta();
                ArrayList<String> loreOffering = new ArrayList<>();
                loreOffering.add(ChatColor.LIGHT_PURPLE + "Offering III");
                metaOffering.setLore(loreOffering);
                axeOffering.setItemMeta(metaOffering);

                // OFFERING AXE 2
                ItemStack axeOffering2 = new ItemStack(Material.GOLDEN_AXE, 1);
                axeOffering2.addUnsafeEnchantment(Main.offeringEnchantment, 10);
                axeOffering2.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
                ItemMeta metaOffering2 = axeOffering.getItemMeta();
                ArrayList<String> loreOffering2 = new ArrayList<>();
                loreOffering2.add(ChatColor.LIGHT_PURPLE + "Offering X");
                metaOffering2.setLore(loreOffering2);
                axeOffering2.setItemMeta(metaOffering2);

                // TEST BOW
                ItemStack testBow = new ItemStack(Material.BOW);
                testBow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 2);
                testBow.addUnsafeEnchantment(Enchantment.MULTISHOT, 1);
                testBow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 4);

                // GIVE ITEMS
                p.getInventory().addItem(axeOffering);
                p.getInventory().addItem(axeOffering2);
                p.getInventory().addItem(cpMarker);
                p.getInventory().addItem(testBow);

            } else {
                p.sendMessage("You do not have the required permission ("+ Main.config.getString("COMMANDS_PERMISSION") +") to run this command.");
            }
        }
        return true;
    }
}
