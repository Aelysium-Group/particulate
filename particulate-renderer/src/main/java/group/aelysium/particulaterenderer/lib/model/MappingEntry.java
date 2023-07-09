package group.aelysium.particulaterenderer.lib.model;

import group.aelysium.particulaterenderer.lib.EmitterCluster;
import group.aelysium.particulaterenderer.lib.effects.Effect;

import java.util.Objects;

public record MappingEntry(EmitterCluster emitter, Effect effect) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MappingEntry that = (MappingEntry) o;
        return (this.emitter.getEffectChannel() == that.emitter().getEffectChannel())
            && (this.effect().getId() == that.effect().getId());
    }
}
