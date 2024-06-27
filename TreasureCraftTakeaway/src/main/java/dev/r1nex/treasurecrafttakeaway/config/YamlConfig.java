package dev.r1nex.treasurecrafttakeaway.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;

import java.io.File;
import java.io.IOException;

public class YamlConfig {
    private final TreasureCraftTakeaway plugin;

    public YamlConfig(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
    }

    public void load(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists())
            plugin.saveResource(fileName, false);

        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.options().copyDefaults(true);
        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        plugin.getConfigurationTools().loadConfiguration(fileName, yamlConfiguration);
    }
}
