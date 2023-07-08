package group.aelysium.particulatebridge;

import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.central.Lifecycle;
import group.aelysium.particulatebridge.lib.messager.redis.RedisClient;
import group.aelysium.particulatebridge.lib.messager.redis.RedisService;
import group.aelysium.particulatebridge.lib.messager.redis.RedisSubscriber;
import group.aelysium.particulatebridge.lib.model.Serviceable;
import group.aelysium.particulatebridge.lib.messager.websocket.WebsocketService;

import java.util.HashMap;

/**
 * Bridge the gap between the particulate control dashboard and Redis!
 */
public class ParticulateBridge extends Serviceable {
    private static Lifecycle lifecycle = new Lifecycle();
    private static API api;
    public static API getAPI() {
        return api;
    }
    public static Lifecycle getLifecycle() {
        return lifecycle;
    }
    public ParticulateBridge() {
        super(new HashMap<>());
    }

    public void start() {
        System.out.println("Preparing websocket listener...");

        int port = 8080;

        WebsocketService websocketService = new WebsocketService(port, "nathan".toCharArray());
        this.services.put(WebsocketService.class, websocketService);
        websocketService.start();
        System.out.println("Finished! Listening to websocket on port: "+port);

        System.out.println("Preparing Redis bridge...");
        RedisClient.Builder redisClientbuilder = new RedisClient.Builder()
                .setHost("redis-16309.c1.us-central1-2.gce.cloud.redislabs.com")
                .setPort(16309)
                .setUser("default")
                .setPassword("m3wv3479r6xyz6vwSdaGmc0NUT47Smft")
                .setDataChannel("particulate")
                .setPrivateKey("redis-key".toCharArray());

        RedisService redisService = new RedisService(redisClientbuilder, "redis-key".toCharArray());
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
