package dev.hcr.yuni.commands;

import dev.hcr.yuni.users.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        User user = User.getUser(player.getUniqueId());
        player.sendMessage("UUID: " + user.getUniqueID());
        player.sendMessage("Name: " + user.getName());
        player.sendMessage("Rank: " + user.getRank().getName());
        player.sendMessage("PermissionSet permissions: " + user.getPermissionSet().getPermissions());
        player.sendMessage("Player permissions: ");
        player.getEffectivePermissions().forEach(permissionAttachmentInfo -> player.sendMessage(permissionAttachmentInfo.getPermission()));
        player.sendMessage("Expected permissions: " + user.getRank().getPermissions());
        return false;
    }
}
