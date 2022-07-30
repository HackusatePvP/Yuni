package dev.hcr.yuni.commands.punishments;

import dev.hcr.yuni.punishments.types.BanType;
import dev.hcr.yuni.users.User;
import dev.hcr.yuni.utils.Duration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TempBanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /tempban [-a] <player> [duration] [reason]");
        }
        if (args.length >= 3) {
            User user;
            if (args[0].equalsIgnoreCase("-a")) {
                user = User.getUser(args[1]);
                if (user == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid user.");
                    return true;
                }
                Duration duration = Duration.fromString(args[2]);
                if (duration.getValue() <= 0) {
                    sender.sendMessage(ChatColor.RED + "&cInvalid duration type. Example: 1d1h1m1s");
                    return true;
                }
                addBan(user, sender, args, duration.getValue(), true);
                //   Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(CC.format(LangValues.BAN_KICK_MESSAGE_PUBLIC.replace("%NAME%", user.getName()).replace("%EXECUTOR%", sender.getName()))));
            } else if (args[0].equalsIgnoreCase("-s")) {
                user = User.getUser(args[1]);
                if (user == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid user.");
                    return true;
                }
                Duration duration = Duration.fromString(args[2]);
                if (duration.getValue() <= 0) {
                    sender.sendMessage(ChatColor.RED + "&cInvalid duration type. Example: 1d1h1m1s");
                    return true;
                }
                addBan(user, sender, args, duration.getValue(), true);
            } else {
                user = User.getUser(args[0]);
                if (user == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid user.");
                    return true;
                }
                Duration duration = Duration.fromString(args[1]);
                if (duration.getValue() <= 0) {
                    sender.sendMessage(ChatColor.RED + "&cInvalid duration type. Example: 1d1h1m1s");
                    return true;
                }
                addBan(user, sender, args, duration.getValue(), false);
            }
        }

        return false;
    }

    private void addBan(User user, CommandSender sender, String[] args, long duration, boolean parameter) {
        StringBuilder builder = new StringBuilder();
        if (parameter) {
            for (int i = 3; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }
        } else {
            for (int i = 2; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }
        }
        String reason = builder.toString().trim();
        if (reason.contains("-s")) {
            reason = reason.replace("-s", "");
        }
        if (reason.contains("-a")) {
            reason = reason.replace("-a", "");
        }
        user.addBan(new BanType(user.getUniqueID(), sender.getName(), reason, true, duration));
        sender.sendMessage(ChatColor.GREEN + "You have temporarily banned " + user.getName() + " for " + args[1] + ".");
    }
}