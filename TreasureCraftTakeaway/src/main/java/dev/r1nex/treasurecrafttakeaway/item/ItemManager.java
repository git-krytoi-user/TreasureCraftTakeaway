package dev.r1nex.treasurecrafttakeaway.item;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.PlayerData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemManager {
    private final TreasureCraftTakeaway plugin;

    public ItemManager(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem(Player player, Material material, String name, List<String> lore, String fileName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (name != null)
            meta.displayName(Component.text(plugin.color(name)));

        if (lore != null && !lore.isEmpty()) {
            List<Component> components = new ArrayList<>(lore.size());
            int count;
            if (fileName != null && !fileName.isEmpty()) {
                UUID uuid = player.getUniqueId();
                StringBuilder stringBuilder = new StringBuilder(fileName);
                int dotIndex = stringBuilder.lastIndexOf(".yml");
                if (dotIndex != -1) {
                    stringBuilder.delete(dotIndex, dotIndex + 4);
                }

                if (plugin.getPlayerData().containsKey(uuid)) {
                    PlayerData playerData = plugin.getPlayerData().get(uuid);
                    count = playerData.getKeys(stringBuilder.toString());
                } else {
                    count = 0;
                }
            } else {
                count = 0;
            }

            lore.forEach(s -> components.add(
                    Component.text(ChatColor.translateAlternateColorCodes('&', s
                            .replaceFirst("%keys%", String.valueOf(count)))
                    )
            ));

            meta.lore(components);
        }

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getCustomHead(String texture, String material) {
        WrappedGameProfile prof = new WrappedGameProfile(UUID.randomUUID(), null);
        prof.getProperties().clear();
        prof.getProperties().put("textures", new WrappedSignedProperty("texture", texture, null));

        ItemStack head = new ItemStack(Material.valueOf(material));
        ItemMeta headMeta = head.getItemMeta();

        if (Objects.equals(material, "PLAYER_HEAD")) {
            try {
                Field profileField = headMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(headMeta, prof.getHandle());
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        head.setItemMeta(headMeta);

        return head;
    }
}
