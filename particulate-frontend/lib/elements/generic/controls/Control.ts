import { v4 as uuidv4 } from 'uuid';
import { InterfaceColor } from '../../../resources/InterfaceColor';
import { Position } from '../../../resources/Position';

export const ControlType = {
    BUTTON_TAP: "button-tap",
    BUTTON_HOLD: "button-hold",
    BUTTON_TOGGLE: "button-toggle",
    LABEL: "label",
} as const;
export type ControlType = typeof ControlType[keyof typeof ControlType];

export type ParseableControlObject = {
    type: ControlType;
    channelID: string;
    effectID: number;
    color: InterfaceColor;
    position: Position;
}

export class Control {
    readonly position: Position;
    readonly type: ControlType;
    readonly color: InterfaceColor;
    readonly uuid: string;
    readonly channelID: string;
    readonly effectID: number;


    constructor(x: number, y: number, channelID: string, effectID: number, color?: InterfaceColor, type?: ControlType) {
        this.uuid = uuidv4();
        this.position = { x, y };
        this.channelID = channelID;
        this.effectID = effectID;
        this.color = color ?? InterfaceColor.RED;
        this.type = type ?? ControlType.BUTTON_HOLD;
    }

    public static parseControl = (object: ParseableControlObject): Control => {
        const requiredKeys = [ 'type', 'color', "position", "channelID", "effectID" ]
        for (const key of requiredKeys)
            if(!(key in object)) throw new Error(`Missing key: ${key}`);
            
        let x = Math.ceil(object.position.x / 124) - 1;
        let y = Math.floor(object.position.y / 124) - 1;

        if(Object.values(ControlType).includes(object.type)) {
            return new Control(x, y, object.channelID, object.effectID, object.color, object.type);
        }
        throw new Error("Object couldn't be parsed as Control");
    }
}