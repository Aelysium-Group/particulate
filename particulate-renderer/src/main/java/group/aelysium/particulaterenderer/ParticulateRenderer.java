package group.aelysium.particulaterenderer;

import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.central.Lifecycle;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParticulateRenderer extends JavaPlugin implements Listener {
    private static Lifecycle lifecycle;
    private static API api;
    public static API getAPI() {
        return api;
    }
    public static Lifecycle getLifecycle() {
        return lifecycle;
    }

    @Override
    public void onEnable() {
        try {
            api = new API(this, this.getSLF4JLogger());
            lifecycle = new Lifecycle();

            lifecycle.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }
}
