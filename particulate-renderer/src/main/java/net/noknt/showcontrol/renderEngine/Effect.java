package net.noknt.showcontrol.renderEngine;

import com.google.common.collect.Maps;
import net.noknt.showcontrol.ShowControl;
import net.noknt.showcontrol.sequencer.Animation;
import net.noknt.showcontrol.sequencer.Frame;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Effect {
    private EffectRender effectRender;
    private Type type;
    private Location location;
    private Map<String, Object> arguments;
    private static Effect instance;

    public static Effect getInstance() { return instance; }

    public Effect(Type type, Location location, Map<String,Object> arguments) {
        instance = this;
        this.type = type;
        this.location = location;
        this.arguments = arguments;
    }

    /*public Animation animate(Effect effect) {
        Map<Integer, Frame> frames = new LinkedHashMap<>();
        Map<String, Object> frameArguments;
        Map<String, Object> animationParameters;
        Integer frameCount = 0;

        if(ShowControl.getInstance().getMap("animations").containsKey("animations."+this.arguments.get("animation"))) {
            animationParameters = new LinkedHashMap<>();
            for (Map.Entry<String, Object> animationsLoopMap : ShowControl.getInstance().getMap("animations").entrySet()) {
                if(animationsLoopMap.getKey().matches("animations\\."+this.arguments+".frames.[.A-z0-9]*")) {
                    if(animationsLoopMap.getKey().matches("animations\\.;"+this.arguments+".frames.[0-9]*.delay")) {
                        frameCount = (Integer) animationsLoopMap.getValue() + frameCount;
                    } else if(animationsLoopMap.getKey().matches("animations\\.;"+this.arguments+".frames.[0-9]*.properties.[A-z]*")) {
                        animationParameters.put(animationsLoopMap.getKey().replaceFirst("animations\\."+this.arguments+".frames.([0-9]*).properties.","$1."),animationsLoopMap.getValue());
                    }
                }
            }

        }
        while()

        for (Map.Entry<String, Object> argumentsLoopMap : this.arguments.entrySet()) {
            arguments.put();
        }

        return new Animation(frames,true);
    }*/


    public void run(Effect effect,String name) {
        String world = effect.location.getWorld().getName();
        Collection<Entity> entities;
        Map<String, Object> effectArguments = effect.arguments;
        if (!effectArguments.containsKey("type"))
            ShowControl.getInstance().log("All effects must have a `type` defined!");
        else
        switch (Effect.Type.valueOf((String) effectArguments.get("type"))) {
            case PARTICLE:
                if (effectArguments.containsKey("particle")) {
                    int count = 1;
                    double speed = 0.0;
                    if (effectArguments.containsKey("count")) count = (int )effectArguments.get("count");
                    if (effectArguments.containsKey("speed")) speed = (double) effectArguments.get("speed");
                    if (effectArguments.containsKey("dx") && effectArguments.containsKey("dy") && effectArguments.containsKey("dz"))
                        Bukkit.getWorld(world).spawnParticle(
                                Particle.valueOf(String.valueOf(effectArguments.get("particle"))),
                                effect.location,
                                count,
                                (double) effectArguments.get("dx"),
                                (double) effectArguments.get("dy"),
                                (double) effectArguments.get("dz"),
                                speed

                        );
                    else Bukkit.getWorld(world).spawnParticle(
                            Particle.valueOf((String) effectArguments.get("particle")),
                            effect.location,
                            count,
                            0.0,
                            0.0,
                            0.0,
                            speed
                    );

                } else {
                    ShowControl.getInstance().log("Effect.Type.PARTICLE requires 'particle' to be defined");
                }
                break;
            case BLOCK:
                if (effectArguments.containsKey("material")) {
                    Bukkit.getWorld(world).getBlockAt(effect.location).setType(Material.matchMaterial((String) effectArguments.get("material")));
                } else {
                    ShowControl.getInstance().log("Effect.Type.BLOCK requires 'material'");
                }
                break;
            case FIREWORK:
                Firework firework = (Firework) Bukkit.getWorld(world).spawnEntity(effect.location, EntityType.FIREWORK);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                FireworkEffect.Builder fireworkEffect = FireworkEffect.builder();

                if (effectArguments.containsKey("shape"))
                    fireworkEffect.with(FireworkEffect.Type.valueOf((String) effectArguments.get("shape")));
                else fireworkEffect.with(FireworkEffect.Type.BALL);

                if (effectArguments.containsKey("AQUA"))    fireworkEffect.withColor(Color.AQUA);
                if (effectArguments.containsKey("BLACK"))   fireworkEffect.withColor(Color.BLACK);
                if (effectArguments.containsKey("BLUE"))    fireworkEffect.withColor(Color.BLUE);
                if (effectArguments.containsKey("FUCHSIA")) fireworkEffect.withColor(Color.FUCHSIA);
                if (effectArguments.containsKey("GRAY"))    fireworkEffect.withColor(Color.GRAY);
                if (effectArguments.containsKey("GREEN"))   fireworkEffect.withColor(Color.GREEN);
                if (effectArguments.containsKey("LIME"))    fireworkEffect.withColor(Color.LIME);
                if (effectArguments.containsKey("MAROON"))  fireworkEffect.withColor(Color.MAROON);
                if (effectArguments.containsKey("NAVY"))    fireworkEffect.withColor(Color.NAVY);
                if (effectArguments.containsKey("OLIVE"))   fireworkEffect.withColor(Color.OLIVE);
                if (effectArguments.containsKey("ORANGE"))  fireworkEffect.withColor(Color.ORANGE);
                if (effectArguments.containsKey("PURPLE"))  fireworkEffect.withColor(Color.PURPLE);
                if (effectArguments.containsKey("RED"))     fireworkEffect.withColor(Color.RED);
                if (effectArguments.containsKey("SILVER"))  fireworkEffect.withColor(Color.SILVER);
                if (effectArguments.containsKey("TEAL"))    fireworkEffect.withColor(Color.TEAL);
                if (effectArguments.containsKey("WHITE"))   fireworkEffect.withColor(Color.WHITE);
                if (effectArguments.containsKey("YELLOW"))  fireworkEffect.withColor(Color.YELLOW);

                if (effectArguments.containsKey("flicker"))
                    fireworkEffect.withFlicker();
                if (effectArguments.containsKey("trail"))
                    fireworkEffect.withTrail();
                if (effectArguments.containsKey("fade"))
                    fireworkEffect.withFade();

                if (effectArguments.containsKey("life"))
                    if ((int) effectArguments.get("life") != 0) fireworkMeta.setPower((int) effectArguments.get("life"));

                fireworkMeta.addEffect(fireworkEffect.build());
                firework.setFireworkMeta(fireworkMeta);

                if (effectArguments.containsKey("life"))
                    if ((int) effectArguments.get("life") == 0) detonateFirework(firework);
                break;
            case DRAGON:
                break;
            case CRYSTAL:

                entities = Bukkit.getWorld(world).getNearbyEntities(effect.location, 1, 1, 1);
                for (Entity entity : entities) if (entity.getName().equals("entity.EnderCrystal.name")) return;


                EnderCrystal crystal = (EnderCrystal) Bukkit.getWorld(world).spawnEntity(effect.location, EntityType.ENDER_CRYSTAL);
                Location beamLocation;
                if (effectArguments.containsKey("targetX") || effectArguments.containsKey("targetY") || effectArguments.containsKey("targetZ"))
                    beamLocation = new Location(effect.location.getWorld(), (double) effectArguments.get("targetX"), (double) effectArguments.get("targetY"), (double) effectArguments.get("targetZ"));
                else {
                    ShowControl.getInstance().log("Effect.Type.CRYSTAL requires targetX, targetY, and targetZ");
                    break;
                }
                crystal.setInvulnerable(true);
                crystal.setGravity(false);
                crystal.setBeamTarget(beamLocation);
                crystal.setShowingBottom(false);
                break;
            case LAZER:
                break;
            case STATUS:
                int time = 20;
                int strength = 1;
                if (effectArguments.containsKey("time")) time = (int) effectArguments.get("time");
                if (effectArguments.containsKey("strength")) strength = (int) effectArguments.get("strength");
                if (effectArguments.containsKey("global")) {
                    if ((boolean) effectArguments.get("global"))
                        for (Player player : Bukkit.getWorld(world).getPlayers())
                            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName((String) arguments.get("potion")), time, strength));
                } else {
                    double[] area = new double[]{1.0, 1.0, 1.0};
                    if (effectArguments.containsKey("areaX")) area[0] = (double) effectArguments.get("areaX");
                    if (effectArguments.containsKey("areaY")) area[1] = (double) effectArguments.get("areaY");
                    if (effectArguments.containsKey("areaZ")) area[2] = (double) effectArguments.get("areaZ");
                    entities = Bukkit.getWorld(world).getNearbyEntities(effect.location, area[0], area[1], area[2]);
                    for (Entity entity : entities) {
                        if (entity instanceof Player)
                            Bukkit.getPlayer(entity.getUniqueId()).addPotionEffect(new PotionEffect(PotionEffectType.getByName((String) arguments.get("potion")), time*20, strength));

                    }
                }
                break;

        }
    }

    public void stop(Effect effect) {
        String world = effect.location.getWorld().getName();
        Collection<Entity> entities;
        Map<String, Object> effectArguments = effect.arguments;
        if (!effectArguments.containsKey("type"))
            ShowControl.getInstance().log("All effects must have a `type` defined!");
        else
            switch (Effect.Type.valueOf((String) effectArguments.get("type"))) {
                case BLOCK:
                    if (effectArguments.containsKey("remove-on-complete")) {
                        if((boolean) effectArguments.get("remove-on-complete"))
                            Bukkit.getWorld(world).getBlockAt(effect.location).setType(Material.AIR);
                    }
                    break;
                case CRYSTAL:
                    entities = Bukkit.getWorld(world).getNearbyEntities(effect.location, 1,1,1);
                    for (Entity entity : entities) if (entity.getName().equals("entity.EnderCrystal.name")) entity.remove();
                    break;
                case STATUS:
                    if(effectArguments.containsKey("global")) {
                        if((boolean)effectArguments.get("global"))
                            for (Player player : Bukkit.getWorld(world).getPlayers())
                                player.removePotionEffect(PotionEffectType.getByName((String) effectArguments.get("potion")));
                    } else {
                        double[] area = new double[]{1.0, 1.0, 1.0};
                        if (effectArguments.containsKey("areaX")) area[0] = (double) effectArguments.get("areaX");
                        if (effectArguments.containsKey("areaY")) area[1] = (double) effectArguments.get("areaY");
                        if (effectArguments.containsKey("areaZ")) area[2] = (double) effectArguments.get("areaZ");
                        entities = Bukkit.getWorld(world).getNearbyEntities(effect.location, area[0], area[1], area[2]);
                        for (Entity entity : entities) {
                            if (entity instanceof Player)
                                Bukkit.getPlayer(entity.getUniqueId()).removePotionEffect(PotionEffectType.getByName((String) effectArguments.get("potion")));
                        }
                    }
                    break;
            }
        location = new Location(Bukkit.getWorld("world"),0,0,0);
        type = Type.PARTICLE;
        arguments.clear();

    }

    public void detonateFirework(final Firework firework){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ShowControl.getInstance(), new Runnable() {
            public void run() {
                try{
                    firework.detonate();
                }catch(Exception e){}
            }
        }, (2));
    }

    public enum Type {
        /**
         * A particle effect.
         */
        PARTICLE,
        /**
         * A block.
         */
        BLOCK,
        /**
         * A firework spawner.
         */
        FIREWORK,
        /**
         * A dragon beam effect.
         */
        DRAGON,
        /**
         * A crystal beam.
         */
        CRYSTAL,
        /**
         * A guardian lazer effect.
         */
        LAZER,
        /**
         * A player status effect.
         */
        STATUS,
        ;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Type type = Type.PARTICLE;
        private Location location;
        private Map<String, Object> arguments = new HashMap<>();
        private Throwable InvalidPropertiesFormatException;
        private Throwable NullPointerException;

        Builder() {}

        /**
         * Sets the type of the effect
         * @param type The type of effect
         */
        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the location of the effect
         * @param location The location of the effect
         * @return This object, for chaining
         */
        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        /**
         * Adds a argument to the effect
         * @param name The argument
         * @param value The value
         * @return This object, for chaining
         */
        public Builder argument(String name, Object value) {

            arguments.put(name,value);
            return this;
        }

        /**
         * Attempts to unwrap an effect
         * @param effectID The effect's ID
         * @return This object, for chaining
         * @apiNote Used with Type.PARTICLE
         * @throws NullPointerException When an effect with the defined ID is not available
         */
        public Builder unwrap(String effectID) throws Throwable {
            arguments.put("unwrapped",true);
            if(ShowControl.getInstance().getMap("effects").containsKey("effects."+effectID)) {
                for (Map.Entry<String, Object> effectLoopMap : ShowControl.getInstance().getMap("effects").entrySet()) {
                    if(effectLoopMap.getKey().matches("effects\\."+effectID+"\\.[A-z\\-]*")) {
                        arguments.put(effectLoopMap.getKey().replaceFirst("effects\\.[0-9]*\\.([A-z\\-]*)","$1"),effectLoopMap.getValue());
                    }
                }
            } else {
                throw NullPointerException;
            }
            return this;
        }

        public Builder unwrapFrame(Frame frame) throws Throwable {
            arguments.put("unwrapped",true);
            for (Map.Entry<String, Object> effectLoopMap : frame.getArguments().entrySet()) {
                arguments.put(effectLoopMap.getKey().replaceFirst("effects\\.[0-9]*\\.([A-z\\-]*)","$1"),effectLoopMap.getValue());
            }
            return this;
        }

        /**
         * Sets which particle to render
         * @param particle The particle to render
         * @return This object, for chaining
         * @apiNote Used with Type.PARTICLE
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder particle(Particle particle) throws Throwable {
            if(type != Type.PARTICLE) throw InvalidPropertiesFormatException;
            arguments.put("particle",particle);
            return this;
        }
        /**
         * Sets number of particles
         * @param count The number of particles
         * @return This object, for chaining
         * @apiNote Used with Type.PARTICLE
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder count(int count) throws Throwable {
            if(type != Type.PARTICLE) throw InvalidPropertiesFormatException;
            arguments.put("count",count);
            return this;
        }
        /**
         * Sets speed of particles
         * @param speed The speed of the particle
         * @return This object, for chaining
         * @apiNote Used with Type.PARTICLE
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder speed(double speed) throws Throwable {
            if(type != Type.PARTICLE) throw InvalidPropertiesFormatException;
            arguments.put("speed",speed);
            return this;
        }
        /**
         * Sets the dynamic location of the particle
         * @param dx The dynamic X location of the particle
         * @param dy The dynamic Y location of the particle
         * @param dz The dynamic Z location of the particle
         * @return This object, for chaining
         * @apiNote Used with Type.PARTICLE
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder dynamics(double dx, double dy, double dz) throws Throwable {
            if(type != Type.PARTICLE) throw InvalidPropertiesFormatException;
            arguments.put("dx",dx);
            arguments.put("dy",dy);
            arguments.put("dz",dz);
            return this;
        }

        /**
         * Sets the material of a block
         * @param material The material of the block
         * @return This object, for chaining
         * @apiNote Used with Type.PARTICLE
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder material(Material material) throws Throwable {
            if(type != Type.BLOCK) throw InvalidPropertiesFormatException;
            arguments.put("material",material);
            return this;
        }

        /**
         * Sets the shape of a firework
         * @param shape The shape of the firework
         * @return This object, for chaining
         * @apiNote Used with Type.FIREWORK
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder shape(FireworkEffect.Type shape) throws Throwable {
            if(type != Type.FIREWORK) throw InvalidPropertiesFormatException;
            arguments.put("shape",shape);
            return this;
        }
        /**
         * Sets if a firework should flicker
         * @return This object, for chaining
         * @apiNote Used with Type.FIREWORK
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder flicker() throws Throwable {
            if(type != Type.FIREWORK) throw InvalidPropertiesFormatException;
            arguments.put("flicker",true);
            return this;
        }
        /**
         * Sets if a firework should have a trail
         * @return This object, for chaining
         * @apiNote Used with Type.FIREWORK
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder trail() throws Throwable {
            if(type != Type.FIREWORK) throw InvalidPropertiesFormatException;
            arguments.put("trail",true);
            return this;
        }
        /**
         * Sets if a firework should fade
         * @return This object, for chaining
         * @apiNote Used with Type.FIREWORK
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder fade() throws Throwable {
            if(type != Type.FIREWORK) throw InvalidPropertiesFormatException;
            arguments.put("fade",true);
            return this;
        }
        /**
         * Sets the color of the firework
         * @param color The color the firework should be
         * @return This object, for chaining
         * @apiNote Used with Type.FIREWORK
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder color(Color color) throws Throwable {
            if(type != Type.FIREWORK) throw InvalidPropertiesFormatException;
            arguments.put("color",color);
            return this;
        }
        /**
         * Sets the color of the firework
         * @param r Red channel of the firework's color
         * @param g Green channel of the firework's color
         * @param b Blue channel of the firework's color
         * @return This object, for chaining
         * @apiNote Used with Type.FIREWORK
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder color(int r,int g,int b) throws Throwable {
            if(type != Type.FIREWORK) throw InvalidPropertiesFormatException;
            arguments.put("r",r);
            arguments.put("g",g);
            arguments.put("b",b);
            return this;
        }
        /**
         * Sets the lifespan of the firework
         * @param life The lifespan of the firework
         * @return This object, for chaining
         * @apiNote Used with Type.FIREWORK
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder life(int life) throws Throwable {
            if(type != Type.FIREWORK) throw InvalidPropertiesFormatException;
            arguments.put("life",life);
            return this;
        }

        /**
         * Sets the target block of the crystal
         * @param target The target block
         * @return This object, for chaining
         * @apiNote Used with Type.CRYSTAL
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder target(Location target) throws Throwable {
            if(type != Type.CRYSTAL) throw InvalidPropertiesFormatException;
            arguments.put("targetX",location.getBlockX());
            arguments.put("targetY",location.getBlockY());
            arguments.put("targetZ",location.getBlockZ());
            return this;
        }
        /**
         * Sets the target block of the crystal
         * @param targetX The X coordinate of the target block
         * @param targetY The Y coordinate of the target block
         * @param targetZ The Z coordinate of the target block
         * @return This object, for chaining
         * @apiNote Used with Type.CRYSTAL
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder target(double targetX,double targetY,double targetZ) throws Throwable {
            if(type != Type.CRYSTAL) throw InvalidPropertiesFormatException;
            arguments.put("targetX",targetX);
            arguments.put("targetY",targetY);
            arguments.put("targetZ",targetZ);
            return this;
        }

        /**
         * Sets the target block of the crystal
         * @param areaX The distance on the X axes from Location to apply the status effect
         * @param areaY The distance on the Y axes from Location to apply the status effect
         * @param areaZ The distance on the Z axes from Location to apply the status effect
         * @return This object, for chaining
         * @apiNote Used with Type.STATUS
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder area(double areaX,double areaY,double areaZ) throws Throwable {
            if(type != Type.STATUS) throw InvalidPropertiesFormatException;
            arguments.put("areaX",areaX);
            arguments.put("areaY",areaY);
            arguments.put("areaZ",areaZ);
            return this;
        }
        /**
         * Sets the type of status effect to be given
         * @param potion The status effect
         * @return This object, for chaining
         * @apiNote Used with Type.STATUS
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder potion(PotionEffect potion) throws Throwable {
            if(type != Type.STATUS) throw InvalidPropertiesFormatException;
            arguments.put("potion",potion);
            return this;
        }
        /**
         * Sets the duration of the status effect
         * @param time The duration
         * @return This object, for chaining
         * @apiNote Used with Type.STATUS
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder time(int time) throws Throwable {
            if(type != Type.STATUS) throw InvalidPropertiesFormatException;
            arguments.put("time",time);
            return this;
        }
        /**
         * Sets the strength of the status effect
         * @param strength The strength
         * @return This object, for chaining
         * @apiNote Used with Type.STATUS
         * @throws InvalidPropertiesFormatException When using the wrong TYPE
         */
        public Builder strength(int strength) throws Throwable {
            if(type != Type.STATUS) throw InvalidPropertiesFormatException;
            arguments.put("strength",strength);
            return this;
        }

        /**
         * Builds the effect
         * @return The effect
         * @throws NullPointerException When location or arguments in unset
         */
        public Effect build() throws Throwable {
            if(location == null) throw NullPointerException;
            if(arguments.isEmpty()) throw NullPointerException;
            return new Effect(
                    type,
                    location,
                    arguments
            );
        }
    }
}
