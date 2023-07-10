package group.aelysium.particulaterenderer.lib.model;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;

public class ShapeParser {
    public static FireworkEffect.Type parse(String string) {
        string = string.toUpperCase();
        return switch (string) {
            case "LARGE_BALL" -> FireworkEffect.Type.BALL_LARGE;
            case "STAR" -> FireworkEffect.Type.STAR;
            case "BURST" -> FireworkEffect.Type.BURST;
            case "CREEPER" -> FireworkEffect.Type.CREEPER;
            default -> FireworkEffect.Type.BALL;
        };

    }
}
