package dev.r1nex.treasurecrafttakeaway.tools;

import com.comphenix.protocol.wrappers.BlockPosition;
import dev.r1nex.treasurecrafttakeaway.animations.*;
import dev.r1nex.treasurecrafttakeaway.wrappers.WrapperPlayServerBlockAction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.r1nex.treasurecrafttakeaway.Configuration;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.GroupData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AnimationTools {
    private final TreasureCraftTakeaway plugin;
    private final YamlConfiguration yamlConfiguration;

    public AnimationTools(TreasureCraftTakeaway plugin, YamlConfiguration yamlConfiguration) {
        this.plugin = plugin;
        this.yamlConfiguration = yamlConfiguration;
    }

    public void start(Player player, String fileName) {
        if (plugin.isCaseOpen) {
            player.sendMessage(Configuration.ALREADY_OPEN.getLine());
            return;
        }

        plugin.isCaseOpen = true;

        final double x = getSpawnX() + 0.5;
        final double y = getSpawnY() + 0.245;
        final double z = getSpawnZ() + 0.5;
        final double yaw = getSpawnYaw();
        final double pitch = getSpawnPitch();
        double peakRotationSpeed = 0.0;
        double peakRotationRadius = 0.0;

        Location center = new Location(
                Bukkit.getWorld("Arena"),
                x,
                y,
                z
        ).clone();
        center.setYaw((float) yaw);
        center.setPitch((float) pitch);

        playExplosion(center);

        switch (Objects.requireNonNull(yamlConfiguration.getString("settings.animation"))) {
            case "ANIMATION-WHEEL": {
                spawnStands(fileName, center, false);
                WheelCircleAnimation wheelCircleAnimation = new WheelCircleAnimation(
                        plugin, yamlConfiguration, peakRotationRadius, peakRotationSpeed, center
                );

                wheelCircleAnimation.runAnimation(
                        player, fileName,
                        yamlConfiguration.getDouble("settings.peakRotationSpeed"),
                        yamlConfiguration.getDouble("settings.peakRotationRadius")
                );
                return;
            }

            case "ANIMATION-FIGURE-EIGHT": {
                spawnStands(fileName, center, false);
                WheelAnimationFigureEight animationFigureEight = new WheelAnimationFigureEight(
                        plugin, yamlConfiguration, peakRotationRadius, peakRotationSpeed, center
                );

                animationFigureEight.runAnimation(
                        player, fileName,
                        yamlConfiguration.getDouble("settings.peakRotationSpeed"),
                        yamlConfiguration.getDouble("settings.peakRotationRadius")
                );
                return;
            }

            case "ANIMATION-HEART": {
                spawnStands(fileName, center, false);
                WheelAnimationHeart animationHeart = new WheelAnimationHeart(
                        plugin, yamlConfiguration, peakRotationRadius, peakRotationSpeed, center
                );

                animationHeart.runAnimation(
                        player, fileName,
                        yamlConfiguration.getDouble("settings.peakRotationSpeed"),
                        yamlConfiguration.getDouble("settings.peakRotationRadius")
                );
                return;
            }

            case "ANIMATION-WHEEL-IRREGULAR": {
                spawnStands(fileName, center, false);
                WheelCircleIrregularAnimation wheelCircleIrregularAnimation = new WheelCircleIrregularAnimation(
                        plugin, yamlConfiguration, peakRotationRadius, peakRotationSpeed, center
                );

                wheelCircleIrregularAnimation.runAnimation(
                        player, fileName,
                        yamlConfiguration.getDouble("settings.peakRotationSpeed"),
                        yamlConfiguration.getDouble("settings.peakRotationRadius")
                );
                return;
            }

            default: {
                spawnStands(fileName, center, false);
                WheelCircleAnimation wheelCircleAnimation = new WheelCircleAnimation(
                        plugin, yamlConfiguration, peakRotationRadius, peakRotationSpeed, center
                );

                wheelCircleAnimation.runAnimation(
                        player, fileName,
                        yamlConfiguration.getDouble("settings.peakRotationSpeed"),
                        yamlConfiguration.getDouble("settings.peakRotationRadius")
                );
            }
        }

        List<String> messageOpen = plugin.getConfiguration().getStringListFromMemory("config.yml", "Messages.messagesRelatedOpeningBox.open");
        messageOpen.forEach(string -> {
            String replace = string.replace("%player%", player.getName());
            plugin.getServer().broadcast(Component.text(plugin.color(replace)));
        });
    }

    public void playExplosion(Location center) {
        boolean explosionEnabled = yamlConfiguration.getBoolean("settings.explosion.enabled");
        if (explosionEnabled) {
            float power = (float) yamlConfiguration.getDouble("settings.explosion.power");
            Objects.requireNonNull(Bukkit.getWorld("world")).createExplosion(center, power);
        }
    }

    public void spawnStands(String fileName, Location center, boolean isOneItem) {
        List<ItemStack> itemStackList = new ArrayList<>();
        List<GroupData> groupDataList = plugin.getGroupTools().getItemsData(fileName);
        int delayTicks = 20; // Задержка в тиках (20 тиков = 1 секунда)

        if (!isOneItem) {
            for (int i = 0; i < plugin.getGroupTools().getCountGroups(yamlConfiguration); i++) {
                int entityId = (int) (Math.random() * Integer.MAX_VALUE);
                itemStackList.add(groupDataList.get(i).getItemStack());
                int random = ThreadLocalRandom.current().nextInt(itemStackList.size());
                ItemStack itemStack = itemStackList.get(random);

                // Планируем создание стенда с задержкой
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    plugin.getArmorStand().spawnArmorStand(center, entityId, itemStack);
                }, (long) i * delayTicks); // Увеличиваем задержку для каждого следующего стенда
            }

            return;
        }

        for (int i = 0; i < 1; i++) {
            int entityId = (int) (Math.random() * Integer.MAX_VALUE);
            // Планируем создание стенда с задержкой
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                plugin.getArmorStand().spawnArmorStand(center, entityId, new ItemStack(Material.DIAMOND_BLOCK));
            }, 0); // Увеличиваем задержку для каждого следующего стенда
        }

        groupDataList.clear();
    }

    public double getSpawnX() {
        return plugin.getConfiguration().getDoubleFromMemory("config.yml", "spawn.X");
    }

    public double getSpawnY() {
        return plugin.getConfiguration().getDoubleFromMemory("config.yml", "spawn.Y");
    }

    public double getSpawnZ() {
        return plugin.getConfiguration().getDoubleFromMemory("config.yml", "spawn.Z");
    }

    public double getSpawnYaw() {
        return plugin.getConfiguration().getDoubleFromMemory("config.yml", "spawn.Yaw");
    }

    public double getSpawnPitch() {
        return plugin.getConfiguration().getDoubleFromMemory("config.yml", "spawn.Pitch");
    }
}
