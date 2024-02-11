package com.whitehallplugins.wolf.Events;

import com.whitehallplugins.wolf.Items.ItemManager;
import com.whitehallplugins.wolf.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class EventListener implements Listener {

    private final Main plugin;
    public static Player playerBeingTracked = null;

    public EventListener (Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType().equals(EntityType.PLAYER)) {
            if (event.getDamager().getType().equals(EntityType.PLAYER)) {
                if (((Player) event.getDamager()).getInventory().getItemInMainHand().hasItemFlag(ItemFlag.HIDE_DYE)
                        && plugin.playerKilledThisRound == null) {
                    event.setDamage(1000);
                    ((Player) event.getEntity()).getInventory().clear();
                    ((Player) event.getEntity()).ban("You were killed by the wolf", (Date) null, null, true);
                    plugin.playerKilledThisRound = ((Player) event.getEntity());
                    if (!plugin.roundActive) {
                        setScoreboardDead();
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    public void setScoreboardDead() {
        if (plugin.getConfig().get("Scoreboard." + plugin.playerKilledThisRound.getName().toLowerCase()) != null) {
            UUID uuid = UUID.fromString(Objects.requireNonNull(plugin.getConfig().getString("Scoreboard." + plugin.playerKilledThisRound.getName().toLowerCase())));
            ItemFrame itemFrame = (ItemFrame) plugin.getServer().getEntity(uuid);
            assert itemFrame != null;
            itemFrame.setItem(plugin.getConfig().getItemStack("Images." + plugin.playerKilledThisRound.getName().toLowerCase() + ".dead"));
            itemFrame.getLocation().getWorld().playSound(itemFrame.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1, 1);
        }
        plugin.playerKilledThisRound = null;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().hasItemMeta()) {
                if (event.getItem().getType().equals(ItemManager.playerTracker.getType()) && event.getItem().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                    Player previousPlayer = playerBeingTracked;
                    playerBeingTracked = null;
                    boolean useNext = false;
                    int failsafe = 5;
                    while (playerBeingTracked == null){
                        for (Player player : plugin.getServer().getOnlinePlayers()) {
                            if (useNext || previousPlayer == null) {
                                playerBeingTracked = player;
                                break;
                            }
                            else if (previousPlayer.equals(player)) {
                                useNext = true;
                            }
                        }
                        failsafe--;
                        if (failsafe == 0) {
                            playerBeingTracked = null;
                            break;
                        }
                    }
                    if (playerBeingTracked == null){
                        event.getPlayer().sendMessage(Component.text("You're not tracking anyone"));
                    }
                    else {
                        event.getPlayer().sendMessage(Component.text("You are now tracking " + playerBeingTracked.getName()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.text(""));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.text(""));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.deathMessage(Component.text(""));
    }

}
