package group.aelysium.particulaterenderer.lib.effects;

import org.bukkit.Location;
import org.bukkit.Material;

public class BlockEffect extends Effect {
    private Material material;

    public BlockEffect(int id, Material material) {
        super(id);
        this.material = material;
    }

    @Override
    public void play(Location location) {
        location.getWorld().getBlockAt(location).setType(this.material);
    }

    @Override
    public void pause(Location location) {
        location.getWorld().getBlockAt(location).setType(Material.AIR);
    }

    public static final class Builder {
        private Integer id;
        private Material material;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder setMaterial(Material material) {
            this.material = material;
            return this;
        }

        public BlockEffect build() throws Throwable {
            if(this.id == null) throw new NullPointerException();
            if(this.material == null) throw new NullPointerException();
            return new BlockEffect(
                    this.id,
                    this.material
            );
        }
    }
}
