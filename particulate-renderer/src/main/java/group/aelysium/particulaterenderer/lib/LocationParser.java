package group.aelysium.particulaterenderer.lib;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationParser {
    public static Location from(String string) {
        String[] stringArguments = string.split(",");
        return new Location(
                Bukkit.getWorld(stringArguments[3]),
                Double.parseDouble(stringArguments[0]),
                Double.parseDouble(stringArguments[1]),
                Double.parseDouble(stringArguments[2])
        );
    }
}
