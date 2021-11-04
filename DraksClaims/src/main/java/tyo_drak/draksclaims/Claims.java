package tyo_drak.draksclaims;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class Claims implements Listener {

    public static final Material CLAIM_ITEM = Material.GOLD_BLOCK;
    private static final String PLAYER_CLAIMS_TAG = " Claims";
    private static final int PLAYER_CLAIMS_DEFAULT_MAX = 1;

    public Map<String, Long> messagesCooldowns = new HashMap<>();

    public static final Set<String> existingPlayerNames = new HashSet<>();

    public static final String BASE_PERMISSION = "draksclaims";

    public static final String BYPASS_PERMISSION = BASE_PERMISSION + ".bypass";

    public static final String BYPASS_LIMIT_PERMISSION = BYPASS_PERMISSION + ".limit";
    public static final String BYPASS_OWNERSHIP_PERMISSION = BYPASS_PERMISSION + ".ownership";


    //<editor-fold defaultstate="collapsed" desc="EVENTS">
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerKey = player.getName() + " " + player.getUniqueId();

        if (Main.config.getString(playerKey) == null) {
            List<String> newTrustedList = new ArrayList<>();
            Main.config.addDefault(playerKey, newTrustedList);
            Main.plugin.saveConfig();
        }

        addPlayerDefaultClaimsList(playerKey);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Chunk chunk = event.getClickedBlock().getChunk();
            Location location = event.getClickedBlock().getLocation();
            Player player = event.getPlayer();
            if (!chunkIsNotClaimedOrIsChunkOwnerOrIsTrustedHere(chunk, location, player)) {
                event.setCancelled(true);
                denyPlayerAction(event.getPlayer(), "CLAIMED_CHUNK", ChatColor.DARK_RED + "Esta Chunk pertence a outro jogador!");
            }
        }
    }

    @EventHandler
    public void safeZonePreventDamage(EntityDamageByEntityEvent event){
        Chunk chunk = event.getEntity().getLocation().getChunk();
        Entity entity = event.getEntity();
        if (entity instanceof Player || entity instanceof Tameable) {
            if (isSafeZone(chunk.getX(), chunk.getZ())) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    player.sendMessage(ChatColor.GREEN+"Você está em uma área segura e, por isso, todo dano recebido é prevenido!");
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void safeZonePreventMobs(EntityTargetEvent event){
        Chunk chunk = event.getEntity().getLocation().getChunk();
        Entity entity = event.getEntity();
        if (isSafeZone(chunk.getX(), chunk.getZ()) && entity instanceof Monster) {
                entity.remove();
        }
    }

    @EventHandler
    public void entityExplodeEvent(EntityExplodeEvent event) {
        List<Block> blocksToRemove = new ArrayList<>();
        Chunk chunk = event.getLocation().getChunk();
        for (Block explodingBlock :
                event.blockList()) {
            if (event.getEntity() instanceof Creeper) {
                Creeper creeper = (Creeper) event.getEntity();
                if (creeper.getTarget() instanceof Player) {
                    Player playerTarget = (Player) creeper.getTarget();
                    if (isClaimedChunk(chunk.getX(), chunk.getZ()) && !isChunkOwner(explodingBlock.getLocation(), playerTarget)) {
                        blocksToRemove.add(explodingBlock);
                    }
                } else {
                    blocksToRemove.add(explodingBlock);
                }
            }
        }
        event.blockList().removeAll(blocksToRemove);
    }

    @EventHandler
    public void blockExplodeEvent(BlockExplodeEvent event) {
        event.blockList().removeIf(this::blockIsOnClaimedChunk);
    }

    @EventHandler
    public void asyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player playerSender = event.getPlayer();
        switch (event.getMessage()) {
            case "..claim":
                claim(playerSender);
                event.setCancelled(true);
                break;
            case "..claims":
                showClaims(playerSender);
                event.setCancelled(true);
                break;
            case "..unclaim":
                unclaim("info", playerSender);
                event.setCancelled(true);
                break;
            case "..chunkAtual":
                chunkAtual(playerSender);
                event.setCancelled(true);
                break;
        }
        if (event.getMessage().contains("..unclaim ")) {
            String targetChunk = event.getMessage().replace("..unclaim ", "");
            unclaim(targetChunk, playerSender);
            event.setCancelled(true);
        }
        if (event.getMessage().contains("..trust ")) {
            String targetName = event.getMessage().replace("..trust ", "");
            if (getServer().getPlayer(targetName) != null) {
                String targetNameAndUUID = getServer().getPlayer(targetName).getName() + " " + getServer().getPlayer(targetName).getUniqueId();
                trust(playerSender, targetNameAndUUID, targetName);
            } else {
                denyPlayerAction(playerSender, "TRUSTED_OFFLINE", ChatColor.DARK_RED + "O jogador confiado precisa estar online!");
            }
            event.setCancelled(true);
        }
        if (event.getMessage().contains("..untrust ")) {
            String targetName = event.getMessage().replace("..untrust ", "");
            untrust(playerSender, targetName);
            event.setCancelled(true);
        }
    }
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="CLAIM AND TRUST">
    private void claim(Player playerSender) {
        String playerName = playerSender.getName();
        UUID playerUUID = playerSender.getUniqueId();
        Chunk playerChunk = playerSender.getLocation().getChunk();
        ItemStack itemMain = playerSender.getInventory().getItemInMainHand();
        String playerKey = playerName + " " + playerUUID;
        List<String> playerClaims = Main.config.getStringList(playerKey + PLAYER_CLAIMS_TAG);
        int playerMaxClaims = Integer.parseInt(playerClaims.get(0).replace("Max Claims: ", ""));

        if (itemMain.getType().equals(CLAIM_ITEM)) {
            if (isClaimedChunk(playerChunk.getX(), playerChunk.getZ())) {
                if (isChunkOwner(playerSender.getLocation(), playerSender)) {
                    denyPlayerAction(playerSender, "CHUNK_OWNER", ChatColor.DARK_RED + "Você já conquistou esta Chunk!");
                } else {
                    denyPlayerAction(playerSender, "CLAIMED_CHUNK", ChatColor.DARK_RED + "Esta Chunk pertence a outro jogador!");
                }
            } else {
                if (playerClaims.size() - 1 < playerMaxClaims || playerSender.hasPermission(BYPASS_LIMIT_PERMISSION)) {
                    String chunkSignature = playerChunk.getX() + "," + playerChunk.getZ();

                    itemMain.setAmount(itemMain.getAmount() - 1);
                    playerClaims.add(chunkSignature);

                    Main.config.set(chunkSignature, playerKey);
                    Main.config.set(playerKey + PLAYER_CLAIMS_TAG, playerClaims);
                    Main.plugin.saveConfig();
                    acceptPlayerAction(playerSender, "CHUNK_CLAIM", ChatColor.GREEN + "Chunk conquistada!");
                } else {
                    denyPlayerAction(playerSender, "HAS_MAX_CLAIMS", ChatColor.DARK_RED + "Você não pode ter mais de " + playerMaxClaims + " Chunk(s)!" + ChatColor.YELLOW + "\n(Para abandonar uma de suas Chunks, digite \"..unclaim\")");
                }
            }
        } else {
            denyPlayerAction(playerSender, "MISSING_CLAIM_ITEM", ChatColor.DARK_RED + "Você deve segurar um Bloco de Ouro");
        }
    }

    private void unclaim(String chunkXY, Player playerSender) {
        String playerName = playerSender.getName();
        UUID playerUUID = playerSender.getUniqueId();
        String playerKey = playerName + " " + playerUUID;
        if (chunkXY != null) {
            if (chunkXY.equals("info")) {
                playerSender.sendMessage(ChatColor.YELLOW + "" +
                        "As chunks têm uma assinatura do tipo \"X,Z\"." +
                        "\nPara saber a assinatura das suas chunks, digite \"..claims\"." +
                        "\nPara abandonar uma de suas chunks, digite ..unclaim [assinatura]." +
                        "\nPara descobrir a assinatura da Chunk atual, digite \"..chunkAtual\".");
            } else if (chunkXY.contains("[") || chunkXY.contains("]")) {
                denyPlayerAction(playerSender, "WRONG_SINTAX", ChatColor.DARK_RED + "Tente novamente sem \"[]\".");
            } else {
                denyPlayerAction(playerSender, "NOT_CLAIMED_BY_PLAYER", ChatColor.DARK_RED + "Esta Chunk não lhe pertence!");
            }
            if (Main.config.get(chunkXY) != null) {
                if (Main.config.get(chunkXY).equals(playerKey)) {
                    List<String> playerClaims = Main.config.getStringList(playerKey + PLAYER_CLAIMS_TAG);
                    playerClaims.remove(chunkXY);

                    Main.config.set(chunkXY, null);
                    Main.config.set(playerKey + PLAYER_CLAIMS_TAG, playerClaims);
                    Main.plugin.saveConfig();
                    acceptPlayerAction(playerSender, "UNCLAIM", ChatColor.GREEN + "Você abandonou sua Chunk " + chunkXY + "!");
                }
            }
        }
    }

    private void showClaims(Player playerSender) {
        for (String line :
                Main.config.getStringList(playerSender.getName() + " " + playerSender.getUniqueId() + PLAYER_CLAIMS_TAG)) {
            playerSender.sendMessage(ChatColor.GREEN + line);
        }
    }

    private void addPlayerDefaultClaimsList(String playerKey) {
        List<String> claimsList = new ArrayList<>();
        claimsList.add("Max Claims: " + PLAYER_CLAIMS_DEFAULT_MAX);
        Main.config.addDefault(playerKey + PLAYER_CLAIMS_TAG, claimsList);
    }

    private void trust(Player playerSender, String targetNameAndUUID, String targetName) {
        String playerKey = playerSender.getName() + " " + playerSender.getUniqueId();
        if (!Main.config.getStringList(playerKey).contains(targetNameAndUUID)) {
            List<String> trustedPlayers = Main.config.getStringList(playerKey);
            trustedPlayers.add(targetNameAndUUID);
            Main.config.set(playerKey, trustedPlayers);
            Main.plugin.saveConfig();
            acceptPlayerAction(playerSender, "PLAYER_TRUST", ChatColor.GREEN + "Você agora confia em " + targetName);
        } else {
            denyPlayerAction(playerSender, "PLAYER_ALREADY_TRUSTED", ChatColor.DARK_RED + "Você já confia neste jogador!");
        }
    }

    private void untrust(Player playerSender, String targetName) {
        String playerTrustKey = playerSender.getName() + " " + playerSender.getUniqueId();
        List<String> trustedPlayers = Main.config.getStringList(playerTrustKey);
        for (String trustedPlayer :
                Main.config.getStringList(playerTrustKey)) {
            if (trustedPlayer.contains(targetName)) {
                trustedPlayers.remove(trustedPlayer);
                acceptPlayerAction(playerSender, "UNTRUST_PLAYER", ChatColor.GREEN + "Você não confia mais em " + targetName);
            }
        }
        if (trustedPlayers == Main.config.getStringList(playerTrustKey)) {
            denyPlayerAction(playerSender, "PLAYER_NOT_TRUSTED", ChatColor.DARK_RED + "Você já não confia neste jogador!");
        } else {
            Main.config.set(playerTrustKey, trustedPlayers);
            Main.plugin.saveConfig();
        }
    }

    private void chunkAtual(Player playerSender){
        Chunk chunkAtual = playerSender.getLocation().getChunk();
        acceptPlayerAction(playerSender, "CURRENT_CHUNK", ChatColor.GREEN + "Assinatura da Chunk atual: " + ChatColor.YELLOW + chunkAtual.getX()+","+chunkAtual.getZ());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CHECKS">
    private boolean chunkIsNotClaimedOrIsChunkOwnerOrIsTrustedHere(Chunk chunk, Location location, Player player) {
        return !isClaimedChunk(chunk.getX(), chunk.getZ()) || isChunkOwner(location, player) || isTrustedHere(chunk.getX(), chunk.getZ(), player) || player.hasPermission(BYPASS_OWNERSHIP_PERMISSION);
        /*
        Debug.consoleMessage("isClaimedChunk? " + isClaimedChunk(chunk.getX(), chunk.getZ()));
        Debug.consoleMessage("isChunkOwner? " + isChunkOwner(location, player));
        Debug.consoleMessage("isTrustedHere? " + isTrustedHere(chunk.getX(), chunk.getZ(), player));
        */
    }

    private boolean isTrustedHere(int x, int y, Player player) {
        String chunkOwnerKey = getChunkOwnerKey(x, y);
        List<String> chunkOwnerTrustedPlayersList = Main.config.getStringList(chunkOwnerKey);
        return chunkOwnerTrustedPlayersList.contains(player.getName() + " " + player.getUniqueId());
        /*
        Debug.consoleMessage("chunkOwnerClaimKey - " + chunkOwnerKey);
        Debug.consoleMessage("chunkOwnerTrustKey - " + chunkOwnerTrustKey);
        Debug.consoleMessage("chunkOwnerTrustedPlayersList - " + chunkOwnerTrustedPlayersList);
        Debug.consoleMessage("chunkOwnerTrustedPlayers - " + chunkOwnerTrustedPlayers);
        Debug.consoleMessage("chunkOwnerTrustedPlayersList.contains(player.getName() + \" \" + player.getUniqueId() - " + chunkOwnerTrustedPlayersList.contains(player.getName() + " " + player.getUniqueId()));
        */
    }

    private boolean isChunkOwner(Location location, Player player) {
        Chunk chunk = location.getChunk();
        String playerKey = player.getName() + " " + player.getUniqueId();
        return getChunkOwnerKey(chunk.getX(), chunk.getZ()).equals(playerKey);
    }

    private void addChunkSignatureToPlayerClaimsList(String chunkSignature, List<String> playerClaimsList) {
        if (!playerClaimsList.contains(chunkSignature)) {
            playerClaimsList.add(chunkSignature);
        }
    }

    private boolean isClaimedChunk(int x, int z) {
        return !getChunkOwnerKey(x, z).equals("NONE");
    }

    private boolean isSafeZone(int x, int z) {
        return getChunkOwnerKey(x, z).contains("SAFE_ZONE");
    }

    private boolean blockIsOnClaimedChunk(Block block) {
        Chunk chunk = block.getChunk();
        return isClaimedChunk(chunk.getX(), chunk.getZ());
    }

    private String getChunkOwnerKey(int x, int z) {
        String chunkSignature = x+","+z;
        if (Main.config.get(chunkSignature) == null) {
            return "NONE";
        } else {
            addChunkSignatureToPlayerClaimsList(chunkSignature, Main.config.getStringList(Main.config.getString(chunkSignature) + PLAYER_CLAIMS_TAG));
            return Main.config.getString(chunkSignature);
        }
    }
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="PLAYER">
    public void denyPlayerAction(Player player, String messageKey, String string) {
        String cooldownKey = player.getName() + " " + messageKey;
        if (!messagesCooldowns.containsKey(cooldownKey) || hasPassedSince(1, messagesCooldowns.get(cooldownKey))) {
            messagesCooldowns.put(cooldownKey, getTimeSeconds());
            player.sendMessage(string);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    } // ALL GOOD

    public void acceptPlayerAction(Player player, String messageKey, String string) {
        String cooldownKey = player.getName() + " " + messageKey;
        if (!messagesCooldowns.containsKey(cooldownKey) || hasPassedSince(1, messagesCooldowns.get(cooldownKey))) {
            messagesCooldowns.put(cooldownKey, getTimeSeconds());
            player.sendMessage(string);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
        }
    } // ALL GOOD
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TIME">
    public static boolean hasPassedSince(int seconds, long initialTime) {
        return getTimeRemaining(seconds, initialTime) <= 0;
    } // ALL GOOD

    public static long getTimeRemaining(long seconds, long timePrevious) {
        return seconds - (getTimeSeconds() - timePrevious);
    } // ALL GOOD

    public static long getTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    } // ALL GOOD
    //</editor-fold>

}
