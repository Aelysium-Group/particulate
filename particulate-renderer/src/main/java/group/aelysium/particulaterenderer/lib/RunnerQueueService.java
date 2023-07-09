package group.aelysium.particulaterenderer.lib;

import group.aelysium.particulaterenderer.lib.effects.CrystalEffect;
import group.aelysium.particulaterenderer.lib.effects.DragonEffect;
import group.aelysium.particulaterenderer.lib.effects.Effect;
import group.aelysium.particulaterenderer.lib.model.MappingEntry;
import group.aelysium.particulaterenderer.lib.model.Service;

import java.util.Vector;

public class RunnerQueueService extends Service {
    private Vector<MappingEntry> emitterCommand = new Vector<>();

    public void queue(Effect effect, EmitterCluster cluster) {

        // Some sanity blockers
        if(effect instanceof DragonEffect) return;
        if(effect instanceof CrystalEffect) return;
        this.emitterCommand.add(new MappingEntry(cluster, effect));
    }

    public void dequeue(Effect effect, EmitterCluster cluster) {
        this.emitterCommand.remove(new MappingEntry(cluster, effect));
    }

    public void clear() {
        this.emitterCommand.clear();
    }

    public Vector<MappingEntry> dump() {
        return this.emitterCommand;
    }

    @Override
    public void kill() {

    }
}
