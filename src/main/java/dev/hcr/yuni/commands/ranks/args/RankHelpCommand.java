package dev.hcr.yuni.commands.ranks.args;

import dev.hcr.yuni.commands.ranks.RankCommand;
import dev.hcr.yuni.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class RankHelpCommand extends RankCommand {
    private final TreeMap<String, Integer> pageMap = new TreeMap<>();

    public RankHelpCommand() {
        super("help", "Main help page.", "yuni.ranks.admin", "/%label% help");
        pagination();
    }

    void pagination() {
        // I stole this from my own faction.
        List<String> commands = new ArrayList<>(RankCommand.getRegisteredCommands());
        int items = commands.size();
        int count = 1;
        int page = 1;
        do {
            if (count == 10) {
                // For every 10 commands we will make a new page
                page++;
                count = 0;
            }
            String command = commands.get(items - 1);
            // Store command to the page map
            pageMap.put(command, page);
            // Remove the command from the array
            commands.remove(command);
            count++;
            --items;
        } while (!commands.isEmpty());
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(ChatColor.RED + "Missing permission: " + getPermission());
            return;
        }
        List<String> message = new ArrayList<>();
        if (args.length < 2) {
            message.add("&7&m-------------------------------------------------------------");
            message.add("&cRank Help &7(&c" + 1 + "&7/&c" + getMaxPage() + "&7)");
            message.add("");
            for (String s : pageMap.keySet()) {
                if (pageMap.get(s) == 1) {
                    RankCommand command = RankCommand.getCommand(s);
                    message.add("&6* &c" + command.getCommand() + ": &7" + command.getDescription());
                }
            }
            message.add("");
            message.add("&7You are on page (" + 1 + "/" + getMaxPage() + ") To view the next page use the command \"/" + label + " " + this.getCommand() + " <page>.\"");
            message.add("&7&m-------------------------------------------------------------");
        }
        if (args.length == 2) {
            message.add("&7&m-------------------------------------------------------------");
            int page = Integer.parseInt(args[1]);
            if (page > getMaxPage()) {
                message.add(ChatColor.RED + "This page doesn't exist!");
            } else {
                message.add("&cRank Help &7(&c" + page + "&7/&c" + getMaxPage() + "&7)");
                message.add("");
                for (String s : pageMap.keySet()) {
                    if (pageMap.get(s) == page) {
                        RankCommand command = RankCommand.getCommand(s);
                        message.add("&6* &c" + command.getCommand() + ": &7" + command.getDescription());
                    }
                }
                message.add("");
                message.add("&7You are on page (" + 1 + "/" + getMaxPage() + ") To view the next page use the command \"/" + label + " " + this.getCommand() + " <page>.\"");
            }
            message.add("&7&m-------------------------------------------------------------");
        }
        message.forEach(msg -> sender.sendMessage(CC.translate(msg)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return null;
    }

    private int getMaxPage() {
        Map.Entry<String, Integer> maxEntry = Collections.max(pageMap.entrySet(), Map.Entry.comparingByValue());
        return maxEntry.getValue();
    }
}
