package group.aelysium.particulaterenderer.central;

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.lib.EffectRenderService;
import group.aelysium.particulaterenderer.lib.EffectService;
import group.aelysium.particulaterenderer.lib.EmitterService;
import group.aelysium.particulaterenderer.lib.RunnerQueueService;
import group.aelysium.particulaterenderer.lib.model.Service;
import group.aelysium.particulaterenderer.lib.model.Serviceable;
import group.aelysium.particulaterenderer.lib.redis.RedisClient;
import group.aelysium.particulaterenderer.lib.redis.RedisService;
import group.aelysium.particulaterenderer.lib.redis.RedisSubscriber;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.SyncFailedException;
import java.util.HashMap;
import java.util.function.Function;

public class API extends Serviceable {
    private PaperCommandManager<CommandSender> commandManager;
    protected final ParticulateRenderer plugin;
    protected final PluginLogger pluginLogger;


    public API(ParticulateRenderer plugin, Logger logger) throws Exception {
        super(new HashMap<>());

        this.plugin = plugin;
        this.pluginLogger = new PluginLogger(logger);

        this.commandManager = new PaperCommandManager<>(
                plugin,
                AsynchronousCommandExecutionCoordinator.<CommandSender>builder().build(),
                Function.identity(),
                Function.identity()
        );
    }

    public void setupRedis(RedisClient.Builder clientBuilder, char[] privateKey) {
        RedisService service = new RedisService(clientBuilder, privateKey);
        this.services.put(RedisService.class, service);

        service.start(RedisSubscriber.class);
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

    public void registerService(Service service) {
        this.services.put(service.getClass(), service);
    }
}
