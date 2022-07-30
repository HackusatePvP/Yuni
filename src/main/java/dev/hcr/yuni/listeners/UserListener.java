package dev.hcr.yuni.listeners;

import dev.hcr.yuni.Yuni;
import dev.hcr.yuni.punishments.types.BanType;
import dev.hcr.yuni.users.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {
    private final Yuni plugin = Yuni.getPlugin();

    @EventHandler
    public void onPreJoinAsync(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        if (User.getUser(uuid) != null) {
            User.getUser(uuid).setName(name);
        } else {
            if (plugin.getStorage().userExists(uuid)) {
                plugin.getStorage().loadUser(uuid);
            } else {
                plugin.getStorage().saveUser(new User(uuid, name));
            }
        }
        handlePunishments(event);
    }

    private void handlePunishments(AsyncPlayerPreLoginEvent event) {
        User user = User.getUser(event.getUniqueId());
        System.out.println("Total bans: " + user.getBans().size());
        BanType banType = user.getBans().stream().filter(BanType::isActive).findAny().orElse(null);
        if (banType != null) {
            if (banType.isTemporary()) {
                if (banType.getTimeLeft() <= 0L) {
                    banType.remove();
                } else {
                    banType.execute(event);
                }
            } else {
                banType.execute(event);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = User.getUser(player.getUniqueId());
        user.hookRankPermissions(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = User.getUser(player.getUniqueId());
        plugin.getStorage().saveUser(user);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        User user = User.getUser(event.getPlayer().getUniqueId());
        plugin.getStorage().saveUser(user);
    }

}
