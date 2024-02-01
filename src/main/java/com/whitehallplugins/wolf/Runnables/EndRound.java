package com.whitehallplugins.wolf.Runnables;

import com.whitehallplugins.wolf.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Objects;

public class EndRound extends BukkitRunnable {

    private final Main plugin;

    public EndRound(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getServer().broadcast(Component.text("Teleporting to spawn in 5 seconds"));
        teleport().runTaskLater(plugin, 100);
        updateScoreboard().runTaskLater(plugin, 200);

        cancel();
    }

    private BukkitRunnable updateScoreboard() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.playerKilledThisRound != null) {
                    if (plugin.getConfig().get("Scoreboard." + plugin.playerKilledThisRound.getName().toLowerCase()) != null) {
                        ItemFrame itemFrame = (ItemFrame) plugin.getConfig().get("Scoreboard." + plugin.playerKilledThisRound.getName().toLowerCase());
                        assert itemFrame != null;
                        itemFrame.setItem(plugin.getConfig().getItemStack("Images." + plugin.playerKilledThisRound.getName().toLowerCase() + ".dead"));
                        itemFrame.getLocation().getWorld().spawnParticle(Particle.VILLAGER_ANGRY, itemFrame.getLocation(), 10, 0.5, 0.5, 0.5, 0.1);
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
}
