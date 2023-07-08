import { useEffect, useState } from 'react';
import { EventName } from "../../events/EventName";
import { PopUp } from './PopUp';
import { useLog } from '../../hooks/useLog';
import { ButtonInput } from '../input/ButtonInput';
import { InterfaceColor } from '../../resources/InterfaceColor';
import { Client } from '../../session/Client';
import { TextareaInput } from '../input/TextareaInput';
import { Control, ParseableControlObject } from '../generic/controls/Control';
import { motion } from 'framer-motion';

type ImportExportPopup = {
    controls: Control[];
    setControls: Function;
}
export const ImportExportPopup = (props: ImportExportPopup) => {
    const log = useLog();

    const [ active, setActive ] = useState(false);
    const [ importTextarea, setImportTextarea ] = useState("");
    const [ exportTextarea, setExportTextarea ] = useState("");

    const unset = () => {
        setImportTextarea("");
        setExportTextarea("");
    }

    const submitImport = async () => {
        if(importTextarea == "")   return log.add.error("Please provide a template to import.");
        const controls: Control[] = [];

        try {
            const object: ParseableControlObject[] = JSON.parse(importTextarea);

            object.forEach(entry => {
                controls.push(Control.parseControl(entry));
            });

            props.setControls(controls);
        } catch(error) {
            log.add.error("Unable to parse import!");
        }
    }

    const submitExport = async () => {
        const array: ParseableControlObject[] = [];

        props.controls.forEach(control => {
            array.push(control.toJSON());
        });

        setExportTextarea(JSON.stringify(array));
    }

    useEffect(() => {
        document.addEventListener(EventName.OpenImportExportPopup, () => {setActive(true); });
        document.addEventListener(EventName.CloseImportExportPopup, () => {setActive(false); unset();});
        return () => {
            if(Client.instance.socket != null) Client.instance.socket.kill();
            document.removeEventListener(EventName.OpenImportExportPopup, () => {setActive(true);});
            document.removeEventListener(EventName.CloseImportExportPopup, () => {setActive(false); unset();});
        }
    },[]);

    return (
        <PopUp
            isVisible={active}
            close={() => {setActive(false); unset();}}
            >
            <span className='block text-center font-bold text-6xl z-10 select-none text-blue-500 mb-20px'>
                Import / Export
            </span>
            <TextareaInput onChange={(value: string) => setImportTextarea(value)} value={importTextarea} placeholder='Import Template' />
            <ButtonInput
                onClick={() => submitImport()}
                title='Import'
                color={InterfaceColor.GREEN}
                />
            <div className='h-2px bg-slate-400 w-full my-20px' />
            <ButtonInput
                onClick={() => submitExport()}
                title='Export'
                color={InterfaceColor.ORANGE}
                />
            <TextareaInput onChange={() => {}} value={exportTextarea} placeholder='Export will appear here' />
        </PopUp>
    );
}