package dev.r1nex.treasurecrafttakeaway.data;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class GroupData {
    private final String group;
    private final String displayName;
    private final double chance;
    private final ItemStack itemStack;

    public GroupData(String group, String displayName, double chance, ItemStack itemStack) {
        this.group = group;
        this.displayName = displayName;
        this.chance = chance;
        this.itemStack = itemStack;
    }

    public String getGroup() {
        return group;
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public double getChance() {
        return chance;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
