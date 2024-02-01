package com.whitehallplugins.wolf.Runnables;

import com.whitehallplugins.wolf.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Objects;
import java.util.UUID;

public class EndRound extends BukkitRunnable {

    private final Main plugin;
    private int count = 0;

    public EndRound(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getServer().broadcast(Component.text("Teleporting to spawn in 5 seconds"));
        teleport().runTaskLater(plugin, 100);
        summonParticles().runTaskTimer(plugin, 100, 20);
        updateScoreboard().runTaskLater(plugin, 160);
        plugin.gameRunning = false;
        cancelRunnables(plugin).runTaskLater(plugin, 500);
    }

    private BukkitRunnable summonParticles() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.playerKilledThisRound != null) {
                    if (plugin.getConfig().get("Scoreboard." + plugin.playerKilledThisRound.getName().toLowerCase()) != null) {
                        UUID uuid = UUID.fromString(Objects.requireNonNull(plugin.getConfig().getString("Scoreboard." + plugin.playerKilledThisRound.getName().toLowerCase())));
                        Entity itemFrame = plugin.getServer().getEntity(uuid);
                        assert itemFrame != null;
                        itemFrame.getLocation().getWorld().spawnParticle(Particle.VILLAGER_ANGRY, itemFrame.getLocation(), 10, 0.5, 0.5, 0.5, 0.1);
                    }
                    count++;
                }
                if (count >= 3) {
                    count = 0;
                    cancel();
                }
            }
        };
    }

    private BukkitRunnable updateScoreboard() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.playerKilledThisRound != null) {
                    if (plugin.getConfig().get("Scoreboard." + plugin.playerKilledThisRound.getName().toLowerCase()) != null) {
                        UUID uuid = UUID.fromString(Objects.requireNonNull(plugin.getConfig().getString("Scoreboard." + plugin.playerKilledThisRound.getName().toLowerCase())));
                        ItemFrame itemFrame = (ItemFrame) plugin.getServer().getEntity(uuid);
                        assert itemFrame != null;
                        itemFrame.setItem(plugin.getConfig().getItemStack("Images." + plugin.playerKilledThisRound.getName().toLowerCase() + ".dead"));
                        itemFrame.getLocation().getWorld().playSound(itemFrame.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1, 1);
                    }
                    plugin.playerKilledThisRound = null;
                }
                cancel();
            }
        };
    }

    private BukkitRunnable teleport() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (plugin.getConfig().get("Spawn") != null) {
                        p.teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Spawn")));
                    } else {
                        p.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
                    }
                }
                cancel();
            }
        };
    }

    private BukkitRunnable cancelRunnables(Main plugin) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getServer().getScheduler().cancelTasks(plugin);
            }
        };
    }
}
