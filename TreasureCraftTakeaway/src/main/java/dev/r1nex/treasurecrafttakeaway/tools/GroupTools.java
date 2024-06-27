package dev.r1nex.treasurecrafttakeaway.tools;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.GroupData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GroupTools {
    private final TreasureCraftTakeaway plugin;

    public GroupTools(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
    }

    public int getCountGroups(YamlConfiguration yamlConfiguration) {
        ConfigurationSection items = yamlConfiguration.getConfigurationSection("items");
        assert items != null;
        List<String> list = new ArrayList<>(items.getKeys(false));

        return list.size();
    }

    public List<GroupData> getItemsData(String fileName) {
        List<GroupData> groupDataList = new ArrayList<>();
        YamlConfiguration yamlConfiguration = plugin.getYamlConfigurations().get(fileName);
        ConfigurationSection items = yamlConfiguration.getConfigurationSection("items");
        assert items != null;
        for (String s : items.getKeys(false)) {
            String displayName = plugin.color(yamlConfiguration.getString("items." + s + ".displayName"));
            double chance = yamlConfiguration.getDouble("items." + s + ".chance");
            String item = yamlConfiguration.getString("items." + s + ".item");
            String texture = yamlConfiguration.getString("items." + s + ".custom-head.texture");
            ItemStack itemStack = plugin.getItemManager().getCustomHead(texture, item);
            groupDataList.add(
                    new GroupData(
                            s,
                            displayName,
                            chance,
                            itemStack
                    )
            );
        }

        return groupDataList;
    }

    public GroupData getRandomPrivilege(String fileName) {
        List<GroupData> prizesData = new ArrayList<>();
        YamlConfiguration yamlConfiguration = plugin.getYamlConfigurations().get(fileName);
        ConfigurationSection section = yamlConfiguration.getConfigurationSection("items");
        assert section != null;
        for (String s : section.getKeys(false)) {
            String displayName = plugin.color(yamlConfiguration.getString("items." + s + ".displayName"));
            double chance = yamlConfiguration.getDouble("items." + s + ".chance");
            String item = yamlConfiguration.getString("items." + s + ".item");
            prizesData.add(new GroupData(s, displayName, chance, new ItemStack(Material.valueOf(item))));
        }

        double totalChance = 0.0;
        for (GroupData prizes : prizesData) {
            totalChance += prizes.getChance();
        }

        double randomValue = ThreadLocalRandom.current().nextDouble() * totalChance;
        double cumulativeChance = 0.0;
        for (GroupData groupData : prizesData) {
            cumulativeChance += groupData.getChance();
            if (randomValue <= cumulativeChance) {
                return groupData;
            }
        }

        return prizesData.get(0);
    }
}
