package com.whitehallplugins.wolf.Runnables;

import com.whitehallplugins.wolf.Main;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SetupScoreboard extends BukkitRunnable {

    private final Main plugin;

    public SetupScoreboard(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers()){
            if (plugin.getConfig().get("Scoreboard." + p.getName().toLowerCase()) != null) {
                ItemFrame itemFrame = (ItemFrame) plugin.getConfig().get("Scoreboard." + p.getName().toLowerCase());
                assert itemFrame != null;
                itemFrame.setItem(plugin.getConfig().getItemStack("Images." + p.getName().toLowerCase() + ".living"));
            }
        }
        cancel();
    }
}
