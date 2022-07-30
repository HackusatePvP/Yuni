package dev.hcr.yuni;

import dev.hcr.yuni.commands.TestCommand;
import dev.hcr.yuni.commands.punishments.BanCommand;
import dev.hcr.yuni.commands.punishments.TempBanCommand;
import dev.hcr.yuni.commands.punishments.UnBanCommand;
import dev.hcr.yuni.commands.ranks.DefaultRankCommand;
import dev.hcr.yuni.commands.ranks.GrantCommand;
import dev.hcr.yuni.commands.ranks.RankCommandMap;
import dev.hcr.yuni.configurations.types.PropertiesConfiguration;
import dev.hcr.yuni.database.IStorage;
import dev.hcr.yuni.database.types.MongoStorage;
import dev.hcr.yuni.listeners.ChatListener;
import dev.hcr.yuni.listeners.UserListener;
import dev.hcr.yuni.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Yuni extends JavaPlugin {
    private IStorage storage;
    private static Yuni plugin;

    @Override
    public void onEnable() {
        plugin = this;
        loadConfigurations();
        loadStorage();
        registerCommands();
        registerListeners();
    }

    private void loadConfigurations() {
        new PropertiesConfiguration("database.properties", "database");
        new PropertiesConfiguration("mongo.properties", "database");
        new PropertiesConfiguration("sql.properties", "database");
        new PropertiesConfiguration("redis.properties", "database");

        new PropertiesConfiguration("yuni.properties");
    }

    private void loadStorage() {
        PropertiesConfiguration configuration = PropertiesConfiguration.getPropertiesConfiguration("database.properties");
        switch (configuration.getString("main-loader").toLowerCase()) {
            case "mongo":
            case "mongodb":
                storage = new MongoStorage();
                break;
            case "mysql":
            case "sql":
                getLogger().severe("SQL is not supported as of " + getDescription().getVersion() + ": Try using mongodb");
                Bukkit.shutdown();
                break;
            case "redis":
                getLogger().severe("Redis is not supported as of " + getDescription().getVersion() + ": Try using mongodb");
                Bukkit.shutdown();
                break;
        }
        storage.loadRanks();
    }

    private void registerCommands() {
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("grant").setExecutor(new GrantCommand());
        getCommand("rank").setExecutor(new DefaultRankCommand());
        getCommand("tempban").setExecutor(new TempBanCommand());
        getCommand("test").setExecutor(new TestCommand());
        getCommand("unban").setExecutor(new UnBanCommand());
        new RankCommandMap();
    }

    private void registerListeners() {
        Arrays.asList(new UserListener(), new ChatListener()).forEach(listener -> {
            Bukkit.getPluginManager().registerEvents(listener, this);
        });
    }

    @Override
    public void onDisable() {
        for (Rank rank : Rank.getRanks()) {
            plugin.getStorage().saveRank(rank);
        }

        plugin = null;
    }

    public IStorage getStorage() {
        return storage;
    }

    public static Yuni getPlugin() {
        return plugin;
    }
}
