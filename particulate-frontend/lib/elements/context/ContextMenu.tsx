import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { ContextMenuOption, Option } from './ContextMenuOption';
import { EventName } from '../../events/EventName';
import { throw_closeContextMenuEvent, throw_genericEventFromContextMenu } from '../../events/events';
const defaultPosition = { x: window.innerWidth * 0.5, y: window.innerHeight * 1.5 };

type ContextMenu = {
}
export const ContextMenu = (props: ContextMenu) => {
    const [ position, setPosition ] = useState(defaultPosition);
    const [ options, setOptions ]: [ Option[], Function ] = useState([]);
    const [ details, setDetails ]: [ Object, Function ] = useState({});
    const [ active, setActive ] = useState(false);

    useEffect(()=>{
        document.addEventListener(EventName.ContextMenuOpen, (e: any) => {
            setActive(true);
            setPosition(e.detail.position);
            setOptions(e.detail.options);
            setDetails(e.detail.details);
        });
        document.addEventListener(EventName.ContextMenuClose, () => {
            setActive(false);
            setPosition(defaultPosition);
            setOptions([]);
            setDetails({});
        });
        return () => {
            document.removeEventListener(EventName.ContextMenuOpen, (e: any) => {
                setActive(true);
                setPosition(e.detail.position);
                setOptions(e.detail.options);
                setDetails(e.detail.details);
            });
            document.removeEventListener(EventName.ContextMenuClose, () => {
                setActive(false);
                setPosition(defaultPosition);
                setOptions([]);
                setDetails({});
            });
        }
    },[]);

    const render = () => {
        let optionElements = []
        let i = 0;
        for (const item of options) {
            optionElements.push(
                <ContextMenuOption
                    key={i} 
                    title={item.title} 
                    icon={item.icon} 
                    eventName={item.eventName} 
                    onClick={() => throw_genericEventFromContextMenu(item.eventName, position, details)}
                    invert={item.invert}
                    binding={item.binding}
                    />)
            i++;
        }

        return (
            <motion.div
                className="frosted-glass bg-zinc-900/75 fixed w-300px rounded-md shadow-md py-5px z-50"
                initial={{ left: position.x, top: position.y }}
                animate={active ? { left: position.x, top: position.y } : { left: window.innerWidth * 0.5, top: window.innerHeight * 1.5 }}
                onClick={throw_closeContextMenuEvent}
                >
                {optionElements}
            </motion.div>
        );
    }
    return render();
}