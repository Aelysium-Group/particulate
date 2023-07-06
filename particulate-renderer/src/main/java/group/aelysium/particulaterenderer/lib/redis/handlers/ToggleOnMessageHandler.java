package group.aelysium.particulaterenderer.lib.redis.handlers;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.EffectService;
import group.aelysium.particulaterenderer.lib.EmitterCluster;
import group.aelysium.particulaterenderer.lib.EmitterService;
import group.aelysium.particulaterenderer.lib.RunnerQueueService;
import group.aelysium.particulaterenderer.lib.effects.Effect;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;
import group.aelysium.particulaterenderer.lib.redis.messages.variants.ToggleOnMessage;

public class ToggleOnMessageHandler implements Runnable {
    private final ToggleOnMessage message;

    public ToggleOnMessageHandler(GenericMessage message) {
        this.message = (ToggleOnMessage) message;
    }

    @Override
    public void run() {
        API api = ParticulateRenderer.getAPI();

        try {
            EmitterCluster cluster = api.getService(EmitterService.class).find(message.getChannelID()).orElseThrow();
            Effect effect = api.getService(EffectService.class).find(message.getEffectID()).orElseThrow();

            api.getService(RunnerQueueService.class).queue(effect, cluster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
