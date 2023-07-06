package group.aelysium.particulaterenderer.lib.effects;

import org.bukkit.*;

public abstract class Effect {
    protected int id;

    public Effect(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public abstract void play(Location location);

    public abstract void pause(Location location);
}
