package group.aelysium.particulaterenderer.lib.effects;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class FireworkEffect extends Effect {
    private boolean hasFlicker;
    private boolean hasTrail;
    private boolean hasFade;
    private int life;
    private List<org.bukkit.FireworkEffect.Type> shapes;
    private List<Color> colors;

    protected FireworkEffect(int id, boolean hasFlicker, boolean hasTrail, boolean hasFade, int life, List<org.bukkit.FireworkEffect.Type> shapes, List<Color> colors) {
        super(id);
        this.hasFlicker = hasFlicker;
        this.hasTrail = hasTrail;
        this.hasFade = hasFade;
        this.life = life;
        this.shapes = shapes;
        this.colors = colors;
    }

    @Override
    public void play(Location location) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        org.bukkit.FireworkEffect.Builder fireworkEffectBuilder = org.bukkit.FireworkEffect.builder();

        if (this.shapes.size() <= 0) this.shapes.add(org.bukkit.FireworkEffect.Type.BALL);

        for (org.bukkit.FireworkEffect.Type shape : this.shapes)
            fireworkEffectBuilder.with(shape);

        if(this.colors.size() == 0) this.colors.add(Color.WHITE);
        fireworkEffectBuilder.withColor(this.colors);

        if(this.hasFlicker) fireworkEffectBuilder.withFlicker();
        if(this.hasTrail) fireworkEffectBuilder.withTrail();
        if(this.hasFade) fireworkEffectBuilder.withFade();

        if(this.life > 0) fireworkMeta.setPower(this.life);

        fireworkMeta.addEffect(fireworkEffectBuilder.build());
        firework.setFireworkMeta(fireworkMeta);

        if(this.life == 0) detonateFirework(firework);
    }

    @Override
    public void pause(Location location) {}

    public static final class Builder {
        private Integer id;
        private Boolean hasFlicker = false;
        private Boolean hasTrail = false;
        private Boolean hasFade = false;
        private Integer life = 0;
        private final List<org.bukkit.FireworkEffect.Type> shapes = new ArrayList<>();
        private final List<Color> colors = new ArrayList<>();

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder setHasFlicker(boolean hasFlicker) {
            this.hasFlicker = hasFlicker;
            return this;
        }
        public Builder setHasTrail(boolean hasTrail) {
            this.hasTrail = hasTrail;
            return this;
        }
        public Builder setHasFade(boolean hasFade) {
            this.hasFade = hasFade;
            return this;
        }
        public Builder setLife(int life) {
            this.life = life;
            return this;
        }
        public Builder addShape(org.bukkit.FireworkEffect.Type shape) {
            this.shapes.add(shape);
            return this;
        }
        public Builder addColor(Color color) {
            this.colors.add(color);
            return this;
        }

        public FireworkEffect build() throws Throwable {
            if(this.id == null) throw new NullPointerException();
            return new FireworkEffect(
                    this.id,
                    this.hasFlicker,
                    this.hasTrail,
                    this.hasFade,
                    this.life,
                    this.shapes,
                    this.colors
            );
        }
    }

    private static void detonateFirework(final Firework firework){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ParticulateRenderer.getPlugin(ParticulateRenderer.class), () -> {
            try{
                firework.detonate();
            } catch(Exception ignore){}
        }, 2);
    }
}
