package dev.hcr.yuni.commands.punishments;

import dev.hcr.yuni.punishments.types.BanType;
import dev.hcr.yuni.users.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /ban [-a] <player> <reason>");
        }
        if (args.length >= 2) {
            User user;
            if (args[0].equalsIgnoreCase("-a")) {
                user = User.getUser(args[1]);
                if (user == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid user.");
                    return true;
                }
                addBan(user, sender, args, true);
            } else if (args[0].equalsIgnoreCase("-s")) {
                user = User.getUser(args[1]);
                if (user == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid user.");
                    return true;
                }
                addBan(user, sender, args, true);
            } else {
                user = User.getUser(args[0]);
                if (user == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid user.");
                    return true;
                }
                addBan(user, sender, args, false);
            }
        }
        return false;
    }

    private void addBan(User user, CommandSender sender, String[] args, boolean parameter) {
        StringBuilder builder = new StringBuilder();
        if (parameter) {
            for (int i = 2; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }
        } else {
            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }
        }
        String reason = builder.toString().trim();
        if (reason.contains("-s")) {
            reason = reason.replace("-s", "");
        }
        sender.sendMessage(ChatColor.GREEN + "You have permanently banned " + user.getName() + ".");
        user.addBan(new BanType(user.getUniqueID(), sender.getName(), reason, false, -1L));
    }

}
