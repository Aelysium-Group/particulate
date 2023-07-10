package group.aelysium.particulaterenderer.lib;

import group.aelysium.particulaterenderer.lib.effects.Effect;
import group.aelysium.particulaterenderer.lib.model.Service;

import java.util.Optional;
import java.util.Vector;

public class EffectService extends Service {
    Vector<Effect> effects = new Vector<>();

    public void add(Effect effect) {
        this.effects.add(effect);
    }
    public Optional<Effect> find(int effectId) {
        return this.effects.stream().filter(effect -> effect.getId() == effectId).findFirst();
    }
    public Vector<Effect> getAllEffects() {
        return this.effects;
    }

    public void remove(int effectId) {
        this.effects.removeIf(effect -> effect.getId() == effectId);
    }

    public void kill() {
        this.effects.clear();
    }
}
