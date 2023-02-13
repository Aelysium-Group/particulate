import { v4 as uuidv4 } from 'uuid';

export class Control {
    protected _uuid: string;

    public get uuid() { return this._uuid; }

    constructor() {
        this._uuid = uuidv4();
    }
}