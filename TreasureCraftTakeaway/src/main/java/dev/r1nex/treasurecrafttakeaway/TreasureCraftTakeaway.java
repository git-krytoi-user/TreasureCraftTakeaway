package dev.r1nex.treasurecrafttakeaway;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import dev.r1nex.treasurecrafttakeaway.armorstand.ArmorStand;
import dev.r1nex.treasurecrafttakeaway.commands.BoxCommand;
import dev.r1nex.treasurecrafttakeaway.config.Configuration;
import dev.r1nex.treasurecrafttakeaway.config.YamlConfig;
import dev.r1nex.treasurecrafttakeaway.data.ConfigData;
import dev.r1nex.treasurecrafttakeaway.data.ItemData;
import dev.r1nex.treasurecrafttakeaway.data.PlayerData;
import dev.r1nex.treasurecrafttakeaway.data.StoryData;
import dev.r1nex.treasurecrafttakeaway.gui.EditorGUI;
import dev.r1nex.treasurecrafttakeaway.gui.Gui;
import dev.r1nex.treasurecrafttakeaway.item.ItemManager;
import dev.r1nex.treasurecrafttakeaway.listeners.Listeners;
import dev.r1nex.treasurecrafttakeaway.mysql.MySQL;
import dev.r1nex.treasurecrafttakeaway.particles.Particles;
import dev.r1nex.treasurecrafttakeaway.story.SortByData;
import dev.r1nex.treasurecrafttakeaway.story.StoryGUI;
import dev.r1nex.treasurecrafttakeaway.tools.BlockTools;
import dev.r1nex.treasurecrafttakeaway.tools.ConfigurationTools;
import dev.r1nex.treasurecrafttakeaway.tools.GroupTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class TreasureCraftTakeaway extends JavaPlugin {

    private Configuration configuration;
    private ArmorStand armorStand;
    private GroupTools groupTools;
    private MySQL mySQL;
    private BlockTools blockTools;
    private ItemManager itemManager;
    private ConfigurationTools configurationTools;
    private Particles particles;
    private EditorGUI editorGUI;
    private SortByData sortByData;
    private StoryGUI storyGUI;
    private final List<StoryData> storyDataList = new ArrayList<>();
    private final HashMap<UUID, PlayerData> playerData = new HashMap<>();
    private final HashMap<String, ConfigData> linesConfiguration = new HashMap<>();
    private final HashMap<String, YamlConfiguration> yamlConfigurations = new HashMap<>();
    private final HashMap<UUID, ItemData> itemDataHashMap = new HashMap<>();
    private final List<Integer> entityIds = new ArrayList<>();
    private final List<String> boxes = new ArrayList<>();
    private final List<Player> disablePlayersChat = new ArrayList<>();
    public boolean isCaseOpen = false;
    private Gui gui;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), this);
        new BoxCommand(this);
        YamlConfig yamlConfig = new YamlConfig(this);
        configuration = new Configuration(this);
        armorStand = new ArmorStand(this);
        groupTools = new GroupTools(this);
        blockTools = new BlockTools();
        itemManager = new ItemManager(this);
        configurationTools = new ConfigurationTools(this);
        particles = new Particles();
        sortByData = new SortByData(this);
        storyGUI = new StoryGUI(this);
        if (configuration.getBooleanFromMemory("config.yml", "Database.enabled")) {
            mySQL = new MySQL(
                    this,
                    configuration.getStringFromMemory("config.yml", "Database.dbHost"),
                    3306,
                    configuration.getStringFromMemory("config.yml", "Database.dbName"),
                    configuration.getStringFromMemory("config.yml", "Database.dbUser"),
                    configuration.getStringFromMemory("config.yml", "Database.dbPassword")
            );
        }

        List<String> configurationsList = configuration.getStringPathList("Configurations");
        for (String s : configurationsList) {
            if (s.contains("box_")) {
                boxes.add(s);
            }

            yamlConfig.load(s);
        }

        YamlConfiguration yamlConfigurationGui = configurationTools.getYamlConfiguration("gui.yml");
        if (yamlConfigurationGui == null) {
            Bukkit.getLogger().info("Конфигурация gui.yml не найдена!");
            return;
        }

        gui = new Gui(
                this,
                yamlConfigurationGui.getInt("gui-settings.size"),
                color(yamlConfigurationGui.getString("gui-settings.title"))
        );

        editorGUI = new EditorGUI(this, 6, "&eРедактор коробок");

        List<String> lines = configuration.getStringListFromMemory("config.yml", "spawn.Title");
        Location location = new Location(
                Bukkit.getWorld(configuration.getStringFromMemory("config.yml", "spawn.world")),
                configuration.getDoubleFromMemory("config.yml", "spawn.X") + 0.5,
                configuration.getDoubleFromMemory("config.yml", "spawn.Y") + 2.5,
                configuration.getDoubleFromMemory("config.yml", "spawn.Z") + 0.5
        );

        if (DHAPI.getHologram("donate_case") == null)
            DHAPI.createHologram("donate_case", location, lines);

        Hologram hologram = DHAPI.getHologram("donate_case");
        assert hologram != null;
        DHAPI.moveHologram(hologram, location);
        DHAPI.setHologramLines(hologram, lines);
    }

    @Override
    public void onDisable() {
        entityIds.clear();
        playerData.clear();
        boxes.clear();
        yamlConfigurations.clear();
        itemDataHashMap.clear();
        boxes.clear();
        linesConfiguration.clear();
        storyDataList.clear();
    }

    public String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public HashMap<UUID, PlayerData> getPlayerData() {
        return playerData;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public GroupTools getGroupTools() {
        return groupTools;
    }

    public List<Integer> getEntityIds() {
        return entityIds;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public BlockTools getBlockTools() {
        return blockTools;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public Gui getGui() {
        return gui;
    }

    public HashMap<String, YamlConfiguration> getYamlConfigurations() {
        return yamlConfigurations;
    }

    public ConfigurationTools getConfigurationTools() {
        return configurationTools;
    }

    public List<String> getBoxes() {
        return boxes;
    }

    public HashMap<UUID, ItemData> getItemDataHashMap() {
        return itemDataHashMap;
    }

    public Particles getParticles() {
        return particles;
    }

    public HashMap<String, ConfigData> getLinesConfiguration() {
        return linesConfiguration;
    }

    public EditorGUI getEditorGUI() {
        return editorGUI;
    }

    public List<StoryData> getStoryDataList() {
        return storyDataList;
    }

    public SortByData getSortByData() {
        return sortByData;
    }

    public StoryGUI getStoryGUI() {
        return storyGUI;
    }

    public List<Player> getDisablePlayersChat() {
        return disablePlayersChat;
    }
}
