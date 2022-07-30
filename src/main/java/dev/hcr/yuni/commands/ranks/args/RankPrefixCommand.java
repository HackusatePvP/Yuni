package dev.hcr.yuni.commands.ranks.args;

import dev.hcr.yuni.commands.ranks.RankCommand;
import dev.hcr.yuni.ranks.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankPrefixCommand extends RankCommand {

    public RankPrefixCommand() {
        super("prefix", "Set a prefix for a rank.", "yuni.ranks.admin", "/%label% prefix <rank> <prefix>");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(ChatColor.RED + "Missing permission: " + getPermission());
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + getUsage().replace("%label%", label));
            return;
        }
        Rank rank = Rank.getRank(args[1]);
        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "Invalid rank.");
            return;
        }
        rank.setPrefix(args[2]);
        sender.sendMessage(ChatColor.GREEN + "Updated " + rank.getName() + "'s prefix.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> ranks = new ArrayList<>();
        Rank.getRanks().forEach(rank -> ranks.add(rank.getName()));
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], ranks, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
