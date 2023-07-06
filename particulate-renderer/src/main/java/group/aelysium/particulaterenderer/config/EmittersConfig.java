package group.aelysium.particulaterenderer.config;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.EmitterCluster;
import group.aelysium.particulaterenderer.lib.EmitterService;
import group.aelysium.particulaterenderer.lib.LocationParser;

import java.io.File;
import java.util.List;

public class EmittersConfig extends YAML {
    private static EmittersConfig config;

    private EmittersConfig(File configPointer, String template) {
        super(configPointer, template);
    }

    /**
     * Get the current config.
     * @return The config.
     */
    public static EmittersConfig getConfig() {
        return config;
    }

    /**
     * Create a new config for the proxy, this will delete the old config.
     * @return The newly created config.
     */
    public static EmittersConfig newConfig(File configPointer, String template) {
        config = new EmittersConfig(configPointer, template);
        return EmittersConfig.getConfig();
    }

    /**
     * Delete all configs associated with this class.
     */
    public static void empty() {
        config = null;
    }

    @SuppressWarnings("unchecked")
    public void register() throws IllegalStateException {
        API api = ParticulateRenderer.getAPI();
        EmitterService service = api.getService(EmitterService.class);

        get(this.data,"emitters").childrenList().forEach(item -> {
            EmitterCluster cluster = new EmitterCluster(this.getNode(item, "effect-channel", String.class));

            List<String> locations = this.getNode(item, "locations", List.class);
            for (String location : locations) {
                try {
                    cluster.add(LocationParser.from(location));
                } catch (Exception ignore) {

                }
            }

            service.add(cluster);
        });
    }
}
