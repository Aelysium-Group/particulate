package group.aelysium.particulaterenderer.lib;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.lib.effects.Effect;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Vector;

public class EmitterCluster {
    private String effectChannel;
    Vector<Location> locations = new Vector<>();

    public EmitterCluster(String effectChannel) {
        this.effectChannel = effectChannel;
    }

    public String getEffectChannel() {
        return this.effectChannel;
    }

    public void add(Location location) {
        this.locations.add(location);
    }

    public void play(Effect effect) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ParticulateRenderer.getPlugin(ParticulateRenderer.class), () -> {
            for (Location location : this.locations)
                effect.play(location);
        });
    }

    public void pause(Effect effect) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ParticulateRenderer.getPlugin(ParticulateRenderer.class), () -> {
            for (Location location : this.locations)
                effect.pause(location);
        });
    }
}
