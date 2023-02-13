import { motion } from "framer-motion";
import { BackgroundColor } from "../../resources/BackgroundColor";
import { InterfaceColor } from "../../resources/InterfaceColor";
import { useState, useRef, useEffect } from 'react';
import { Position } from '../../resources/Position';
import { Icon, IconName } from "../Icon";
import { throw_closeContextMenuEvent } from "../../events/ContextMenuEvents";
import { EventType } from "../../events/EventType";
import { ContextLaunchingDiv } from "../context/ContextLaunchingDiv";
import { Option } from "../context/ContextMenuOption";

const editingButton = [
    new Option(IconName.TRASH, 'Delete Controller', EventType.ContextMenuOption_DeleteElement, "", false),
];

const calcNewTarget = (x: number, y: number) => {
    x = Math.round(x / 124);
    y = Math.round(y / 124) - 1; // The whole grid is offset on y by 1

    const position = { x: x * 124, y: y * 124 };
    if(position.y < 124) position.y = 0;

    return position;
}

interface DButtonClick {
    uuid: string;
    initialCell: Position;
    editMode?: boolean;
    color?: BackgroundColor;
    onDragStart?: Function;
    onDragEnd?: Function;
}
export const DButtonClick = (props: DButtonClick) => {
    const [ target, setTarget ] = useState({x: props.initialCell.x * 124, y: props.initialCell.y * 124});
    const [ active, setActive ] = useState(false);
    const [ drag, setDrag ] = useState(false);
    const currentRef: any = useRef(null);

    const render_live = () => (
        <ContextLaunchingDiv options={[]} details={{uuid: props.uuid}}>
            <motion.div
                ref={currentRef}
                className={`absolute rounded-3xl ${props.color ?? InterfaceColor.RED} w-100px m-12px aspect-square overflow-hidden cursor-pointer`}
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9, boxShadow: "0px 0px 25px -5px white"}}
                transition={{ type: "spring", stiffness: 400, damping: 10 }}
                onClick={() => { setActive(!active); throw_closeContextMenuEvent(); }}
                initial={{x: window.innerWidth * 0.5, y: window.innerHeight, boxShadow: "0px 0px 0px -5px white"}}
                animate={{x: target.x, y: target.y}}
                draggable={false}
                >
                <div className={`absolute inset-0 w-100 aspect-square shadow-inset-xl`} />
            </motion.div>
        </ContextLaunchingDiv>
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
            }
            if(props.onDragEnd) props.onDragEnd();
        }

        return (
            <ContextLaunchingDiv options={editingButton} details={{uuid: props.uuid}}>
                <motion.div
                    ref={currentRef}
                    className={`absolute rounded-3xl ${props.color ?? InterfaceColor.RED} w-100px m-12px aspect-square overflow-hidden cursor-move ${drag ? "shadow-xl z-20" : ""}`}
                    whileHover={{ scale: 1.1 }}
                    initial={{x: 0, y: 0}}
                    style={{left: target.x, top: target.y}}
                    transition={{ type: false }}
                    drag
                    dragSnapToOrigin={true}
                    onDragStart={handleOnDragStart}
                    onDragEnd={handleOnDragEnd}
                    >
                    <div className='absolute inset-0 w-100 aspect-square shadow-inset-xl'>
                        <Icon className="w-40px aspect-square invert opacity-20 m-[29px]" iconName="dots" />
                    </div>
                </motion.div>
            </ContextLaunchingDiv>
        )
    }

    return props.editMode ? render_editing() : render_live();
}

interface DButtonToggle {
    uuid: string;
    initialCell: Position;
    editMode?: boolean;
    color?: BackgroundColor;
    onDragStart?: Function;
    onDragEnd?: Function;
}
export const DButtonToggle = (props: DButtonToggle) => {
    const [ target, setTarget ] = useState({x: props.initialCell.x * 124, y: props.initialCell.y * 124});
    const [ active, setActive ] = useState(false);
    const [ drag, setDrag ] = useState(false);
    const currentRef: any = useRef(null);
    const buttonShadowInset = active ? "shadow-white-inset-xl" : "shadow-inset-xl";

    useEffect(() => {
        document.addEventListener(EventType.ContextMenuOption_StopEffects,()=>setActive(false));
        return () => {
            document.removeEventListener(EventType.ContextMenuOption_StopEffects,()=>setActive(false));
        }
    },[]);

    const render_live = () => {
        const buttonShadowOutset = active ? "shadow-white-md" : "";

        return (
            <ContextLaunchingDiv options={[]} details={{uuid: props.uuid}}>
                <motion.div
                    ref={currentRef}
                    className={`absolute rounded ${props.color ?? InterfaceColor.RED} w-100px m-12px aspect-square overflow-hidden cursor-pointer ${buttonShadowOutset}`}
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.9 }}
                    transition={{ type: "spring", stiffness: 400, damping: 10 }}
                    onClick={() => { setActive(!active); throw_closeContextMenuEvent(); }}
                    initial={{x: window.innerWidth * 0.5, y: window.innerHeight}}
                    animate={{x: target.x, y: target.y}}
                    draggable={false}
                    >
                    <div className={`absolute inset-0 w-100 aspect-square ${buttonShadowInset}`} />
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
                    className={`absolute rounded ${props.color ?? InterfaceColor.RED} w-100px m-12px aspect-square overflow-hidden cursor-move ${drag ? "shadow-xl z-20" : ""}`}
                    whileHover={{ scale: 1.1 }}
                    initial={{x: 0, y: 0}}
                    style={{left: target.x, top: target.y}}
                    transition={{ type: false }}
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

    return props.editMode ? render_editing() : render_live();
}