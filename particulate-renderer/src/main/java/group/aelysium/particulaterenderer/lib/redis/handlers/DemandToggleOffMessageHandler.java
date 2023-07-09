package group.aelysium.particulaterenderer.lib.redis.handlers;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.EffectService;
import group.aelysium.particulaterenderer.lib.EmitterCluster;
import group.aelysium.particulaterenderer.lib.EmitterService;
import group.aelysium.particulaterenderer.lib.RunnerQueueService;
import group.aelysium.particulaterenderer.lib.effects.Effect;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;
import group.aelysium.particulaterenderer.lib.redis.messages.variants.DemandToggleOffMessage;

public class DemandToggleOffMessageHandler implements Runnable {
    private final DemandToggleOffMessage message;

    public DemandToggleOffMessageHandler(GenericMessage message) {
        this.message = (DemandToggleOffMessage) message;
    }

    @Override
    public void run() {
        API api = ParticulateRenderer.getAPI();

        try {
            EmitterCluster cluster = api.getService(EmitterService.class).find(message.getChannelID()).orElseThrow();
            Effect effect = api.getService(EffectService.class).find(message.getEffectID()).orElseThrow();

            cluster.pause(effect);
            api.getService(RunnerQueueService.class).dequeue(effect, cluster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
