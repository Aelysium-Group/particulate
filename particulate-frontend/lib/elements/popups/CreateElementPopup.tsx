import { useEffect, useState } from 'react';
import { EventName } from "../../events/EventName";
import { PopUp } from './PopUp';
import { TextInput } from '../input/TextInput';
import { ColorInput } from '../input/ColorInput';
import { InterfaceColor } from '../../resources/InterfaceColor';
import { useLog } from '../../hooks/useLog';
import { ButtonInput } from '../input/ButtonInput';
import { ControlSelectInput } from '../input/ControlSelectInput';
import { throw_createNewElementEvent, throw_event } from '../../events/events';
import { motion } from 'framer-motion';
import { ControlType } from '../generic/controls/Control';

export const CreateElementPopup = () => {
    const log = useLog();

    const [ active, setActive ] = useState(false);
    const [ position, setPosition ] = useState({x: 0, y: 0});
    const [ channelID, setChannelID ]: [ string, Function ] = useState("");
    const [ effectID, setEffectID ]: [ any, Function ] = useState("");
    const [ color, setColor ]: [ any, Function ] = useState(undefined);
    const [ type, setType ]: [ ControlType, Function ] = useState(undefined);

    const unset = () => {
        setChannelID("");
        setEffectID("");
        setColor(undefined);
        setType(undefined);
    }

    const submit = () => {
        if(type == undefined) return log.add.error("You must choose which element to use!");
        let finalColor = color;
        if(finalColor == undefined) finalColor = InterfaceColor.RED;
        if(channelID == "") 
            if(type != ControlType.LABEL) return log.add.error("You must set a Channel ID!");
        if(effectID == "") 
            if(type != ControlType.LABEL) return log.add.error("You must set an Effect ID!");

        if(isNaN(effectID)) return log.add.error("Effect ID must be a number");
        if(type != ControlType.LABEL) return throw_createNewElementEvent(type, channelID, effectID, finalColor, position, "");
        throw_createNewElementEvent(type, undefined, undefined, finalColor, position, "Text Label");
    }

    useEffect(() => {
        document.addEventListener(EventName.OpenCreateElementPopUp,(e: any) => {setActive(true); setPosition(e.detail.target); });
        document.addEventListener(EventName.CloseCreateElementPopUp,() => {setActive(false); unset();});
        return () => {
            document.removeEventListener(EventName.OpenCreateElementPopUp,(e: any) => {setActive(true); setPosition(e.detail.target);});
            document.removeEventListener(EventName.CloseCreateElementPopUp,() => {setActive(false); unset();});
        }
    },[]);

    return (
        <PopUp
            isVisible={active}
            close={() => { setActive(false); unset(); }}
            >
            <span className='block text-center font-bold text-6xl z-10 select-none text-blue-500 mb-20px duration-300' style={{color: color}}>
                Create Element
            </span>
            <ControlSelectInput onChange={(value: string) => setType(value)} value={type} color={color} />
            <ColorInput value={color} onChange={(value: InterfaceColor) => setColor(value)} barOnly={true} />

            <motion.div
                className='h-0px overflow-hidden'
                animate={{ scale: type == ControlType.LABEL ? 0 : 1}}
                transition={{ type: "spring", stiffness: 400, damping: 17 }}
            >
                <span className='block text-left font-bold text-2xl z-10 select-none text-blue-500 mb-5px mt-50px duration-300' style={{color: color}}>Channel ID</span>
                <TextInput onChange={(value: string) => setChannelID(value)} value={channelID} placeholder='Enter a Channel ID' />
                <span className='block text-left font-bold text-2xl z-10 select-none text-blue-500 mb-5px mt-50px duration-300' style={{color: color}}>Effect ID</span>
                <TextInput onChange={(value: string) => setEffectID(value)} value={effectID} placeholder='Enter a Channel ID' />
            </motion.div>
            <ButtonInput
                onClick={() => submit()}
                title='Create >>>'
                color={color ?? "#3B82F6"}
                />
        </PopUp>
    );
}