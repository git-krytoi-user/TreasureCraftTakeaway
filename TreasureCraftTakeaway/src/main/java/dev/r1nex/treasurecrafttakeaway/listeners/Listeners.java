package dev.r1nex.treasurecrafttakeaway.listeners;

import dev.r1nex.treasurecrafttakeaway.StoryType;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.r1nex.treasurecrafttakeaway.Configuration;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class Listeners implements Listener {
    private final TreasureCraftTakeaway plugin;

    public Listeners(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();

        final double x = getSpawnX();
        final double y = getSpawnY();
        final double z = getSpawnZ();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        Location location = event.getClickedBlock().getLocation();
        if (!(location.getX() == x && location.getY() == y && location.getZ() == z)) return;

        plugin.getGui().open(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        final double x = getSpawnX();
        final double y = getSpawnY();
        final double z = getSpawnZ();

        if (!(location.getX() == x && location.getY() == y && location.getZ() == z)) return;

        player.sendMessage(Configuration.BLOCK_BREAK.getLine());
        event.setCancelled(true);
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        if (!plugin.getPlayerData().containsKey(uuid)) {
            if (plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
                plugin.getMySQL().getDataBoxes(uuid);

            return;
        }

        plugin.getPlayerData().remove(uuid);
        if (plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
            plugin.getMySQL().getDataBoxes(uuid);
    }

    @EventHandler
    public void onChatAsync(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = event.message().toString();
        AtomicBoolean is = new AtomicBoolean(false);
        plugin.getBoxes().forEach(string -> {
            if (message.contains(string)) {
                is.set(true);
            }
        });

        if (is.get()) {
            plugin.getStoryGUI().open(player, "box_donate.yml", StoryType.BOX);
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerData().remove(player.getUniqueId());
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
}
