package dev.r1nex.treasurecrafttakeaway.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.ItemData;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class EditorGUI {
    private final TreasureCraftTakeaway plugin;
    private final int size;
    private final String title;

    public EditorGUI(TreasureCraftTakeaway plugin, int size, String title) {
        this.plugin = plugin;
        this.size = size;
        this.title = title;
    }

    public void open(Player player) {
        ChestGui chestGui = new ChestGui(size, plugin.color(title));
        for (String s : plugin.getBoxes()) {
            YamlConfiguration boxesConfigurations = plugin.getYamlConfigurations().get(s);
            int offsetX = boxesConfigurations.getInt("settings.offsetX");
            int offsetY = boxesConfigurations.getInt("settings.offsetY");
            String title = plugin.color(boxesConfigurations.getString("settings.title"));

            ItemStack item = plugin.getItemManager().createItem(player,
                    Material.TRIPWIRE_HOOK,
                    title,
                    null,
                    s
            );

            OutlinePane button = new OutlinePane(offsetX, offsetY, 1, 1);
            plugin.getItemDataHashMap().put(button.getUUID(), new ItemData(s));
            button.addItem(new GuiItem(item));
            button.setOnClick(inventoryClickEvent -> {
                openEditor(player, button.getUUID());
                inventoryClickEvent.setCancelled(true);
            });
            chestGui.addPane(button);
        }

        chestGui.show(player);
    }

    public void openEditor(Player player, UUID uuid) {
        YamlConfiguration yamlConfiguration = plugin.getYamlConfigurations().get("gui.yml");

        ChestGui chestGui = new ChestGui(size, plugin.color(title));
        OutlinePane buttonSpeed = new OutlinePane(1, 1, 1, 1);

        ItemStack itemSpeed = plugin.getItemManager().createItem(
                player,
                Material.valueOf(yamlConfiguration.getString("buttons-gui.speed-edit.material")),
                plugin.color(yamlConfiguration.getString("buttons-gui.speed-edit.title")),
                yamlConfiguration.getStringList("buttons-gui.speed-edit.desc"),
                null
        );

        buttonSpeed.addItem(new GuiItem(itemSpeed));
        buttonSpeed.setOnClick(inventoryClickEvent -> {
            ItemData itemData = plugin.getItemDataHashMap().get(uuid);
            String fileName = itemData.getFileName();
            YamlConfiguration yamlButtonSpeed = plugin.getYamlConfigurations().get(fileName);

            if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                double peakRotationSpeed = yamlButtonSpeed.getDouble("settings.peakRotationSpeed");
                double increment = peakRotationSpeed + 0.1;
                yamlButtonSpeed.set("settings.peakRotationSpeed", increment);
                try {
                    yamlButtonSpeed.save(plugin.getDataFolder() + File.separator + fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
                double peakRotationSpeed = yamlButtonSpeed.getDouble("settings.peakRotationSpeed");
                double increment = peakRotationSpeed - 0.1;
                if (increment <= 0.0) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }

                yamlButtonSpeed.set("settings.peakRotationSpeed", increment);
                try {
                    yamlButtonSpeed.save(plugin.getDataFolder() + File.separator + fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            inventoryClickEvent.setCancelled(true);
        });

        chestGui.addPane(buttonSpeed);

        OutlinePane buttonRadius = new OutlinePane(3, 1, 1, 1);

        ItemStack itemRadius = plugin.getItemManager().createItem(
                player,
                Material.valueOf(yamlConfiguration.getString("buttons-gui.radius-edit.material")),
                plugin.color(yamlConfiguration.getString("buttons-gui.radius-edit.title")),
                yamlConfiguration.getStringList("buttons-gui.radius-edit.desc"),
                null
        );

        buttonRadius.addItem(new GuiItem(itemRadius));
        buttonRadius.setOnClick(inventoryClickEvent -> {
            ItemData itemData = plugin.getItemDataHashMap().get(uuid);
            String fileName = itemData.getFileName();
            YamlConfiguration yamlButtonRadius = plugin.getYamlConfigurations().get(fileName);

            if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                double peakRotationRadius = yamlButtonRadius.getDouble("settings.peakRotationRadius");
                double increment = peakRotationRadius + 0.1;
                yamlButtonRadius.set("settings.peakRotationRadius", increment);
                try {
                    yamlButtonRadius.save(plugin.getDataFolder() + File.separator + fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
                double peakRotationRadius = yamlButtonRadius.getDouble("settings.peakRotationRadius");
                double increment = peakRotationRadius - 0.1;
                if (increment <= 0.0) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }

                yamlButtonRadius.set("settings.peakRotationRadius", increment);
                try {
                    yamlButtonRadius.save(plugin.getDataFolder() + File.separator + fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            inventoryClickEvent.setCancelled(true);
        });

        chestGui.addPane(buttonRadius);

        OutlinePane buttonExplosion = new OutlinePane(5, 1, 1, 1);

        ItemStack itemExplosion = plugin.getItemManager().createItem(
                player,
                Material.valueOf(yamlConfiguration.getString("buttons-gui.explosion-edit.material")),
                plugin.color(yamlConfiguration.getString("buttons-gui.explosion-edit.title")),
                yamlConfiguration.getStringList("buttons-gui.explosion-edit.desc"),
                null
        );
        buttonExplosion.addItem(new GuiItem(itemExplosion));

        buttonExplosion.setOnClick(inventoryClickEvent -> {
            if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
                ItemData itemData = plugin.getItemDataHashMap().get(uuid);
                String fileName = itemData.getFileName();
                YamlConfiguration yamlButtonExplosion = plugin.getYamlConfigurations().get(fileName);
                boolean aBoolean = yamlButtonExplosion.getBoolean("settings.explosion.enabled");
                aBoolean = !aBoolean;

                yamlButtonExplosion.set("settings.explosion.enabled", aBoolean);
                try {
                    yamlButtonExplosion.save(plugin.getDataFolder() + File.separator + fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                player.sendMessage("Напишите в чат мощность тратила (пример: 0.3/1.0/5.0)");
                player.closeInventory();
            }

            inventoryClickEvent.setCancelled(true);
        });

        chestGui.addPane(buttonExplosion);

        OutlinePane buttonParticle = new OutlinePane(7, 1, 1, 1);

        ItemStack itemParticle = plugin.getItemManager().createItem(
                player,
                Material.valueOf(yamlConfiguration.getString("buttons-gui.particles-edit.material")),
                plugin.color(yamlConfiguration.getString("buttons-gui.particles-edit.title")),
                yamlConfiguration.getStringList("buttons-gui.particles-edit.desc"),
                null
        );
        buttonParticle.addItem(new GuiItem(itemParticle));

        buttonParticle.setOnClick(inventoryClickEvent -> {
            if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
                ItemData itemData = plugin.getItemDataHashMap().get(uuid);
                String fileName = itemData.getFileName();
                YamlConfiguration yamlButtonParticles = plugin.getYamlConfigurations().get(fileName);
                boolean aBoolean = yamlButtonParticles.getBoolean("settings.particles.enabled");
                aBoolean = !aBoolean;

                yamlButtonParticles.set("settings.particles.enabled", aBoolean);
                try {
                    yamlButtonParticles.save(plugin.getDataFolder() + File.separator + fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                player.sendMessage("Напишите в чат название партикла");
                player.closeInventory();
            }

            inventoryClickEvent.setCancelled(true);
        });

        chestGui.addPane(buttonParticle);
        chestGui.show(player);
    }
}
