import { useState, useEffect } from 'react';
import { DButtonClick, DButtonToggle } from "./elements/dashboard/DButton";
import { DBackground } from "./elements/dashboard/DBackground";
import { Button, ButtonType } from './elements/generic/controls/Button';
import { MasterEditButton } from "./elements/dashboard/MasterEditButton";
import { InterfaceColor } from "./resources/InterfaceColor";
import { ContextMenu } from "./elements/context/ContextMenu";
import { IconName } from "./elements/Icon";
import { EventType } from "./events/EventType";
import { Option } from "./elements/context/ContextMenuOption";
import { ContextLaunchingDiv } from "./elements/context/ContextLaunchingDiv";
import { CreateElementPopup } from './elements/popups/CreateElementPopup';
import { LightBar, throw_lightBarColorUpdate } from './elements/LightBar';
import { Control } from './elements/generic/controls/Control';

const editingContextMenu = [
    new Option(IconName.ADD, 'New Controller', EventType.ContextMenuOption_NewElement),
];

const liveContextMenu = [
    new Option(IconName.STOP, 'Stop All Effects', EventType.ContextMenuOption_StopEffects, "", false),
];

const controls = [
    new Button(0,0),
    new Button(0,1),
    new Button(0,2),
    new Button(0,3),
    new Button(1,0, InterfaceColor.ORANGE, ButtonType.TOGGLE),
    new Button(1,1, InterfaceColor.ORANGE, ButtonType.TOGGLE),
    new Button(1,2, InterfaceColor.ORANGE, ButtonType.TOGGLE),
    new Button(1,3, InterfaceColor.ORANGE, ButtonType.TOGGLE),
]

export const App = () => {
    const [ backgroundGridShow, setBackgroundGridShow ] = useState(false);
    const [ editMode, setEditMode ] = useState(false);
    const [ activeControls, setControls ]: [ Control[], Function ] = useState([]);

    const removeController = (uuid: string) => {
        activeControls.find((control => control.uuid == uuid));
    }
    const addController = (control: Control) => {
        let newControls = activeControls;
        newControls.push(control);
        setControls(newControls);
    }

    useEffect(()=>{
        document.addEventListener(EventType.ContextMenuOption_StopEffects,() => throw_lightBarColorUpdate("main","bg-red-500"));
        document.addEventListener(EventType.RegisterNewControl,(e: any) => addController(e.detail.control));
        return () => {
            document.removeEventListener(EventType.ContextMenuOption_StopEffects,() => throw_lightBarColorUpdate("main","bg-red-500"));
            document.removeEventListener(EventType.RegisterNewControl,(e: any) => addController(e.detail.control));
        }
    },[]);

    const view_default = () => {
        let controlComponents: JSX.Element[] = [];
        let i = 0;
        for (const item of controls) {
            if(item instanceof Button) {
                if(item.type == ButtonType.CLICK)  controlComponents.push(<DButtonClick  key={i} uuid={item.uuid} color={item.color} initialCell={item.position} editMode={editMode} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)}></DButtonClick> );
                if(item.type == ButtonType.TOGGLE) controlComponents.push(<DButtonToggle key={i} uuid={item.uuid} color={item.color} initialCell={item.position} editMode={editMode} onDragStart={() => setBackgroundGridShow(true)} onDragEnd={() => setBackgroundGridShow(false)}></DButtonToggle>);
            }
            i++;
        }

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
                            <DBackground active={backgroundGridShow}/>
                        </ContextLaunchingDiv>
                        <div className="z-10" draggable={false}>
                            {controlComponents}
                        </div>
                    </div>
                    <MasterEditButton onClick={() => setEditMode(!editMode)} editMode={editMode} />
                </div>
            </>
        );
    }

    return view_default();
}
