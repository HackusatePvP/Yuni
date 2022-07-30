package dev.hcr.yuni.commands.ranks;

import dev.hcr.yuni.commands.ranks.args.*;

public class RankCommandMap {

    public RankCommandMap() {
        new RankColorCommand();
        new RankCreateCommand();
        new RankHelpCommand();
        new RankPermissionCommand();
        new RankPrefixCommand();
        new RankSuffixCommand();
    }
}
