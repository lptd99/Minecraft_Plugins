package tyo_drak.draksrpgclasses;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Checks {

    //<editor-fold defaultstate="collapsed" desc="FOLDING PLACEHOLDER">
    //</editor-fold>

    // ENTITIES

    //<editor-fold defaultstate="collapsed" desc="LIVING ENTITIES">
    public static boolean isVillager(EntityType entityType) {
        return (entityType.equals(EntityType.VILLAGER) ||
                entityType.equals(EntityType.WANDERING_TRADER));
    }

    public static boolean isEquid(EntityType entityType) {
        return (entityType.equals(EntityType.MULE) ||
                entityType.equals(EntityType.HORSE) ||
                entityType.equals(EntityType.DONKEY) ||
                entityType.equals(EntityType.SKELETON_HORSE));
    }

    public static boolean isTameable(EntityType entityType) {
        return (entityType.equals(EntityType.CAT) ||
                entityType.equals(EntityType.WOLF) ||
                entityType.equals(EntityType.OCELOT) ||
                entityType.equals(EntityType.PARROT));
    }

    public static boolean isFarmAnimal(EntityType entityType) {
        return (entityType.equals(EntityType.COW) ||
                entityType.equals(EntityType.PIG) ||
                entityType.equals(EntityType.SHEEP) ||
                entityType.equals(EntityType.CHICKEN) ||
                entityType.equals(EntityType.MUSHROOM_COW));
    }

    public static boolean isMiniBoss(EntityType entityType) {
        return (entityType.equals(EntityType.EVOKER) ||
                entityType.equals(EntityType.RAVAGER) ||
                entityType.equals(EntityType.ELDER_GUARDIAN));
    }

    public static boolean isBoss(EntityType entityType) {
        return (entityType.equals(EntityType.WITHER) ||
                entityType.equals(EntityType.ENDER_DRAGON));
    }

    public static boolean isPassive(EntityType entityType) {
        return (isVillager(entityType) ||
                entityType.equals(EntityType.BAT) ||
                entityType.equals(EntityType.CAT) ||
                entityType.equals(EntityType.COD) ||
                entityType.equals(EntityType.COW) ||
                entityType.equals(EntityType.FOX) ||
                entityType.equals(EntityType.PIG) ||
                entityType.equals(EntityType.MULE) ||
                entityType.equals(EntityType.HORSE) ||
                entityType.equals(EntityType.SQUID) ||
                entityType.equals(EntityType.SHEEP) ||
                entityType.equals(EntityType.TURTLE) ||
                entityType.equals(EntityType.DONKEY) ||
                entityType.equals(EntityType.OCELOT) ||
                entityType.equals(EntityType.PARROT) ||
                entityType.equals(EntityType.RABBIT) ||
                entityType.equals(EntityType.SALMON) ||
                entityType.equals(EntityType.STRIDER) ||
                entityType.equals(EntityType.CHICKEN) ||
                entityType.equals(EntityType.SNOWMAN) ||
                entityType.equals(EntityType.MUSHROOM_COW) ||
                entityType.equals(EntityType.TROPICAL_FISH) ||
                entityType.equals(EntityType.SKELETON_HORSE));
    }

    public static boolean isNeutral(EntityType entityType) {
        return (entityType.equals(EntityType.BEE) ||
                entityType.equals(EntityType.WOLF) ||
                entityType.equals(EntityType.LLAMA) ||
                entityType.equals(EntityType.PANDA) ||
                entityType.equals(EntityType.DOLPHIN) ||
                entityType.equals(EntityType.IRON_GOLEM) ||
                entityType.equals(EntityType.POLAR_BEAR) ||
                entityType.equals(EntityType.PUFFERFISH) ||
                entityType.equals(EntityType.TRADER_LLAMA));
    }

    public static boolean isHostile(EntityType entityType) {
        return (isBoss(entityType) ||
                isMiniBoss(entityType) ||
                isHostileMadeOfFlesh(entityType) ||
                isHostileMadeOfBones(entityType) ||
                entityType.equals(EntityType.VEX) ||
                entityType.equals(EntityType.SLIME) ||
                entityType.equals(EntityType.BLAZE) ||
                entityType.equals(EntityType.GHAST) ||
                entityType.equals(EntityType.CREEPER) ||
                entityType.equals(EntityType.PHANTOM) ||
                entityType.equals(EntityType.SHULKER) ||
                entityType.equals(EntityType.GUARDIAN) ||
                entityType.equals(EntityType.ENDERMITE) ||
                entityType.equals(EntityType.MAGMA_CUBE) ||
                entityType.equals(EntityType.SILVERFISH) ||
                entityType.equals(EntityType.ELDER_GUARDIAN));
    }

    public static boolean isHostileMadeOfFlesh(EntityType entityType) {
        return entityType.equals(EntityType.HUSK) ||
                entityType.equals(EntityType.WITCH) ||
                entityType.equals(EntityType.EVOKER) ||
                entityType.equals(EntityType.HOGLIN) ||
                entityType.equals(EntityType.ZOGLIN) ||
                entityType.equals(EntityType.ZOMBIE) ||
                entityType.equals(EntityType.DROWNED) ||
                entityType.equals(EntityType.RAVAGER) ||
                entityType.equals(EntityType.PILLAGER) ||
                entityType.equals(EntityType.VINDICATOR) ||
                entityType.equals(EntityType.PIGLIN_BRUTE) ||
                entityType.equals(EntityType.ZOMBIE_VILLAGER);
    }

    public static boolean isHostileMadeOfBones(EntityType entityType) {
        return entityType.equals(EntityType.STRAY) ||
                entityType.equals(EntityType.SKELETON) ||
                entityType.equals(EntityType.WITHER_SKELETON);
    }

    public static boolean isEvil(EntityType entityType) {
        return (isHostile(entityType) ||
                entityType.equals(EntityType.SPIDER) ||
                entityType.equals(EntityType.PIGLIN) ||
                entityType.equals(EntityType.ENDERMAN) ||
                entityType.equals(EntityType.CAVE_SPIDER) ||
                entityType.equals(EntityType.ZOMBIFIED_PIGLIN));
    }

    public static boolean isSacrificeable(LivingEntity livingEntity) {
        return livingEntity.hasPotionEffect(PotionEffectType.POISON) &&
                livingEntity.hasPotionEffect(PotionEffectType.SLOW) &&
                livingEntity.hasPotionEffect(PotionEffectType.CONFUSION) &&
                livingEntity.hasPotionEffect(PotionEffectType.BLINDNESS) &&
                livingEntity.hasPotionEffect(PotionEffectType.WEAKNESS) &&
                livingEntity.hasPotionEffect(PotionEffectType.HUNGER);
    }

    public static boolean isDruidaFriend(EntityType entityType) {
        return isPassive(entityType) ||
                isNeutral(entityType) ||
                entityType.equals(EntityType.SPIDER) ||
                entityType.equals(EntityType.CAVE_SPIDER);
    }
    //</editor-fold>

    // ITEMS

    public static boolean IsActualItem(ItemStack itemStack) {
        return itemStack!=null && !itemStack.getType().equals(Material.AIR) && itemStack.getAmount() > 0;
    }

    //<editor-fold defaultstate="collapsed" desc="isTool() ~ isWeapon()">
    public static boolean isSword(Material material) {
        return material.toString().toLowerCase().contains("_sword");
    }

    public static boolean isAxe(Material material) {
        return material.toString().toLowerCase().contains("_axe");
    }

    public static boolean isPickaxe(Material material) {
        return material.toString().toLowerCase().contains("_pickaxe");
    }

    public static boolean isShovel(Material material) {
        return material.toString().toLowerCase().contains("_shovel");
    }

    public static boolean isHoe(Material material) {
        return material.toString().toLowerCase().contains("_hoe");
    }

    public static boolean isTool(Material material) {
        return isPickaxe(material) || isShovel(material) || isHoe(material);
    }

    public static boolean isWeapon(Material material) {
        return isAxe(material) || isSword(material);
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="isArmor()">
    public static boolean isHelmet(Material material) {
        return material.toString().toLowerCase().contains("_helmet");
    }

    public static boolean isChestplate(Material material) {
        return material.toString().toLowerCase().contains("_chestplate");
    }

    public static boolean isLeggings(Material material) {
        return material.toString().toLowerCase().contains("_leggings");
    }

    public static boolean isBoots(Material material) {
        return material.toString().toLowerCase().contains("_boots");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FOOD">
    public static boolean isRawMeat(Material material) {
        return material.equals(Material.BEEF) ||
                material.equals(Material.CHICKEN) ||
                material.equals(Material.PORKCHOP) ||
                material.equals(Material.RABBIT) ||
                material.equals(Material.MUTTON) ||
                material.equals(Material.TROPICAL_FISH) ||
                material.equals(Material.SALMON) ||
                material.equals(Material.COD) ||
                material.equals(Material.ROTTEN_FLESH);
    }

    public static boolean isCookedMeat(Material material) {
        return material.equals(Material.COOKED_BEEF) ||
                material.equals(Material.COOKED_CHICKEN) ||
                material.equals(Material.COOKED_COD) ||
                material.equals(Material.COOKED_MUTTON) ||
                material.equals(Material.COOKED_PORKCHOP) ||
                material.equals(Material.COOKED_RABBIT) ||
                material.equals(Material.COOKED_SALMON) ||
                material.equals(Material.RABBIT_STEW);
    }

    public static boolean isMeat(Material material) {
        return isRawMeat(material) ||
                isCookedMeat(material);
    }

    public static boolean isVegan(Material material) {
        return material.equals(Material.CARROT) ||
                material.equals(Material.GOLDEN_CARROT) ||
                material.equals(Material.POTATO) ||
                material.equals(Material.BAKED_POTATO) ||
                material.equals(Material.BREAD) ||
                material.equals(Material.SWEET_BERRIES) ||
                material.equals(Material.MELON_SLICE) ||
                material.equals(Material.COOKIE) ||
                material.equals(Material.APPLE) ||
                material.equals(Material.GOLDEN_APPLE) ||
                material.equals(Material.ENCHANTED_GOLDEN_APPLE);
    }

    public static boolean isVegetarian(Material material) {
        return isVegan(material) ||
                material.equals(Material.PUMPKIN_PIE) ||
                material.equals(Material.CAKE) ||
                material.equals(Material.HONEY_BOTTLE) ||
                material.equals(Material.MILK_BUCKET);
    }

    public static boolean isAnimalByproduct(Material material) {
        return material.equals(Material.MILK_BUCKET) ||
                material.equals(Material.EGG) ||
                material.equals(Material.HONEY_BOTTLE) ||
                material.equals(Material.HONEYCOMB);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INGOTS">
    public static boolean isIngot(Material itemType) {
        return itemType.equals(Material.IRON_NUGGET) ||
                itemType.equals(Material.GOLD_NUGGET) ||
                itemType.equals(Material.IRON_INGOT) ||
                itemType.equals(Material.GOLD_INGOT) ||
                itemType.equals(Material.REDSTONE) ||
                itemType.equals(Material.LAPIS_LAZULI) ||
                itemType.equals(Material.QUARTZ) ||
                itemType.equals(Material.EMERALD) ||
                itemType.equals(Material.DIAMOND) ||
                itemType.equals(Material.NETHERITE_SCRAP);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MISC">
    //</editor-fold>

    // BLOCKS

    //<editor-fold defaultstate="collapsed" desc="ORE">
    public static boolean isOre(Material material) {
        return isT0Ore(material) || isT1Ore(material) || isT2Ore(material) || isT3Ore(material);
    }

    public static boolean isT0Ore(Material material) {
        return material.equals(Material.COAL_ORE) || material.equals(Material.NETHER_GOLD_ORE);
    }

    public static boolean isT1Ore(Material material) {
        return material.equals(Material.REDSTONE_ORE) || material.equals(Material.GOLD_ORE);
    }

    public static boolean isT2Ore(Material material) {
        return material.equals(Material.IRON_ORE) || material.equals(Material.LAPIS_ORE) || material.equals(Material.NETHER_QUARTZ_ORE);
    }

    public static boolean isT3Ore(Material material) {
        return material.equals(Material.DIAMOND_ORE) || material.equals(Material.EMERALD_ORE);
    }
    //</editor-fold>

    public static boolean isInfestableStone(Material material) {
        return isMossy(material) ||
                isCobblestone(material) ||
                isCrackedBricks(material) ||
                material.equals(Material.COBBLESTONE) ||
                material.equals(Material.STONE) ||
                material.equals(Material.DIORITE) ||
                material.equals(Material.ANDESITE) ||
                material.equals(Material.GRANITE) ||
                material.equals(Material.NETHERRACK) ||
                material.equals(Material.BASALT) ||
                material.equals(Material.BLACKSTONE) ||
                material.equals(Material.END_STONE);
    }

    public static boolean containsSTONE(Material material) {
        return material.toString().toUpperCase().contains("STONE");
    }

    public static boolean isSandstone(Material material) {
        return material.equals(Material.SANDSTONE) ||
                material.equals(Material.SANDSTONE_SLAB) ||
                material.equals(Material.SANDSTONE_STAIRS) ||
                material.equals(Material.SANDSTONE_WALL) ||
                material.equals(Material.RED_SANDSTONE) ||
                material.equals(Material.RED_SANDSTONE_SLAB) ||
                material.equals(Material.RED_SANDSTONE_STAIRS) ||
                material.equals(Material.RED_SANDSTONE_WALL);
    }

    public static boolean isCobblestone(Material material) {
        return material.equals(Material.COBBLESTONE) ||
                material.equals(Material.COBBLESTONE_SLAB) ||
                material.equals(Material.COBBLESTONE_STAIRS) ||
                material.equals(Material.COBBLESTONE_WALL) ||
                material.equals(Material.MOSSY_COBBLESTONE) ||
                material.equals(Material.MOSSY_COBBLESTONE_SLAB) ||
                material.equals(Material.MOSSY_COBBLESTONE_STAIRS) ||
                material.equals(Material.MOSSY_COBBLESTONE_WALL);
    }

    public static boolean isCrackedBricks(Material material) {
        return material.equals(Material.CRACKED_STONE_BRICKS) ||
                material.equals(Material.INFESTED_CRACKED_STONE_BRICKS) ||
                material.equals(Material.CRACKED_NETHER_BRICKS) ||
                material.equals(Material.CRACKED_POLISHED_BLACKSTONE_BRICKS);
    }

    public static boolean isMossy(Material material) {
        return material.equals(Material.MOSSY_COBBLESTONE) ||
                material.equals(Material.MOSSY_COBBLESTONE_SLAB) ||
                material.equals(Material.MOSSY_COBBLESTONE_STAIRS) ||
                material.equals(Material.MOSSY_COBBLESTONE_WALL) ||
                material.equals(Material.MOSSY_STONE_BRICKS) ||
                material.equals(Material.MOSSY_STONE_BRICK_SLAB) ||
                material.equals(Material.MOSSY_STONE_BRICK_STAIRS) ||
                material.equals(Material.MOSSY_STONE_BRICK_WALL);
    }

    public static boolean isPlank(Material material) {
        return material.toString().toLowerCase().contains("_wood");
    }

    public static boolean isLog(Material material) {
        return material.toString().toLowerCase().contains("_log");
    }

    public static boolean isWood(Material material) {
        return isPlank(material) || isLog(material);
    }

    public static boolean isWebbable(Material material) {
        return isFlower(material) ||
                isGrass(material) ||
                isSapling(material) ||
                isVine(material) ||
                material.equals(Material.AIR);
    }

    public static boolean isFlower(Material material) {
        return isFlowerShort(material) || isFlowerTall(material);
    }

    public static boolean isFlowerShort(Material material) {
        return material.equals(Material.DANDELION) ||
                material.equals(Material.POPPY) ||
                material.equals(Material.BLUE_ORCHID) ||
                material.equals(Material.ALLIUM) ||
                material.equals(Material.AZURE_BLUET) ||
                material.equals(Material.RED_TULIP) ||
                material.equals(Material.ORANGE_TULIP) ||
                material.equals(Material.WHITE_TULIP) ||
                material.equals(Material.PINK_TULIP) ||
                material.equals(Material.OXEYE_DAISY) ||
                material.equals(Material.CORNFLOWER) ||
                material.equals(Material.LILY_OF_THE_VALLEY) ||
                material.equals(Material.WITHER_ROSE);
    }

    public static boolean isFlowerTall(Material material) {
        return material.equals(Material.SUNFLOWER) ||
                material.equals(Material.LILAC) ||
                material.equals(Material.ROSE_BUSH) ||
                material.equals(Material.PEONY);
    }

    public static boolean isSapling(Material material) {
        return material.equals(Material.ACACIA_SAPLING) ||
                material.equals(Material.BIRCH_SAPLING) ||
                material.equals(Material.DARK_OAK_SAPLING) ||
                material.equals(Material.JUNGLE_SAPLING) ||
                material.equals(Material.OAK_SAPLING) ||
                material.equals(Material.SPRUCE_SAPLING) ||
                material.equals(Material.BAMBOO_SAPLING);
    }

    public static boolean isGrass(Material material) {
        return isGrassShort(material) || isGrassTall(material);
    }

    public static boolean isGrassShort(Material material) {
        return material.equals(Material.GRASS) ||
                material.equals(Material.FERN) ||
                material.equals(Material.DEAD_BUSH);
    }

    public static boolean isGrassTall(Material material) {
        return material.equals(Material.TALL_GRASS) ||
                material.equals(Material.LARGE_FERN);
    }

    public static boolean isVine(Material material) {
        return material.equals(Material.VINE) ||
                material.equals(Material.TWISTING_VINES) ||
                material.equals(Material.TWISTING_VINES_PLANT) ||
                material.equals(Material.WEEPING_VINES) ||
                material.equals(Material.WEEPING_VINES_PLANT);
    }

    public static boolean isLeaves(Material blockType) {
        return blockType.equals(Material.ACACIA_LEAVES) ||
                blockType.equals(Material.BIRCH_LEAVES) ||
                blockType.equals(Material.JUNGLE_LEAVES) ||
                blockType.equals(Material.OAK_LEAVES) ||
                blockType.equals(Material.SPRUCE_LEAVES) ||
                blockType.equals(Material.DARK_OAK_LEAVES);
    }

    public static boolean isCrop(Material material) {
        return isFarmlandCrop(material) || isBlockCrop(material);
    }

    public static boolean isMatureCrop(Block block) {
        return block.getBlockData() instanceof Ageable && isFarmlandCrop(block.getType()) && ((Ageable) block.getBlockData()).getAge() == ((Ageable) block.getBlockData()).getMaximumAge();
    }


    public static boolean isFarmlandCrop(Material material) {
        return material.equals(Material.WHEAT) ||
                material.equals(Material.CARROTS) ||
                material.equals(Material.POTATOES) ||
                material.equals(Material.BEETROOTS) ||
                material.equals(Material.ATTACHED_MELON_STEM) ||
                material.equals(Material.ATTACHED_PUMPKIN_STEM) ||
                material.equals(Material.MELON_STEM) ||
                material.equals(Material.PUMPKIN_STEM);
    }

    public static boolean isBlockCrop(Material material) {
        return isVine(material) || isSapling(material) ||
                material.equals(Material.KELP) ||
                material.equals(Material.KELP_PLANT) ||
                material.equals(Material.CACTUS) ||
                material.equals(Material.MUSHROOM_STEM) ||
                material.equals(Material.MELON) ||
                material.equals(Material.PUMPKIN) ||
                material.equals(Material.BAMBOO) ||
                material.equals(Material.BAMBOO_SAPLING) ||
                material.equals(Material.SWEET_BERRY_BUSH) ||
                material.equals(Material.CHORUS_PLANT) ||
                material.equals(Material.CHORUS_FLOWER) ||
                material.equals(Material.CHORUS_FRUIT) ||
                material.equals(Material.NETHER_WART) ||
                material.equals(Material.COCOA) ||
                material.equals(Material.COCOA_BEANS) ||
                material.equals(Material.WEEPING_VINES) ||
                material.equals(Material.TWISTING_VINES) ||
                material.equals(Material.WEEPING_VINES_PLANT) ||
                material.equals(Material.TWISTING_VINES_PLANT) ||
                material.equals(Material.VINE) ||
                material.equals(Material.LILY_PAD);
    }

    public static boolean isUnduppableCrop(Material blockType) {
        return isFarmlandCrop(blockType);
    }

    public static boolean isUndead(EntityType entityType) {
        return entityType.equals(EntityType.HUSK) ||
                entityType.equals(EntityType.STRAY) ||
                entityType.equals(EntityType.ZOGLIN) ||
                entityType.equals(EntityType.WITHER) ||
                entityType.equals(EntityType.ZOMBIE) ||
                entityType.equals(EntityType.DROWNED) ||
                entityType.equals(EntityType.PHANTOM) ||
                entityType.equals(EntityType.SKELETON) ||
                entityType.equals(EntityType.ZOMBIE_HORSE) ||
                entityType.equals(EntityType.SKELETON_HORSE) ||
                entityType.equals(EntityType.ZOMBIE_VILLAGER) ||
                entityType.equals(EntityType.WITHER_SKELETON) ||
                entityType.equals(EntityType.ZOMBIFIED_PIGLIN);
    }

    // RPG CLASSES CHECKS

    public static boolean isForFerreirosInteract(Material material) {
        return material.equals(Material.ANVIL) ||
                material.equals(Material.DAMAGED_ANVIL) ||
                material.equals(Material.CHIPPED_ANVIL) ||
                material.equals(Material.GRINDSTONE) ||
                material.equals(Material.BLAST_FURNACE) ||
                material.equals(Material.ENCHANTING_TABLE);
    }

    public static boolean isForFerreirosPlace(Material material) {
        return material.equals(Material.ANVIL) ||
                material.equals(Material.DAMAGED_ANVIL) ||
                material.equals(Material.CHIPPED_ANVIL) ||
                material.equals(Material.GRINDSTONE) ||
                material.equals(Material.BLAST_FURNACE) ||
                material.equals(Material.ENCHANTING_TABLE) ||
                material.equals(Material.BEACON) ||
                material.equals(Material.RAIL) ||
                material.equals(Material.POWERED_RAIL) ||
                material.equals(Material.DETECTOR_RAIL) ||
                material.equals(Material.ACTIVATOR_RAIL) ||
                material.equals(Material.MINECART) ||
                material.equals(Material.TNT_MINECART) ||
                material.equals(Material.CHEST_MINECART) ||
                material.equals(Material.HOPPER_MINECART) ||
                material.equals(Material.FURNACE_MINECART) ||
                material.equals(Material.COMMAND_BLOCK_MINECART);
    }

    public static boolean isForFerreirosBreak(Material material) {
        return isForMasterFerreirosBreak(material) ||
                material.equals(Material.ANVIL) ||
                material.equals(Material.DAMAGED_ANVIL) ||
                material.equals(Material.CHIPPED_ANVIL) ||
                material.equals(Material.GRINDSTONE) ||
                material.equals(Material.BLAST_FURNACE) ||
                material.equals(Material.ENCHANTING_TABLE) ||
                material.equals(Material.BEACON) ||
                material.equals(Material.RAIL) ||
                material.equals(Material.POWERED_RAIL) ||
                material.equals(Material.DETECTOR_RAIL) ||
                material.equals(Material.ACTIVATOR_RAIL) ||
                material.equals(Material.MINECART) ||
                material.equals(Material.TNT_MINECART) ||
                material.equals(Material.CHEST_MINECART) ||
                material.equals(Material.HOPPER_MINECART) ||
                material.equals(Material.FURNACE_MINECART) ||
                material.equals(Material.COMMAND_BLOCK_MINECART) ||
                material.equals(Material.ANCIENT_DEBRIS);
    }

    public static boolean isForMasterFerreirosBreak(Material material) {
        return material.equals(Material.ANCIENT_DEBRIS) ||
                material.equals(Material.NETHERITE_BLOCK);
    }

    public static boolean isForDruidasInteract(Material material) {
        return material.equals(Material.COMPOSTER) ||
                material.equals(Material.BREWING_STAND);
    }

    public static boolean isForDruidasPlant(Material material) {
        return isCrop(material);
    }

    public static boolean isForDruidasPlace(Material material) {
        return material.equals(Material.COMPOSTER) ||
                material.equals(Material.BREWING_STAND);
    }

    public static boolean isForDruidasHarvest(Material material) {
        return isCrop(material);
    }

    public static boolean isForDruidasBreak(Material material) {
        return material.equals(Material.COMPOSTER) ||
                material.equals(Material.BREWING_STAND);
    }

    public static boolean isLavable(Material blockType) {
        return isWebbable(blockType) ||
                blockType.equals(Material.WATER) ||
                blockType.equals(Material.LAVA);
    }

    public static boolean isMadeOfFlesh(EntityType entityType) {
        return (isPassive(entityType) &&
                !entityType.equals(EntityType.TURTLE) &&
                !entityType.equals(EntityType.STRIDER) &&
                !entityType.equals(EntityType.SKELETON_HORSE)) ||
                (isNeutral(entityType) &&
                        !entityType.equals(EntityType.IRON_GOLEM)) ||
                isHostileMadeOfFlesh(entityType) ||
                entityType.equals(EntityType.PLAYER);
    }

    public static boolean isMadeOfBones(EntityType entityType) {
        return isHostileMadeOfBones(entityType) ||
                entityType.equals(EntityType.SKELETON_HORSE);
    }

    public static boolean playerMainHasEnchantment(Player player, Enchantment enchantment) {
        return player.getInventory().getItemInMainHand().getEnchantments().containsKey(enchantment);
    }

    public static boolean playerOffHasEnchantment(Player player, Enchantment enchantment) {
        return player.getInventory().getItemInOffHand().getEnchantments().containsKey(enchantment);
    }
}
