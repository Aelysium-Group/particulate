package group.aelysium.particulatebridge;

import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.config.DefaultConfig;
import group.aelysium.particulatebridge.lib.messager.redis.RedisClient;
import group.aelysium.particulatebridge.lib.messager.redis.RedisService;
import group.aelysium.particulatebridge.lib.model.Serviceable;
import group.aelysium.particulatebridge.lib.messager.websocket.WebsocketService;

import java.io.File;
import java.util.HashMap;

/**
 * Bridge the gap between the particulate control dashboard and Redis!
 */
public class ParticulateBridge extends Serviceable {
    private static API api;
    public static API getAPI() {
        return api;
    }
    public ParticulateBridge() {
        super(new HashMap<>());
    }

    public void start() {
        System.out.println("Loading config.yml...");
        DefaultConfig defaultConfig = DefaultConfig.newConfig(new File(String.valueOf(api.getDataFolder()), "config.yml"), "config_template.yml");
        if(!defaultConfig.generate())
            throw new IllegalStateException("Unable to load or create config.yml!");
        defaultConfig.register();

        System.out.println("Preparing websocket listener...");

        WebsocketService websocketService = new WebsocketService(defaultConfig.getWebsocket_port(), defaultConfig.getWebsocket_privateKey());
        this.services.put(WebsocketService.class, websocketService);
        websocketService.start();
        System.out.println("Finished! Listening to websocket on port: "+defaultConfig.getWebsocket_port());

        System.out.println("Preparing Redis bridge...");
        RedisClient.Builder redisClientbuilder = new RedisClient.Builder()
                .setHost(defaultConfig.getRedis_host())
                .setPort(defaultConfig.getWebsocket_port())
                .setUser(defaultConfig.getRedis_user())
                .setPassword(defaultConfig.getRedis_password())
                .setDataChannel(defaultConfig.getRedis_dataChannel())
                .setPrivateKey(defaultConfig.getRedis_privateKey());

        RedisService redisService = new RedisService(redisClientbuilder, defaultConfig.getRedis_privateKey());
        this.services.put(RedisService.class, redisService);
        // The bridge doesn't need to listen for messages, it only needs to send them!
        // redisService.start(RedisSubscriber.class);
        System.out.println("Finished! Redis is ready to transmit!");
        System.out.println("Started.");
    }

    public static void main(String[] args ) {
        ParticulateBridge particulateBridge = new ParticulateBridge();

        api = new API(particulateBridge);

        particulateBridge.start();
    }
}
