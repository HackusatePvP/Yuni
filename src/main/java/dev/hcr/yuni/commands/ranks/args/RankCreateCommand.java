package dev.hcr.yuni.commands.ranks.args;

import dev.hcr.yuni.commands.ranks.RankCommand;
import dev.hcr.yuni.ranks.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankCreateCommand extends RankCommand {

    public RankCreateCommand() {
        super("create", "Create a new rank.", "yuni.ranks.admin", "/%label% create <name>");
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
        Rank rankExists = Rank.getRank(args[1]);
        if (rankExists != null) {
            sender.sendMessage(ChatColor.RED + "Rank already exists!");
            return;
        }
        new Rank(args[1]);
        sender.sendMessage(ChatColor.GREEN + "Created " + args[1] + " rank.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("create");
        }
        Collections.sort(completions);
        return completions;
    }
}
