package net.noknt.showcontrol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Maps;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import net.noknt.showcontrol.chat.Chat;
import net.noknt.showcontrol.chat.Mute;
import net.noknt.showcontrol.command.CommandShowControl;
import net.noknt.showcontrol.renderEngine.EffectRender;
import net.noknt.showcontrol.controlPanel.block.OnPlace;
import net.noknt.showcontrol.controlPanel.block.OnBreak;
import net.noknt.showcontrol.database.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class ShowControl extends JavaPlugin implements Listener {
    Chat chat = new Chat();
    Mute mute = new Mute();
    public LinkChannel linkChannel;
    private static ShowControl instance;


    private DataSource dataSource;

    private YamlConfiguration genericYML; // Used for overwriting
    private YamlConfiguration mainConfigYML;
    private YamlConfiguration controlPanelYML;
    private YamlConfiguration emittersYML;
    private YamlConfiguration effectsYML;
    private YamlConfiguration screensYML;
    private YamlConfiguration scenesYML;
    private YamlConfiguration animationsYML;
    public Map<String, Object> mainConfig;
    public Map<String, Object> controlPanel;
    public Map<String, Object> emitters;
    public Map<String, Object> effects;
    public Map<String, Object> screens;
    public Map<String, Object> scenes;
    public Map<String, Object> animations;
    //public Map<String, Animation> generatedAnimations;
    private Plugin plugin;
    private ProtocolManager protocolManager;

    /**
     * linkStatus - Used to test if a link is currently active
     * @params String The link-id
     * @params Boolean If the effect is turned on or not
     */
    public Map<Integer,Integer> linkStatus = Maps.newHashMap();
    public String controlPanelSlot;

    // Getters/Setters
    public static ShowControl getInstance() {
        return instance;
    }


    public void setLinkStatus(Map<Integer, Integer> linkStatus) {
        this.linkStatus = linkStatus;
    }
    public void addLinkStatus(Integer linkStatus1) {
        linkStatus.put(linkStatus1,1);
    }
    public void removeLinkStatus(Integer linkStatus1) {
        linkStatus.remove(linkStatus1);
    }
    public Map<Integer, Integer> getLinkStatus() {
        return this.linkStatus;
    }

    /* YAML FILES */

    public void setYML(YamlConfiguration controlPanelYML, String type) {
        switch (type) {
            case "generic":
                this.genericYML = controlPanelYML;
                break;
            case "mainConfig":
                this.mainConfigYML = controlPanelYML;
                break;
            case "controlPanel":
                this.controlPanelYML = controlPanelYML;
                break;
            case "emitters":
                this.emittersYML = controlPanelYML;
                break;
            case "effects":
                this.effectsYML = controlPanelYML;
                break;
            case "screens":
                this.screensYML = controlPanelYML;
                break;
            case "scenes":
                this.scenesYML = controlPanelYML;
                break;
            case "animations":
                this.animationsYML = controlPanelYML;
                break;
        }
    }
    public YamlConfiguration getYML(String type) {
        switch (type) {
            case "generic":
                return this.genericYML;
            case "mainConfig":
                return this.mainConfigYML;
            case "controlPanel":
                return this.controlPanelYML;
            case "emitters":
                return this.emittersYML;
            case "effects":
                return this.effectsYML;
            case "screens":
                return this.screensYML;
            case "scenes":
                return this.scenesYML;
            case "animations":
                return this.animationsYML;
        }
        return null;
    }

    /* CONFIG MAPS */

    public void setMap(Map<String, Object> map, String type) {
        switch (type) {
            case "mainConfig":
                this.mainConfig = map;
                break;
            case "controlPanel":
                this.controlPanel = map;
                break;
            case "emitters":
                this.emitters = map;
                break;
            case "effects":
                this.effects = map;
                break;
            case "screens":
                this.screens = map;
                break;
            case "scenes":
                this.scenes = map;
                break;
            case "animations":
                this.animations = map;
                break;
        }
    }
    public Map<String, Object> getMap(String type) {
        switch (type) {
            case "mainConfig":
                return this.mainConfig;
            case "controlPanel":
                return this.controlPanel;
            case "emitters":
                return this.emitters;
            case "effects":
                return effects;
            case "screens":
                return screens;
            case "scenes":
                return scenes;
            case "animations":
                return animations;
        }
        return Maps.newHashMap();
    }

    @Override
    public void onEnable() {
        instance = this;

        linkChannel = new LinkChannel(this);

        protocolManager = ProtocolLibrary.getProtocolManager();

        registerConfig();


        try {
            dataSource = initMySQLDataSource();
            initDb();
        } catch (SQLException | IOException throwables) {
            log("Unable to connect to or setup the MySQL server!");
        }

        registerCommands();
        registerEvents();
        log("Starting Effects Render Engine...");
        if(getMap("mainConfig").get("enable-sequencer").equals("false")) {
            EffectRender effectRender = new EffectRender();
            effectRender.runTaskTimer(this, 10, 1);
        } else {
            log("Generating animation frames...");
            log("ERROR! This version of Showcontrol does not support animation! Loading regular EffectRender Engine!");
            EffectRender effectRender = new EffectRender();
            effectRender.runTaskTimer(this, 0, 1);
            /*
            for (Map.Entry<String, Object> effectsLoopMap : ShowControl.getInstance().getMap("effects").entrySet()) {
                if (effectsLoopMap.getKey().matches("effects\\.[0-9]*\\.animation")) { // Only continue if the current entry is an animation option
                    try {
                        generatedAnimations.put((String)effectsLoopMap.getValue(),Effect.getInstance().animate(Effect.builder().location(new Location(Bukkit.getWorlds().get(0),0,0,0)).unwrap(effectsLoopMap.getKey().replaceAll("effects\\.([0-9]*)\\.animation","$1")).build()));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
            TimelineManager timelineManager = new TimelineManager();
            timelineManager.runTaskTimer(this, 0, 1);*/
        }


        log("Started Successfully!");
    }

    @Override
    public void onDisable() {
/*
        http http = new http();
        JsonObject json = new JsonObject();
        json.add("registration", new JsonPrimitive(mainConfig.get("registration-key").toString()));
        String response = http.post("4aabb6cf391a89e94417f97a1b3b6593",json);*/

        log("Shutting down ShowControl...");
    }

    /**
     * Register all of the plugin's commands
     */
    public void registerCommands() {
        this.getCommand("sc").setExecutor(new CommandShowControl());
    }

    /**
     * Register all of the plugin's Event Listeners
     */
    public void registerEvents() {
        PluginManager eventManager = getServer().getPluginManager();

        eventManager.registerEvents(new OnPlace(), this);
        eventManager.registerEvents(new OnBreak(), this);
    }

    /**
     * Register the plugin's config files
     */
    public void registerConfig() {
        createCustomConfig("config.yml");
        setYML(getYML("generic"),"mainConfig");
        setMap(getYML("generic").getValues(true),"mainConfig");

        createCustomConfig("controlPanel.yml");
        setYML(getYML("generic"),"controlPanel");
        setMap(getYML("controlPanel").getValues(true),"controlPanel");

        createCustomConfig("emitters.yml");
        setYML(getYML("generic"),"emitters");
        setMap(getYML("emitters").getValues(true),"emitters");

        createCustomConfig("effects.yml");
        setYML(getYML("generic"),"effects");
        setMap(getYML("effects").getValues(true),"effects");

        createCustomConfig("screens.yml");
        setYML(getYML("generic"),"screens");
        setMap(getYML("screens").getValues(true),"screens");

        createCustomConfig("scenes.yml");
        setYML(getYML("generic"),"scenes");
        setMap(getYML("scenes").getValues(true),"scenes");

        createCustomConfig("animations.yml");
        setYML(getYML("generic"),"animations");
        setMap(getYML("scenes").getValues(true),"animations");

    }

    /**
     * Creates a custom config file in the plugin's data file using a template from resources/config
     * @param configName The name of the config file in resources/config
     * @throws StackTraceElement Thrown if file cannot be loaded after creation
     */
    private void createCustomConfig(String configName) {
        File customConfigFile;
        customConfigFile = new File(getDataFolder(), configName); // Load the custom config from the plugins data file
        log("Searching for "+configName);
        if (!customConfigFile.exists()) { // Check if the custom config actually exists
            log(configName + " could not be found. Making it now!");
            this.saveResource(configName, false); // If it doesn't, create it
            log(configName + " was successfully generated!");
        } else {
            log(configName + " was found!");
        }

        if (customConfigFile.exists()) { // Re-check if the custom config exists
            try {
                setYML(YamlConfiguration.loadConfiguration(customConfigFile),"generic"); // Load the custom config into a variable
            } catch (IllegalArgumentException e) {
                log(configName + " could not be loaded!");
            }
        } else {
            log(configName + " still doesn't exist!");
        }
    }

    public void reloadConfig(String configName) {
        setYML(YamlConfiguration.loadConfiguration(new File(getDataFolder(), configName)),"generic");
    }

    public void saveConfig(String configName, Map<String, Object> configMap) {
        FileConfiguration customFileConfiguration;
        File customConfigFile = new File(getDataFolder(), configName);
        customFileConfiguration = YamlConfiguration.loadConfiguration(customConfigFile);
        for(Map.Entry<String, Object> entry : configMap.entrySet()) {
            customFileConfiguration.set(entry.getKey(),entry.getValue());
        }
        try {
            customFileConfiguration.save(customConfigFile);
        } catch (IOException e) {
            log("Failed to save "+configName); // shouldn't really happen, but save throws the exception
        }
    }

    /**
     * Sends a String to the log under preffix [ShowControl]
     * @param log The text to be logged
     */
    public void log(String log) {
        System.out.println("[ShowControl] " + log);
    }
    public void debugLog(String log) {
        if(mainConfig.get("debug-log")=="TRUE") {
            System.out.println("[ShowControl] " + log);
        }
    }

    /**
     * Establishes a MySQL database connection
     */
    private DataSource initMySQLDataSource() throws SQLException {
        // set credentials

        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName((String) getMap("mainConfig").get("database.host"));
        dataSource.setPortNumber((int) getMap("mainConfig").get("database.port"));
        dataSource.setDatabaseName((String) getMap("mainConfig").get("database.database"));
        dataSource.setUser((String) getMap("mainConfig").get("database.user"));
        dataSource.setPassword((String) getMap("mainConfig").get("database.password"));

        // Test connection
        testDataSource(dataSource);
        return dataSource;
    }

    /**
     * Tests the connection to the provided MySQL server
     * @param dataSource The datasource
     */
    private DataSource testDataSource(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1000)) {
                throw new SQLException("Could not establish database connection.");
            } else {
                return dataSource;
            }
        }
    }

    /**
     * Initializes the database
     */
    private void initDb() throws SQLException, IOException {

        String setup;
        try (InputStream in = getClassLoader().getResourceAsStream("dbsetup.sql")) {
            setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not read db setup file.", e);
            throw e;
        }

        String[] queries = setup.split(";");

        for (String query : queries) {
            if (query.isEmpty()) continue;
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.execute();
            }
        }
        log("Database setup complete.");
    }

    public Connection conn() throws SQLException {
        return dataSource.getConnection();
    }
}
