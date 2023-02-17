import { useState, useEffect } from 'react';
import { DBackground } from "./elements/dashboard/DBackground";
import { MasterEditButton } from "./elements/dashboard/MasterEditButton";
import { ContextMenu } from "./elements/context/ContextMenu";
import { IconName } from "./elements/Icon";
import { EventName } from "./events/EventName";
import { Option } from "./elements/context/ContextMenuOption";
import { ContextLaunchingDiv } from "./elements/context/ContextLaunchingDiv";
import { CreateElementPopup } from './elements/popups/CreateElementPopup';
import { LightBar } from './elements/LightBar';
import { Control, ControlType } from './elements/generic/controls/Control';
import { AppLog } from './elements/log/AppLog';
import { throw_lightBarColorUpdate, throw_event } from './events/events';
import { DButtonClick } from './elements/dashboard/DButtonClick';
import { DButtonToggle } from './elements/dashboard/DButtonToggle';
import { useLog } from './hooks/useLog';
import { AnimatePresence } from 'framer-motion';
import { DLabel } from './elements/dashboard/DLabel';

const editingContextMenu = [
    new Option(IconName.ADD, 'New Controller', EventName.OpenCreateElementPopUp),
];

const liveContextMenu = [
    new Option(IconName.STOP, 'Stop All Effects', EventName.ContextMenuOption_StopEffects, "", false),
];

export const App = () => {
    const log = useLog();
    const [ backgroundGridShow, setBackgroundGridShow ] = useState(false);
    const [ editMode, setEditMode ] = useState(false);
    const [ controls, setControls ]: [ Control[], Function ] = useState([]);

    const stopEffects = () => {
        throw_lightBarColorUpdate("main","bg-red-500");
        log.add.error("Stopped all effects!");
    }
    const removeControl = (uuid: string) => setControls(controls.filter(control => control.uuid !== uuid));
    const addController = (object: object) => {
        const newControl = Control.parseControl(object);

        let newArray = controls.map((item: Control) => { return {...item}; })
        newArray.push(newControl);
        
        setControls(newArray);
        throw_event(EventName.CloseCreateElementPopUp);
    }

    const toggleEditMode = () => {
        if(editMode) {
            throw_event(EventName.ExitControlEditMode);
            setEditMode(false);
        } else {
            throw_event(EventName.EnterControlEditMode);
            setEditMode(true);
        }
    }

    useEffect(()=>{
        document.addEventListener(EventName.ContextMenuOption_StopEffects,stopEffects);
        document.addEventListener(EventName.RegisterNewControl,(e: any) => addController(e.detail));
        document.addEventListener(EventName.DeleteControl,(e: any) => removeControl(e.detail.details.uuid));
        return () => {
            document.removeEventListener(EventName.ContextMenuOption_StopEffects, stopEffects);
            document.removeEventListener(EventName.RegisterNewControl,(e: any) => addController(e.detail));
            document.removeEventListener(EventName.DeleteControl,(e: any) => removeControl(e.detail.details.uuid));
        }
    },[controls]);

    const view_default = () => {
        return (
            <>
                <ContextMenu />
                <CreateElementPopup />
                <div className="text-center">
                    <div className='relative h-124px w-screen bg-template-gray overflow-hidden shadow-xl'>
                        <span
                            className={`absolute top-30px left-[62px] text-left font-sneakers text-6xl cursor-default select-none text-neutral-100`}>
                                Particulate
                                <span className={`text-3xl ${editMode ? "text-blue-500" : "text-amber-500"}`}>
                                    {editMode ? " Editor" : " Controller"}
                                </span>
                        </span>
                        <div className='absolute top-[120px] left-0 w-screen'>
                            <LightBar catchId='main' defaultColor={editMode ? "bg-blue-500" : "bg-amber-500"}/>
                        </div>
                    </div>
                    <div className="relative h-[calc(100vh_-_124px)] w-screen">
                        <ContextLaunchingDiv options={editMode ? editingContextMenu : liveContextMenu}>
                            { editMode ? <DBackground active={backgroundGridShow}/> : <div className={`absolute w-screen h-screen inset-0 bg-template-gray z-0`} />}
                        </ContextLaunchingDiv>
                        <div className="z-10" draggable={false}>
                            <AnimatePresence>
                                {controls.map((item: Control) => {
                                    if(item.type == ControlType.BUTTON_CLICK)   return (<DButtonClick  key={item.uuid} uuid={item.uuid} color={item.color} initialCell={item.position} initialEditMode={true} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)} />);
                                    if(item.type == ControlType.BUTTON_TOGGLE)  return (<DButtonToggle key={item.uuid} uuid={item.uuid} color={item.color} initialCell={item.position} initialEditMode={true} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)} />);
                                    if(item.type == ControlType.LABEL)          return (<DLabel        key={item.uuid} uuid={item.uuid} color={item.color} initialCell={item.position} initialEditMode={true} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)} />);
                                })}
                            </AnimatePresence>
                        </div>
                    </div>
                    <MasterEditButton onClick={toggleEditMode} editMode={editMode} />
                </div>
                <AppLog />
            </>
        );
    }

    return view_default();
}
