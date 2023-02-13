import { Manager } from "./Manager";

/**
 * Barrel is a type of Manager that does not allow you to search for specific items.
 * Instead you must dump the contents of the Barrel, which will then empty all contents from the Barrel.
 */
export class Barrel<O> extends Manager<O> {
    /**
     * @deprecated This method is not supported on Barrel. To get the contents of this barrel use `.dump()`
     */
    public find = (key: any, parameter: string): O[] | void => {}

    /**
     * Dump the contents of the barrel.
     * This method will remove all data from the barrel.
     * @returns All the contents of the barrel.
     */
    public dump = (): O[] => {
        const itemsToDump = [...this._items]; // Items are copied by reference
        this.clear();
        return itemsToDump;
    }
}