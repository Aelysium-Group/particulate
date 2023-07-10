package group.aelysium.particulaterenderer.central;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.command.CommandParticulate;
import group.aelysium.particulaterenderer.config.DefaultConfig;
import group.aelysium.particulaterenderer.config.EffectsConfig;
import group.aelysium.particulaterenderer.config.EmittersConfig;
import group.aelysium.particulaterenderer.lib.EffectRenderService;
import group.aelysium.particulaterenderer.lib.EffectService;
import group.aelysium.particulaterenderer.lib.EmitterService;
import group.aelysium.particulaterenderer.lib.RunnerQueueService;
import group.aelysium.particulaterenderer.lib.redis.RedisClient;

import java.io.File;

public class Lifecycle {
    protected boolean isRunning = false;

    public boolean isRunning() {
        return this.isRunning;
    }

    public boolean start() {

        loadConfigs();
        loadCommands();

        this.isRunning = true;
        return true;
    }
    public void stop() {
        API api = ParticulateRenderer.getAPI();
        api.killServices();
    }

    protected boolean loadCommands() {
        API api = ParticulateRenderer.getAPI();
        PluginLogger logger = api.getLogger();
        try {

            CommandParticulate.create(api.getCommandManager());

            return true;
        } catch (Exception e) {
            logger.log(e.getMessage());
            return false;
        }
    }

    protected boolean loadConfigs() {
        API api = ParticulateRenderer.getAPI();
        PluginLogger logger = api.getLogger();

        api.registerService(new EmitterService());
        api.registerService(new EffectService());
        api.registerService(new RunnerQueueService());
        api.registerService(new EffectRenderService());
        api.getService(EffectRenderService.class).start();

        logger.log("Starting Renderer...");
        logger.log("Loading config.yml...");
        DefaultConfig defaultConfig = DefaultConfig.newConfig(new File(String.valueOf(api.getDataFolder()), "config.yml"), "paper_config_template.yml");
        if(!defaultConfig.generate())
            throw new IllegalStateException("Unable to load or create config.yml!");
        defaultConfig.register();

        handleDefaultConfig(defaultConfig);
        DefaultConfig.empty();


        logger.log("Loading effects.yml...");
        EffectsConfig effectsConfig = EffectsConfig.newConfig(new File(String.valueOf(api.getDataFolder()), "effects.yml"), "paper_effects_template.yml");
        if(!effectsConfig.generate())
            throw new IllegalStateException("Unable to load or create effects.yml!");
        effectsConfig.register();
        EffectsConfig.empty();

        logger.log("Loading emitters.yml...");
        EmittersConfig emittersConfig = EmittersConfig.newConfig(new File(String.valueOf(api.getDataFolder()), "emitters.yml"), "paper_emitters_template.yml");
        if(!emittersConfig.generate())
            throw new IllegalStateException("Unable to load or create emitters.yml!");
        emittersConfig.register();
        EmittersConfig.empty();

        logger.log("Finished!");
        return true;
    }

    protected boolean handleDefaultConfig(DefaultConfig config) {
        API api = ParticulateRenderer.getAPI();

        // Setup Redis
        RedisClient.Builder redisClientBuilder = new RedisClient.Builder()
                .setHost(config.getRedis_host())
                .setPort(config.getRedis_port())
                .setUser(config.getRedis_user())
                .setPrivateKey(config.getRedis_privateKey())
                .setDataChannel(config.getRedis_dataChannel());

        if(!config.getRedis_password().equals(""))
            redisClientBuilder.setPassword(config.getRedis_password());

        api.setupRedis(redisClientBuilder, config.getRedis_privateKey());

        return true;
    }
}
