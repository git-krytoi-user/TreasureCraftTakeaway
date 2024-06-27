package dev.r1nex.treasurecrafttakeaway.armorstand;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.wrappers.*;

import java.util.Optional;

public class ArmorStand {

    private final TreasureCraftTakeaway plugin;

    public ArmorStand(TreasureCraftTakeaway plugin) {
        this.plugin = plugin;
    }

    public void spawnArmorStand(Location location, int entityId, ItemStack item) {
        WrapperPlayServerSpawnEntityLiving entityLiving = new WrapperPlayServerSpawnEntityLiving();
        WrapperPlayServerEntityEquipment entityEquipment = new WrapperPlayServerEntityEquipment();

        entityLiving.setEntityID(entityId);
        entityLiving.setType(1);
        entityLiving.setX(location.getX());
        entityLiving.setY(location.getY());
        entityLiving.setZ(location.getZ());

        entityEquipment.setEntityID(entityId);
        entityEquipment.setSlotStackPair(EnumWrappers.ItemSlot.HEAD, item);

        entityLiving.broadcastPacket();
        entityEquipment.broadcastPacket();

        setVisibleStand(entityId);
        if (!plugin.getConfiguration().getBooleanFromMemory("config.yml", "Settings-rotation.big-heads"))
            setSmallStand(entityId);

        plugin.getEntityIds().add(entityId);
    }

    public void setVisibleStand(int entityId) {
        WrapperPlayServerEntityMetadata entityMetadata = new WrapperPlayServerEntityMetadata();
        entityMetadata.setEntityID(entityId);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(
                0, WrappedDataWatcher.Registry.get(Byte.class));
        watcher.setObject(visible, (byte) 0x20);
        entityMetadata.setMetadata(watcher.getWatchableObjects());
        entityMetadata.broadcastPacket();
    }

    public void setSmallStand(int entityId) {
        WrapperPlayServerEntityMetadata entityMetadata = new WrapperPlayServerEntityMetadata();
        entityMetadata.setEntityID(entityId);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.WrappedDataWatcherObject small = new WrappedDataWatcher.WrappedDataWatcherObject(
                14, WrappedDataWatcher.Registry.get(Byte.class));
        watcher.setObject(small, (byte) 0x01);
        entityMetadata.setMetadata(watcher.getWatchableObjects());
        entityMetadata.broadcastPacket();
    }

    public void setDisplayName(int entityId, String displayName) {
        WrapperPlayServerEntityMetadata entityMetadata = new WrapperPlayServerEntityMetadata();
        entityMetadata.setEntityID(entityId);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        Optional<?> optional = Optional.of(WrappedChatComponent.fromChatMessage(plugin.color(displayName))[0]
                .getHandle());
        WrappedDataWatcher.WrappedDataWatcherObject nameq = new WrappedDataWatcher.WrappedDataWatcherObject(
                2, WrappedDataWatcher.Registry.getChatComponentSerializer(true));
        watcher.setObject(nameq, optional);

        WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(
                3, WrappedDataWatcher.Registry.get(Boolean.class));
        watcher.setObject(nameVisible, true);
        entityMetadata.setMetadata(watcher.getWatchableObjects());
        entityMetadata.broadcastPacket();
    }

    public void teleportArmorStand(Location location, int entityId, YamlConfiguration yamlConfiguration) {
        WrapperPlayServerEntityTeleport entityTeleport = new WrapperPlayServerEntityTeleport();
        entityTeleport.setEntityID(entityId);
        entityTeleport.setX(location.getX());
        entityTeleport.setY(location.getY());
        entityTeleport.setZ(location.getZ());
        entityTeleport.broadcastPacket();

        plugin.getParticles().spawnParticle(location, yamlConfiguration);
    }

    public void deleteArmorStand(int entityId) {
        WrapperPlayServerEntityDestroy entityDestroy = new WrapperPlayServerEntityDestroy();
        entityDestroy.setEntityIds(new int[]{entityId});
        entityDestroy.broadcastPacket();
    }
}
