package group.aelysium.particulatebridge;

import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.central.Lifecycle;
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
        int port = 8080;

        WebsocketService websocketService = new WebsocketService(port, "nathan".toCharArray());
        this.services.put(WebsocketService.class, websocketService);
        websocketService.getServer().run();
    }

    public static void main(String[] args ) {
        ParticulateBridge particulateBridge = new ParticulateBridge();

        api = new API(particulateBridge);

        particulateBridge.start();
    }
}
