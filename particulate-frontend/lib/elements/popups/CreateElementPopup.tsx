import { useEffect, useState } from 'react';
import { EventType } from "../../events/EventType";
import { PopUp } from './PopUp';
import { TextInput } from '../input/Input';

export const CreateElementPopup = () => {
    const [ active, setActive ] = useState(false);

    const [ channelID, setChannelID ] = useState("");

    useEffect(() => {
        document.addEventListener(EventType.ContextMenuOption_NewElement,() => {setActive(true)});
        return () => {
            document.removeEventListener(EventType.ContextMenuOption_NewElement,() => {setActive(true)});
        }
    },[]);

    return (
        <PopUp
            isVisible={active}
            close={() => setActive(false)}
            >
            <span className='block text-center font-bold text-6xl z-10 select-none text-blue-500 mb-20px'>
                Create Element
            </span>
            <span className='block text-left font-bold text-2xl z-10 select-none text-blue-500 mb-5px mt-50px'>FX Channel ID</span>
            <TextInput onChange={(value: string) => setChannelID(value)} value={channelID} />
        </PopUp>
    );
}