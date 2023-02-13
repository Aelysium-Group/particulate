import { Position } from "../../../resources/Position";
import { InterfaceColor } from "../../../resources/InterfaceColor";
import { Control } from "./Control";

export const ButtonType = {
    CLICK: "CLICK",
    TOGGLE: "TOGGLE",
} as const;
export type ButtonType = typeof ButtonType[keyof typeof ButtonType];

export class Button extends Control {
    protected _position: Position;
    protected _type: ButtonType;
    protected _color: InterfaceColor;

    public get position() { return this._position; }
    public get type() { return this._type; }
    public get color() { return this._color; }

    constructor(x: number, y: number, color?: InterfaceColor, type?: ButtonType) {
        super();
        this._position = { x, y };
        this._color = color ?? InterfaceColor.RED;
        this._type = type ?? ButtonType.CLICK;
    }
}