import { Option } from "../elements/context/ContextMenuOption";
import { Position } from "../resources/Position";

export const throw_openContextMenuEvent = (position: Position, options: Option[], details: Object) => {
    const event = new CustomEvent("react-contextMenu-open", { detail: {position, options, details} });
    document.dispatchEvent(event);
}
export const throw_closeContextMenuEvent = () => {
    const event = new CustomEvent("react-contextMenu-close");
    document.dispatchEvent(event);
}