package group.aelysium.particulaterenderer.lib.model;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;

public class PotionParser {
    public static PotionEffectType parse(String string) {
        string = string.toUpperCase();
        return switch (string) {
            case "SLOW" -> PotionEffectType.SLOW;
            case "FAST_DIGGING" -> PotionEffectType.FAST_DIGGING;
            case "SLOW_DIGGING" -> PotionEffectType.SLOW_DIGGING;
            case "INCREASE_DAMAGE" -> PotionEffectType.INCREASE_DAMAGE;
            case "HEAL" -> PotionEffectType.HEAL;
            case "HARM" -> PotionEffectType.HARM;
            case "JUMP" -> PotionEffectType.JUMP;
            case "CONFUSION" -> PotionEffectType.CONFUSION;
            case "REGENERATION" -> PotionEffectType.REGENERATION;
            case "DAMAGE_RESISTANCE" -> PotionEffectType.DAMAGE_RESISTANCE;
            case "FIRE_RESISTANCE" -> PotionEffectType.FIRE_RESISTANCE;
            case "WATER_BREATHING" -> PotionEffectType.WATER_BREATHING;
            case "INVISIBILITY" -> PotionEffectType.INVISIBILITY;
            case "BLINDNESS" -> PotionEffectType.BLINDNESS;
            case "NIGHT_VISION" -> PotionEffectType.NIGHT_VISION;
            case "HUNGER" -> PotionEffectType.HUNGER;
            case "WEAKNESS" -> PotionEffectType.WEAKNESS;
            case "POISON" -> PotionEffectType.POISON;
            case "WITHER" -> PotionEffectType.WITHER;
            case "HEALTH_BOOST" -> PotionEffectType.HEALTH_BOOST;
            case "ABSORPTION" -> PotionEffectType.ABSORPTION;
            case "SATURATION" -> PotionEffectType.SATURATION;
            case "GLOWING" -> PotionEffectType.GLOWING;
            case "LEVITATION" -> PotionEffectType.LEVITATION;
            case "LUCK" -> PotionEffectType.LUCK;
            case "UNLUCK" -> PotionEffectType.UNLUCK;
            case "SLOW_FALLING" -> PotionEffectType.SLOW_FALLING;
            case "CONDUIT_POWER" -> PotionEffectType.CONDUIT_POWER;
            case "DOLPHINS_GRACE" -> PotionEffectType.DOLPHINS_GRACE;
            case "BAD_OMEN" -> PotionEffectType.BAD_OMEN;
            case "HERO_OF_THE_VILLAGE" -> PotionEffectType.HERO_OF_THE_VILLAGE;
            case "DARKNESS" -> PotionEffectType.DARKNESS;

            default -> PotionEffectType.SPEED;
        };

    }
}
