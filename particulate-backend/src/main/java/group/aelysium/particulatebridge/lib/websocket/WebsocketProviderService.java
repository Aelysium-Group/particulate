package group.aelysium.particulatebridge.lib.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebsocketProviderService extends WebSocketServer {
    public WebsocketProviderService(int port) {
        super(new InetSocketAddress("localhost", port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Hello!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Good bye!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        System.out.println(message);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {

    }
}