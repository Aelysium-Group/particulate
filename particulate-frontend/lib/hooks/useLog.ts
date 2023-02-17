import { useEffect, useState } from "react";
import { dirtyClone } from "../features/dirtyClone";
import { EventName } from "../events/EventName";
import { throw_transportLogMessagesEvent } from "../events/events";

let index = 0;
let messages = {};
interface Log {
    'add': {
        'message': Function,
        'confirm': Function,
        'error':   Function
    },
    'remove': Function,
    'getAll': Function
}

export type LogMessages = {
    [key: number]: {
        contents: string;
        type: string;
    }
}

/**
 * Add, remove, and access the App's client log
 */
export const useLog = (): Log => {
    /**
     * Add a log entry to the log.
     * @param contents The text contents of the log.
     * @param type The type of the log
     */
    const addLog = (contents: string, type: string): void => {
        const newIndex: number = index + 1;
        const newMessages: LogMessages = dirtyClone(messages);
    
        newMessages[newIndex] = {
            contents,
            type
        };
        
        messages = newMessages;
        index = newIndex;

        throw_transportLogMessagesEvent(messages);
    }
    
    /**
     * Remove a log entry from the log.
     * @param index The index of the log to remove.
     */
    const removeLog = (index: string): void => {
        const newMessages: LogMessages = dirtyClone(messages);
        delete newMessages[parseInt(index)];

        messages = newMessages;

        throw_transportLogMessagesEvent(messages);
    }
    
    /**
     * Add a log entry of type `message` to the log.
     * @param contents The text contents of the log.
     */
    const sendMessage = (contents: string): void => addLog(contents,'message');
    
    /**
     * Add a log entry of type `confirm` to the log.
     * @param contents The text contents of the log.
     */
    const sendConfirm = (contents: string): void => addLog(contents,'confirm');
    
    /**
     * Add a log entry of type `error` to the log.
     * @param contents The text contents of the log.
     */
    const sendError = (contents: string): void => addLog(contents,'error');
    
    /**
     * Returns all logs
     * @returns All logs
     */
    const getAll = (): LogMessages => messages;

    const returnBody = {
        'add': {
            'message': sendMessage,
            'confirm': sendConfirm,
            'error':   sendError
        },
        'remove': removeLog,
        getAll
    };
    return (returnBody as Log);
}
