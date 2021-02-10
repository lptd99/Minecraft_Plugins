package tyo_drak.me.draks_work.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RawInfo implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            ItemStack item = p.getInventory().getItemInMainHand();
            String m = "\n---------- RAW INFO ----------";
            m = m.concat("\nEnchantments: "+ item.getEnchantments().toString());
            m = m.concat("\nItem Meta: "+ item.getItemMeta().toString());
            m = m.concat("\nAmount: "+ item.getAmount());
            m = m.concat("\nData: "+ item.getData().toString());
            m = m.concat("\nMax Stack Size: "+ item.getMaxStackSize());
            m = m.concat("\nType: "+ item.getType().toString());
            m = m.concat("\nClass: "+ item.getClass().toString());
            m = m.concat("\nDurability: "+ item.getDurability());
            m = m.concat("\n---------- END OF RAW INFO ----------");
            p.sendMessage(m);
        }
        return true;
    }
}
