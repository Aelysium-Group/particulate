package group.aelysium.particulaterenderer;

import group.aelysium.particulaterenderer.lib.effects.Effect;
import org.bukkit.Location;

import java.util.Vector;

public class EmitterCluster {
    private int effectChannel;
    Vector<Location> locations = new Vector<>();

    public void add(Location location) {
        this.locations.add(location);
    }

    public void play(Effect effect) {
        for (Location location : this.locations) {
            effect.play(location);
        }
    }
}
