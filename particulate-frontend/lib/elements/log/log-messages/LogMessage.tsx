import * as React from 'react';
import { Message } from './Message';

interface LogMessage {
    unloadDelay: number;
    index: string;
    children: string;
}
export const LogMessage = (props: LogMessage) => (
        <Message
            unloadDelay={props.unloadDelay}
            index={props.index}
            children={props.children}
            color='bg-neutral-600'
            />
    );