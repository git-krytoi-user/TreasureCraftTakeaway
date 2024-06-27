package dev.r1nex.treasurecrafttakeaway.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import dev.r1nex.treasurecrafttakeaway.TreasureCraftTakeaway;
import dev.r1nex.treasurecrafttakeaway.data.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class MySQL {
    private final TreasureCraftTakeaway plugin;

    private final HikariDataSource source;

    public MySQL(TreasureCraftTakeaway plugin, String host, int port, String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.source = new HikariDataSource(config);
        this.plugin = plugin;
    }

    public Connection connection() throws SQLException {
        return source.getConnection();
    }

    public void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            synchronized (MySQL.this) {
                runnable.run();
            }
        });
    }

    public void getDataBoxes(UUID uuid) {
        if (!plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
            return;

        async(() -> {
            HashMap<String, Integer> boxesData = new HashMap<>();
            try (Connection connection = connection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM cases WHERE uuid = ?")) {
                    ps.setString(1, uuid.toString());
                    try (ResultSet resultSet = ps.executeQuery()) {
                        while (resultSet.next()) {
                            String boxName = resultSet.getString("box_name");
                            int boxCount = resultSet.getInt("boxes_count");
                            boxesData.put(boxName, boxCount);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            plugin.getPlayerData().put(uuid, new PlayerData(plugin, boxesData, uuid));
        });
    }

    public void insertKeys(UUID uuid, String boxName, int boxCount) {
        if (!plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
            return;

        if (uuid == null) return;
        if (boxName == null || boxName.isEmpty()) return;
        if (boxCount <= 0) return;

        async(() -> {
            try (Connection connection = connection()) {
                try (PreparedStatement ps = connection.prepareStatement("INSERT INTO cases (uuid, box_name, boxes_count) VALUES (?, ?, ?)")) {
                    ps.setString(1, uuid.toString());
                    ps.setString(2, boxName);
                    ps.setInt(3, boxCount);

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void updateKeys(UUID uuid, String boxName, int boxCount) {
        if (!plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
            return;

        if (boxName == null || boxName.isEmpty()) return;
        if (boxCount <= 0) {
            deleteKeys(uuid, boxName);
            return;
        }

        async(() -> {
            try (Connection connection = connection()) {
                try (PreparedStatement ps = connection.prepareStatement("UPDATE cases SET boxes_count = ? WHERE box_name = ? AND uuid = ?")) {
                    ps.setInt(1, boxCount);
                    ps.setString(2, boxName);
                    ps.setString(3, uuid.toString());

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void deleteKeys(UUID uuid, String boxName) {
        if (!plugin.getConfiguration().getBooleanFromMemory("config.yml", "Database.enabled"))
            return;

        if (uuid == null) return;
        if (boxName == null || boxName.isEmpty()) return;

        async(() -> {
            try (Connection connection = connection()) {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM cases WHERE uuid = ? AND box_name = ?")) {
                    ps.setString(1, uuid.toString());
                    ps.setString(2, boxName);
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
