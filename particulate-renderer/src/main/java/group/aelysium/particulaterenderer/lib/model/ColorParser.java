package group.aelysium.particulaterenderer.lib.model;

import org.bukkit.Color;

public class ColorParser {
    public static Color parse(String string) {
        string = string.toUpperCase();
        return switch (string) {
            case "WHITE" -> Color.WHITE;
            case "DARK_GRAY", "SILVER" -> Color.SILVER;
            case "GRAY" -> Color.GRAY;
            case "BLACK" -> Color.BLACK;
            case "RED" -> Color.RED;
            case "DARK_RED", "MAROON" -> Color.MAROON;
            case "YELLOW" -> Color.YELLOW;
            case "OLIVE" -> Color.OLIVE;
            case "LIME" -> Color.LIME;
            case "GREEN" -> Color.GREEN;
            case "AQUA" -> Color.AQUA;
            case "CYAN", "TEAL" -> Color.TEAL;
            case "BLUE" -> Color.BLUE;
            case "DARK_BLUE", "NAVY" -> Color.NAVY;
            case "PINK", "FUCHSIA" -> Color.FUCHSIA;
            case "PURPLE" -> Color.PURPLE;
            case "ORANGE" -> Color.ORANGE;
            default -> Color.RED;
        };

    }
}
