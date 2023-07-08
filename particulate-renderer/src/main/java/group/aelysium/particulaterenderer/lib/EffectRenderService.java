package group.aelysium.particulaterenderer.lib;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.model.ClockService;
import group.aelysium.particulaterenderer.lib.model.Service;
import group.aelysium.particulaterenderer.lib.redis.RedisSubscriber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EffectRenderService extends ClockService {
    public EffectRenderService() {
        super(3);
    }

    public void start() {
        this.executorService.scheduleAtFixedRate(() -> {
            API api = ParticulateRenderer.getAPI();

            try {
                api.getService(RunnerQueueService.class).dump().forEach(entry -> {
                    entry.emitter().play(entry.effect());
                });
            } catch (Exception ignore) {}
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void kill() {
        try {
            this.executorService.shutdown();
            try {
                if (!this.executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    this.executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.executorService.shutdownNow();
            }
        } catch (Exception ignore) {}
    }
}
