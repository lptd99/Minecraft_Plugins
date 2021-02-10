package me.tyodrak.tyodraksrpgplugin;

import net.braincraft.tyo_drak.main.rpg.classes.arqueiro.Cacador;
import net.braincraft.tyo_drak.main.rpg.classes.druida.Druida;
import net.braincraft.tyo_drak.main.rpg.classes.mercador.Ferreiro;
import net.braincraft.tyo_drak.main.rpg.classes.gatuno.Gatuno;
import net.braincraft.tyo_drak.main.rpg.classes.espadachim.Espadachim;
import net.braincraft.tyo_drak.main.rpg.classes.mago.Mago;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.tools.Tool;
import java.util.Map;
import java.util.UUID;

public class MainEvents implements Listener {

    public static Map<UUID, AttributeModifier> correctors;

    @EventHandler
    private void playerRespawnEvent(PlayerRespawnEvent event){
        try {
            resetPlayerMaxHealth(event.getPlayer());
            event.wait(100L);
            adjustHealthByClass(event.getPlayer());
            applyClassEffects(event.getPlayer());
        } catch (Exception ignored) {}
    }
    @EventHandler
    private void cancelEffectByClassImmunity(EntityPotionEffectEvent event){
        if (event.getEntity() instanceof Player){
            Player p = (Player) event.getEntity();
            if (event.getNewEffect() != null) {
                if (p.hasPermission(Druida.BASE_PERMISSION)) {
                        event.setCancelled(event.getNewEffect().getType().equals(PotionEffectType.POISON));
                }
                if (p.hasPermission(Ferreiro.BASE_PERMISSION)) {
                    event.setCancelled(event.getNewEffect().getType().equals(PotionEffectType.SLOW_DIGGING));
                }
                if (p.hasPermission(Gatuno.BASE_PERMISSION)) {
                    event.setCancelled(event.getNewEffect().getType().equals(PotionEffectType.SLOW));
                }
                if (p.hasPermission(Espadachim.BASE_PERMISSION)) {
                    event.setCancelled(event.getNewEffect().getType().equals(PotionEffectType.WEAKNESS));
                }
                if (p.hasPermission(Mago.BASE_PERMISSION)) {
                    event.setCancelled(event.getNewEffect().getType().equals(PotionEffectType.BLINDNESS));
                }
                if (p.hasPermission(Cacador.BASE_PERMISSION)){
                    event.setCancelled(event.getNewEffect().getType().equals(PotionEffectType.CONFUSION));
                }
            }
        }
    }
    private void applyClassEffects(Player p){
        if (p.isOp()){
            BCDebug.roleplayMessage(p, "OPs can't have CLASS EFFECTS.");
        } else if (p.hasPermission(Druida.BASE_PERMISSION)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 1000000, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1000000, 0));
            BCDebug.fullSout("[Braincraft] Applied DRUIDA effects to player: " +p.getName()+ ", with UUID:" +p.getUniqueId());
        } else if (p.hasPermission(Ferreiro.BASE_PERMISSION)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000, 0));
            if (p.hasPermission(Ferreiro.MASTERY_PERMISSION)){
                p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000, 3));
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000, 1));
            }
            BCDebug.fullSout("[Braincraft] Applied FERREIRO effects to player: " +p.getName()+ ", with UUID:" +p.getUniqueId());
        } else if (p.hasPermission(Gatuno.BASE_PERMISSION)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 1000000, 2));
            BCDebug.fullSout("[Braincraft] Applied GATUNO effects to player: " +p.getName()+ ", with UUID:" +p.getUniqueId());
        } else if (p.hasPermission(Espadachim.BASE_PERMISSION)){
            if (!p.hasPotionEffect(PotionEffectType.REGENERATION)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 0));
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1000000, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 0));
            BCDebug.fullSout("[Braincraft] Applied ESPADACHIM effects to player: " +p.getName()+ ", with UUID:" +p.getUniqueId());
        } else if (p.hasPermission(Mago.BASE_PERMISSION)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1000000, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1000000, 0));
            BCDebug.fullSout("[Braincraft] Applied MAGO effects to player: " +p.getName()+ ", with UUID:" +p.getUniqueId());
        } else if (p.hasPermission(Cacador.BASE_PERMISSION)){
            // NONE TO BE ADDED
            //BCDebug.fullSout("[Braincraft] Applied CACADOR effects to player: " +p.getName()+ ", with UUID:" +p.getUniqueId());
        }
    }
    private void removeClassEffects(Player p) {
        for (PotionEffect effect :
                p.getActivePotionEffects()) {
            if (effect.getDuration() > 99999) {
                p.removePotionEffect(effect.getType());
            }
        }
    }
    public static void adjustHealthByClass(Player p) {
        if (p.isOp()){
            resetPlayerMaxHealth(p);
        } else if(p.hasPermission(Druida.BASE_PERMISSION)) {
            if (!containsMaxHealthModifier(p, Druida.healthModifier)) {
                BCDebug.roleplaySout("[Braincraft] Applying DRUIDA MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(Druida.healthModifier);
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                BCDebug.roleplaySout("[Braincraft] Applied DRUIDA MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
            }
        } else if (p.hasPermission(Ferreiro.BASE_PERMISSION)) {
            if (!containsMaxHealthModifier(p, Ferreiro.healthModifier)) {
                BCDebug.roleplaySout("[Braincraft] Applying FERREIRO MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(Ferreiro.healthModifier);
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                BCDebug.roleplaySout("[Braincraft] Applied FERREIRO MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
            }
        } else if (p.hasPermission(Gatuno.BASE_PERMISSION)) {
            if (!containsMaxHealthModifier(p, Gatuno.healthModifier)) {
                BCDebug.roleplaySout("[Braincraft] Applying GATUNO MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(Gatuno.healthModifier);
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                BCDebug.roleplaySout("[Braincraft] Applied GATUNO MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
            }
        } else if(p.hasPermission(Espadachim.BASE_PERMISSION)) {
            if (!containsMaxHealthModifier(p, Espadachim.healthModifier)) {
                BCDebug.roleplaySout("[Braincraft] Applying ESPADACHIM MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(Espadachim.healthModifier);
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                BCDebug.roleplaySout("[Braincraft] Applied ESPADACHIM MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
            }
        } else if (p.hasPermission(Mago.BASE_PERMISSION)) {
            if (!containsMaxHealthModifier(p, Mago.healthModifier)) {
                BCDebug.roleplaySout("[Braincraft] Applying MAGO MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(Mago.healthModifier);
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                BCDebug.roleplaySout("[Braincraft] Applied MAGO MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
            }
        } else if (p.hasPermission(Cacador.BASE_PERMISSION)) {
            if (!containsMaxHealthModifier(p, Cacador.healthModifier)) {
                BCDebug.roleplaySout("[Braincraft] Applying CACADOR MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(Cacador.healthModifier);
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                BCDebug.roleplaySout("[Braincraft] Applied CACADOR MAX HEALTH to player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                        "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
            }
        } else {
            resetPlayerMaxHealth(p);
        }
        if (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size() >= 2){
            resetPlayerMaxHealth(p);
            adjustHealthByClass(p);
        }
    }
    public static boolean containsMaxHealthModifier(Player p, AttributeModifier modifier){
        boolean b = false;
        if (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size() > 0) {
            for (AttributeModifier currentModifier :
                    p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers()) {
                if (currentModifier.equals(modifier)) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }
    public static boolean containsMaxHealthModifierByName(Player p, String name){
        boolean b = false;
        if (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size() > 0) {
            for (AttributeModifier currentModifier :
                    p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers()) {
                if (currentModifier.getName().equals(name)) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }

    public static void removeExistingMaxHealthModifier(Player p, AttributeModifier modifier){
        if (containsMaxHealthModifier(p, modifier)){
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
        }
    }
    public static void removeExistingClassMaxHealthModifiers(Player p) {
        removeExistingMaxHealthModifier(p, Druida.healthModifier);
        removeExistingMaxHealthModifier(p, Gatuno.healthModifier);
        removeExistingMaxHealthModifier(p, Ferreiro.healthModifier);
        removeExistingMaxHealthModifier(p, Espadachim.healthModifier);
        removeExistingMaxHealthModifier(p, Mago.healthModifier);
        removeExistingMaxHealthModifier(p, Cacador.healthModifier);
    }
    public static void removeAllExistingMaxHealthModifiers(Player p){
    if (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size() > 0) {
        BCDebug.roleplaySout("[Braincraft] Removing "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size() +
                " modifiers from player: " +p.getName()+ ", with UUID:" +p.getUniqueId());
            for (AttributeModifier modifier :
                    p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers()) {
                removeExistingMaxHealthModifier(p, modifier);
            }
        }
    }
    public static void resetPlayerMaxHealth(Player p){
        BCDebug.roleplaySout("[Braincraft] RESETTING PLAYER MAX HEALTH. Player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
        removeAllExistingMaxHealthModifiers(p);
        if (playerMaxHealthOutOfBounds(p) || playerMaxHealthNotNatural(p)){
            removeAllExistingMaxHealthModifiers(p);
            double currentMaxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (correctors.get(p.getUniqueId()) == null) {
                AttributeModifier playerCorrector = new AttributeModifier("CORRECTOR", (20 - currentMaxHealth), AttributeModifier.Operation.ADD_NUMBER);
                correctors.put(p.getUniqueId(), playerCorrector);
            } else {
                AttributeModifier playerCorrector = new AttributeModifier("CORRECTOR", (20 - (currentMaxHealth - correctors.get(p.getUniqueId()).getAmount())), AttributeModifier.Operation.ADD_NUMBER);
                correctors.put(p.getUniqueId(), playerCorrector);
            }
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(correctors.get(p.getUniqueId()));
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }
        BCDebug.roleplaySout("[Braincraft] PLAYER MAX HEALTH RESET. Player: " +p.getName()+ ", UUID:" +p.getUniqueId() +
                "\n\tValue: " +p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + ", Modifiers: "+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().size());
    }
    public static boolean playerMaxHealthOutOfBounds(Player p){
        return (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() > 60 || p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() < 1 ||
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > 60 || p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < 1);
    }
    public static boolean playerMaxHealthNotNatural(Player p){
        return (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != 20 || p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() != 20);
    }

}
