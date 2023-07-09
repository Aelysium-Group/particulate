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
    channelID?: string;
    effectID?: string;
    label?: string
    color: InterfaceColor;
    position: Position;
}

export class Control {
    public position: Position = { x: 0, y: 0 };
    readonly type: ControlType;
    readonly color: InterfaceColor;
    readonly uuid: string;
    readonly channelID: string;
    readonly effectID: string;
    private _label: string;

    constructor(x: number, y: number, channelID: string, effectID: string, color?: InterfaceColor, type?: ControlType, label?: string) {
        this.uuid = uuidv4();
        this.position = { x: Math.round(x), y: Math.round(y) };
        this.channelID = channelID;
        this.effectID = effectID;
        this.color = color ?? InterfaceColor.RED;
        this.type = type ?? ControlType.BUTTON_HOLD;
        this._label = type == ControlType.LABEL ? label ?? "Empty Label" : type;
    }

    public get label() { return this._label; }
    public toJSON = (): ParseableControlObject => {
        if(this.type == ControlType.LABEL) {
            return {
                type: this.type,
                color: this.color,
                position: this.position,
                label: this.label
            };
        } else {
            return {
                type: this.type,
                color: this.color,
                position: this.position,
                channelID: this.channelID,
                effectID: this.effectID
            };
        }
    }

    public update = (label: string, position: Position) => {
        this._label = label;
        this.position = { x: Math.round(position.x), y: Math.round(position.y) };
    }

    public static parseControl = (object: ParseableControlObject): Control => {
        const requiredKeys = [ 'type', 'color', "position" ]
        for (const key of requiredKeys)
            if(!(key in object)) throw new Error(`Missing key: ${key}`);

        if(object.type == ControlType.LABEL) {
            if(!('label' in object)) object.label = "Empty Label";
            object.channelID = undefined;
            object.effectID = undefined;
        } else {
            if(!('channelID' in object)) throw new Error(`Missing key: channelID`);
            if(!('effectID' in object)) throw new Error(`Missing key: effectID`);
            object.label = undefined;
        }
        
        let x: number = object.position.x;
        let y: number = object.position.y;
        if(object.type == ControlType.LABEL) {
            x = Math.round(object.position.x);
            y = Math.round(object.position.y);
        } else if(object.position.x > 70 && object.position.y > 70) {
            x = Math.ceil(object.position.x / 124) - 1;
            y = Math.floor(object.position.y / 124) - 1;
        }

        if(Object.values(ControlType).includes(object.type)) {
            return new Control(x, y, object.channelID, object.effectID, object.color, object.type, object.label);
        }
        throw new Error("Object couldn't be parsed as Control");
    }
}