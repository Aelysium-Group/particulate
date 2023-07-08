package group.aelysium.particulaterenderer.lib.effects;

import org.bukkit.Location;
import org.bukkit.Material;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Vector;

public class SceneEffect extends Effect {
    private Vector<WeakReference<Effect>> effects;

    public SceneEffect(int id, Vector<WeakReference<Effect>> effects) {
        super(id);
        this.effects = effects;
    }

    @Override
    public void play(Location location) {
        for (WeakReference<Effect> weakEffect : this.effects) {
            try {
                Objects.requireNonNull(weakEffect.get()).play(location);
            } catch (Exception ignore) {}
        }
    }

    @Override
    public void pause(Location location) {
        for (WeakReference<Effect> weakEffect : this.effects) {
            try {
                Objects.requireNonNull(weakEffect.get()).pause(location);
            } catch (Exception ignore) {}
        }
    }

    public static final class Builder {
        private Integer id;
        private Vector<WeakReference<Effect>> effects = new Vector<>();

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder addEffect(Effect effect) {
            this.effects.add(new WeakReference<>(effect));
            return this;
        }

        public SceneEffect build() throws Throwable {
            if(this.id == null) throw new NullPointerException();
            if(this.effects == null) throw new NullPointerException();
            return new SceneEffect(
                    this.id,
                    this.effects
            );
        }
    }
}
