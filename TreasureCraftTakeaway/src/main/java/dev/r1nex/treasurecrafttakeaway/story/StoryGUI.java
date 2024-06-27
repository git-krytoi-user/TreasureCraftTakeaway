package dev.r1nex.treasurecrafttakeaway.story;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.r1nex.treasurecrafttakeaway.Configuration;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.StoryType;
import dev.r1nex.treasurecrafttakeaway.data.StoryData;

import java.util.ArrayList;
import java.util.List;

public class StoryGUI {
    private final TreasureCraftTakeaway plugin;

    public StoryGUI(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, String string, StoryType storyType) {
        if (plugin.getStoryDataList().isEmpty()) {
            player.sendMessage(Configuration.MESSAGES_STORY_EMPTY.getLine());
            return;
        }

        ChestGui chestGui = new ChestGui(6, plugin.color("&eИстория открытий коробок"));
        PaginatedPane storyPane = new PaginatedPane(0, 0, 9, 5);
        List<StoryData> storyDataList = plugin.getSortByData().sort(storyType, string);
        if (storyDataList == null) {
            player.sendMessage(Configuration.MESSAGES_STORY_SORT_EMPTY.getLine());
            return;
        }

        List<ItemStack> itemStackList = new ArrayList<>();
        storyDataList.forEach(storyData -> itemStackList.add(storyData.getItemStack()));
        storyPane.populateWithItemStacks(itemStackList);

        storyPane.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(new GuiItem(new ItemStack(Material.RED_WOOL), event -> {
            if (storyPane.getPage() > 0) {
                storyPane.setPage(storyPane.getPage() - 1);

                chestGui.update();
                player.sendMessage("123");
            }

            event.setCancelled(true);
        }), 0, 0);

        navigation.addItem(new GuiItem(new ItemStack(Material.ARROW), event -> {
            if (storyPane.getPage() < storyPane.getPages() - 1) {
                storyPane.setPage(storyPane.getPage() + 1);

                chestGui.update();
                player.sendMessage("fff");
            }

            event.setCancelled(true);
        }), 8, 0);

        chestGui.addPane(storyPane);
        chestGui.addPane(navigation);
        chestGui.show(player);

        storyDataList.clear();
    }
}
