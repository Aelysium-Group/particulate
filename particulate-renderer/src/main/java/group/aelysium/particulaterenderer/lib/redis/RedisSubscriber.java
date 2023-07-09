package group.aelysium.particulaterenderer.lib.redis;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.redis.handlers.DemandKillAllMessageHandler;
import group.aelysium.particulaterenderer.lib.redis.handlers.DemandPingMessageHandler;
import group.aelysium.particulaterenderer.lib.redis.handlers.DemandToggleOffMessageHandler;
import group.aelysium.particulaterenderer.lib.redis.handlers.DemandToggleOnMessageHandler;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;
import group.aelysium.particulaterenderer.lib.redis.messages.MessageType;
import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisConnectionStateAdapter;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

import javax.naming.AuthenticationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RedisSubscriber {
    private CountDownLatch lock = new CountDownLatch(0);
    private final RedisClient client;
    public RedisSubscriber(RedisClient client) {
        this.client = client;
        this.client.addListener(new RedisSubscriberListener());
    }

    /**
     * Subscribe to a specific Redis data channel.
     * This method is thread locking.
     */
    public void subscribeToChannel() {
        if(this.lock.getCount() != 0) throw new RuntimeException("Channel subscription is already active for this RedisIO! Either kill it with .shutdow(). Or create a new RedisIO to use!");

        try (StatefulRedisPubSubConnection<String, String> connection = this.client.connectPubSub()) {
            this.lock = new CountDownLatch(1);

            RedisPubSubCommands<String, String> sync = connection.sync();

            connection.addListener(new RedisMessageListener());

            sync.subscribe(this.client.getDataChannel());

            this.lock.await();
        } catch (Exception e) {
            e.printStackTrace();
            this.lock.countDown();
        }
    }

    /**
     * Dispose of all Redis subscriptions and close all open connections.
     * This RedisSubscriber becomes worthless after this is used.
     */
    public void shutdown() {
        this.lock.countDown();
        this.lock.countDown();
        this.lock.countDown();

        try {
            this.client.shutdownAsync(2, 2, TimeUnit.SECONDS);
        } catch (Exception ignore) {}
    }

    /**
     * Called by `.subscribeToChannel()` when a message is received over the data channel.
     * @param rawMessage The message received.
     */
    protected void onMessage(String rawMessage) {
        API api = ParticulateRenderer.getAPI();
        try {
            GenericMessage.Serializer serializer = new GenericMessage.Serializer();
            GenericMessage message = serializer.parseReceived(rawMessage);

            if (!(api.getService(RedisService.class).validatePrivateKey(message.getAuthKey())))
                throw new AuthenticationException("This message has an invalid private key!");

            if(message.getType() == MessageType.DEMAND_PING)       new DemandPingMessageHandler(message).run();
            if(message.getType() == MessageType.DEMAND_TOGGLE_ON)  new DemandToggleOnMessageHandler(message).run();
            if(message.getType() == MessageType.DEMAND_TOGGLE_OFF) new DemandToggleOffMessageHandler(message).run();
            if(message.getType() == MessageType.DEMAND_KILL_ALL)   new DemandKillAllMessageHandler(message).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected class RedisMessageListener extends RedisPubSubAdapter<String, String> {
        @Override
        public void message(String channel, String rawMessage) {
            RedisSubscriber.this.onMessage(rawMessage);
        }
    }

    static class RedisSubscriberListener extends RedisConnectionStateAdapter {
        @Override
        public void onRedisExceptionCaught(RedisChannelHandler<?, ?> connection, Throwable cause) {
            cause.printStackTrace();
        }
    }
}