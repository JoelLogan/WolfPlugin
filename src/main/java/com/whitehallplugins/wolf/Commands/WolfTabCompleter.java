package com.whitehallplugins.wolf.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WolfTabCompleter implements TabCompleter {

    private static final List<String> COMMANDS = Arrays.asList("setup", "give", "start");

    public WolfTabCompleter(){

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (!(sender instanceof Player)){
            return Collections.emptyList();
        }
        if (sender.hasPermission("wolf.admin")) {
            List<String> filteredCommands = new ArrayList<>();
            if (args.length == 1) {
                for (String mycommand : COMMANDS) {
                    if (mycommand.toLowerCase().startsWith((args[args.length - 1]).toLowerCase())) {
                        filteredCommands.add(mycommand);
                    }
                }
                return filteredCommands;
            } else if (args.length > 1) {
                if (args[0].equalsIgnoreCase("setup")) {
                    switch (args.length) {
                        case 2:
                            filteredCommands.add("spawn");
                            filteredCommands.add("wolf");
                            filteredCommands.add("scoreboard");
                            filteredCommands.add("images");
                            filteredCommands.add("timer");
                            return filteredCommands;
                        case 3:
                            if (args[1].equalsIgnoreCase("images") || args[1].equalsIgnoreCase("wolf")
                                    || args[1].equalsIgnoreCase("scoreboard")){
                                filteredCommands.add("<player>");
                            }
                            return filteredCommands;
                        case 4:
                            if (args[1].equalsIgnoreCase("images")) {
                                filteredCommands.add("living");
                                filteredCommands.add("dead");
                            }
                            return filteredCommands;
                    }
                }
                else if (args[0].equalsIgnoreCase("give")) {
                    if (args.length == 2) {
                        filteredCommands.add("compass");
                        filteredCommands.add("sword");
                        return filteredCommands;
                    }
                }
            }
        }
        return Collections.emptyList();
    }

}
