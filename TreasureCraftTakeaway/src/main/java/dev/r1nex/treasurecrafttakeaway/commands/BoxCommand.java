package dev.r1nex.treasurecrafttakeaway.commands;

import dev.r1nex.treasurecrafttakeaway.StoryType;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.r1nex.treasurecrafttakeaway.Configuration;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.PlayerData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BoxCommand extends AbstractCommand {

    private final TreasureCraftTakeaway plugin;

    public BoxCommand(TreasureCraftTakeaway plugin) {
        super(plugin, "tct");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(
                    Configuration.SYNTAX_COMMAND_USAGE.getLine()
            );
            return;
        }

        switch (args[0]) {
            case "set": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Эту команду может выполнить только игрок!");
                    return;
                }

                Player player = (Player) sender;
                if (!player.hasPermission("treasure-craft.command.set")) {
                    player.sendMessage(plugin.color("&cУ Вас нет прав!"));
                    return;
                }

                Block block = plugin.getBlockTools().getBlockLookAt(player, 100);

                Location blockLocation = block.getLocation();
                double x = blockLocation.getX();
                double y = blockLocation.getY();
                double z = blockLocation.getZ();
                double yaw = 90;
                double pitch = -90;
                plugin.getConfiguration().setData("spawn.X", x);
                plugin.getConfiguration().setData("spawn.Y", y);
                plugin.getConfiguration().setData("spawn.Z", z);
                plugin.getConfiguration().setData("spawn.Yaw", yaw);
                plugin.getConfiguration().setData("spawn.Pitch", pitch);
                plugin.getConfiguration().setData("spawn.world", player.getWorld().getName());
                try {
                    plugin.getConfig().save(plugin.getDataFolder() + File.separator + "config.yml");
                    plugin.getConfiguration().loadConfigurationsLines();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<String> lines = plugin.getConfiguration().getStringListFromMemory(
                        "config.yml", "spawn.Title"
                );

                Location location = new Location(
                        Bukkit.getWorld(plugin.getConfiguration().getStringFromMemory(
                                "config.yml", "spawn.world")
                        ),
                        plugin.getConfiguration().getDoubleFromMemory("config.yml", "spawn.X") + 0.5,
                        plugin.getConfiguration().getDoubleFromMemory("config.yml", "spawn.Y") + 2.5,
                        plugin.getConfiguration().getDoubleFromMemory("config.yml", "spawn.Z") + 0.5
                );

                if (DHAPI.getHologram("donate_case") == null)
                    DHAPI.createHologram("donate_case", location, lines);

                Hologram hologram = DHAPI.getHologram("donate_case");
                assert hologram != null;
                DHAPI.moveHologram(hologram, location);
                DHAPI.setHologramLines(hologram, lines);

                player.sendMessage(plugin.color(
                                Configuration.OPENING_POINT_OF_BOX_SET.getLine()
                        )
                );
                return;
            }

            case "give": {
                if (!sender.hasPermission("treasure-craft.command.give")) {
                    sender.sendMessage(plugin.color("&cУ Вас нет прав!"));
                    return;
                }

                if (args.length != 4) {
                    sender.sendMessage(Configuration.SYNTAX_COMMAND_GIVE.getLine());
                    return;
                }

                String name = args[1];
                String boxName = args[2];
                if (!new File(plugin.getDataFolder() + File.separator + boxName + ".yml").exists()) {
                    sender.sendMessage(Configuration.SPECIFIED_BOX_NOT_FOUND.getLine()
                            .replaceFirst("%box_name%", boxName)
                    );
                    return;
                }

                int count = Integer.parseInt(args[3]);

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(name);
                if (offlinePlayer == null) {
                    sender.sendMessage(Configuration.MESSAGES_PLAYER_HAS_NEVER_LOGGED.getLine());
                    return;
                }

                HashMap<String, Integer> boxesData = new HashMap<>();
                boxesData.put(boxName, count);
                if (!plugin.getPlayerData().containsKey(offlinePlayer.getUniqueId())) {
                    plugin.getPlayerData().put(
                            offlinePlayer.getUniqueId(),
                            new PlayerData(plugin, boxesData, offlinePlayer.getUniqueId())
                    );
                    if (plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
                        plugin.getMySQL().insertKeys(offlinePlayer.getUniqueId(), boxName, count);

                    sender.sendMessage(Configuration.GIVE_PLAYER_THE_KEYS.getLine()
                            .replaceFirst("%count%", String.valueOf(count))
                            .replaceFirst("%box_name%", boxName)
                    );
                    return;
                }

                sender.sendMessage(Configuration.GIVE_PLAYER_THE_KEYS.getLine()
                        .replaceFirst("%count%", String.valueOf(count))
                        .replaceFirst("%box_name%", boxName)
                );
                PlayerData playerData = plugin.getPlayerData().get(offlinePlayer.getUniqueId());
                playerData.setKey(boxName, count);
                return;
            }

            case "editor": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Эту команду может выполнить только игрок!");
                    return;
                }

                Player player = (Player) sender;
                if (!player.hasPermission("treasure-craft.command.editor")) {
                    player.sendMessage(plugin.color("&cУ Вас нет прав!"));
                    return;
                }

                player.sendMessage(ChatColor.RED + "Временно отключено!");
                return;
            }

            case "story": {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Эту команду может выполнить только игрок!");
                    return;
                }

                if (args.length != 2) {
                    sender.sendMessage(
                            ChatColor.translateAlternateColorCodes(
                                    '&',
                                    "&7[Коробки] &e/tct story [название коробки]")
                    );
                    return;
                }

                String boxName = args[1];
                plugin.getStoryGUI().open(player, boxName, StoryType.BOX);
                return;
            }

            case "debug": {
                plugin.getServer().broadcast(Component.text("123"));
            }
        }
    }

    @Override
    public List<String> completer(CommandSender sender, Command command, String[] args) {
        return null;
    }
}
