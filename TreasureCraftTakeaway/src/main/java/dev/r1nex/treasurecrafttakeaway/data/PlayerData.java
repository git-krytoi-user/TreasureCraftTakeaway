package dev.r1nex.treasurecrafttakeaway.data;

import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private final HashMap<String, Integer> boxesData;
    private final TreasureCraftTakeaway plugin;
    private final UUID uuid;

    public PlayerData(TreasureCraftTakeaway plugin, HashMap<String, Integer> boxesData, UUID uuid) {
        this.plugin = plugin;
        this.boxesData = boxesData;
        this.uuid = uuid;
    }

    public void takeKey(String boxName, int count) {
        if (!boxesData.containsKey(boxName)) return;
        int currentCount = boxesData.get(boxName);
        boxesData.replace(boxName, currentCount - count);

        if (plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
            plugin.getMySQL().updateKeys(uuid, boxName, currentCount - count);
    }

    public void setKey(String boxName, int count) {
        if (!boxesData.containsKey(boxName)) {
            boxesData.put(boxName, count);
        }

        if (plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
            plugin.getMySQL().insertKeys(uuid, boxName, count);

        boxesData.replace(boxName, count);
    }

    public boolean isValidBox(String boxName) {
        return boxesData.containsKey(boxName);
    }

    public int getKeys(String boxName) {
        if (!isValidBox(boxName))
            return 0;

        return boxesData.get(boxName);
    }
}
