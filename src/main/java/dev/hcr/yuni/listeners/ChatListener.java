package dev.hcr.yuni.listeners;

import dev.hcr.yuni.ranks.Rank;
import dev.hcr.yuni.users.User;
import dev.hcr.yuni.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = User.getUser(player.getUniqueId());
        Rank rank = user.getRank();
        // TODO: 7/29/2022 Check if the player is muted
        event.setFormat(CC.translate( (rank.getPrefix() == null || rank.getPrefix().isEmpty() ? "" : rank.getPrefix())) + rank.getColor() + " %s " +
                ChatColor.WHITE + CC.translate( (rank.getSuffix() == null || rank.getSuffix().isEmpty() ? "" : rank.getSuffix()) ) + ": %s");
    }
}
