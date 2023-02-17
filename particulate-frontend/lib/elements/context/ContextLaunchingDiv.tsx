import { throw_closeContextMenuEvent, throw_openContextMenuEvent } from "../../events/events";
import { Option } from "./ContextMenuOption";

type ContextMenuLauncherCallBack = (x: number, y: number) => any;

type ContextLaunchingDiv = {
    className?: string;
    onContextMenu?: ContextMenuLauncherCallBack;
    details?: Object;
    options: Option[];
    children: JSX.Element | JSX.Element[];
}
export const ContextLaunchingDiv = (props: ContextLaunchingDiv, options: Option[]) => {
        return (
            <div
                className={props.className ?? ""}
                onContextMenu={(e) => {
                    e.preventDefault();
                    if(props.onContextMenu)
                        props.onContextMenu(e.clientX, e.clientY);
                        throw_openContextMenuEvent({x: e.clientX, y: e.clientY}, props.options, props.details ?? {});
                }}
                onClick={throw_closeContextMenuEvent}
                >
                {props.children}
            </div>
        );
    }