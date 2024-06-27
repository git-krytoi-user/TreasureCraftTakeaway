package dev.r1nex.treasurecrafttakeaway.commands;

import org.bukkit.command.*;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    public AbstractCommand(TreasureCraftTakeaway plugin, String command) {
        PluginCommand pluginCommand = plugin.getCommand(command);
        if (pluginCommand == null) return;
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    public abstract void execute(CommandSender sender, Command command, String[] args);

    public abstract List<String> completer(CommandSender sender, Command command, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender, command, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return completer(sender, command, args);
    }
}
