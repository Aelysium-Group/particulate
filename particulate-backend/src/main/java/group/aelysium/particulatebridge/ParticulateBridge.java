package group.aelysium.particulatebridge;

import group.aelysium.particulatebridge.lib.websocket.WebsocketProviderService;

/**
 * Bridge the gap between the particulate control dashboard and Redis!
 */
public class ParticulateBridge
{
    public static void main( String[] args ) {
        System.out.println("helllo!");

        int port = 8887;

        WebsocketProviderService server = new WebsocketProviderService(port);
        server.run();
    }
}
