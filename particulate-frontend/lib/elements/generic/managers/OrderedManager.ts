import { Manager } from "./Manager";

/**
 * An OrderedManager is a type of manager that cares about the order of it's contents.
 */
export class OrderedManager<K, O> {
    protected _items: Map<K, O> = new Map<K, O>();

    /**
     * Add an item to the manager.
     * @param key The key to set.
     * @param value The value to set.
     */
    public set = (key: K, value: O): void => { this._items.set(key, value); };

    /**
     * Get an item using it's index.
     * @param key The key of the item to get.
     * @returns The item or `null` if there isn't one.
     */
    public get = (key: K): O | null => this._items.get(key) ?? null;

    /**
     * Remove an item from the manager.
     * @param key The key of the item to remove.
     */
    public remove = (key: K): void => { this._items.delete(key); }

    /**
     * Get all items from the OrderedManager
     */
    public values = (): O[] => Array.from(this._items.values());

    /**
     * Get all items from the OrderedManager
     */
    public entries = (key: K): O[] => Object.create(this._items.entries());
}