package group.aelysium.particulatebridge.lib.messager.handlers;

import group.aelysium.particulatebridge.ParticulateBridge;
import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.lib.messager.messages.DemandKillAllMessage;
import group.aelysium.particulatebridge.lib.messager.messages.DemandToggleOnMessage;
import group.aelysium.particulatebridge.lib.messager.messages.GenericMessage;
import group.aelysium.particulatebridge.lib.messager.redis.RedisService;

public class DemandKillAllMessageHandler implements Runnable {

    public DemandKillAllMessageHandler(GenericMessage message) {
    }

    @Override
    public void run() {
        API api = ParticulateBridge.getAPI();

        api.getService(RedisService.class).publish(new DemandKillAllMessage());
    }
}
