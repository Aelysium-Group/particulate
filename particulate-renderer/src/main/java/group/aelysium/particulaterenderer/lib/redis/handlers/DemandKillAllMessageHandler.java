package group.aelysium.particulaterenderer.lib.redis.handlers;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.RunnerQueueService;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;

public class DemandKillAllMessageHandler implements Runnable {
    public DemandKillAllMessageHandler(GenericMessage message) {
    }

    @Override
    public void run() {
        API api = ParticulateRenderer.getAPI();

        try {
            api.getService(RunnerQueueService.class).clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
