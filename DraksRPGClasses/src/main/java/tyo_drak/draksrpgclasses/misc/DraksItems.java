package tyo_drak.draksrpgclasses.misc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tyo_drak.draksrpgclasses.Checks;
import tyo_drak.draksrpgclasses.Debug;
import tyo_drak.draksrpgclasses.MainEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DraksItems {

    //<editor-fold defaultstate="" desc="ITEM SACKS">
    /*
    public static void unsackItemWithMetaAndLore (Player player, ItemStack sack) {
        List<String> sackLore = sack.getItemMeta().getLore();

        ItemStack sackedItem = new ItemStack(Material.AIR, 1);
        ItemMeta sackedItemMeta = sackedItem.getItemMeta();
        ArrayList<String> sackedItemLore = new ArrayList<>();

        String sackedItemAmountAndName = sack.getItemMeta().getDisplayName().replace("Baú com 9x ", "");

        // SPLIT AMOUNT AND NAME
        String[] sackedItemAmountAndNameArray = sackedItemAmountAndName.split(" ", 2);
        Material sackedItemType = nameToMaterial(sackedItemAmountAndNameArray[1]);
        int sackedItemAmount = Integer.parseInt(sackedItemAmountAndNameArray[0]);
        sackedItem.setType(sackedItemType);
        sackedItem.setAmount(sackedItemAmount);

        sackedItemMeta.get

        for (int i = 0; i < sackLore.size(); i++) {
            if (sackLore.get(i).equals("----------------")){
                for (int j = i+1; sackLore.get(j).equals("----------------"); j++) {
                    sackedItemLore.add(sackLore.get(j));
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            player.getInventory().addItem(sackedItem);
        }
        player.getInventory().addItem();
    }
    */
    /*
    public static ItemStack getItemSackWithMetaAndLore(ItemStack firstItem) {
        ItemStack itemSack = new ItemStack(Material.CHEST, 1);
        itemSack.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        ItemMeta itemSackMeta = itemSack.getItemMeta();
        ArrayList<String> itemSackLore = new ArrayList<>();

        itemSackMeta.setDisplayName("Baú com 9x " + firstItem.getAmount() + " " + keyToName(firstItem.getType().toString()));
        itemSackLore.add("Baú de itens compactados.");
        itemSackLore.add("----------------");
        if (firstItem.getItemMeta() != null && firstItem.getItemMeta().getLore() != null) {
            for (String line :
                    firstItem.getItemMeta().getLore()) {
                itemSackLore.add(line);
            }
        }
        itemSackLore.add("----------------");

        itemSackMeta.setLore(itemSackLore);
        itemSack.setItemMeta(itemSackMeta);
        DraksItems.addUndroppableTag(itemSack);
        DraksItems.addUnplaceableTag(itemSack);


        minDelayCrystal.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 1);
        ItemMeta minDelayCrystalMeta = minDelayCrystal.getItemMeta();
        ArrayList<String> minDelayCrystalLore = new ArrayList<>();
        minDelayCrystalMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Cristal da Velocidade");
        minDelayCrystalLore.add("Diminui o tempo mínimo do Spawner.");
        minDelayCrystalMeta.setLore(minDelayCrystalLore);
        minDelayCrystal.setItemMeta(minDelayCrystalMeta);
        DraksItems.addUndroppableTag(minDelayCrystal);
        return null;
    }
    */
    /*
    public static void unsackSimpleItem(Player player, ItemStack sack) {
        // SPLIT AMOUNT AND NAME
        String sackedItemAmountAndName = sack.getItemMeta().getLore().get(0).replace("Baú com 8x ", "");
        String[] sackedItemAmountAndNameArray = sackedItemAmountAndName.split(" ", 2);
        Material sackedItemType = nameToMaterial(sackedItemAmountAndNameArray[1]);
        int sackedItemAmount = Integer.parseInt(sackedItemAmountAndNameArray[0]);

        ItemStack sackedItem = new ItemStack(sackedItemType, sackedItemAmount);

        sack.setAmount(sack.getAmount() - 1);
        for (int i = 0; i < 8; i++) {
            player.getInventory().addItem(sackedItem);
        }
        player.getInventory().addItem(new ItemStack(Material.CHEST, 1));
    }*/
    /*
    public static ItemStack getSimpleItemSack(ItemStack simpleItem) {
        ItemStack itemSack = new ItemStack(Material.CHEST, 1);
        itemSack.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        ItemMeta itemSackMeta = itemSack.getItemMeta();
        ArrayList<String> itemSackLore = new ArrayList<>();

        itemSackMeta.setDisplayName("Baú de itens simples. ("+keyToName(simpleItem.getType().toString())+")");
        itemSackLore.add("Baú com 8x " + simpleItem.getAmount() + " " + keyToName(simpleItem.getType().toString()));

        itemSackMeta.setLore(itemSackLore);
        itemSack.setItemMeta(itemSackMeta);

        DraksItems.addUndroppableTag(itemSack);
        DraksItems.addUnplaceableTag(itemSack);

        return itemSack;
    }*/

    //<editor-fold defaultstate="" desc="GET_ITEM_TEMPLATE">
    /*
    public static ItemStack getITEMSTACK_NAME() {
        ItemStack ITEMSTACK_NAME = new ItemStack(Material.MATERIAL, AMOUNT);
        ITEMSTACK_NAME.addUnsafeEnchantment(Enchantment.ENCHANTMENT, LEVEL);
        ItemMeta ITEMSTACK_NAMEMeta = ITEMSTACK_NAME.getItemMeta();
        ArrayList<String> ITEMSTACK_NAMELore = new ArrayList<>();
        //noinspection ConstantConditions
        ITEMSTACK_NAMEMeta.setDisplayName(ITEM_NAME);
        ITEMSTACK_NAMELore.add(ITEM_LORE);
        ITEMSTACK_NAMEMeta.setLore(ITEMSTACK_NAMELore);
        ITEMSTACK_NAME.setItemMeta(ITEMSTACK_NAMEMeta);
        DraksItems.addUndroppableTag(ITEMSTACK_NAME);
        return ITEMSTACK_NAME;
    }
    */
    //</editor-fold>


    public static ItemStack getMinDelayCrystal() {
        ItemStack minDelayCrystal = new ItemStack(Material.EMERALD, 1);
        minDelayCrystal.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 1);
        ItemMeta minDelayCrystalMeta = minDelayCrystal.getItemMeta();
        ArrayList<String> minDelayCrystalLore = new ArrayList<>();
        minDelayCrystalMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Cristal da Velocidade");
        minDelayCrystalLore.add("Diminui o tempo mínimo do Spawner.");
        minDelayCrystalMeta.setLore(minDelayCrystalLore);
        minDelayCrystal.setItemMeta(minDelayCrystalMeta);
        DraksItems.addUndroppableTag(minDelayCrystal);
        return minDelayCrystal;
    }

    public static ItemStack getMaxDelayCrystal() {
        ItemStack maxDelayCrystal = new ItemStack(Material.EMERALD, 1);
        maxDelayCrystal.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 1);
        ItemMeta maxDelayCrystalMeta = maxDelayCrystal.getItemMeta();
        ArrayList<String> maxDelayCrystalLore = new ArrayList<>();
        maxDelayCrystalMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Cristal da Rapidez");
        maxDelayCrystalLore.add("Diminui o tempo máximo do Spawner.");
        maxDelayCrystalMeta.setLore(maxDelayCrystalLore);
        maxDelayCrystal.setItemMeta(maxDelayCrystalMeta);
        DraksItems.addUndroppableTag(maxDelayCrystal);
        return maxDelayCrystal;
    }

    public static ItemStack getPlayerRangeCrystal() {
        ItemStack playerRangeCrystal = new ItemStack(Material.EMERALD, 1);
        playerRangeCrystal.addUnsafeEnchantment(Enchantment.LOYALTY, 1);
        ItemMeta playerRangeCrystalMeta = playerRangeCrystal.getItemMeta();
        ArrayList<String> playerRangeCrystalLore = new ArrayList<>();
        playerRangeCrystalMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Cristal do Alcance");
        playerRangeCrystalLore.add("Aumenta a distância mínima do Jogador pro Spawner.");
        playerRangeCrystalMeta.setLore(playerRangeCrystalLore);
        playerRangeCrystal.setItemMeta(playerRangeCrystalMeta);
        DraksItems.addUndroppableTag(playerRangeCrystal);
        return playerRangeCrystal;
    }

    public static ItemStack getSpawnEgg(Entity entity) {
        ItemStack egg = new ItemStack(Material.EGG);
        switch (entity.getType()) {
            case VEX:
                egg = new ItemStack(Material.VEX_SPAWN_EGG);
                break;
            case HUSK:
                egg = new ItemStack(Material.HUSK_SPAWN_EGG);
                break;
            case SLIME:
                egg = new ItemStack(Material.SLIME_SPAWN_EGG);
                break;
            case STRAY:
                egg = new ItemStack(Material.STRAY_SPAWN_EGG);
                break;
            case WITCH:
                egg = new ItemStack(Material.WITCH_SPAWN_EGG);
                break;
            case BLAZE:
                egg = new ItemStack(Material.BLAZE_SPAWN_EGG);
                break;
            case GHAST:
                egg = new ItemStack(Material.GHAST_SPAWN_EGG);
                break;
            case SPIDER:
                egg = new ItemStack(Material.SPIDER_SPAWN_EGG);
                break;
            case PIGLIN:
                egg = new ItemStack(Material.PIGLIN_SPAWN_EGG);
                break;
            case EVOKER:
                egg = new ItemStack(Material.EVOKER_SPAWN_EGG);
                break;
            case HOGLIN:
                egg = new ItemStack(Material.HOGLIN_SPAWN_EGG);
                break;
            case ZOGLIN:
                egg = new ItemStack(Material.ZOGLIN_SPAWN_EGG);
                break;
            case ZOMBIE:
                egg = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
                break;
            case CREEPER:
                egg = new ItemStack(Material.CREEPER_SPAWN_EGG);
                break;
            case DROWNED:
                egg = new ItemStack(Material.DROWNED_SPAWN_EGG);
                break;
            case PHANTOM:
                egg = new ItemStack(Material.PHANTOM_SPAWN_EGG);
                break;
            case RAVAGER:
                egg = new ItemStack(Material.RAVAGER_SPAWN_EGG);
                break;
            case SHULKER:
                egg = new ItemStack(Material.SHULKER_SPAWN_EGG);
                break;
            case GUARDIAN:
                egg = new ItemStack(Material.GUARDIAN_SPAWN_EGG);
                break;
            case PILLAGER:
                egg = new ItemStack(Material.PILLAGER_SPAWN_EGG);
                break;
            case SKELETON:
                egg = new ItemStack(Material.SKELETON_SPAWN_EGG);
                break;
            case ENDERMAN:
                egg = new ItemStack(Material.ENDERMAN_SPAWN_EGG);
                break;
            case ENDERMITE:
                egg = new ItemStack(Material.ENDERMITE_SPAWN_EGG);
                break;
            case MAGMA_CUBE:
                egg = new ItemStack(Material.MAGMA_CUBE_SPAWN_EGG);
                break;
            case SILVERFISH:
                egg = new ItemStack(Material.SILVERFISH_SPAWN_EGG);
                break;
            case VINDICATOR:
                egg = new ItemStack(Material.VINDICATOR_SPAWN_EGG);
                break;
            case CAVE_SPIDER:
                egg = new ItemStack(Material.CAVE_SPIDER_SPAWN_EGG);
                break;
        /*  case PIGLIN_BRUTE:
                egg = new ItemStack(Material.PIGLIN_BRUTE_SPAWN_EGG);
                break; */
            case ELDER_GUARDIAN:
                egg = new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG);
                break;
            case WITHER_SKELETON:
                egg = new ItemStack(Material.WITHER_SKELETON_SPAWN_EGG);
                break;
            case ZOMBIE_VILLAGER:
                egg = new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG);
                break;
            case ZOMBIFIED_PIGLIN:
                egg = new ItemStack(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG);
                break;
        }
        return egg;
    }

    public static ItemStack getWitherHead() {
        //<editor-fold defaultstate="" desc="CHANGING ITEM NAME">
        /*
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(NEW_NAME);
        item.setItemMeta(itemMeta);
        */
        //</editor-fold>
        //<editor-fold defaultstate="" desc="CHANGING ITEM LORE">
        /*
        ArrayList<String> witherHeadLore = new ArrayList<>();
        witherHeadLore.add(LINE_1);
        witherHeadLore.add(LINE_2);
        witherHeadLore.add(LINE_3);
        Objects.requireNonNull(item).setLore(itemLore);
        */
        //</editor-fold>

        ItemStack witherHead = new ItemStack(Material.WITHER_SKELETON_SKULL, 3);
        ItemMeta witherHeadMeta = witherHead.getItemMeta();
        ArrayList<String> witherHeadLore = new ArrayList<>();
        Objects.requireNonNull(witherHeadMeta).setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Crânio do Decaimento");
        witherHeadLore.add(ChatColor.DARK_GRAY + "A prova de que a lendária");
        witherHeadLore.add(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Ameaça do Decaimento");
        witherHeadLore.add(ChatColor.DARK_GRAY + "foi derrotada por um valoroso guerreiro.");
        Objects.requireNonNull(witherHeadMeta).setLore(witherHeadLore);
        witherHead.setItemMeta(witherHeadMeta);
        addUnplaceableTag(witherHead);
        addUndroppableTag(witherHead);
        return witherHead;
    }

    public static ItemStack getBalrogSkull() {
        //<editor-fold defaultstate="" desc="CHANGING ITEM NAME">
        /*
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(NEW_NAME);
        item.setItemMeta(itemMeta);
        */
        //</editor-fold>
        //<editor-fold defaultstate="" desc="CHANGING ITEM LORE">
        /*
        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add(LINE_1);
        itemLore.add(LINE_2);
        itemLore.add(LINE_3);
        item.getItemMeta().setLore(itemLore);
        */
        //</editor-fold>

        ItemStack balrogSkull = new ItemStack(Material.SKELETON_SKULL, 1);
        ItemMeta balrogSkullMeta = balrogSkull.getItemMeta();
        ArrayList<String> balrogSkullLore = new ArrayList<>();
        Objects.requireNonNull(balrogSkullMeta).setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Crânio de Balrog");
        balrogSkullLore.add(ChatColor.RED + "O Crânio de um");
        balrogSkullLore.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Balrog");
        balrogSkullLore.add(ChatColor.RED + "das profundezas do inferno.");
        Objects.requireNonNull(balrogSkullMeta).setLore(balrogSkullLore);
        balrogSkull.setItemMeta(balrogSkullMeta);
        addUnplaceableTag(balrogSkull);
        addUndroppableTag(balrogSkull);
        return balrogSkull;
    }

    public static ItemStack getSpawnerItemStack(CreatureSpawner spawnerBlock) {
        ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
        ItemMeta spawnerMeta = spawner.getItemMeta();
        ArrayList<String> spawnerLore = new ArrayList<>();
        if (spawnerBlock != null) {
            if (spawnerBlock.getSpawnedType().equals(EntityType.ARMOR_STAND)) {
                Objects.requireNonNull(spawnerMeta).setDisplayName(
                        ChatColor.YELLOW + "Empty" +
                                ChatColor.DARK_AQUA + " Spawner");
                spawnerLore.add("Spawned Type: " + "None");
            } else {
                Objects.requireNonNull(spawnerMeta).setDisplayName(
                        ChatColor.YELLOW + DraksItems.keyToName(spawnerBlock.getSpawnedType().toString()) +
                                ChatColor.DARK_AQUA + " Spawner");
                spawnerLore.add("Spawned Type: " + DraksItems.keyToName(spawnerBlock.getSpawnedType().toString()));
            }
            spawnerLore.add("Spawn Count: " + spawnerBlock.getSpawnCount());
            spawnerLore.add("Spawn Range: " + spawnerBlock.getSpawnRange());
            spawnerLore.add("Required Player Range: " + spawnerBlock.getRequiredPlayerRange());
            spawnerLore.add("Max Nearby Entities: " + spawnerBlock.getMaxNearbyEntities());
            spawnerLore.add("Min Spawn Delay: " + spawnerBlock.getMinSpawnDelay());
            spawnerLore.add("Max Spawn Delay: " + spawnerBlock.getMaxSpawnDelay());
        } else {
            Objects.requireNonNull(spawnerMeta).setDisplayName(
                    ChatColor.YELLOW + "Empty" +
                            ChatColor.DARK_AQUA + " Spawner");
            spawnerLore.add("Spawned Type: " + "None");
            spawnerLore.add("Spawn Count: " + 4);
            spawnerLore.add("Spawn Range: " + 4);
            spawnerLore.add("Required Player Range: " + 16);
            spawnerLore.add("Max Nearby Entities: " + 6);
            spawnerLore.add("Min Spawn Delay: " + 200);
            spawnerLore.add("Max Spawn Delay: " + 800);
        }
        Objects.requireNonNull(spawnerMeta).setLore(spawnerLore);
        spawner.setItemMeta(spawnerMeta);
        DraksItems.addUndroppableTag(spawner);
        return spawner;
    }

    public static ItemStack getSpiritualIronBars() {
        ItemStack spiritualIronBars = new ItemStack(Material.IRON_BARS, 1);
        spiritualIronBars.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        spiritualIronBars.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 1);
        ItemMeta spiritualIronBarsMeta = spiritualIronBars.getItemMeta();
        ArrayList<String> spiritualIronBarsLore = new ArrayList<>();
        spiritualIronBarsMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Grade de Ferro Espiritual");
        spiritualIronBarsLore.add("Retém almas nas grades para uso futuro.");
        spiritualIronBarsMeta.setLore(spiritualIronBarsLore);
        spiritualIronBars.setItemMeta(spiritualIronBarsMeta);
        DraksItems.addUndroppableTag(spiritualIronBars);
        return spiritualIronBars;
    }

    public static ItemStack getSparkingCondensedSoulSand() {
        ItemStack sparkingCondensedSoulSand = new ItemStack(Material.SOUL_SAND, 1);
        sparkingCondensedSoulSand.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 3);
        ItemMeta sparkingCondensedSoulSandMeta = sparkingCondensedSoulSand.getItemMeta();
        ArrayList<String> sparkingCondensedSoulSandLore = new ArrayList<>();
        sparkingCondensedSoulSandMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Areia de Alma Condensada Faiscante");
        sparkingCondensedSoulSandLore.add("As faíscas parecem soar como vozes,");
        sparkingCondensedSoulSandLore.add("desesperadas para sair vagueando.");
        sparkingCondensedSoulSandMeta.setLore(sparkingCondensedSoulSandLore);
        sparkingCondensedSoulSand.setItemMeta(sparkingCondensedSoulSandMeta);
        DraksItems.addUndroppableTag(sparkingCondensedSoulSand);
        return sparkingCondensedSoulSand;
    }

    public static ItemStack getCondensedGoldNugget() {
        ItemStack condensedGoldNugget = new ItemStack(Material.GOLD_NUGGET, 1);
        condensedGoldNugget.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta condensedGoldNuggetMeta = condensedGoldNugget.getItemMeta();
        ArrayList<String> condensedGoldNuggetLore = new ArrayList<>();
        condensedGoldNuggetMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Pepita de Ouro Condensado");
        condensedGoldNuggetLore.add("Criada a partir de 9 Blocos de Ouro.");
        condensedGoldNuggetMeta.setLore(condensedGoldNuggetLore);
        condensedGoldNugget.setItemMeta(condensedGoldNuggetMeta);
        DraksItems.addUndroppableTag(condensedGoldNugget);
        return condensedGoldNugget;
    }

    public static ItemStack getCondensedEmeraldGem() {
        ItemStack condensedEmeraldGem = new ItemStack(Material.EMERALD, 1);
        condensedEmeraldGem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta condensedEmeraldGemMeta = condensedEmeraldGem.getItemMeta();
        ArrayList<String> condensedEmeraldGemLore = new ArrayList<>();
        condensedEmeraldGemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gema de Esmeralda Condensada");
        condensedEmeraldGemLore.add("Criada a partir de 9 Blocos de Esmeralda.");
        condensedEmeraldGemMeta.setLore(condensedEmeraldGemLore);
        condensedEmeraldGem.setItemMeta(condensedEmeraldGemMeta);
        DraksItems.addUndroppableTag(condensedEmeraldGem);
        return condensedEmeraldGem;
    }

    public static ItemStack getPlayerEXPBottle(int levels, int playerEXP) {
        ItemStack playerEXPBottle = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        playerEXPBottle.addUnsafeEnchantment(Enchantment.MENDING, 1);
        ItemMeta playerEXPBottleMeta = playerEXPBottle.getItemMeta();
        ArrayList<String> playerEXPBottleLore = new ArrayList<>();
        playerEXPBottleMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Garrafa de Experiência de Jogador (" + levels + "L)");
        playerEXPBottleLore.add("Concede " + playerEXP + " pontos de experiência ao ser consumida.");
        playerEXPBottleLore.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + ChatColor.ITALIC + "OLHE PARA ALGUM BLOCO AO USAR!");
        playerEXPBottleMeta.setLore(playerEXPBottleLore);
        playerEXPBottle.setItemMeta(playerEXPBottleMeta);
        return playerEXPBottle;
    }

    public static ItemStack getPlayerSpawnPointApple() {
        ItemStack playerSpawnPointApple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        playerSpawnPointApple.addUnsafeEnchantment(Enchantment.LOYALTY, 1);
        ItemMeta playerSpawnPointAppleMeta = playerSpawnPointApple.getItemMeta();
        ArrayList<String> playerSpawnPointAppleLore = new ArrayList<>();
        //noinspection ConstantConditions
        playerSpawnPointAppleMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Maçã do Retorno");
        playerSpawnPointAppleLore.add("Coma para definir seu Ponto de Retorno na posição atual.");
        playerSpawnPointAppleMeta.setLore(playerSpawnPointAppleLore);
        playerSpawnPointApple.setItemMeta(playerSpawnPointAppleMeta);
        DraksItems.addUndroppableTag(playerSpawnPointApple);
        return playerSpawnPointApple;
    }

    public static ItemStack getBlankScroll() {
        ItemStack blankScroll = new ItemStack(Material.PAPER, 1);
        ItemMeta blankScrollMeta = blankScroll.getItemMeta();
        ArrayList<String> blankScrollLore = new ArrayList<>();
        //noinspection ConstantConditions
        blankScrollMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Pergaminho em Branco");
        blankScrollLore.add("Usado para criar pergaminhos de feitiços.");
        blankScrollMeta.setLore(blankScrollLore);
        blankScroll.setItemMeta(blankScrollMeta);
        DraksItems.addUndroppableTag(blankScroll);
        return blankScroll;
    }

    public static ItemStack getTeleportationPearl() {
        ItemStack teleportationPearl = new ItemStack(Material.ENDER_PEARL, 1);
        teleportationPearl.addUnsafeEnchantment(Enchantment.MENDING, 1);
        ItemMeta teleportationPearlMeta = teleportationPearl.getItemMeta();
        ArrayList<String> teleportationPearlLore = new ArrayList<>();
        //noinspection ConstantConditions
        teleportationPearlMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pérola de Teleporte");
        teleportationPearlLore.add("Usado para criar itens mágicos relacionados a teletransporte.");
        teleportationPearlMeta.setLore(teleportationPearlLore);
        teleportationPearl.setItemMeta(teleportationPearlMeta);
        DraksItems.addUndroppableTag(teleportationPearl);
        return teleportationPearl;
    }

    public static ItemStack getTeleportScroll() {
        ItemStack teleportScroll = new ItemStack(Material.PAPER, 1);
        teleportScroll.addUnsafeEnchantment(Enchantment.LOYALTY, 1);
        ItemMeta teleportScrollMeta = teleportScroll.getItemMeta();
        ArrayList<String> teleportScrollLore = new ArrayList<>();
        //noinspection ConstantConditions
        teleportScrollMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pergaminho de Teleporte");
        teleportScrollLore.add("Teleporta o usuário para a localização-alvo num piscar de olhos.");
        teleportScrollLore.add(ChatColor.DARK_RED + "Botão ESQUERDO (O| ) para salvar a localização atual.");
        teleportScrollLore.add(ChatColor.DARK_RED + "Botão DIREITO ( |O) para teleportar à localização-alvo.");
        teleportScrollLore.add("Localização-alvo: Nenhuma");
        teleportScrollMeta.setLore(teleportScrollLore);
        teleportScroll.setItemMeta(teleportScrollMeta);
        DraksItems.addUndroppableTag(teleportScroll);
        return teleportScroll;
    }

    public static ItemStack getScrollOfResurrection() {
        ItemStack scrollOfResurrection = new ItemStack(Material.PAPER, 1);
        scrollOfResurrection.addUnsafeEnchantment(Enchantment.MENDING, 1);
        ItemMeta scrollOfResurrectionMeta = scrollOfResurrection.getItemMeta();
        ArrayList<String> scrollOfResurrectionLore = new ArrayList<>();
        //noinspection ConstantConditions
        scrollOfResurrectionMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Pergaminho da Ressurreição");
        scrollOfResurrectionLore.add("Permite a ressurreição prematura de um companheiro.");
        scrollOfResurrectionMeta.setLore(scrollOfResurrectionLore);
        scrollOfResurrection.setItemMeta(scrollOfResurrectionMeta);
        DraksItems.addUndroppableTag(scrollOfResurrection);
        return scrollOfResurrection;
    }
    //</editor-fold>

    //<editor-fold defaultstate="" desc="FUNCTIONS">
    public static boolean compareLore(ItemStack item1, ItemStack item2) {
        if (hasLore(item1) && hasLore(item2)) {
            //noinspection ConstantConditions
            return item1.getItemMeta().getLore().equals(item2.getItemMeta().getLore());
        }
        return false;
    }

    public static boolean hasMeta(ItemStack item) {
        return item.getItemMeta() != null;
    }

    public static boolean hasLore(ItemStack item) {
        //noinspection ConstantConditions
        return hasMeta(item) && item.getItemMeta().getLore() != null;
    }

    public static int addEnchantmentsIfType(ItemStack item, Material itemType, Enchantment enchantment, Integer level) {
        if (item.getType().equals(itemType)) {
            if (level >= 1) {
                item.addUnsafeEnchantment(enchantment, level);
            }
            return level;
        } else {
            return 0;
        }
    }

    public static int applyRarity(ItemStack item, int itemQuality) {
        int maxQuality = determineMaxQuality(item);
        ItemMeta itemMeta = item.getItemMeta();
        if (maxQuality >= 0) {
            switch (itemQuality) {
                case 0:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.DARK_GRAY + "(HORRÍVEL) " + ChatColor.DARK_GRAY + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 1:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.GRAY + "(RUIM) " + ChatColor.GRAY + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 2:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.WHITE + "(COMUM) " + ChatColor.WHITE + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 3:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + "(BOM) " + ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 4:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.AQUA + "" + ChatColor.ITALIC + "(ÓTIMO) " + ChatColor.AQUA + "" + ChatColor.ITALIC + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 5:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.ITALIC + "(EXCELENTE) " + ChatColor.DARK_BLUE + "" + ChatColor.ITALIC + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 6:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.BLUE + "" + ChatColor.ITALIC + "(ESTUPENDO) " + ChatColor.BLUE + "" + ChatColor.ITALIC + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 7:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "(IMPECÁVEL) " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 8:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "(MAGNÍFICO) " + ChatColor.GREEN + "" + ChatColor.BOLD + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 9:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "(PERFEITO) " + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 10:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "(BRILHANTE) " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 11:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "(SURREAL) " + ChatColor.DARK_RED + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 12:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "(ÚNICO) " + ChatColor.RED + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 13:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "(OBRA-PRIMA) " + ChatColor.YELLOW + keyToName(item.getType().toString()));
                    item.setItemMeta(itemMeta);
                    break;
                case 14:
                    Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.YELLOW + "" + ChatColor.MAGIC + " .aA" +
                            ChatColor.DARK_RED + "" + ChatColor.MAGIC + "." +
                            ChatColor.YELLOW + "" + ChatColor.MAGIC + "Aa. " +
                            ChatColor.RESET + "" + ChatColor.GOLD + "" + ChatColor.BOLD + "" + "(RELÍQUIA)" +
                            ChatColor.YELLOW + "" + ChatColor.MAGIC + " .aA" +
                            ChatColor.DARK_RED + "" + ChatColor.MAGIC + "1" +
                            ChatColor.YELLOW + "" + ChatColor.MAGIC + "Aa. ");
                    item.setItemMeta(itemMeta);
                    DraksItems.addUndroppableTag(item);
                    break;
                // Calculate Rarity
                // (                         Italic,                                             Bold,                                               Italic+Bold                            Obfuscated)
                // (DARK_GRAY,   GRAY,   WHITE,   DARK_AQUA,   AQUA,     DARK_BLUE,        BLUE,       DARK_GREEN,     GREEN,    DARK_PURPLE,  LIGHT_PURPLE,  DARK_RED,      RED,       YELLOW,        GOLD)
                //  (HORRÍVEL), (RUIM), (COMUM),    (BOM),    (ÓTIMO),   (EXCELENTE),   (ESTUPENDO),  (IMPECÁVEL),  (MAGNÍFICO),  (PERFEITO),  (BRILHANTE),   (SURREAL),   (ÚNICO),  (OBRA-PRIMA),   (RELÍQUIA)
            }
        }
        return itemQuality;
    }

    public static String keyToName(String key) {
        String formattedKey = key.toLowerCase();
        formattedKey = formattedKey.replaceAll("_", " ");
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            if (i == 0 || formattedKey.charAt(i - 1) == ' ') {
                name.append(Character.toUpperCase(formattedKey.charAt(i)));
            } else {
                name.append(formattedKey.charAt(i));
            }
        }
        return name.toString();
    }

    public static EntityType nameToEntityType(String name) {
        name = name.toUpperCase();
        name = name.replaceAll(" ", "_");
        return EntityType.valueOf(name);
    }

    public static Material nameToMaterial(String name) {
        name = name.toUpperCase();
        name = name.replaceAll(" ", "_");
        return Material.valueOf(name);
    }

    public static ItemStack addUndroppableTag(ItemStack item) {
        ArrayList<String> itemLore;
        Debug.consoleMessage("addUndroppable");
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            Debug.consoleMessage("not nulls");
            ItemMeta itemMeta = item.getItemMeta();
            itemLore = (ArrayList<String>) item.getItemMeta().getLore();
            itemLore.add("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Item não derrubável");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            Debug.consoleMessage("lore: " + itemLore);
        } else if (item.getItemMeta() != null && item.getItemMeta().getLore() == null) {
            Debug.consoleMessage("lore null");
            ItemMeta itemMeta = item.getItemMeta();
            itemLore = new ArrayList<>();
            itemLore.add("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Item não derrubável");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            Debug.consoleMessage("lore: " + itemLore);
        }
        return item;
    }

    public static ItemStack addUnplaceableTag(ItemStack item) {
        ArrayList<String> itemLore;
        Debug.consoleMessage("addUnplaceable");
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            Debug.consoleMessage("not nulls");
            ItemMeta itemMeta = item.getItemMeta();
            itemLore = (ArrayList<String>) item.getItemMeta().getLore();
            itemLore.add("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Item não posicionável");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            Debug.consoleMessage("lore: " + itemLore);
        } else if (item.getItemMeta() != null && item.getItemMeta().getLore() == null) {
            Debug.consoleMessage("lore null");
            ItemMeta itemMeta = item.getItemMeta();
            itemLore = new ArrayList<>();
            itemLore.add("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Item não posicionável");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            Debug.consoleMessage("isUndroppable");
        }
        return item;
    }

    public static boolean isCrystal(ItemStack itemMain) {
        return itemMain.getItemMeta() != null && itemMain.getItemMeta().getLore() != null && itemMain.getType().equals(Material.EMERALD);
    }

    public static ItemStack silverfishLootTable(int d100) {
        ItemStack item = new ItemStack(Material.AIR, 0);
        if (d100 >= 53) {
            switch (MainEvents.random(1, 3)) {
                case 1:
                    item = new ItemStack(Material.GRAVEL, 1);
                    break;
                case 2:
                    item = new ItemStack(Material.COBBLESTONE, 1);
                    break;
                case 3:
                    item = new ItemStack(Material.STONE, 1);
                    break;
            } // GRAVEL, COBBLESTONE, STONE
        }
        if (d100 >= 85) {
            switch (MainEvents.random(1, 2)) {
                case 1:
                    item = new ItemStack(Material.IRON_NUGGET, 1);
                    break;
                case 2:
                    item = new ItemStack(Material.GOLD_NUGGET, 1);
                    break;
            } // IRON NUGGET, GOLD NUGGET
        }
        if (d100 >= 93) {
            switch (MainEvents.random(1, 3)) {
                case 1:
                    item = new ItemStack(Material.IRON_INGOT, 1);
                    break;
                case 2:
                    item = new ItemStack(Material.REDSTONE, 1);
                    break;
                case 3:
                    item = new ItemStack(Material.QUARTZ, 1);
                    break;
            } // IRON INGOT, REDSTONE, QUARTZ
        }
        if (d100 >= 97) {
            switch (MainEvents.random(1, 3)) {
                case 1:
                    item = new ItemStack(Material.GOLD_INGOT);
                    break;
                case 2:
                    item = new ItemStack(Material.LAPIS_LAZULI);
                    break;
                case 3:
                    item = new ItemStack(Material.EMERALD);
                    break;
            } // GOLD INGOT, LAPIS LAZULI, EMERALD
        }
        if (d100 == 100) { // DIAMOND
            item = new ItemStack(Material.DIAMOND);
        }
        return item;
    }

    public static int determineMaxQuality(ItemStack item) {
        int maxQuality = -1;
        switch (item.getType()) {

            case STONE_SWORD:
            case STONE_PICKAXE:
            case STONE_SHOVEL:
                maxQuality = 2;
                break;
            case IRON_SWORD:
            case IRON_PICKAXE:
            case IRON_AXE:
                maxQuality = 3;
                break;
            case STONE_AXE:
                maxQuality = 4;
                break;
            case STONE_HOE:
            case CHAINMAIL_HELMET:
            case IRON_SHOVEL:
                maxQuality = 5;
                break;
            case SHIELD:
            case CHAINMAIL_CHESTPLATE:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
                maxQuality = 6;
                break;
            case DIAMOND_PICKAXE:
            case CHAINMAIL_BOOTS:
            case CHAINMAIL_LEGGINGS:
            case IRON_HELMET:
            case DIAMOND_BOOTS:
                maxQuality = 7;
            case IRON_HOE:
            case DIAMOND_SHOVEL:
            case IRON_BOOTS:
                maxQuality = 8;
                break;
            case CROSSBOW:
            case DIAMOND_SWORD:
                maxQuality = 9;
                break;
            case DIAMOND_AXE:
            case DIAMOND_LEGGINGS:
                maxQuality = 10;
                break;
            case DIAMOND_HELMET:
                maxQuality = 11;
            case DIAMOND_CHESTPLATE:
                maxQuality = 12;
            case DIAMOND_HOE:
            case GOLDEN_SWORD:
            case GOLDEN_PICKAXE:
            case GOLDEN_SHOVEL:
            case GOLDEN_AXE:
            case GOLDEN_HOE:
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS:
                maxQuality = 14;
                break;
        }
        return maxQuality;
    }

    public static boolean isUndroppable(ItemStack item) {
        boolean isUndroppable = false;
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            for (String string :
                    item.getItemMeta().getLore()) {
                if (string.contains("Item não derrubável")) {
                    isUndroppable = true;
                }
            }
        }
        return isUndroppable;
    }

    public static boolean isUnplaceable(ItemStack item) {
        boolean isUnplaceable = false;
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            for (String string :
                    item.getItemMeta().getLore()) {
                if (string.contains("Item não posicionável")) {
                    isUnplaceable = true;
                }
            }
        }
        return isUnplaceable;
    }

    public static void safeDropItem(Location entityLocation, ItemStack itemStack) {
        if (!itemStack.getType().equals(Material.AIR) && itemStack.getAmount() > 0) {
            //noinspection ConstantConditions
            entityLocation.getWorld().dropItem(entityLocation, itemStack);
        }
    }
    //</editor-fold>

}
