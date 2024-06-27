package dev.r1nex.treasurecrafttakeaway.tools;

import com.comphenix.protocol.wrappers.BlockPosition;
import dev.r1nex.treasurecrafttakeaway.wrappers.WrapperPlayServerBlockAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class BlockTools {
    public Block getBlockLookAt(Player player, int range) {
        BlockIterator blockIterator = new BlockIterator(player, range);
        Block lastBlock = blockIterator.next();
        while (blockIterator.hasNext()) {
            lastBlock = blockIterator.next();
            if (lastBlock.getType() == Material.AIR) continue;
            break;
        }

        return lastBlock;
    }

    public void setChestOpened(Block block, boolean opened) {
        WrapperPlayServerBlockAction packet = new WrapperPlayServerBlockAction();
        packet.setLocation(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        packet.setByte1(1);
        packet.setByte2(opened ? 1 : 0);
        packet.setBlockType(block.getType());
        int distanceSquared = 64 * 64;
        Location loc = block.getLocation();
        for (Player player : block.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(loc) < distanceSquared) {
                packet.sendPacket(player);
            }
        }
    }
}
