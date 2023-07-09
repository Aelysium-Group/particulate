package group.aelysium.particulatebridge.lib.messager.websocket;

import group.aelysium.particulatebridge.ParticulateBridge;
import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.lib.messager.handlers.DemandKillAllMessageHandler;
import group.aelysium.particulatebridge.lib.messager.handlers.DemandPingMessageHandler;
import group.aelysium.particulatebridge.lib.messager.handlers.DemandToggleOffMessageHandler;
import group.aelysium.particulatebridge.lib.messager.handlers.DemandToggleOnMessageHandler;
import group.aelysium.particulatebridge.lib.messager.messages.DemandKillAllMessage;
import group.aelysium.particulatebridge.lib.messager.messages.GenericMessage;
import group.aelysium.particulatebridge.lib.messager.messages.MessageType;
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
        GenericMessage.Serializer serializer = new GenericMessage.Serializer();
        GenericMessage message = serializer.parseReceived(rawMessage);

        try {
            if (!(api.getService(WebsocketService.class).validatePrivateKey(message.getAuthKey())))
                throw new AuthenticationException("This message has an invalid private key!");

            if(message.getType() == MessageType.DEMAND_PING)       new DemandPingMessageHandler(message).run();
            if(message.getType() == MessageType.DEMAND_TOGGLE_ON)  new DemandToggleOnMessageHandler(message).run();
            if(message.getType() == MessageType.DEMAND_TOGGLE_OFF) new DemandToggleOffMessageHandler(message).run();
            if(message.getType() == MessageType.DEMAND_KILL_ALL)   new DemandKillAllMessageHandler(message).run();
        } catch (Exception e) {
            e.printStackTrace();
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