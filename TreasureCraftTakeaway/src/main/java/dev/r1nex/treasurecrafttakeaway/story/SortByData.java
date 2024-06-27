package dev.r1nex.treasurecrafttakeaway.story;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.StoryType;
import dev.r1nex.treasurecrafttakeaway.data.StoryData;

import java.util.ArrayList;
import java.util.List;

public class SortByData {
    private final TreasureCraftTakeaway plugin;

    public SortByData(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
    }

    public List<StoryData> sort(StoryType storyType, String string) {
        List<StoryData> storyDataList = new ArrayList<>();

        if (storyType == StoryType.BOX) {
            for (StoryData storyData : plugin.getStoryDataList()) {
                if (storyData.getBoxName().contains(string)) {
                    YamlConfiguration yaml = plugin.getYamlConfigurations().get("gui.yml");
                    Player player = storyData.getUser();
                    String boxName = storyData.getBoxName();
                    String privilege = plugin.color(storyData.getPrivilege());
                    ItemStack itemStack = storyData.getItemStack();
                    long millis = System.currentTimeMillis() - storyData.getMillis();
                    storyDataList.add(new StoryData(player, millis, boxName, itemStack, privilege));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.displayName(Component.text(plugin.color("&eИстория...")));
                    itemMeta.lore(setLore(yaml.getStringList("story-example"), player, boxName, privilege, millis));
                    itemStack.setItemMeta(itemMeta);
                }
            }
        }

        if (storyDataList.isEmpty())
            return null;

        return storyDataList;
    }

    public List<Component> setLore(List<String> lore, Player player, String boxName, String privilege, long millis) {
        YamlConfiguration yaml = plugin.getYamlConfigurations().get(boxName);
        String boxDisplayName = plugin.color(yaml.getString("settings.title"));
        String remainingTimeFormat = getRemainingTimeFormat(millis);

        List<Component> components = new ArrayList<>(lore.size());
        lore.forEach(string -> components.add(Component.text(plugin.color(string)
                .replaceAll("%player%", player.getName())
                .replaceAll("%box%", boxDisplayName)
                .replaceAll("%prize%", privilege)
                .replaceAll("%date%", remainingTimeFormat)))
        );

        return components;
    }

    public String getRemainingTimeFormat(long millis) {
        long second = (millis / 1000) % 60;
        long minute = ((millis / (1000 * 60)) % 60);
        long hour = ((millis / (1000 * 60 * 60)) % 24);

        StringBuilder sb = new StringBuilder();
        if (hour >= 0) {
            sb.append(hour).append(" ч назад. ");
        }

        if (minute >= 0) {
            sb.append(minute).append(" мин назад. ");
        }

        if (second >= 0) {
            sb.append(second).append(" с назад.");
        }

        return sb.toString().trim();
    }
}
