package dev.hcr.yuni.commands.ranks.args;

import dev.hcr.yuni.commands.ranks.RankCommand;
import dev.hcr.yuni.ranks.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankPermissionCommand extends RankCommand {

    public RankPermissionCommand() {
        super("permissions", "Add or remove permissions from a rank.", "yuni.ranks.admin", "/%label% permissions <rank> <add|remove> <permission>");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(ChatColor.RED + "Missing permission: " + getPermission());
            return;
        }
        if (args.length < 4) {
            sender.sendMessage(ChatColor.RED + getUsage().replace("%label%", label));
            return;
        }
        String permission = args[3];
        Rank rank = Rank.getRank(args[1]);
        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "Invalid rank.");
            return;
        }
        if (args[2].equalsIgnoreCase("add")) {
            rank.getPermissions().add(permission);
        } else {
            rank.getPermissions().remove(permission);
        }
        sender.sendMessage(ChatColor.GREEN + "Updated " + rank.getName() + "'s permissions.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> ranks = new ArrayList<>();
        Rank.getRanks().forEach(rank -> ranks.add(rank.getName()));
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], ranks, completions);
        } else if (args.length == 3) {
            completions.add("add");
            completions.add("remove");
        }
        Collections.sort(completions);
        return completions;
    }
}
