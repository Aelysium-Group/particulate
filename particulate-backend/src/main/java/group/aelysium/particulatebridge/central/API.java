package group.aelysium.particulatebridge.central;

import group.aelysium.particulatebridge.ParticulateBridge;
import group.aelysium.particulatebridge.lib.model.Service;

import java.io.InputStream;

public class API {
    protected final ParticulateBridge particulateBridge;

    public API(ParticulateBridge particulateBridge) {
        this.particulateBridge = particulateBridge;
    }

    /**
     * Gets a resource by name and returns it as a stream.
     * @param filename The name of the resource to get.
     * @return The resource as a stream.
     */
    public InputStream getResourceAsStream(String filename)  {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    public String getDataFolder() {
        return "";
    }

    public <S extends Service> S getService(Class<S> type) {
        return this.particulateBridge.getService(type);
    }
}
