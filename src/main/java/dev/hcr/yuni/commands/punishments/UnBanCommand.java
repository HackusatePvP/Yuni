package dev.hcr.yuni.commands.punishments;

import dev.hcr.yuni.Yuni;
import dev.hcr.yuni.punishments.types.BanType;
import dev.hcr.yuni.users.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnBanCommand implements CommandExecutor {
    private final Yuni plugin = Yuni.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/unban <player>");
            return true;
        }
        User user = User.getUser(args[0]);
        if (user == null) {
            if (plugin.getStorage().userExists(args[0])) {
                plugin.getStorage().loadUser(args[0]);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid user.");
                return true;
            }
        }
        user = User.getUser(args[0]);
        user.getBans().stream().filter(BanType::isActive).forEach(BanType::remove);
        return false;
    }
}
