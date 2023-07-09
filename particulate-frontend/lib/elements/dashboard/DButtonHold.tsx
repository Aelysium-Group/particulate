import { motion } from "framer-motion";
import { InterfaceColor } from "../../resources/InterfaceColor";
import { useState, useRef, useEffect } from 'react';
import { Position } from '../../resources/Position';
import { Icon, IconName } from "../Icon";
import { throw_closeContextMenuEvent, throw_sendDemandToggleOffMessage, throw_sendDemandToggleOnMessage } from "../../events/events";
import { EventName } from "../../events/EventName";
import { ContextLaunchingDiv } from "../context/ContextLaunchingDiv";
import { Option } from "../context/ContextMenuOption";

const editingButton = [
    new Option(IconName.TRASH, 'Delete Controller', EventName.DeleteControl, "", false),
    new Option(IconName.EDIT, 'Toggle Edit Mode', EventName.ToggleEditMode, "", true),
];

const calcNewTarget = (x: number, y: number) => {
    x = Math.round(x / 124);
    y = Math.round(y / 124) - 1; // The whole grid is offset on y by 1

    const position = { x: x * 124, y: y * 124 };
    if(position.y < 124) position.y = 0;

    return position;
}

interface DButtonHold {
    uuid: string;
    channelID: string;
    effectID: string;
    initialEditMode?: boolean;
    initialCell: Position;
    color?: InterfaceColor;
    onDragStart?: Function;
    onDragEnd?: Function;
}
export const DButtonHold = (props: DButtonHold) => {
    const [ editable, setEditable ] = useState(props.initialEditMode ?? false);
    const [ target, setTarget ] = useState({x: props.initialCell.x * 124, y: props.initialCell.y * 124});
    const [ active, setActive ] = useState(false);
    const [ drag, setDrag ] = useState(false);
    const currentRef: any = useRef(null);
    const buttonShadowInset = active ? "shadow-white-inset-xl" : "shadow-inset-xl";

    useEffect(() => {
        document.addEventListener(EventName.ContextMenuOption_StopEffects,()=>setActive(false));
        document.addEventListener(EventName.EnterControlEditMode, () => setEditable(true));
        document.addEventListener(EventName.ExitControlEditMode, () => setEditable(false));
        return () => {
            document.removeEventListener(EventName.ContextMenuOption_StopEffects,()=>setActive(false));
            document.removeEventListener(EventName.EnterControlEditMode, () => setEditable(true));
            document.removeEventListener(EventName.ExitControlEditMode, () => setEditable(false));
        }
    },[]);

    const render_live = () => {
        const buttonShadowOutset = active ? "shadow-white-md" : "";

        return (
            <ContextLaunchingDiv options={[]} details={{uuid: props.uuid}}>
                <motion.div
                    ref={currentRef}
                    className={`absolute rounded-3xl w-100px m-12px aspect-square overflow-hidden cursor-pointer ${buttonShadowOutset} ${ active ? "animated-hold-vibrate" : ""}`}
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.9 }}
                    transition={{ type: "spring", stiffness: 400, damping: 10 }}
                    onTapStart={(e) => {
                        throw_sendDemandToggleOnMessage(props.channelID, props.effectID);

                        setActive(true);
                        e.preventDefault();
                        throw_closeContextMenuEvent();
                    }}
                    onTapCancel={() => {
                        throw_sendDemandToggleOffMessage(props.channelID, props.effectID);
                        setActive(false);
                    }}
                    onTap={() => {
                        throw_sendDemandToggleOffMessage(props.channelID, props.effectID);
                        setActive(false);
                    }}
                    initial={{background: props.color ?? InterfaceColor.RED, x: window.innerWidth * 0.5, y: window.innerHeight}}
                    animate={{x: target.x, y: target.y}}
                    draggable={false}
                    >
                    <div className={`absolute inset-0 w-100 aspect-square ${buttonShadowInset}`} />
                    {
                        active ?
                        <div className="aspect-square overflow-hidden w-100px">
                            <motion.div 
                                className="absolute aspect-square border-neutral-50 border-8 animated-hold-spin"
                                initial={{width: "300px", inset: "-120px"}}
                                transition={{ type: "spring", stiffness: 100, damping: 100 }}
                                animate={{width: "90px", inset: "5px"}}
                                />
                            <motion.div 
                                className="absolute inset-25px w-50px aspect-square opacity-75 border-neutral-50 border-4 animated-hold-spin-reverse"
                                initial={{width: "150px", inset: "-25px"}}
                                transition={{ type: "spring", stiffness: 75, damping: 100 }}
                                animate={{width: "50px", inset: "25px"}}
                                />
                            <motion.div 
                                className="absolute aspect-square opacity-50 border-neutral-50 border-2 animated-hold-spin"
                                initial={{width: "100px", inset: "0px"}}
                                transition={{ type: "spring", stiffness: 100, damping: 100 }}
                                animate={{width: "25px", inset: "37.5px"}}
                                />
                        </div>
                        : <></>
                    }
                </motion.div>
            </ContextLaunchingDiv>
        )
    }
    
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
            }
            if(props.onDragEnd) props.onDragEnd();
        }

        return (
            <ContextLaunchingDiv options={editingButton} details={{uuid: props.uuid}}>
                <motion.div
                    ref={currentRef}
                    className={`absolute rounded-3xl w-100px m-12px aspect-square overflow-hidden cursor-move ${drag ? "shadow-xl z-20" : ""}`}
                    whileHover={{ scale: 1.1 }}
                    exit={{ scale: 0, rotate: 90 }}
                    initial={{background: props.color ?? InterfaceColor.RED, x: 0, y: 0, scale: 0, rotate: 90}}
                    animate={{ scale: 1, rotate: 0 }}
                    style={{left: target.x, top: target.y}}
                    transition={{ type: "spring", stiffness: 100, damping: 10 }}
                    drag
                    dragSnapToOrigin={true}
                    onDragStart={handleOnDragStart}
                    onDragEnd={handleOnDragEnd}
                    >
                    <div className={`absolute inset-0 w-100 aspect-square shadow-inset-xl ${buttonShadowInset}`}>
                        <Icon className="w-40px aspect-square invert opacity-20 m-[29px]" iconName="dots" />
                    </div>
                </motion.div>
            </ContextLaunchingDiv>
        )
    }

    return editable ? render_editing() : render_live();
}