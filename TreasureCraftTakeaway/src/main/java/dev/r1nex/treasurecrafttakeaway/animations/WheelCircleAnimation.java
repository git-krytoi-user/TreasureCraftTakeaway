package dev.r1nex.treasurecrafttakeaway.animations;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.GroupData;
import dev.r1nex.treasurecrafttakeaway.data.StoryData;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class WheelCircleAnimation {
    private final TreasureCraftTakeaway plugin;
    private final YamlConfiguration yamlConfiguration;
    private double radiusRotation;
    private double speedRotation;
    private final Location location;
    private boolean isOneItem = false;

    public WheelCircleAnimation(TreasureCraftTakeaway plugin, YamlConfiguration yamlConfiguration, double radiusRotation,
                                double speedRotation, Location location) {
        this.plugin = plugin;
        this.yamlConfiguration = yamlConfiguration;
        this.radiusRotation = radiusRotation;
        this.speedRotation = speedRotation;
        this.location = location;
    }

    public void runAnimation(Player player, String fileName, double peakSpeed, double peakRadius) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (plugin.getConfiguration().getBooleanFromMemory("config.yml", "Settings-rotation.stopOperationRotateIfPlayerLeft") && !player.isOnline()) {
                    GroupData groupData = plugin.getGroupTools().getRandomPrivilege(fileName);
                    broadcast(player, groupData);
                    plugin.getEntityIds().forEach(entityIds -> plugin.getArmorStand().deleteArmorStand(entityIds));
                    plugin.getEntityIds().clear();
                    plugin.isCaseOpen = false;
                    cancel();
                    return;
                }

                if (plugin.getEntityIds().isEmpty()) {
                    cancel();
                    return;
                }

                ticks++;
                final Location loc = location.clone();
                final double offset = 2 * Math.PI / plugin.getGroupTools().getCountGroups(yamlConfiguration);
                double angle = Math.toRadians(ticks / 20.0 * speedRotation * 2 * Math.PI);

                for (int entityIds : plugin.getEntityIds()) {
                    double x = 0.5 * radiusRotation * Math.cos(angle);
                    double y = 0.5 * radiusRotation * Math.sin(angle);

                    Vector rotationAxis = loc.getDirection().crossProduct(new Vector(0, 1, 0)).normalize();
                    Location newLoc = location.clone().add(
                            rotationAxis.multiply(x).add(loc.getDirection().multiply(y))
                    );

                    plugin.getArmorStand().teleportArmorStand(newLoc, entityIds, yamlConfiguration);

                    if (!(speedRotation >= peakSpeed) && !isOneItem) {
                        speedRotation += 0.01;
                    }

                    if (radiusRotation < peakRadius && !isOneItem) {
                        radiusRotation += 0.01;
                    }

                    angle += offset;
                }

                endItem(player, ticks, fileName);
            }
        }.runTaskTimer(plugin, 5L, 1L);
    }

    public void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            synchronized (WheelCircleAnimation.this) {
                runnable.run();
            }
        });
    }

    public void broadcast(Player player, GroupData groupData) {
        if (player.isOnline()) {
            player.sendTitle(
                    plugin.color("&6&lВаш приз:"),
                    groupData.getDisplayName(),
                    10,
                    70,
                    20
            );
        }

        if (!isBlackListItem(player, groupData.getGroup())) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    Objects.requireNonNull(yamlConfiguration.getString("settings.command"))
                            .replaceFirst("%player%", player.getName())
                            .replaceFirst("%item%", groupData.getGroup())
            );
        }

        List<String> messagePrize = plugin.getConfiguration().getStringListFromMemory("config.yml", "Messages.messagesRelatedOpeningBox.wonThePrize");
        messagePrize.forEach(string -> {
            String replace = string
                    .replace("%player%", player.getName())
                    .replace("%prize%", groupData.getDisplayName()
                    );
            plugin.getServer().broadcast(Component.text(plugin.color(replace)));
        });
    }

    public void endItem(Player player, int ticks, String fileName) {
        if (ticks >= 180) {
            isOneItem = true;
            Queue<Integer> queue = new LinkedList<>(plugin.getEntityIds());
            plugin.getServer().getScheduler().runTaskTimer(plugin, (task) -> {
                if (queue.size() <= 1) {
                    plugin.getBlockTools().setChestOpened(location.getBlock(), true);

                    if (radiusRotation <= -0.1) {
                        int entityId = queue.element();
                        GroupData groupData = plugin.getGroupTools().getRandomPrivilege(fileName);
                        plugin.getArmorStand().setDisplayName(entityId, groupData.getDisplayName());
                        broadcast(player, groupData);

                        Bukkit.getScheduler().cancelTasks(plugin);
                        Bukkit.getScheduler().runTaskTimer(plugin, (bukkitTask) -> {
                            if (queue.isEmpty()) {
                                plugin.getEntityIds().clear();
                                plugin.isCaseOpen = false;
                                Bukkit.getScheduler().cancelTasks(plugin);
                                return;
                            }

                            plugin.getArmorStand().deleteArmorStand(queue.poll());
                            plugin.getEntityIds().clear();
                            plugin.isCaseOpen = false;
                            plugin.getBlockTools().setChestOpened(location.getBlock(), false);

                            plugin.getStoryDataList().add(new StoryData(
                                    player,
                                    System.currentTimeMillis(),
                                    fileName,
                                    new ItemStack(Material.WRITABLE_BOOK),
                                    groupData.getDisplayName())
                            );
                            Bukkit.getScheduler().cancelTasks(plugin);
                        }, 40L, 40L);
                        return;
                    }

                    radiusRotation -= 0.000999 * plugin.getGroupTools().getCountGroups(yamlConfiguration) * Math.PI;
                    return;
                }

                speedRotation -= 0.0001 * 8 * Math.PI;
                plugin.getArmorStand().deleteArmorStand(queue.poll());
            }, 0L,8L);
        }
    }

    public void bounceBack(Player p, Location loc) {
        double dX = loc.getX() - p.getLocation().getX();
        double dY = loc.getY() - p.getLocation().getY();
        double dZ = loc.getZ() - p.getLocation().getZ();
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        double X = Math.sin(pitch) * Math.cos(yaw);
        double Y = Math.sin(pitch) * Math.sin(yaw);
        double Z = Math.cos(pitch);

        Vector vector = new Vector(X, Y, Z);
        p.setVelocity(vector);
    }

    public boolean isBlackListItem(Player player, String item) {
        if (player.hasPermission(Objects.requireNonNull(yamlConfiguration.getString("black-list.bypass"))))
            return false;

        List<String> blackList = yamlConfiguration.getStringList("black-list.list");
        return blackList.contains(item);
    }
}
