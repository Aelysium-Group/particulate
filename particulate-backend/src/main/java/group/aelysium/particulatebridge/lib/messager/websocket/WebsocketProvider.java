package group.aelysium.particulatebridge.lib.websocket;

import group.aelysium.particulatebridge.ParticulateBridge;
import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.lib.websocket.messages.GenericWebsocketMessage;
import group.aelysium.particulatebridge.lib.websocket.messages.variants.ResponseFailureWebsocketMessage;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import javax.naming.AuthenticationException;
import java.net.InetSocketAddress;

public class WebsocketProvider extends WebSocketServer {
    public WebsocketProvider(int port) {
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
    public void onMessage(WebSocket webSocket, String rawMessage) {
        API api = ParticulateBridge.getAPI();
        GenericWebsocketMessage.Serializer serializer = new GenericWebsocketMessage.Serializer();
        GenericWebsocketMessage message = serializer.parseReceived(rawMessage);

        try {
            if (!(api.getService(WebsocketService.class).validatePrivateKey(message.getAuthKey())))
                throw new AuthenticationException("This message has an invalid private key!");

        } catch (Exception e) {
            e.printStackTrace();
            ResponseFailureWebsocketMessage responseMessage = ResponseFailureWebsocketMessage.from(e.getMessage());
            this.broadcast(responseMessage.toString());
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {

    }
}