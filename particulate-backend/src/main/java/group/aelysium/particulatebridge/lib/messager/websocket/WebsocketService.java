package group.aelysium.particulatebridge.lib.websocket;

import group.aelysium.particulatebridge.lib.model.Service;
import group.aelysium.particulatebridge.lib.websocket.messages.GenericWebsocketMessage;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;

import java.util.Arrays;

public class WebsocketService extends Service {
    private char[] privateKey;
    protected WebsocketProvider provider;

    public WebsocketService(int port, char[] privateKey) {
        super(true);
        this.provider = new WebsocketProvider(port);
        this.privateKey = privateKey;
    }

    public WebsocketProvider getServer() {
        return this.provider;
    }

    public void publish(GenericWebsocketMessage message) {
        this.provider.broadcast(message.toString());
    }

    /**
     * Validate a private key.
     * @param privateKey The private key that needs to be validated.
     * @return `true` if the key is valid. `false` otherwise.
     */
    public boolean validatePrivateKey(char[] privateKey) {
        return Arrays.equals(this.privateKey, privateKey);
    }

    @Override
    public void kill() {

    }
}
