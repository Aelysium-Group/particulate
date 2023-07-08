package group.aelysium.particulatebridge.lib.messager.handlers;

import group.aelysium.particulatebridge.ParticulateBridge;
import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.lib.messager.messages.DemandMessage;
import group.aelysium.particulatebridge.lib.messager.redis.RedisService;
import group.aelysium.particulatebridge.lib.messager.messages.GenericMessage;

public class DemandPingMessageHandler implements Runnable {
    private final DemandMessage message;

    public DemandPingMessageHandler(GenericMessage message) {
        this.message = (DemandMessage) message;
    }

    @Override
    public void run() {
        API api = ParticulateBridge.getAPI();

        DemandMessage demandMessage = DemandMessage.from(message.getChannelID(), message.getEffectID());

        api.getService(RedisService.class).publish(demandMessage);
    }
}
