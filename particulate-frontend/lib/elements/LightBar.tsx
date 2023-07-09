import { useState, useEffect } from 'react';
import { EventName } from '../events/EventName';
import { BackgroundColor } from '../resources/BackgroundColor';

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
        document.addEventListener(EventName.LightBar_ColorUpdate,event_catchLightBarUpdate);
        return () => {
            document.removeEventListener(EventName.LightBar_ColorUpdate,event_catchLightBarUpdate);
        }
    },[]);

    return <div className={`relative w-full h-3px overflow-hidden duration-200 ${color ?? props.defaultColor}`} />
}