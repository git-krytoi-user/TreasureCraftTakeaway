package dev.r1nex.treasurecrafttakeaway.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StoryData {
    private final Player player;
    private final long millis;
    private final String boxName;
    private final ItemStack itemStack;
    private final String privilege;

    public StoryData(Player player, long millis, String boxName, ItemStack itemStack, String privilege) {
        this.player = player;
        this.millis = millis;
        this.boxName = boxName;
        this.itemStack = itemStack;
        this.privilege = privilege;
    }

    public Player getUser() {
        return player;
    }

    public long getMillis() {
        return millis;
    }

    public String getBoxName() {
        return boxName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getPrivilege() {
        return privilege;
    }
}
