package dev.r1nex.treasurecrafttakeaway.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.r1nex.treasurecrafttakeaway.Configuration;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.ItemData;
import dev.r1nex.treasurecrafttakeaway.data.PlayerData;
import dev.r1nex.treasurecrafttakeaway.tools.AnimationTools;

public class Gui {
    private final TreasureCraftTakeaway plugin;
    private final int size;
    private final String title;

    public Gui(TreasureCraftTakeaway plugin, int size, String title) {
        this.plugin = plugin;
        this.size = size;
        this.title = title;
    }

    public void open(Player player) {
        YamlConfiguration yamlConfiguration = plugin.getYamlConfigurations().get("gui.yml");
        ChestGui chestGui = new ChestGui(size, title);

        ItemStack itemStoryLoss = plugin.getItemManager().createItem(player,
                Material.valueOf(yamlConfiguration.getString("buttons-gui.story-loss.material")),
                plugin.color(yamlConfiguration.getString("buttons-gui.story-loss.title")),
                yamlConfiguration.getStringList("buttons-gui.story-loss.desc"),
                "box_donate"
        );


        for (String s : plugin.getBoxes()) {
            YamlConfiguration boxesConfigurations = plugin.getYamlConfigurations().get(s);
            int offsetX = boxesConfigurations.getInt("settings.offsetX");
            int offsetY = boxesConfigurations.getInt("settings.offsetY");
            String title = plugin.color(boxesConfigurations.getString("settings.title"));

            ItemStack item = plugin.getItemManager().createItem(player,
                    Material.TRIPWIRE_HOOK,
                    title,
                    boxesConfigurations.getStringList("settings.desc"),
                    s
            );

            OutlinePane buttonOpen = new OutlinePane(offsetX, offsetY, 1, 1);
            buttonOpen.addItem(new GuiItem(item));
            plugin.getItemDataHashMap().put(buttonOpen.getUUID(), new ItemData(s));
            buttonOpen.setOnClick(inventoryClickEvent -> {
                ItemData itemData = plugin.getItemDataHashMap().get(buttonOpen.getUUID());
                if (!plugin.getPlayerData().containsKey(player.getUniqueId())) {
                    player.sendMessage(Configuration.NO_KEYS.getLine());
                    inventoryClickEvent.setCancelled(true);
                    return;
                }

                PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());
                StringBuilder stringBuilder = new StringBuilder(itemData.getFileName());
                int dotIndex = stringBuilder.lastIndexOf(".yml");
                if (dotIndex != -1) {
                    stringBuilder.delete(dotIndex, dotIndex + 4);
                }

                if (playerData.getKeys(stringBuilder.toString()) <= 0) {
                    player.sendMessage(Configuration.NO_KEY.getLine()
                            .replaceFirst("%box_name%", stringBuilder.toString())
                    );
                    inventoryClickEvent.setCancelled(true);
                    return;
                } else if (!(playerData.getKeys(stringBuilder.toString()) <= 0) && !plugin.isCaseOpen) {
                    playerData.takeKey(stringBuilder.toString(), 1);
                }

                YamlConfiguration box = plugin.getYamlConfigurations().get(itemData.getFileName());
                AnimationTools animation = new AnimationTools(plugin, box);
                animation.start(player, itemData.getFileName());

                player.closeInventory();
                inventoryClickEvent.setCancelled(true);
            });

            chestGui.addPane(buttonOpen);
        }

        OutlinePane background = new OutlinePane(0, 4, 9, 1);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        chestGui.addPane(background);

        OutlinePane storyLoss = new OutlinePane(4, 3, 2, 2);
        storyLoss.addItem(new GuiItem(itemStoryLoss));
        storyLoss.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        chestGui.addPane(storyLoss);

        OutlinePane storyItems = new OutlinePane(0, 5, 9, 1);
        for (int i = 0; i < 9; i++) {
            storyItems.addItem(new GuiItem(new ItemStack(Material.BARRIER)));
        }

        chestGui.addPane(storyItems);
        storyItems.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        chestGui.show(player);
    }
}
