import { motion } from "framer-motion";
import { InterfaceColor } from "../../resources/InterfaceColor";
import { useState, useRef, useEffect } from 'react';
import { Position } from '../../resources/Position';
import { Icon, IconName } from "../Icon";
import { EventName } from "../../events/EventName";
import { ContextLaunchingDiv } from "../context/ContextLaunchingDiv";
import { Option } from "../context/ContextMenuOption";
import { throw_createNewElementEvent, throw_removeElementEvent, throw_updateElementEvent } from "../../events/events";
import { ControlType } from "../generic/controls/Control";

const editingButton = [
    new Option(IconName.ADD, 'New Controller', EventName.OpenCreateElementPopUp),
    new Option(IconName.TRASH, 'Delete Label', EventName.DeleteControl, "", false),
    new Option(IconName.EDIT, 'Toggle Edit Mode', EventName.ToggleEditMode, "", true),
];

const calcNewTarget = (x: number, y: number) => {
    y = y - 124; // The whole grid is offset on y by 1

    const position = { x: x, y: y };
    if(position.y < 0) position.y = 0;

    return position;
}

interface DLabel {
    uuid: string;
    initialEditMode?: boolean;
    initialCell: Position;
    color?: InterfaceColor;
    onDragStart?: Function;
    onDragEnd?: Function;
    label: string;
}
export const DLabel = (props: DLabel) => {
    const [ editable, setEditable ] = useState(props.initialEditMode ?? false);
    const [ target, setTarget ] = useState({x: props.initialCell.x, y: props.initialCell.y});
    const [ drag, setDrag ] = useState(false);
    const [ value, setValue ] = useState(props.label ?? "Text Label");
    const currentRef: any = useRef(null);

    useEffect(() => {
        document.addEventListener(EventName.EnterControlEditMode, () => setEditable(true));
        document.addEventListener(EventName.ExitControlEditMode, () => setEditable(false));
        return () => {
            document.removeEventListener(EventName.EnterControlEditMode, () => setEditable(true));
            document.removeEventListener(EventName.ExitControlEditMode, () => setEditable(false));
        }
    },[]);

    const updateLabelControl = (position: Position) => {
        throw_updateElementEvent(props.uuid, ControlType.LABEL, undefined, undefined, props.color, position, value);
    }

    const render_live = () => (
        <motion.span
            ref={currentRef}
            className={`absolute h-47px px-20px font-bold text-xl rounded-3xl text-template-white overflow-hidden`}
            transition={{ type: "spring", stiffness: 400, damping: 10 }}
            initial={{background: props.color ?? InterfaceColor.RED, x: window.innerWidth * 0.5, y: window.innerHeight, left: 0, top: 0}}
            animate={{x: target.x, y: target.y}}
            draggable={false}
            >
            <div className={`absolute inset-0 shadow-inset rounded-3xl`} />
            <span className="relative top-8px">
                { value }
            </span>
        </motion.span>
        );
    
    const render_editing = () => {
        const handleOnDragStart = () => {
            setDrag(true);
            if(props.onDragStart) props.onDragStart();
        }

        const handleOnDragEnd = () => {
            setDrag(false);
            const boundingBox = currentRef.current.getBoundingClientRect();
            if(boundingBox) {
                const newLocation = calcNewTarget(boundingBox.x, boundingBox.y);
                setTarget({ x: newLocation.x, y: newLocation.y });

                updateLabelControl({ x: newLocation.x, y: newLocation.y });
            }
            if(props.onDragEnd) props.onDragEnd();
        }

        return (
            <ContextLaunchingDiv options={editingButton} details={{uuid: props.uuid}}>
                <motion.div
                    ref={currentRef}
                    className={`absolute h-47px font-bold text-xl text-template-white rounded-3xl cursor-pointer shadow-md z-10`}
                    whileHover={{ scale: 1.01 }}
                    initial={{background: props.color ?? InterfaceColor.RED, x: 0, y: 0, scale: 0, rotate: 90}}
                    animate={{ scale: 1, rotate: 0 }}
                    exit={{ scale: 0, rotate: 90 }}
                    style={{left: target.x, top: target.y}}
                    transition={{ type: "spring", stiffness: 100, damping: 10 }}
                    drag={drag}
                    dragSnapToOrigin={true}
                    onDragStart={handleOnDragStart}
                    onDragEnd={handleOnDragEnd}
                    >
                    <span className="relative top-8px mx-20px opacity-0 -z-50">{value}</span>
                    <motion.input
                        type="text"
                        className="absolute pl-20px pb-3px w-full h-full inset-0 shadow-inset rounded-3xl bg-transparent"
                        value={value}
                        onChange={(event) => setValue(event.target.value)}
                        onFocus={() => setDrag(false)}
                        onBlur={() => setDrag(true)}
                        onHoverStart={() => setDrag(false)}
                        onBlurCapture={() => updateLabelControl(target)}
                        />
                    <motion.div
                        className="absolute -top-5px -right-5px bg-neutral-500 w-20px aspect-square rounded-full overflow-hidden cursor-move"
                        onHoverStart={() => setDrag(true)}
                        >
                        <Icon className="absolute m-2px w-15px aspect-square invert" iconName="dots" />
                    </motion.div>
                </motion.div>
            </ContextLaunchingDiv>
        )
    }

    return editable ? render_editing() : render_live();
}