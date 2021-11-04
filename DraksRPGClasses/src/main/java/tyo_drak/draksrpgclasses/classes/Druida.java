package tyo_drak.draksrpgclasses.classes;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityMountEvent;
import tyo_drak.drakslib.*;
import tyo_drak.draksrpgclasses.Main;

public class Druida extends RPGClass {

    // CHARACTERISTICS
    public static final Enum<ChatColor> CLASS_COLOR = ChatColor.DARK_GREEN;
    public static final Integer BASE_HEALTH = 6;
    public static final PotionEffect[] CLASS_EFFECTS = {new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0),
            new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 99999, 0),
            new PotionEffect(PotionEffectType.WATER_BREATHING, 99999, 0)};
    public static final PotionEffectType[] CLASS_IMMUNITIES = {PotionEffectType.POISON};

    // PERMISSIONS
    public static final String BASE_PERMISSION = "group.druida";

    public static final int DRUIDA_PREVENT_PLANT_DAMAGE_MIN_LEVEL = 3;

    public static void preventPlantDamage(EntityDamageEvent event, Entity damagee) {
        Misc.dLog("Druida.preventPlantDamage() START");
        if (damagee instanceof Player) {
            if (damagee.hasPermission(BASE_PERMISSION) && ((Player) damagee).getLevel() >= DRUIDA_PREVENT_PLANT_DAMAGE_MIN_LEVEL) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)) {
                    event.setCancelled(true);
                }
            }
        }
        Misc.dLog("Druida.preventPlantDamage() END");
    }

    public static final int DRUIDA_3X3_HARVEST_MIN_LEVEL = 7;

    public static void druida3x3Harvest(BlockBreakEvent event, Player player, Block block, World world) {
        Misc.dLog("Druida.druida3x3Harvest() START");
        if (player.hasPermission(Druida.BASE_PERMISSION)) {
            if (Checks.isFarmlandCrop(block.getType())) {
                if (player.getLevel() >= DRUIDA_3X3_HARVEST_MIN_LEVEL) {
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            Block currentBlock = world.getBlockAt(block.getX() - i, block.getY(), block.getZ() - j);
                            druidaHarvest(event, player, world, currentBlock);
                        }
                    }
                } else {
                    druidaHarvest(event, player, world, block);
                }
            }
        }
        Misc.dLog("Druida.druida3x3Harvest() END");
    }

    public static final int DRUIDA_INSTA_HARVEST_MIN_LEVEL = 10;

    public static void instaHarvest(BlockDamageEvent event, Player player, Block block, Material blockType) {
        Misc.dLog("Druida.instaHarvest() START");
        if (!event.isCancelled()) {
            if (player.hasPermission(Druida.BASE_PERMISSION) && player.getLevel() >= DRUIDA_INSTA_HARVEST_MIN_LEVEL) {
                if (Checks.isLeaves(blockType) || Checks.isLog(blockType) || Checks.isBlockCrop(blockType) || Checks.isLog_Wood(blockType)) {
                    if (player.getFoodLevel() > 6) {
                        ItemStack itemMain = player.getInventory().getItemInMainHand();
                        if (Checks.isLeaves(blockType) && !itemMain.getType().equals(Material.SHEARS) && !itemMain.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                            int boneMealDice = Misc.random(15, 50);
                            int boneMealsDropped = boneMealDice / 15;
                            Items.safeDropItem(block.getLocation(), new ItemStack(Material.BONE_MEAL, boneMealsDropped));
                            if (boneMealsDropped >= 1) {
                                if (player.getSaturation() >= boneMealsDropped) {
                                    player.setSaturation(player.getSaturation() - boneMealsDropped);
                                } else {
                                    player.setFoodLevel(player.getFoodLevel() - (1 + boneMealsDropped) / 2);
                                }
                            }
                        } else {
                            player.setFoodLevel(player.getFoodLevel() - 1);
                        }
                        block.breakNaturally(player.getInventory().getItemInMainHand());
                    }
                }
            }
        }
        Misc.dLog("Druida.instaHarvest() END");
    }

    public static final int DRUIDA_BLESSING_MIN_LEVEL = 15;

    public static void druidaBlessing(Player playerInteracting, Entity entity) {
        Misc.dLog("Druida.druidaBlessing() START");
        if (playerInteracting.hasPermission(BASE_PERMISSION) && playerInteracting.getLevel() >= DRUIDA_BLESSING_MIN_LEVEL) {
            if (entity instanceof Player) {
                Player playerInteracted = (Player) entity;
                if (playerInteracted.getHealth() < playerInteracted.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() && playerInteracting.getFoodLevel() > 6) {
                    playerInteracted.setHealth(Math.ceil(Double.parseDouble ("" + playerInteracted.getHealth() + 2)));
                    playerInteracting.setFoodLevel(playerInteracting.getFoodLevel() - 2);
                    Players.acceptPlayerAction(playerInteracting, "DRUIDA HEALING", ChatColor.GREEN + "Você curou " + RPGClass.getPlayerClassColor(playerInteracting) + playerInteracted.getName() + ChatColor.GREEN + "!");
                    Players.acceptPlayerAction(playerInteracted, "DRUIDA HEALING", ChatColor.GREEN + "Você foi curado por "+Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + "!");
                }
                if (playerInteracted.getPotionEffect(PotionEffectType.SLOW) != null) {
                    PotionEffect potionEffect = playerInteracted.getPotionEffect(PotionEffectType.SLOW);
                    playerInteracting.setFoodLevel(playerInteracting.getFoodLevel() - 4 + (2 * (potionEffect.getAmplifier()+1)));
                    Players.acceptPlayerAction(playerInteracting, "DRUIDA HEALING", ChatColor.GREEN + "Você removeu o efeito de Lentidão "+(potionEffect.getAmplifier()+1)+" de " + getPlayerClassColor(playerInteracting) + playerInteracted.getName() + ChatColor.GREEN + "!");
                    Players.acceptPlayerAction(playerInteracted, "DRUIDA HEALING", Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " removeu o efeito de Lentidão "+(potionEffect.getAmplifier()+1)+" de você!");
                }
            }
        }
        Misc.dLog("Druida.druidaBlessing() END");
    }

    public static final int DRUIDA_STRIDER_MOUNT_MIN_LEVEL = 25;

    public static void mountStrider(EntityMountEvent event) {
        Misc.dLog("Druida.mountStrider() START");
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getMount() instanceof Strider) {
                Strider strider = (Strider) event.getMount();
                if (player.hasPermission(Druida.BASE_PERMISSION)) {
                    if (player.getLevel() >= DRUIDA_STRIDER_MOUNT_MIN_LEVEL) {
                        strider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(2);
                        Entities.setBaseMaxHealth(strider, 2);
                    } else {
                        event.setCancelled(true);
                        Players.denyPlayerAction(player, "NOT_ENOUGH_LEVEL_STRIDER_MOUNT", ChatColor.DARK_RED + "Você não possui nível suficiente para montar um Strider!");
                    }
                } else {
                    event.setCancelled(true);
                    Players.denyPlayerAction(player, "NOT_DRUIDA_STRIDER_MOUNT", ChatColor.DARK_RED + "Apenas " + Druida.CLASS_COLOR + "Druidas" + ChatColor.DARK_RED + " podem montar Striders!");
                }
            }
        }
        Misc.dLog("Druida.mountStrider() END");
    }

    public static void druidaHarvest(BlockBreakEvent event, Player player, World world, Block currentBlock) {
        Misc.dLog("Druida.druidaHarvest() START");
        if (Checks.isMatureCrop(currentBlock) &&
                !(currentBlock.getType().equals(Material.MELON_STEM) ||
                        currentBlock.getType().equals(Material.PUMPKIN_STEM) ||
                        currentBlock.getType().equals(Material.ATTACHED_MELON_STEM) ||
                        currentBlock.getType().equals(Material.ATTACHED_PUMPKIN_STEM))) {
            int expGenerated = 4;
            ExperienceOrb experienceOrb = (ExperienceOrb) world.spawnEntity(currentBlock.getLocation(), EntityType.EXPERIENCE_ORB);
            for (ItemStack item :
                    currentBlock.getDrops(player.getInventory().getItemInMainHand())) {
                world.dropItem(currentBlock.getLocation(), item);
                expGenerated++;
            }
            Ageable ageableCurrentBlock = ((Ageable) currentBlock.getBlockData());
            ageableCurrentBlock.setAge(0);
            currentBlock.setBlockData(ageableCurrentBlock);
            experienceOrb.setExperience(expGenerated / 2);
            event.setCancelled(true);
        }
        Misc.dLog("Druida.druidaHarvest() END");
    }

    public static void natureFertility(EntityBreedEvent event, int dice) {
        Misc.dLog("Druida.natureFertility() START");
        if (event.getBreeder() instanceof Player) {
            if (event.getEntity() instanceof Animals) {
                Animals mother = (Animals) event.getMother();
                if (event.getBreeder().hasPermission(BASE_PERMISSION)) {
                    if (dice == 0) {
                        event.getEntity().setHealth(0);
                    } else if (dice == 4 || dice == 5) {
                        ((Animals) Entities.duplicate(event.getMother())).setBaby();
                    } else {
                        ((Animals) Entities.duplicate(event.getMother())).setBaby();
                        ((Animals) Entities.duplicate(event.getMother())).setBaby();
                        ((Animals) Entities.duplicate(event.getMother())).setBaby();
                        Entities.applyEffectShiny(mother, PotionEffectType.POISON, 99999, 9);
                        Entities.applyEffectShiny(mother, PotionEffectType.SLOW, 99999, 9);
                        Entities.applyEffectShiny(mother, PotionEffectType.CONFUSION, 99999, 9);
                        Entities.applyEffectShiny(mother, PotionEffectType.BLINDNESS, 99999, 9);
                        Entities.applyEffectShiny(mother, PotionEffectType.WEAKNESS, 99999, 9);
                        Entities.applyEffectShiny(mother, PotionEffectType.HUNGER, 99999, 9);
                        event.getBreeder().sendMessage(Druida.CLASS_COLOR + "" + ChatColor.ITALIC + "Você vê que o animal está sofrendo com o parto. Você sabe que sacrificá-lo é a melhor opção.");
                    }
                    event.setExperience((event.getExperience() * 2) * (1 + dice));
                } else {
                    ((Animals) event.getFather()).setLoveModeTicks(0);
                    ((Animals) event.getMother()).setLoveModeTicks(0);
                    Players.denyPlayerAction((Player) event.getBreeder(), "NOT_DRUIDA_BREED", ChatColor.DARK_RED + "Os animais não se sentem à vontade para dar à luz com você por perto! Talvez um " + Druida.CLASS_COLOR + "Druida" + ChatColor.DARK_RED + " possa te ajudar.");
                    event.setCancelled(true);
                }
            }
        }
        Misc.dLog("Druida.natureFertility() END");
    }

    public static void greenBlood(EntityTargetEvent event) {
        Misc.dLog("Druida.greenBlood() START");
        if (!event.isCancelled()) {
            if (event.getTarget() instanceof Player) {
                Player player = (Player) event.getTarget();
                if (event.getEntity().getType().equals(EntityType.CAVE_SPIDER) ||
                        event.getEntity().getType().equals(EntityType.SPIDER)) {
                    if (player.hasPermission(BASE_PERMISSION)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        Misc.dLog("Druida.greenBlood() END");
    }

    public static void sacrificeAnimal(Player playerInteracting, Entity entity, Animals animal) {
        Misc.dLog("Druida.sacrificeAnimal() START");
        if (Checks.isSacrificeable(animal) && playerInteracting.hasPermission(Druida.BASE_PERMISSION) && (playerInteracting.getInventory().getItemInMainHand().getType().equals(Material.AIR) || playerInteracting.getInventory().getItemInOffHand().getType().equals(Material.AIR))) {
            playerInteracting.sendMessage(Druida.CLASS_COLOR + "" + ChatColor.ITALIC + "Você se concentra e, ao tocá-lo, toda a agonia e sofrimento que ele sentia são dissipados. O animal te olha nos olhos, quase como se agradecesse.");
            animal.getActivePotionEffects().clear();
            playerInteracting.playSound(playerInteracting.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 10, 2);
            entity.playEffect(EntityEffect.TOTEM_RESURRECT);
            animal.setHealth(0);
            Druida.giveBreedableLoot(playerInteracting, animal, Misc.random(1, 100));
        }
        Misc.dLog("Druida.sacrificeAnimal() END");
    }

    public static void applyImmunities(EntityPotionEffectEvent event) {
        Misc.dLog("Druida.applyImmunities() START");
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (event.getNewEffect() != null) {
                if (Main.config.getBoolean("RPG_CLASSES")) {
                    if (player.hasPermission(Druida.BASE_PERMISSION)) {
                        for (PotionEffectType immunity :
                                Druida.CLASS_IMMUNITIES) {
                            if (event.getNewEffect().getType().equals(immunity)) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
        Misc.dLog("Druida.applyImmunities() END");
    }

    public static void preventDruidaHurtAnimals(EntityDamageByEntityEvent event) {
        Misc.dLog("Druida.preventDruidaHurtAnimals() START");
        if (!event.isCancelled()) {
            if (event.getDamager() instanceof Player) {
                Player playerDamager = (Player) event.getDamager();
                if (Checks.isDruidaFriend(event.getEntity().getType()) && playerDamager.hasPermission(Druida.BASE_PERMISSION)) {
                    event.setCancelled(true);
                    Players.denyPlayerAction(playerDamager, "IS_DRUIDA_FRIEND", Druida.CLASS_COLOR + "" + ChatColor.ITALIC + "Você sente que seria errado ferir esta criatura.");
                }
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    Player playerShooter = (Player) projectile.getShooter();
                    if (Checks.isDruidaFriend(event.getEntity().getType()) && playerShooter.hasPermission(Druida.BASE_PERMISSION)) {
                        event.setCancelled(true);
                        Players.denyPlayerAction(playerShooter, "IS_DRUIDA_FRIEND", Druida.CLASS_COLOR + "" + ChatColor.ITALIC + "Você sente que seria errado ferir esta criatura.");
                    }
                }
            }
        }
        Misc.dLog("Druida.preventDruidaHurtAnimals() END");
    }

    public static void giveBreedableLoot(Player playerInteracting, Animals animal, int d100) {
        Misc.dLog("Druida.giveBreedableLoot() START");
        switch (animal.getType()) {
            case HORSE:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.LEATHER, 16));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 8));
                if (d100 == 100) {
                    playerInteracting.getInventory().addItem(new ItemStack(Material.HORSE_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case DONKEY:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.LEATHER, 16));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 8));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.DONKEY_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case SHEEP:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.MUTTON, 4));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 4));
                if (!((Sheep) animal).isSheared()) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.WHITE_WOOL, 8));
                }
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.SHEEP_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case COW:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BEEF, 6));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.LEATHER, 8));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 4));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.COW_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case MUSHROOM_COW:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BEEF, 4));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.LEATHER, 4));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BROWN_MUSHROOM, 4));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.RED_MUSHROOM, 4));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 4));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.MOOSHROOM_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case PIG:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.PORKCHOP, 8));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 4));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.PIG_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case CHICKEN:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.CHICKEN, 2));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.FEATHER, 8));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 2));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.CHICKEN_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case WOLF:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 4));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.WOLF_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case CAT:
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.CAT_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case OCELOT:
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.OCELOT_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case RABBIT:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.RABBIT_FOOT, 2));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.RABBIT_HIDE, 2));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.RABBIT, 2));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.RABBIT_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case LLAMA:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.LEATHER, 2));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.WHITE_WOOL, 4));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BEEF, 2));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 4));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.LLAMA_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case TRADER_LLAMA:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.LEATHER, 2));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.WHITE_WOOL, 4));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BEEF, 2));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 4));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.TRADER_LLAMA_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case TURTLE:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.SCUTE, 1));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.TURTLE_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case PANDA:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BAMBOO, 4));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BEEF, 4));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.PANDA_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case FOX:
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.FOX_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case BEE:
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BEE_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case HOGLIN:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.LEATHER, 8));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.PORKCHOP, 8));
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.BONE, 8));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.HOGLIN_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
            case STRIDER:
                animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.STRING, 8));
                if (d100 == 100) {
                    animal.getWorld().dropItem(animal.getLocation(), new ItemStack(Material.STRIDER_SPAWN_EGG, 1));
                    playerInteracting.getServer().broadcastMessage(ChatColor.GREEN + "O jogador " + Druida.CLASS_COLOR + playerInteracting.getName() + ChatColor.GREEN + " conseguiu um Ovo de Spawn de Animal!");
                }
                break;
        }
        Misc.dLog("Druida.giveBreedableLoot() END");
    }

    public static void preventFarmlandTrample(PlayerInteractEvent event, Player player) {
        Misc.dLog("Druida.preventFarmlandTrample() START");
        if (!(event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)) {
            event.setCancelled(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.FARMLAND && player.hasPermission(Druida.BASE_PERMISSION));
        }
        Misc.dLog("Druida.preventFarmlandTrample() END");
    }

}
