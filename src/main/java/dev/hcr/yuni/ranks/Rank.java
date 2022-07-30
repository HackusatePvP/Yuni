package dev.hcr.yuni.ranks;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Rank {
    private final String name;
    private String prefix, suffix;
    private boolean def;
    private ChatColor color = ChatColor.WHITE;
    private ArrayList<String> permissions = new ArrayList<>();

    private static final Map<String, Rank> ranks = new HashMap<>();

    public Rank(String name) {
        this.name = name;
        ranks.put(name, this);
    }

    public Rank(String name, Map<String, Object> map) {
        this.name = name;
        this.prefix = (String) map.get("prefix");
        this.suffix = (String) map.get("suffix");
        this.def = (Boolean) map.get("def");
        this.color = ChatColor.valueOf((String) map.get("color"));
        this.permissions = (ArrayList<String>) map.get("permissions");
        System.out.println(map);
        ranks.put(name, this);
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isDefault() {
        return def;
    }

    private void setDefault(boolean def) {
        this.def = def;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("prefix", prefix);
        map.put("suffix", suffix);
        map.put("def", def);
        map.put("color", color);
        map.put("permissions", permissions);
        return map;
    }

    /* All static methods below */

    public static Rank getRank(String name) {
        return ranks.get(name);
    }

    public static Rank getDefaultRank() {
        Rank rank = ranks.values().stream().filter(Rank::isDefault).findAny().orElse(null);
        if (rank == null) {
            Rank defaultRank = new Rank("Default");
            defaultRank.setDefault(true);
            return defaultRank;
        }
        return rank;
    }

    public static Collection<Rank> getRanks() {
        return ranks.values();
    }

}
