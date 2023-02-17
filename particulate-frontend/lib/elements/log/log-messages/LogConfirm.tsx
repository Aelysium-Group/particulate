import * as React from 'react';
import { Message } from './Message';

interface LogConfirm {
    unloadDelay: number;
    index: string;
    children: string;
}
export const LogConfirm = (props: LogConfirm) => (
        <Message
            unloadDelay={props.unloadDelay}
            index={props.index}
            children={props.children}
            color='bg-green-500'
            />
    );