package com.whitehallplugins.wolf.Commands;

import com.whitehallplugins.wolf.Items.ItemManager;
import com.whitehallplugins.wolf.Main;
import com.whitehallplugins.wolf.Runnables.EndRound;
import com.whitehallplugins.wolf.Runnables.PlayerTracker;
import com.whitehallplugins.wolf.Runnables.SetupScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WolfCommands implements CommandExecutor {
    private static final TextComponent NoPermission = Component.text("§4You don't have permission to access this resource.");
    private final Main plugin;

    public WolfCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(Component.text("Only players can use this command."));
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("wolf")){
            if (player.hasPermission("wolf.admin")) {
                if (args.length == 0){
                    player.sendMessage(Component.text("§aTo configure the wolf, type /wolf <option>"));
                    return true;
                }
                else if (args.length == 1){
                    switch (args[0].toLowerCase()) {
                        case "setup":
                            player.sendMessage(Component.text("§aTo configure the wolf, type /wolf setup <option>"));
                            return true;
                        case "give":
                            player.sendMessage(Component.text("§aTo give yourself a wolf item, type /wolf give <item>"));
                            return true;
                        case "start":
                            if (plugin.getConfig().get("Spawn") == null){
                                player.sendMessage(Component.text("§aYou need to set the spawnpoint first."));
                            }
                            else if (plugin.getConfig().get("Timer") == null){
                                player.sendMessage(Component.text("§aYou need to set the timer first."));
                            }
                            else if (plugin.getConfig().get("Wolf") == null){
                                player.sendMessage(Component.text("§aYou need to set the wolf first."));
                            }
                            else {
                                if (!plugin.gameRunning){
                                    new SetupScoreboard(plugin).runTask(plugin);
                                    Player wolf = plugin.getServer().getPlayer(Objects.requireNonNull(plugin.getConfig().getString("Wolf")));
                                    assert wolf != null;
                                    wolf.getInventory().setHeldItemSlot(8);
                                    wolf.getInventory().addItem(ItemManager.playerTracker);
                                    wolf.getInventory().addItem(ItemManager.wolfSword);
                                    new PlayerTracker(wolf).runTaskTimer(plugin, 20, 5);
                                    plugin.gameRunning = true;
                                }
                                plugin.roundActive = true;
                                new EndRound(plugin).runTaskLater(plugin, (plugin.getConfig().getInt("Timer") * 1200L) - 100);
                                player.sendMessage(Component.text("§aStarting the round!"));
                            }
                            return true;
                        default:
                            player.sendMessage(Component.text("§aThe command you entered wasn't found"));
                            return true;
                    }
                }
                else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("setup")) {
                        switch (args[1].toLowerCase()) {
                            case "spawn":
                                plugin.getConfig().set("Spawn", player.getLocation());
                                plugin.saveConfig();
                                player.sendMessage(Component.text("§aSpawn set!"));
                                return true;
                            case "wolf":
                                player.sendMessage(Component.text("§aTo configure the wolf, type /wolf setup wolf <player>"));
                                return true;
                            case "scoreboard":
                                player.sendMessage(Component.text("§aTo configure the scoreboard, type /wolf setup scoreboard <player>"));
                                return true;
                            case "images":
                                player.sendMessage(Component.text("§aTo configure the images, type /wolf setup images <player> <living/dead>"));
                                return true;
                            case "timer":
                                player.sendMessage(Component.text("§aTo configure the timer, type /wolf setup timer <minutes>"));
                                return true;
                            default:
                                player.sendMessage(Component.text("§aThe command you entered wasn't found"));
                                return true;
                        }
                    } else if (args[0].equalsIgnoreCase("give")) {
                        switch (args[1].toLowerCase()) {
                            case "compass":
                                player.getInventory().addItem(ItemManager.playerTracker);
                                player.sendMessage(Component.text("§aCompass given!"));
                                return true;
                            case "sword":
                                player.getInventory().addItem(ItemManager.wolfSword);
                                player.sendMessage(Component.text("§aSword given!"));
                                return true;
                            default:
                                player.sendMessage(Component.text("§aThe command you entered wasn't found"));
                                return true;
                        }
                    } else {
                        player.sendMessage(Component.text("§aThe command you entered wasn't found"));
                        return true;
                    }
                }
                else if (args.length == 3){
                    if (args[0].equalsIgnoreCase("setup")){
                        if (args[1].equalsIgnoreCase("images")){
                            player.sendMessage(Component.text("§aTo configure the images, type /wolf setup images <player> <living/dead>"));
                            return true;
                        }
                        else if (args[1].equalsIgnoreCase("scoreboard")){
                            try {
                                plugin.getConfig().set("Scoreboard." + args[2].toLowerCase(), Objects.requireNonNull(Objects.requireNonNull(getItemFrame(player)).getUniqueId().toString()));
                                plugin.saveConfig();
                                player.sendMessage(Component.text("§aItemFrame set!"));
                            }
                            catch (NullPointerException e){
                                player.sendMessage(Component.text("§aYou need to be looking at an item frame."));
                            }
                            return true;
                        }
                        else if (args[1].equalsIgnoreCase("wolf")){
                            plugin.getConfig().set("Wolf", args[2]);
                            plugin.saveConfig();
                            player.sendMessage(Component.text("§aWolf set!"));
                            return true;
                        }
                        else if (args[1].equalsIgnoreCase("timer")){
                            try {
                                plugin.getConfig().set("Timer", Integer.parseInt(args[2]));
                                plugin.saveConfig();
                                player.sendMessage(Component.text("§aTimer set!"));
                            }
                            catch (NumberFormatException e){
                                player.sendMessage(Component.text("§aThe command you entered wasn't found"));
                            }
                            return true;
                        }
                        else {
                            player.sendMessage(Component.text("§aThe command you entered wasn't found"));
                            return true;
                        }
                    }
                }
                else if (args.length == 4){
                    if (args[0].equalsIgnoreCase("setup")){
                        if (args[1].equalsIgnoreCase("images")){
                            if (args[3].equalsIgnoreCase("living") || args[3].equalsIgnoreCase("dead")){
                                try {
                                    plugin.getConfig().set("Images." + args[2].toLowerCase() + "." + args[3].toLowerCase(), Objects.requireNonNull(getItemFrame(player)).getItem());
                                    plugin.saveConfig();
                                    player.sendMessage(Component.text("§aImage set!"));
                                }
                                catch (NullPointerException e){
                                    player.sendMessage(Component.text("§aYou need to be looking at an item frame."));
                                }
                            }
                            else {
                                player.sendMessage(Component.text("§aThe command you entered wasn't found"));
                            }
                            return true;
                        }
                    }
                }
            }
            else {
                player.sendMessage(NoPermission);
                return true;
            }
        }
        return true;
    }

    private ItemFrame getItemFrame(Player player){
        Entity entity;
        try {
            entity = player.getTargetEntity(5);
        } catch (IllegalArgumentException e) {
            player.sendMessage(Component.text("§aYou need to be looking at an item frame."));
            return null;
        }
        if (entity != null) {
            if (entity.getType().equals(EntityType.ITEM_FRAME)) {
                return (ItemFrame) entity;
            }
        }
        return null;
    }
}