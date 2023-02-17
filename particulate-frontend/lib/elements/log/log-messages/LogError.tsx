import * as React from 'react';
import { Message } from './Message';

interface LogError {
    unloadDelay: number;
    index: string;
    children: string;
}
export const LogError = (props: LogError) => (
        <Message
            unloadDelay={props.unloadDelay}
            index={props.index}
            children={props.children}
            color='bg-rose-600 text-neutral-300 font-bold'
            />
    );