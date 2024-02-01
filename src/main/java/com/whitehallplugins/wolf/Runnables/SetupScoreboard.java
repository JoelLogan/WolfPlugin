package com.whitehallplugins.wolf.Runnables;

import com.whitehallplugins.wolf.Main;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

public class SetupScoreboard extends BukkitRunnable {

    private final Main plugin;

    public SetupScoreboard(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers()){
            if (plugin.getConfig().isSet("Scoreboard." + p.getName().toLowerCase())) {
                UUID uuid = UUID.fromString(Objects.requireNonNull(plugin.getConfig().getString("Scoreboard." + p.getName().toLowerCase())));
                ItemFrame itemFrame = (ItemFrame) plugin.getServer().getEntity(uuid);
                assert itemFrame != null;
                itemFrame.setItem(plugin.getConfig().getItemStack("Images." + p.getName().toLowerCase() + ".living"));
            }
        }
        cancel();
    }
}
