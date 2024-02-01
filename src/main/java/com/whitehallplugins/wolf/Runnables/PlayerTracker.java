package com.whitehallplugins.wolf.Runnables;

import com.whitehallplugins.wolf.Events.EventListener;
import com.whitehallplugins.wolf.Items.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTracker extends BukkitRunnable {

    private final Player wolf;

    public PlayerTracker(Player wolf) {
        this.wolf = wolf;
    }

    @Override
    public void run() {
        if (wolf.getInventory().contains(ItemManager.playerTracker)) {
            if (EventListener.playerBeingTracked != null) {
                if (EventListener.playerBeingTracked.isOnline()) {
                    wolf.setCompassTarget(EventListener.playerBeingTracked.getLocation());
                }
            }
        }
    }
}
