package group.aelysium.particulatebridge.lib.model;

import group.aelysium.particulatebridge.lib.exception.DisabledServiceException;

public abstract class Service {
    private final boolean enabled;

    public Service(boolean enabled) {
       this.enabled = enabled;
    }

    public abstract void kill();

    protected void throwIfDisabled() {
        if(!this.enabled) throw new DisabledServiceException("Attempted to access a Service which is disabled!");
    }
}
