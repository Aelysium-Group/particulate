package group.aelysium.particulaterenderer.lib.effects;

import group.aelysium.particulaterenderer.lib.model.DeltaLocation;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleEffect extends Effect {
    private Particle particle;
    private int count;
    private double speed;
    private DeltaLocation delta;

    public ParticleEffect(int id, Particle particle, int count, double speed, DeltaLocation delta) {
        super(id);
        this.particle = particle;
        this.count = count;
        this.speed = speed;
        this.delta = delta;
    }

    @Override
    public void play(Location location) {
        location.getWorld().spawnParticle(
                this.particle,
                location,
                this.count,
                this.delta.dx(),
                this.delta.dy(),
                this.delta.dz(),
                this.speed
        );
    }

    @Override
    public void pause(Location location) {}

    public static final class Builder {
        private Integer id;
        private Particle particle;
        private Integer count;
        private Double speed;
        private DeltaLocation delta;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder setParticle(Particle particle) {
            this.particle = particle;
            return this;
        }
        public Builder setCount(Integer count) {
            this.count = count;
            return this;
        }
        public Builder setSpeed(Double speed) {
            this.speed = speed;
            return this;
        }
        public Builder setDelta(DeltaLocation delta) {
            this.delta = delta;
            return this;
        }

        public ParticleEffect build() throws Throwable {
            if(this.id == null) throw new NullPointerException();
            if(this.particle == null) throw new NullPointerException();
            if(this.count == null) throw new NullPointerException();
            if(this.speed == null) throw new NullPointerException();
            if(this.delta == null) throw new NullPointerException();
            return new ParticleEffect(
                    this.id,
                    this.particle,
                    this.count,
                    this.speed,
                    this.delta
            );
        }
    }
}
