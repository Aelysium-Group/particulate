package group.aelysium.particulatebridge.lib.messager.handlers;

import group.aelysium.particulatebridge.ParticulateBridge;
import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.lib.messager.messages.DemandToggleOffMessage;
import group.aelysium.particulatebridge.lib.messager.messages.GenericMessage;
import group.aelysium.particulatebridge.lib.messager.redis.RedisService;

public class DemandToggleOffMessageHandler implements Runnable {
    private final DemandToggleOffMessage message;

    public DemandToggleOffMessageHandler(GenericMessage message) {
        this.message = (DemandToggleOffMessage) message;
    }

    @Override
    public void run() {
        API api = ParticulateBridge.getAPI();

        DemandToggleOffMessage demandMessage = DemandToggleOffMessage.from(message.getChannelID(), message.getEffectID());

        api.getService(RedisService.class).publish(demandMessage);
    }
}
