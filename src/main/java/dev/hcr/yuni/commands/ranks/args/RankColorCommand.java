package dev.hcr.yuni.commands.ranks.args;

import dev.hcr.yuni.commands.ranks.RankCommand;
import dev.hcr.yuni.ranks.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RankColorCommand extends RankCommand {

    public RankColorCommand() {
        super("color", "Set the display color of a rank.", "yuni.ranks.admin", "/%label% color <rank> <color>");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(ChatColor.RED + "Missing permission: " + getPermission());
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + getUsage().replace("%label%", label));
            return;
        }
        Rank rank = Rank.getRank(args[1]);
        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "Invalid rank.");
            return;
        }
        ChatColor color = ChatColor.valueOf(args[2].toUpperCase());
        rank.setColor(color);
        sender.sendMessage(ChatColor.GREEN + "Updated " + rank.getName() + "s color.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> ranks = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        Arrays.stream(ChatColor.values()).forEach(chatColor -> colors.add(chatColor.name()));
        Rank.getRanks().forEach(rank -> ranks.add(rank.getName()));
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], ranks, completions);
        } else if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], colors, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
