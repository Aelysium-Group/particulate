package group.aelysium.particulatebridge.central;

import group.aelysium.particulatebridge.lib.exception.DuplicateLifecycleException;

public class Lifecycle {
    protected boolean isRunning = false;

    public boolean isRunning() {
        return this.isRunning;
    }

    public boolean start() throws DuplicateLifecycleException {
        return true;
    }
    public void stop() {}

    protected boolean loadConfigs() {
        return true;
    }
}
