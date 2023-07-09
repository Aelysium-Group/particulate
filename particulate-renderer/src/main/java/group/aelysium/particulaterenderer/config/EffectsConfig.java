package group.aelysium.particulaterenderer.config;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.lib.EffectService;
import group.aelysium.particulaterenderer.lib.effects.*;
import group.aelysium.particulaterenderer.lib.effects.Effect;
import group.aelysium.particulaterenderer.lib.effects.FireworkEffect;
import group.aelysium.particulaterenderer.lib.model.ColorParser;
import group.aelysium.particulaterenderer.lib.model.DeltaLocation;
import org.bukkit.*;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectsConfig extends YAML {
    private static EffectsConfig config;

    private EffectsConfig(File configPointer, String template) {
        super(configPointer, template);
    }

    /**
     * Get the current config.
     * @return The config.
     */
    public static EffectsConfig getConfig() {
        return config;
    }

    /**
     * Create a new config for the proxy, this will delete the old config.
     * @return The newly created config.
     */
    public static EffectsConfig newConfig(File configPointer, String template) {
        config = new EffectsConfig(configPointer, template);
        return EffectsConfig.getConfig();
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
        EffectService service = api.getService(EffectService.class);

        Map<Integer, ConfigurationNode> scenes = new HashMap<>();

        get(this.data,"effects").childrenMap().forEach((key, value) -> {
            String type = this.getNode(value, "type", String.class);
            try {
                switch (type) {
                    case "PARTICLE" -> {
                            DeltaLocation delta;
                            try {
                                delta = new DeltaLocation(
                                        this.getNode(value, "dx", Double.class),
                                        this.getNode(value, "dy", Double.class),
                                        this.getNode(value, "dz", Double.class)
                                );
                            } catch (Exception ignore) {
                                delta = new DeltaLocation(0.0,0.0,0.0);
                            }

                            ParticleEffect particleEffect = new ParticleEffect.Builder()
                                    .setId((int) key)
                                    .setParticle(Particle.valueOf(this.getNode(value, "particle", String.class)))
                                    .setCount(this.getNode(value, "count", Integer.class))
                                    .setDelta(delta)
                                    .setSpeed(this.getNode(value, "speed", Double.class))
                                    .build();
                            service.add(particleEffect);
                        }
                    case "BLOCK" -> {
                        BlockEffect blockEffect = new BlockEffect.Builder()
                                .setId((int) key)
                                .setMaterial(Material.valueOf(this.getNode(value, "material", String.class)))
                                .build();
                        service.add(blockEffect);
                    }
                    case "FIREWORK" -> {
                        FireworkEffect.Builder fireworkBuilder = new FireworkEffect.Builder()
                                .setId((int) key);

                        try {
                            fireworkBuilder.setHasFade(this.getNode(value, "fade", Boolean.class));
                        } catch (Exception ignore) {}
                        try {
                            fireworkBuilder.setHasFlicker(this.getNode(value, "flicker", Boolean.class));
                        } catch (Exception ignore) {}
                        try {
                            fireworkBuilder.setHasTrail(this.getNode(value, "trail", Boolean.class));
                        } catch (Exception ignore) {}
                        try {
                            fireworkBuilder.setLife(this.getNode(value, "life", Integer.class));
                        } catch (Exception ignore) {}

                        try {
                            List<String> stringColors = this.getNode(value, "colors", List.class);
                            for (String color : stringColors)
                                fireworkBuilder.addColor(ColorParser.parse(color));
                        } catch (Exception ignore) { }

                        try {
                            List<String> stringShapes = this.getNode(value, "shapes", List.class);
                            for (String shape : stringShapes)
                                fireworkBuilder.addShape(org.bukkit.FireworkEffect.Type.valueOf(shape));
                        } catch (Exception ignore) {}

                        service.add(fireworkBuilder.build());
                    }
                    case "CRYSTAL" -> {
                        Location target = new Location(
                                Bukkit.getWorld(this.getNode(value, "world", String.class)),
                                this.getNode(value, "x", Double.class),
                                this.getNode(value, "y", Double.class),
                                this.getNode(value, "z", Double.class)
                        );

                        CrystalEffect crystalEffect = new CrystalEffect.Builder()
                                .setId((int) key)
                                .setBeamTarget(target)
                                .build();

                        service.add(crystalEffect);
                    }
                    case "DRAGON" -> {
                        DragonEffect dragonEffect = new DragonEffect.Builder()
                                .setId((int) key)
                                .build();

                        service.add(dragonEffect);
                    }
                    case "POTION" -> {
                        PotionEffect.Builder potionBuilder = new PotionEffect.Builder()
                                .setId((int) key)
                                .setType(PotionEffectType.getByName(this.getNode(value, "potion", String.class)))
                                .setAmplifier(this.getNode(value, "amplifier", Integer.class))
                                .setDuration(this.getNode(value, "duration", Integer.class));

                        try {
                            DeltaLocation delta = new DeltaLocation(
                                    this.getNode(value, "dx", Double.class),
                                    this.getNode(value, "dy", Double.class),
                                    this.getNode(value, "dz", Double.class)
                            );

                            potionBuilder.setArea(delta);
                            potionBuilder.setGlobal(false);
                        } catch (Exception ignore) {
                            potionBuilder.setGlobal(true);
                        }

                        service.add(potionBuilder.build());
                    }
                    case "SCENE" -> {
                        scenes.put((int) key, value);
                    }
                }
            } catch (Throwable e) {
                api.getLogger().log("Failed to load an effect!");
                e.printStackTrace();
            }
        });

        // Process scenes now that all effects are loaded

        scenes.forEach((key, value) -> {
            SceneEffect.Builder sceneBuilder = new SceneEffect.Builder()
                    .setId(key);

            YAML.get(value, "effects").childrenList().forEach(entry -> {
                try {
                    int effectId = entry.getInt();
                    Effect effect = service.find(effectId).orElseThrow();

                    sceneBuilder.addEffect(effect);
                } catch (Exception ignore) {
                    api.getLogger().log("Failed to load the scene "+ key);
                }
            });

            try {
                service.add(sceneBuilder.build());
            } catch (Throwable e) {
                api.getLogger().log("Failed to load scene "+key);
                e.printStackTrace();
            }

            api.getLogger().log("Registered scene "+key);
        });
    }
}
