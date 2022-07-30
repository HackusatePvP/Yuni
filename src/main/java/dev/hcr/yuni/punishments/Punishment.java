package dev.hcr.yuni.punishments;

import org.bukkit.event.Event;

import java.util.Map;

public abstract class Punishment {

    public abstract void execute(Event event);

    /**
     * 1.0-SNAPSHOT
     * <p>
     *     This function does not actually remove a punishment but instead inactivates the punishment. Some punishments can have different states; active inactive.
     *     When a punishment is inactive it only shows in the {@link dev.hcr.yuni.users.User}'s history and does not affect the user at all.
     * </p>
     */
    public abstract void remove();

    /**
     * 1.0-SNAPSHOT
     * <p>
     *     This function will completely remove a punishment from a user. Once the punishment is purged it will no longer show in the {@link dev.hcr.yuni.users.User}'s history..
     * </p>
     */
    public abstract void purge();

    /**
     * 1.0-SNAPSHOT
     * <p>
     *     Converts all punishment data into a new {@link Map} to be converted into a usable database object.
     * </p>
     * @return - {@link Map} of all punishment data.
     */
    public abstract Map<String, Object> toMap();
}
