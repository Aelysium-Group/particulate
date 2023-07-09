package group.aelysium.particulaterenderer.lib;

import group.aelysium.particulaterenderer.lib.model.Service;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class EmitterService extends Service {
    Vector<EmitterCluster> clusters = new Vector<>();

    public void add(EmitterCluster cluster) {
        this.clusters.add(cluster);
    }

    public Optional<EmitterCluster> find(String effectChannel) {
        return this.clusters.stream().filter(cluster -> cluster.getEffectChannel().equals(effectChannel)).findAny();
    }

    public void remove(EmitterCluster cluster) {
        this.clusters.remove(cluster);
    }

    public void kill() {
        this.clusters.clear();
    }
}
