import { v4 as uuidv4 } from 'uuid';
import { InterfaceColor } from '../../../resources/InterfaceColor';
import { Position } from '../../../resources/Position';

export const ControlType = {
    BUTTON_CLICK: "button-click",
    BUTTON_TOGGLE: "button-toggle",
    LABEL: "label",
} as const;
export type ControlType = typeof ControlType[keyof typeof ControlType];

export class Control {
    readonly position: Position;
    readonly type: ControlType;
    readonly color: InterfaceColor;
    readonly uuid: string;


    constructor(x: number, y: number, color?: InterfaceColor, type?: ControlType) {
        this.uuid = uuidv4();
        this.position = { x, y };
        this.color = color ?? InterfaceColor.RED;
        this.type = type ?? ControlType.BUTTON_CLICK;
    }

    public static parseControl = (object: any): Control => {
        const requiredKeys = [ 'type', 'color' ]
        for (const key of requiredKeys)
            if(!(key in object)) throw new Error(`Missing key: ${key}`);
            
        if(Object.values(ControlType).includes(object.type)) {
            return new Control(0, 0, object.color, object.type);
        }
        throw new Error("Object couldn't be parsed as Control");
    }
}