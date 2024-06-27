package dev.r1nex.treasurecrafttakeaway.config;

import org.bukkit.configuration.file.FileConfiguration;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.ConfigData;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Configuration {
    private final FileConfiguration fileConfiguration;
    private final TreasureCraftTakeaway plugin;

    public Configuration(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
        if (!new File(plugin.getDataFolder() + File.separator + "config.yml").exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();
        }
        this.fileConfiguration = plugin.getConfig();
        loadConfigurationsLines();
    }

    public String getStringFromConfig(String path) {
        return plugin.color(fileConfiguration.getString(path));
    }

    public String getStringFromMemory(String fileName, String path) {
        ConfigData configData = plugin.getLinesConfiguration().get(fileName);
        return configData.getStringFromMemory(path);
    }

    public List<String> getStringPathList(String path) {
        return fileConfiguration.getStringList(path);
    }

    public List<String> getStringListFromMemory(String fileName, String path) {
        ConfigData configData = plugin.getLinesConfiguration().get(fileName);
        return configData.getStringListFromMemory(path);
    }

    public double getDoubleFromMemory(String fileName, String path) {
        ConfigData configData = plugin.getLinesConfiguration().get(fileName);
        return configData.getADouble(path);
    }

    public double getDoublePath(String path) {
        return fileConfiguration.getDouble(path);
    }

    public boolean getBooleanPath(String path) {
        return fileConfiguration.getBoolean(path);
    }

    public List<String> getStringListPath(String path) {
        return fileConfiguration.getStringList(path);
    }

    public boolean getBooleanFromMemory(String fileName, String path) {
        ConfigData configData = plugin.getLinesConfiguration().get(fileName);
        return configData.getBooleanFromMemory(path);
    }

    public void setData(String path, Object object) {
        fileConfiguration.set(path, object);
    }

    public void loadConfigurationsLines() {
        plugin.getLinesConfiguration().clear();
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("Messages.alreadyOpen", getStringFromConfig("Messages.alreadyOpen"));
        stringHashMap.put("Messages.noKey", getStringFromConfig("Messages.noKey"));
        stringHashMap.put("Messages.noKeys", getStringFromConfig("Messages.noKeys"));
        stringHashMap.put("Messages.blockBreak", getStringFromConfig("Messages.blockBreak"));
        stringHashMap.put("Messages.openingPointOfBox", getStringFromConfig("Messages.openingPointOfBox"));
        stringHashMap.put("Messages.specifiedBoxNotFound", getStringFromConfig("Messages.specifiedBoxNotFound"));
        stringHashMap.put("Messages.playerHasNeverLogged", getStringFromConfig("Messages.playerHasNeverLogged"));
        stringHashMap.put("Messages.givePlayerTheKeys", getStringFromConfig("Messages.givePlayerTheKeys"));
        stringHashMap.put("Messages.playerNotKeysOfBox", getStringFromConfig("Messages.playerNotKeysOfBox"));
        stringHashMap.put("Messages.takeTheKeysOfBox", getStringFromConfig("Messages.takeTheKeysOfBox"));
        stringHashMap.put("Messages.syntaxCommand.usage", getStringFromConfig("Messages.syntaxCommand.usage"));
        stringHashMap.put("Messages.syntaxCommand.give", getStringFromConfig("Messages.syntaxCommand.give"));
        stringHashMap.put("Messages.syntaxCommand.set", getStringFromConfig("Messages.syntaxCommand.set"));
        stringHashMap.put("Messages.messagesRelatedOpeningBox.open", getStringFromConfig("Messages.messagesRelatedOpeningBox.open"));
        stringHashMap.put("Messages.messagesRelatedOpeningBox.wonThePrize", getStringFromConfig("Messages.messagesRelatedOpeningBox.wonThePrize"));
        stringHashMap.put("spawn.Title", getStringFromConfig("spawn.Title"));
        stringHashMap.put("Messages.playerNotConnected", getStringFromConfig("Messages.playerNotConnected"));
        stringHashMap.put("Database.dbHost", getStringFromConfig("Database.dbHost"));
        stringHashMap.put("Database.dbName", getStringFromConfig("Database.dbName"));
        stringHashMap.put("Database.dbUser", getStringFromConfig("Database.dbUser"));
        stringHashMap.put("Database.dbPassword", getStringFromConfig("Database.dbPassword"));
        stringHashMap.put("Messages.storySortEmpty", getStringFromConfig("Messages.storySortEmpty"));
        stringHashMap.put("Messages.storyEmpty", getStringFromConfig("Messages.storyEmpty"));
        stringHashMap.put("Configurations", getStringFromConfig("Configurations"));
        HashMap<String, Double> doubleHashMap = new HashMap<>();
        doubleHashMap.put("spawn.X", getDoublePath("spawn.X"));
        doubleHashMap.put("spawn.Y", getDoublePath("spawn.Y"));
        doubleHashMap.put("spawn.Z", getDoublePath("spawn.Z"));
        doubleHashMap.put("spawn.Yaw", getDoublePath("spawn.Yaw"));
        doubleHashMap.put("spawn.Pitch", getDoublePath("spawn.Pitch"));
        stringHashMap.put("spawn.world", getStringFromConfig("spawn.world"));
        HashMap<String, List<String>> listHashMap = new HashMap<>();
        listHashMap.put("spawn.Title", getStringPathList("spawn.Title"));
        listHashMap.put("Messages.messagesRelatedOpeningBox.open", getStringListPath("Messages.messagesRelatedOpeningBox.open"));
        listHashMap.put("Messages.messagesRelatedOpeningBox.wonThePrize", getStringListPath("Messages.messagesRelatedOpeningBox.wonThePrize"));
        HashMap<String, Boolean> booleanHashMap = new HashMap<>();
        booleanHashMap.put("Database.enabled", getBooleanPath("Database.enabled"));
        booleanHashMap.put("Settings-rotation.stopOperationRotateIfPlayerLeft", getBooleanPath("Settings-rotation.stopOperationRotateIfPlayerLeft"));
        booleanHashMap.put("Settings-rotation.big-heads", getBooleanPath("Settings-rotation.big-heads"));


        plugin.getLinesConfiguration().put(
                "config.yml",
                new ConfigData(stringHashMap, listHashMap, doubleHashMap, booleanHashMap)
        );
    }
}
