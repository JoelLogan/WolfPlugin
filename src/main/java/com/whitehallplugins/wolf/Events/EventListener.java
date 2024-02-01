package com.whitehallplugins.wolf.Events;

import com.whitehallplugins.wolf.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;

import java.time.Duration;

public class EventListener implements Listener {

    private final Main plugin;

    public EventListener (Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPVP(PlayerInteractEntityEvent event) {
        //event.setCancelled(true);
        event.getPlayer().sendMessage(Component.text(event.getEventName()));
    }

    @EventHandler
    public void onOTHERPVP(PlayerInteractEvent event) {
        //event.setCancelled(true);
        event.getPlayer().sendMessage(Component.text(event.getEventName()));
        event.getPlayer().sendMessage(Component.text(event.getAction().name()));
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
        if (event.getEntity().getType().equals(EntityType.PLAYER)) {
            if (!event.getEntity().getInventory().getItemInMainHand().isEmpty()) {
                if (event.getEntity().getInventory().getItemInMainHand().hasItemFlag(ItemFlag.HIDE_DYE)) {
                    event.getPlayer().ban("You were killed by the wolf", Duration.ofDays(365), null, true);
                }
            }
        }
    }

}
