package group.aelysium.particulaterenderer.lib;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import org.bukkit.scheduler.BukkitRunnable;

public class EffectRender extends BukkitRunnable {
    private static API api = ParticulateRenderer.getAPI();

    @Override
    public void run() {
        api.getService(RunnerQueueService.class).dump().forEach(entry -> {
            entry.emitter().play(entry.effect());
        });
    }
}
