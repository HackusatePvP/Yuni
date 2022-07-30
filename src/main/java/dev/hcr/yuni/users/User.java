package dev.hcr.yuni.users;

import dev.hcr.yuni.Yuni;
import dev.hcr.yuni.punishments.types.BanType;
import dev.hcr.yuni.ranks.Rank;
import dev.hcr.yuni.ranks.permissions.PermissionSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final UUID uuid;
    private String name;
    private Rank rank;
    private PermissionSet permissionSet;
    private final Collection<BanType> bans = new HashSet<>();

    private static final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.rank = Rank.getDefaultRank();
        users.put(uuid, this);
    }

    public User(UUID uuid, Map<String, Object> map) {
        this.uuid = uuid;
        this.name = (String) map.get("name");
        this.rank = Rank.getRank((String) map.get("rank"));
        users.put(uuid, this);
    }

    public void hookRankPermissions(Player player) {
        if (permissionSet != null) {
            permissionSet.remove();
        }
        permissionSet = new PermissionSet(player, getRank().getPermissions()).build();
    }

    public UUID getUniqueID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PermissionSet getPermissionSet() {
        return permissionSet;
    }

    public Rank getRank() {
        if (rank == null) {
            return Rank.getDefaultRank();
        } else {
            return rank;
        }
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        hookRankPermissions(toPlayer());
    }

    public Collection<BanType> getBans() {
        return bans;
    }

    public void addBan(BanType banType) {
        bans.stream().filter(BanType::isActive).forEach(banType1 -> banType1.setActive(false)); // Inactivates the previous ban
        bans.add(banType);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("name", name);
        map.put("rank", getRank().getName());
        return map;
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /* All static methods below */
    public static User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public static User getUser(String name) {
        User user = users.values().stream().filter(user1 -> user1.getName().equalsIgnoreCase(name)).findAny().orElse(null);
        if (user == null) {
           return Yuni.getPlugin().getStorage().loadUser(name);
        }
        return null;
    }
}
