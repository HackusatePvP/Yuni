package dev.hcr.yuni.punishments.types;

import dev.hcr.yuni.Yuni;
import dev.hcr.yuni.configurations.types.PropertiesConfiguration;
import dev.hcr.yuni.punishments.Punishment;
import dev.hcr.yuni.utils.CC;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class BanType extends Punishment {
    private final int id;
    private final UUID uuid;
    private final long executionTime, expireTime;
    private final String reason, executor;
    private final boolean temporary;
    private boolean active;

    public BanType(UUID uuid, String executor, String reason, boolean temporary, long expireTime) {
        this.id = new Random().nextInt(999999);
        this.uuid = uuid;
        this.executor = executor;
        this.reason = reason;
        this.temporary = temporary;
        this.executionTime = System.currentTimeMillis();
        this.expireTime = expireTime;
        this.active = true;

        if (Bukkit.getPlayer(uuid) != null) {
            Player player = Bukkit.getPlayer(uuid);
            player.kickPlayer(CC.translate(getKickMessage()));
        }

        Yuni.getPlugin().getStorage().savePunishments(this);
    }

    public BanType(UUID uuid, Map<String, Object> map) {
        this.id = (Integer) map.get("id");
        this.uuid = uuid;
        this.executor = (String) map.get("executor");
        this.reason = (String) map.get("reason");
        this.temporary = (Boolean) map.get("temporary");
        this.executionTime = (Long) map.get("executionTime");
        this.expireTime = ((Number) map.get("expireTime")).longValue();
        this.active = (Boolean) map.get("active");
    }

    @Override
    public void execute(Event e) {
        if (e instanceof AsyncPlayerPreLoginEvent) {
            AsyncPlayerPreLoginEvent event = (AsyncPlayerPreLoginEvent) e;
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate(getKickMessage()));
        }
    }

    @Override
    public void remove() {
        this.active = false;
    }

    @Override
    public void purge() {

    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("uuid", uuid.toString());
        map.put("executor", executor);
        map.put("reason", reason);
        map.put("temporary", temporary);
        map.put("executionTime", executionTime);
        map.put("expireTime", expireTime);
        map.put("active", active);
        return map;
    }

    public int getId() {
        return id;
    }

    public UUID getUniqueID() {
        return uuid;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public String getExecutor() {
        return executor;
    }

    public String getReason() {
        return reason;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getDuration() {
        return expireTime - executionTime;
    }

    public long getTimeLeft() {
        return getDuration() - System.currentTimeMillis();
    }

    public String getKickMessage() {
        PropertiesConfiguration configuration = PropertiesConfiguration.getPropertiesConfiguration("yuni.properties");
        String message = "&4&l" + configuration.getString("server-name") + "\n"
                + "&cYou are banned from the server.\n" +
                (temporary ? "&7Time Left: &c" + DurationFormatUtils.formatDurationWords(getTimeLeft(), true, true) + "\n" : "&7Time Left: &cPERMANENT") + "\n"
                +
                "&7Reason: &c" + reason + "\n" +
                "&7Executor: &c" + executor + "\n" +
                "";


        return message;
    }


}
