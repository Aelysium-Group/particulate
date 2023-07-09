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
import { Control, ControlType, ParseableControlObject } from './elements/generic/controls/Control';
import { AppLog } from './elements/log/AppLog';
import { throw_lightBarColorUpdate, throw_event, throw_openImportExportPopup } from './events/events';
import { DButtonTap } from './elements/dashboard/DButtonTap';
import { DButtonToggle } from './elements/dashboard/DButtonToggle';
import { useLog } from './hooks/useLog';
import { AnimatePresence } from 'framer-motion';
import { DLabel } from './elements/dashboard/DLabel';
import { LoginPopup } from './elements/popups/LoginPopup';
import { Client } from './session/Client';
import { LoadingCircle } from './elements/LoadingCircle';
import { DButtonHold } from './elements/dashboard/DButtonHold';
import { MasterImportExportButton } from './elements/dashboard/MasterImportExportButton';
import { ImportExportPopup } from './elements/popups/ImportExportPopup';
import { Position } from './resources/Position';

const editingContextMenu = [
    new Option(IconName.ADD, 'New Controller', EventName.OpenCreateElementPopUp),
    new Option(IconName.EDIT, 'Toggle Edit Mode', EventName.ToggleEditMode, "", true),
];

const liveContextMenu = [
    new Option(IconName.STOP, 'Stop All Effects', EventName.ContextMenuOption_StopEffects, "", false),
    new Option(IconName.EDIT, 'Toggle Edit Mode', EventName.ToggleEditMode, "", true),
];

type App = {
    template: Control[];
}
export const App = (props: App) => {
    const log = useLog();
    const [ backgroundGridShow, setBackgroundGridShow ] = useState(false);
    const [ editMode, setEditMode ] = useState(false);
    const [ controls, setControls ]: [ Control[], Function ] = useState(props.template);

    const stopEffects = () => {
        throw_lightBarColorUpdate("main","bg-red-500");
        log.add.error("Stopped all effects!");
    }
    const removeControl = (uuid: string) => setControls(controls.filter(control => control.uuid !== uuid));
    const updateControl = (object) => {
        const control: Control = controls.filter(control => control.uuid == object.uuid)[0];
        if(control == null) return;

        control.update(object.label, object.position);
    }
    const addController = (object: ParseableControlObject) => {
        const newControl = Control.parseControl(object);

        let newArray = controls.map((item: Control) => { return {...item}; })
        newArray.push(newControl);
        
        setControls(newArray);
        throw_event(EventName.CloseCreateElementPopUp);
    }
    const cycleNewControllers = (controls: Control[]) => {
        setControls(controls);
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
        document.addEventListener(EventName.ContextMenuOption_StopEffects, stopEffects);
        document.addEventListener(EventName.RegisterNewControl,(e: any) => addController(e.detail));
        document.addEventListener(EventName.DeleteControl,(e: any) => removeControl(e.detail.details.uuid));
        document.addEventListener(EventName.UpdateControl,(e: any) => updateControl(e.detail));
        document.addEventListener(EventName.ToggleEditMode, toggleEditMode);

        if(Client.instance == null || Client.instance.socket == null) throw_event(EventName.OpenLoginPopup);
        return () => {
            document.removeEventListener(EventName.ContextMenuOption_StopEffects, stopEffects);
            document.removeEventListener(EventName.RegisterNewControl,(e: any) => addController(e.detail));
            document.removeEventListener(EventName.DeleteControl,(e: any) => removeControl(e.detail.details.uuid));
            document.removeEventListener(EventName.UpdateControl,(e: any) => updateControl(e.detail));
            document.removeEventListener(EventName.ToggleEditMode, toggleEditMode);
        }
    }, [controls, editMode]);

    const view_default = () => {
        return (
            <>
                <LoginPopup />
                <ContextMenu />
                <CreateElementPopup />
                <ImportExportPopup controls={controls} setControl={(controls: Control[]) => {cycleNewControllers(controls)}} />
                <div className="text-center">
                    <div className='relative h-124px w-screen bg-template-gray overflow-hidden shadow-xl z-30'>
                        <span
                            className={`absolute top-5px left-[62px] text-left font-sneakers text-6xl cursor-default select-none text-neutral-100`}>
                                <div className={`relative top-5px mr-10px inline-block w-500px h-75px bg-no-repeat bg-center bg-contain`} style={{backgroundImage: `url(./icons/particulate-wordmark.png)`}}></div>
                                <span className={`relative -left-100px top-20px text-3xl ${editMode ? "text-blue-500" : "text-amber-500"}`}>
                                    {editMode ? " Editor" : " Controller"}
                                </span>
                        </span>
                        <LoadingCircle />
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
                                    if(item.type == ControlType.BUTTON_TAP)     return (<DButtonTap    key={item.uuid} uuid={item.uuid} channelID={item.channelID} effectID={item.effectID} color={item.color} initialCell={item.position} initialEditMode={editMode} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)} />);
                                    if(item.type == ControlType.BUTTON_HOLD)    return (<DButtonHold   key={item.uuid} uuid={item.uuid} channelID={item.channelID} effectID={item.effectID} color={item.color} initialCell={item.position} initialEditMode={editMode} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)} />);
                                    if(item.type == ControlType.BUTTON_TOGGLE)  return (<DButtonToggle key={item.uuid} uuid={item.uuid} channelID={item.channelID} effectID={item.effectID} color={item.color} initialCell={item.position} initialEditMode={editMode} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)} />);
                                    if(item.type == ControlType.LABEL)          return (<DLabel        key={item.uuid} uuid={item.uuid} color={item.color} initialCell={item.position} initialEditMode={editMode} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)} label={item.label} />);
                                })}
                            </AnimatePresence>
                        </div>
                    </div>
                    <MasterEditButton onClick={toggleEditMode} editMode={editMode} />
                    <MasterImportExportButton onClick={throw_openImportExportPopup} />
                </div>
                <AppLog />
            </>
        );
    }

    return view_default();
}
