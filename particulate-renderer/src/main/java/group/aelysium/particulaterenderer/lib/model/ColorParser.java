package group.aelysium.particulaterenderer.lib.model;

import org.bukkit.Color;

public class ColorParser {
    public static Color parse(String string) {
        string = string.toUpperCase();
        switch (string) {
            case "WHITE":
                return Color.WHITE;
            case "DARK_GRAY":
            case "SILVER":
                return Color.SILVER;
            case "GRAY":
                return Color.GRAY;
            case "BLACK":
                return Color.BLACK;
            case "RED":
                return Color.RED;
            case "DARK_RED":
            case "MAROON":
                return Color.MAROON;
            case "YELLOW":
                return Color.YELLOW;
            case "OLIVE":
                return Color.OLIVE;
            case "LIME":
                return Color.LIME;
            case "GREEN":
                return Color.GREEN;
            case "AQUA":
                return Color.AQUA;
            case "CYAN":
            case "TEAL":
                return Color.TEAL;
            case "BLUE":
                return Color.BLUE;
            case "DARK_BLUE":
            case "NAVY":
                return Color.NAVY;
            case "PINK":
            case "FUCHSIA":
                return Color.FUCHSIA;
            case "PURPLE":
                return Color.PURPLE;
            case "ORANGE":
                return Color.ORANGE;
        }

        return Color.RED;
    }
}
