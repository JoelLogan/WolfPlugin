package com.whitehallplugins.wolf;

import com.whitehallplugins.wolf.Commands.WolfCommands;
import com.whitehallplugins.wolf.Commands.WolfTabCompleter;
import com.whitehallplugins.wolf.Events.EventListener;
import com.whitehallplugins.wolf.Items.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    public Player playerKilledThisRound = null;
    public boolean gameRunning = false;

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("wolf")).setExecutor(new WolfCommands(this));
        Objects.requireNonNull(getCommand("wolf")).setTabCompleter(new WolfTabCompleter());
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        ItemManager.init();
        saveDefaultConfig();
        getLogger().info("Plugin Started");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Stopped");
    }
}
