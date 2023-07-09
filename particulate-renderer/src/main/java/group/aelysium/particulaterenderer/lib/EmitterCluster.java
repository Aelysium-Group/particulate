package group.aelysium.particulaterenderer.lib;

import group.aelysium.particulaterenderer.lib.effects.Effect;
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
        for (Location location : this.locations)
            effect.play(location);
    }

    public void pause(Effect effect) {
        for (Location location : this.locations)
            effect.pause(location);
    }
}
