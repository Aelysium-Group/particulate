import { useState, useEffect } from 'react';
import { EventName } from '../events/EventName';
import { BackgroundColor } from '../resources/BackgroundColor';

export const LoadingCircle = () => {
    const [ enabled, setEnabled ] = useState(false);

    useEffect(()=> {
        document.addEventListener(EventName.ShowLoader,() => setEnabled(true));
        document.addEventListener(EventName.HideLoader,() => setEnabled(false));
        return () => {
            document.addEventListener(EventName.ShowLoader,() => setEnabled(true));
            document.removeEventListener(EventName.HideLoader,() => setEnabled(false));
        }
    },[]);

    return (
        <div className={`fixed top-25px right-25px duration-500 ease-in-out ${enabled ? "scale-100" : "scale-0"}`}>
            <div className='rounded-full w-50px h-50px border-4 border-neutral-500 border-t-white animate-spin'></div>
        </div>
    );
}