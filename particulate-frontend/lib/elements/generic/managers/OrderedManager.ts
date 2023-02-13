import { Manager } from "./Manager";

/**
 * An OrderedManager is a type of manager that cares about the order of it's contents.
 */
export class OrderedManager<O> extends Manager<O> {
    protected _items: O[] = [];

    /**
     * Equivalent to `.add()`.
     * Append an item to the end of the Manager.
     * @param value The item to add.
     */
    public append = (item: O): void => { this._items.push(item); };

    /**
     * Append an item to the beginning of the Manager.
     * @param value The item to add.
     */
    public prepend = (item: O): void => { this._items.unshift(item); };

    /**
     * Get the index of an item.
     */
    public getIndex = (item: O): number => this._items.indexOf(item);

    /**
     * Get an item using it's index.
     * @param index The index of the item to get.
     * @returns The item or `undefined` if there isn't one.
     */
    public get = (index: number): O => this._items[index];

    /**
     * Get the first item in the manager.
     * @returns The item.
     */
    public getFirst = (): O => this.get(0);

    /**
     * Get the last item in the manager.
     * @returns The item.
     */
    public getLast = (): O => this.get(this._items.length - 1);

    /**
     * Trim the length of the manager to be length.
     * This will remove any items from the end of the manager.
     * If the new length is larger than the current length, nothing will happen.
     * @param length The new length to set to.
     */
    public trim = (length: number): void => {
        if(this._items.length <= length) return;
        this._items.length = length;
    }

    /**
     * Remove an item from the manager.
     * @param index The index of the item to remove.
     */
    public remove = (index: number): void => { this._items.splice(index, 1); }
}