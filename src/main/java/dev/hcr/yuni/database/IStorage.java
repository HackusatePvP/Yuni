package dev.hcr.yuni.database;

import dev.hcr.yuni.punishments.Punishment;
import dev.hcr.yuni.ranks.Rank;
import dev.hcr.yuni.users.User;

import java.util.UUID;

public interface IStorage {

    // All methods are subject to change as the project progresses. Please check the changelog for any api changes that may effect third party plugins.

    void loadUser(UUID uuid);

    User loadUser(String name);

    void loadUserAsync(UUID uuid);

    void saveUser(User user);

    void saveUserAsync(User user);

    boolean userExists(UUID uuid);

    boolean userExists(String name);

    void loadRanks();

    void saveRank(Rank rank);

    void savePunishments(Punishment punishment);
}
