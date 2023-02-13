import { EventType } from "../../events/EventType";
import { Icon, IconName } from "../Icon";

export class Option {
    protected _icon: IconName;
    protected _title: string;
    protected _eventName: EventType;
    protected _binding: string;
    protected _invert: boolean;

    public get icon() { return this._icon; }
    public get title() { return this._title; }
    public get eventName() { return this._eventName; }
    public get invert() { return this._invert; }
    public get binding() { return this._binding; }

    constructor(icon: IconName, title: string, eventName: EventType, binding?: string, invert?: boolean) {
        this._icon = icon;
        this._title = title;
        this._eventName = eventName;
        this._binding = binding ?? "";
        this._invert = invert ?? true;
    }
}

type ContextMenuOption = {
    icon: IconName;
    title: string;
    eventName: EventType;
    binding: string;
    invert: boolean;
    onClick: Function;
}
export const ContextMenuOption = (props: ContextMenuOption) => {
    const render = () => (
        <div
            className="relative h-[38px] my-2px rounded block p-5px px-10px text-left cursor-pointer duration-300 bg-transparent hover:bg-gradient-to-br from-orange-900 to-cyan-900 z-0"
            onClick={() => props.onClick(props.eventName)}>
            <Icon className={`absolute top-5px left-10px w-[27px] aspect-square inline-block ${props.invert ? "invert" : ""}`} iconName={props.icon} />
            <span className="absolute top-6px left-50px text-white">{props.title}</span>
        </div>
    );
    return render();
}