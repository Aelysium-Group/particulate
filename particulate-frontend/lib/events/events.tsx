import { Option } from "../elements/context/ContextMenuOption";
import { ParseableControlObject } from "../elements/generic/controls/Control";
import { LogMessages } from "../hooks/useLog";
import { BackgroundColor } from "../resources/BackgroundColor";
import { Position } from "../resources/Position";
import { EventName } from "./EventName";

export const throw_event = (eventName: EventName, detail: Object = {}) => {
    const event = new CustomEvent(eventName, { detail });
    document.dispatchEvent(event);
}

export const throw_lightBarColorUpdate = (id: string, color: BackgroundColor) => {
    const event = new CustomEvent(EventName.LightBar_ColorUpdate, { detail: {id, color} });
    document.dispatchEvent(event);
}

export const throw_openContextMenuEvent = (position: Position, options: Option[], details: Object) => {
    const event = new CustomEvent(EventName.ContextMenuOpen, { detail: {position, options, details} });
    document.dispatchEvent(event);
}
export const throw_closeContextMenuEvent = () => {
    const event = new CustomEvent(EventName.ContextMenuClose);
    document.dispatchEvent(event);
}

export const throw_createNewElementEvent = (type: string, channelID: string, effectID: number, color: string, position: Position) => {
    const event = new CustomEvent(EventName.RegisterNewControl, { detail: {type, channelID, effectID, color, position} as ParseableControlObject });
    document.dispatchEvent(event);
}

export const throw_transportLogMessagesEvent = (messages: LogMessages) => {
    const event = new CustomEvent(EventName.TransportLogMessages, {detail: messages});
    document.dispatchEvent(event);
}

export const throw_genericEventFromContextMenu = (eventName: EventName, target: Position, details: Object) => {
    const event = new CustomEvent(eventName, {detail: { target, details }});
    document.dispatchEvent(event);
}

export const throw_sendDemandMessage = (channelID: string, effectID: number) => {
    const event = new CustomEvent(EventName.DispatchDemandMessage, {detail: { channelID, effectID }});
    document.dispatchEvent(event);
}
export const throw_sendDemandToggleOnMessage = (channelID: string, effectID: number) => {
    const event = new CustomEvent(EventName.DispatchDemandToggleOnMessage, {detail: { channelID, effectID }});
    document.dispatchEvent(event);
}
export const throw_sendDemandToggleOffMessage = (channelID: string, effectID: number) => {
    const event = new CustomEvent(EventName.DispatchDemandToggleOffMessage, {detail: { channelID, effectID }});
    document.dispatchEvent(event);
}