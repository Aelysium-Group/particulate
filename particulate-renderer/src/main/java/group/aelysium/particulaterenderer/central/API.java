package group.aelysium.particulaterenderer.central;

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import group.aelysium.particulaterenderer.ParticulateRenderer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.SyncFailedException;
import java.util.function.Function;

public class API {
    private PaperCommandManager<CommandSender> commandManager;
    protected final ParticulateRenderer plugin;
    protected final PluginLogger pluginLogger;


    public API(ParticulateRenderer plugin, Logger logger) throws Exception {
        this.plugin = plugin;
        this.pluginLogger = new PluginLogger(logger);

        this.commandManager = new PaperCommandManager<>(
                plugin,
                AsynchronousCommandExecutionCoordinator.<CommandSender>builder().build(),
                Function.identity(),
                Function.identity()
        );
    }

    public InputStream getResourceAsStream(String filename)  {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    public BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }

    public PluginLogger getLogger() {
        return this.pluginLogger;
    }

    public String getDataFolder() {
        return plugin.getDataFolder().getPath();
    }

    /**
     * Get the paper server
     */
    public Server getServer() {
        return this.plugin.getServer();
    }

    /**
     * Attempt to access the plugin instance directly.
     * @return The plugin instance.
     * @throws SyncFailedException If the plugin is currently running.
     */
    public ParticulateRenderer accessPlugin() throws SyncFailedException {
        if(ParticulateRenderer.getLifecycle().isRunning()) throw new SyncFailedException("You can't get the plugin instance while the plugin is running!");
        return this.plugin;
    }

    public PaperCommandManager<CommandSender> getCommandManager() {
        return commandManager;
    }
}
