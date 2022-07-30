package dev.hcr.yuni.ranks.permissions;

import dev.hcr.yuni.Yuni;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;

public class PermissionSet {
    private final Player player;
    private final PermissionAttachment attachment;
    private final List<String> permissions;


    public PermissionSet(Player player, List<String> permission) {
        this.player = player;
        this.attachment = player.addAttachment(Yuni.getPlugin());
        this.permissions = permission;
    }

    public PermissionSet build() {
        for (String perm : permissions) {
            attachment.setPermission(perm.toLowerCase(), true);
        }
        return this;
    }

    public PermissionSet remove() {
        for (String perm : permissions) {
            attachment.unsetPermission(perm.toLowerCase());
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public PermissionAttachment getAttachment() {
        return attachment;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
