package group.aelysium.particulaterenderer.lib.redis.handlers;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.EffectService;
import group.aelysium.particulaterenderer.lib.EmitterService;
import group.aelysium.particulaterenderer.lib.RunnerQueueService;
import group.aelysium.particulaterenderer.lib.effects.Effect;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;

import java.util.Vector;

public class DemandKillAllMessageHandler implements Runnable {
    public DemandKillAllMessageHandler(GenericMessage message) {
    }

    @Override
    public void run() {
        API api = ParticulateRenderer.getAPI();

        try {
            api.getService(RunnerQueueService.class).clear();
            Vector<Effect> effects = api.getService(EffectService.class).getAllEffects();
            api.getService(EmitterService.class).getAllEmitters().forEach(emitterCluster -> {
                effects.forEach(emitterCluster::pause);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
