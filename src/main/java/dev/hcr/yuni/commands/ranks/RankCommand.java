package dev.hcr.yuni.commands.ranks;

import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class RankCommand {
    private final String command;
    private final String description;
    private final String permission;
    private final String usage;

    private static final Map<String, RankCommand> commandMap = new HashMap<>();
    private static final Map<String, RankCommand> aliasMap = new HashMap<>();

    public RankCommand(String command, String description, String permission, String usage) {
        this.command = command;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
        commandMap.put(command, this);
    }

    public RankCommand(String command, String description, String permission, String usage, String... aliases) {
        this.command = command;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
        commandMap.put(command, this);
        for (String alias : aliases) {
            aliasMap.put(alias, this);
        }
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String label, String[] args);

    /* All static methods below */

    public static RankCommand getCommand(String command) {
        if (aliasMap.containsKey(command)) {
            return aliasMap.get(command);
        }
        return commandMap.get(command);
    }

    public static Set<String> getRegisteredCommands() {
        return commandMap.keySet();
    }
}
