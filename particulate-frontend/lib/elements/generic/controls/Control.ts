import { v4 as uuidv4 } from 'uuid';
import { InterfaceColor } from '../../../resources/InterfaceColor';
import { Position } from '../../../resources/Position';

export const ControlType = {
    BUTTON_CLICK: "button-click",
    BUTTON_TOGGLE: "button-toggle",
    LABEL: "label",
} as const;
export type ControlType = typeof ControlType[keyof typeof ControlType];

export type ParseableControlObject = {
    type: ControlType;
    color: InterfaceColor;
    position: Position;
}

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

    public static parseControl = (object: ParseableControlObject): Control => {
        const requiredKeys = [ 'type', 'color', "position" ]
        for (const key of requiredKeys)
            if(!(key in object)) throw new Error(`Missing key: ${key}`);
            
        let x = Math.ceil(object.position.x / 124) - 1;
        let y = Math.floor(object.position.y / 124) - 1;

        if(Object.values(ControlType).includes(object.type)) {
            return new Control(x, y, object.color, object.type);
        }
        throw new Error("Object couldn't be parsed as Control");
    }
}