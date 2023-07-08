package group.aelysium.particulaterenderer.lib.redis.handlers;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.EffectService;
import group.aelysium.particulaterenderer.lib.EmitterCluster;
import group.aelysium.particulaterenderer.lib.EmitterService;
import group.aelysium.particulaterenderer.lib.effects.Effect;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;
import group.aelysium.particulaterenderer.lib.redis.messages.variants.DemandMessage;

public class DemandMessageHandler implements Runnable {
    private final DemandMessage message;

    public DemandMessageHandler(GenericMessage message) {
        this.message = (DemandMessage) message;
    }

    @Override
    public void run() {
        API api = ParticulateRenderer.getAPI();

        try {
            EmitterCluster cluster = api.getService(EmitterService.class).find(message.getChannelID()).orElseThrow();
            Effect effect = api.getService(EffectService.class).find(message.getEffectID()).orElseThrow();

            cluster.play(effect);

            cluster.pause(effect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
