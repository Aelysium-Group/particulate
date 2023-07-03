package group.aelysium.particulaterenderer.lib;

import group.aelysium.particulaterenderer.EmitterCluster;
import group.aelysium.particulaterenderer.lib.model.Service;

import java.util.Vector;

public class EmitterService extends Service {
    Vector<EmitterCluster> clusters = new Vector<>();

    public void add(EmitterCluster cluster) {
        this.clusters.add(cluster);
    }

    public void remove(EmitterCluster cluster) {
        this.clusters.remove(cluster);
    }

    public void kill() {

    }
}
