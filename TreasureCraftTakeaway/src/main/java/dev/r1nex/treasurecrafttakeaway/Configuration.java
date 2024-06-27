package dev.r1nex.treasurecrafttakeaway;

import org.bukkit.plugin.java.JavaPlugin;

public enum Configuration {
    ALREADY_OPEN("Messages.alreadyOpen", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    NO_KEY("Messages.noKey", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    NO_KEYS("Messages.noKeys", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    BLOCK_BREAK("Messages.blockBreak", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    SYNTAX_COMMAND_USAGE("Messages.syntaxCommand.usage", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    SYNTAX_COMMAND_SET("Messages.syntaxCommand.set", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    SYNTAX_COMMAND_GIVE("Messages.syntaxCommand.give", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    SYNTAX_COMMAND_TAKE("Messages.syntaxCommand.take", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    OPENING_POINT_OF_BOX_SET("Messages.openingPointOfBox", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    SPECIFIED_BOX_NOT_FOUND("Messages.specifiedBoxNotFound", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    GIVE_PLAYER_THE_KEYS("Messages.givePlayerTheKeys", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    PLAYER_NOT_KEYS_OF_BOX("Messages.playerNotKeysOfBox", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    TAKE_THE_KEYS_OF_BOX("Messages.takeTheKeysOfBox", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    MESSAGES_RELATED_OPEN("Messages.messagesRelatedOpeningBox.open", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    MESSAGES_RELATED_WON_PRIZE("Messages.messagesRelatedOpeningBox.wonThePrize", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    MESSAGES_PLAYER_HAS_NEVER_LOGGED("Messages.playerHasNeverLogged", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    MESSAGES_STORY_EMPTY("Messages.storyEmpty", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    MESSAGES_STORY_SORT_EMPTY("Messages.storySortEmpty", JavaPlugin.getPlugin(TreasureCraftTakeaway.class)),
    MESSAGES_PLAYER_NOT_CONNECTED("Messages.playerNotConnected", JavaPlugin.getPlugin(TreasureCraftTakeaway.class));

    private final String line;
    private final TreasureCraftTakeaway plugin;

    Configuration(String line, TreasureCraftTakeaway plugin) {
        this.line = line;
        this.plugin = plugin;
    }

    public String getLine() {
        return plugin.color(plugin.getConfiguration().getStringFromMemory("config.yml", line));
    }
}
