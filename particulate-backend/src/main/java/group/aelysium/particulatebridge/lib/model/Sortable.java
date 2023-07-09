package group.aelysium.particulatebridge.lib.model;

public interface Sortable {
    /**
     * Get the sort index.
     * @return The index by which sorting must occur.
     */
    int getSortIndex();

    /**
     * Get the weight value.
     * @return The weight of this item.
     */
    int getWeight();
}
