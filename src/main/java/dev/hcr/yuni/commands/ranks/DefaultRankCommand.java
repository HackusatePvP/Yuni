package dev.hcr.yuni.commands.ranks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultRankCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        RankCommand helpCommand = RankCommand.getCommand("help");
        if (args.length == 0) {
            if (helpCommand == null) {
                sender.sendMessage(ChatColor.RED + "No help page available.");
                return true;
            }
            helpCommand.execute(sender, label, args);
        } else {
            RankCommand argCommand = RankCommand.getCommand(args[0]);
            if (argCommand == null) {
                if (helpCommand == null) {
                    sender.sendMessage(ChatColor.RED + "No help page available.");
                    return true;
                }
                helpCommand.execute(sender, label, args);
                return true;
            }
            argCommand.execute(sender, label, args);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command c, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], RankCommand.getRegisteredCommands(), completions);
        }
        if (args.length >= 2) {
            RankCommand command = RankCommand.getCommand(args[0]);
            if (command != null) {
                StringUtil.copyPartialMatches(args[args.length - 1], command.onTabComplete(sender, label, args), completions);
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
