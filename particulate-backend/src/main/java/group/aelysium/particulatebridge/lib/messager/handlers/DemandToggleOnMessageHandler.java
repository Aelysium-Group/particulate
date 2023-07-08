package group.aelysium.particulatebridge.lib.messager.handlers;

import group.aelysium.particulatebridge.ParticulateBridge;
import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.lib.messager.messages.DemandMessage;
import group.aelysium.particulatebridge.lib.messager.messages.DemandToggleOnMessage;
import group.aelysium.particulatebridge.lib.messager.messages.GenericMessage;
import group.aelysium.particulatebridge.lib.messager.redis.RedisService;

public class DemandToggleOnMessageHandler implements Runnable {
    private final DemandToggleOnMessage message;

    public DemandToggleOnMessageHandler(GenericMessage message) {
        this.message = (DemandToggleOnMessage) message;
    }

    @Override
    public void run() {
        API api = ParticulateBridge.getAPI();

        DemandToggleOnMessage demandMessage = DemandToggleOnMessage.from(message.getChannelID(), message.getEffectID());

        api.getService(RedisService.class).publish(demandMessage);
    }
}
