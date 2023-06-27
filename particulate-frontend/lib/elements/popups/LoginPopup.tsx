import { useEffect, useState } from 'react';
import { EventName } from "../../events/EventName";
import { PopUp } from './PopUp';
import { TextInput } from '../input/TextInput';
import { useLog } from '../../hooks/useLog';
import { ButtonInput } from '../input/ButtonInput';
import { InterfaceColor } from '../../resources/InterfaceColor';
import { PasswordInput } from '../input/PasswordInput';
import { ConnectionResolvable } from '../../session/ConnectionResolvable';
import { URIBuilder } from '../../websocket-provider/providers/URIBuilder';
import { StandardProtocol } from '../../websocket-provider/interfaces/StandardProtocol';
import { usePromise } from '../../hooks/usePromise';
import { Client } from '../../session/Client';
import { WebsocketMessage } from '../../websocket-provider/message/WebsocketMessage';
import { MessageType } from '../../websocket-provider/message/MessageType';

export const LoginPopup = () => {
    const log = useLog();

    const [ active, setActive ] = useState(false);
    const [ address, setAddress ] = useState("127.0.0.1");
    const [ port, setPort ] = useState(8080);
    const [ username, setUsername ] = useState("");
    const [ password, setPassword ] = useState("");

    const unset = () => {
        setAddress("127.0.0.1");
        setPort(8080);
    }

    const submit = async () => {
        if(username == "")   return log.add.error("Please provide a username to authenticate with.");
        if(password == "")   return log.add.error("Please provide a password to authenticate with.");

        try {
            const URI = new URIBuilder()
                            .setHostname(address)
                            .setPort(port)
                            .setProtocol(StandardProtocol.WS);
    
            const resolvable = new ConnectionResolvable(URI, null, username, password);
            const socket = await resolvable.resolve();
    
            if(!socket) throw new Error("Unable to connect to the remote host!");

            Client.instance.socket = socket
        } catch(error) {
            log.add.error(error.message);
        }
    }

    useEffect(() => {
        document.addEventListener(EventName.OpenLoginPopup,() => {setActive(true); });
        document.addEventListener(EventName.CloseLoginPopup,() => {setActive(false); unset();});
        return () => {
            document.removeEventListener(EventName.OpenLoginPopup,() => {setActive(true);});
            document.removeEventListener(EventName.CloseLoginPopup,() => {setActive(false); unset();});
        }
    },[]);

    return (
        <PopUp
            isVisible={active}
            close={() => {}}
            unCloseable={true}
            >
            <span className='block text-center font-bold text-6xl z-10 select-none text-blue-500 mb-20px'>
                Login
            </span>
            <span className='block text-left font-bold text-2xl z-10 select-none text-blue-500 mb-5px mt-20px'>IP Address</span>
            <TextInput onChange={(value: string) => setAddress(value)} value={address} placeholder='127.0.0.1' />
            <span className='block text-left font-bold text-2xl z-10 select-none text-blue-500 mb-5px mt-20px'>Port Number</span>
            <TextInput onChange={(value: string) => setPort(isNaN(parseInt(value)) ? port : parseInt(value))} value={port} placeholder='8080' />
            <div className='text-center'>
                <span className='block font-bold text-2xl z-10 select-none text-neutral-300 mb-5px mt-20px'>Connecting to:</span>
                <span className='"block text-neutral-300 h-47px w-full my-10px px-20px py-4px pt-3px font-bold text-xl bg-zinc-800 border-none rounded-xl appearance-none shadow-inset-md"'>
                    {address == "" ? "127.0.0.1" : address}:{port == 0 ? 8080 : port}
                </span>
            </div>

            <span className='block text-center font-bold text-4xl z-10 select-none text-blue-500 mb-20px mt-80px'>
                Authentication
            </span>
            <span className='block text-left font-bold text-2xl z-10 select-none text-blue-500 mb-5px mt-20px'>Username</span>
            <TextInput onChange={(value: string) => setUsername(value)} value={username} placeholder='Enter a username' />
            <span className='block text-left font-bold text-2xl z-10 select-none text-blue-500 mb-5px mt-20px'>Password</span>
            <PasswordInput onChange={(value: string) => setPassword(value)} value={password} placeholder='Enter a password' />
            <ButtonInput
                onClick={() => submit()}
                title='Login >>>'
                color={InterfaceColor.BLUE}
                />
        </PopUp>
    );
}