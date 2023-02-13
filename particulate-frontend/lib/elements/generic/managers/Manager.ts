export class Manager<O> {
    protected _items: O[] = [];

    /**
     * Adds an item to manager.
     * @param value The item to add.
     */
    public add = (item: O): void => { this._items.push(item) };

    /**
     * Get the value associated with a key from the manager.
     * @param key The key to find.
     * @param parameter The parameter name to use in order to search for the key.
     * @returns The value associated with `key` or `undefined` if there is none.
     */
    public find = (key: any, parameter: string): O[] | void => this._items.filter(o => o[parameter] === key);

    /**
     * Get all items from the manager.
     * @returns The items.
     */
    public dump = (): O[] => this._items;

    /**
     * Remove all items from the manager.
     */
    public clear = (): void => { this._items.length = 0; }
}