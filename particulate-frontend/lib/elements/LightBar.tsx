import { useState, useEffect } from 'react';
import { EventType } from '../events/EventType';
import { BackgroundColor } from '../resources/BackgroundColor';

export const throw_lightBarColorUpdate = (id: string, color: BackgroundColor) => {
    const event = new CustomEvent(EventType.LightBar_ColorUpdate, { detail: {id, color} });
    document.dispatchEvent(event);
}

type LightBar = {
    catchId: string;
    defaultColor?: BackgroundColor;
}
export const LightBar = (props: LightBar) => {
    const [ color, setColor ] = useState(undefined);

    const event_catchLightBarUpdate = (e: any) => {
        if(e.detail.id == props.catchId)
            if(e.detail.color) {
                setColor(e.detail.color);
                setTimeout(() => setColor(undefined),2000);
            }
    }
    useEffect(()=> {
        document.addEventListener(EventType.LightBar_ColorUpdate,event_catchLightBarUpdate);
        return () => {
            document.removeEventListener(EventType.LightBar_ColorUpdate,event_catchLightBarUpdate);
        }
    },[]);

    return <div className={`relative w-full h-3px overflow-hidden duration-200 ${color ?? props.defaultColor}`} />
}