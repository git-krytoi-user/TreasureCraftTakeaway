package dev.r1nex.treasurecrafttakeaway.tools;

import org.bukkit.configuration.file.YamlConfiguration;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;

public class ConfigurationTools {
    private final TreasureCraftTakeaway plugin;

    public ConfigurationTools(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
    }

    public void loadConfiguration(String fileName, YamlConfiguration yamlConfiguration) {
        plugin.getYamlConfigurations().put(fileName, yamlConfiguration);
    }

    public YamlConfiguration getYamlConfiguration(String fileName) {
        return plugin.getYamlConfigurations().get(fileName);
    }
}
