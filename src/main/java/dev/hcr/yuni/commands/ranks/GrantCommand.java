package dev.hcr.yuni.commands.ranks;

import dev.hcr.yuni.Yuni;
import dev.hcr.yuni.ranks.Rank;
import dev.hcr.yuni.users.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrantCommand implements CommandExecutor {
    private final Yuni plugin = Yuni.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            // TODO: 7/29/2022
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            // TODO: 7/29/2022
            return true;
        }
        if (args.length == 2) {
            Rank rank = Rank.getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(ChatColor.RED + "Invalid rank.");
                return true;
            }
            User user = User.getUser(args[0]);
            if (user == null) {
                if (plugin.getStorage().userExists(args[0])) {
                    plugin.getStorage().loadUser(args[0]);
                } else {
                    return true;
                }
            }
            assert user != null;
            user.setRank(rank);
            sender.sendMessage(ChatColor.GREEN + "Updated " + player.getName() + "'s rank.");
        }
        return false;
    }
}
