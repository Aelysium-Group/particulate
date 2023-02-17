import { useState } from 'react';
import { useLog } from '../../../hooks/useLog';
import { useTimeout } from '../../../useTimeout';
import { motion } from 'framer-motion';

interface Message {
    unloadDelay: number;
    index: string;
    children: string;
    color: string
}
export const Message = (props: Message) => {
    const [ isVisible, setVisible] = useState(false);

    const log = useLog();
    
    useTimeout(
        () => setVisible(true),
        20
    );
    useTimeout(
        () => setVisible(false),
        props.unloadDelay * 1000
    );
    useTimeout(
        () => log.remove(props.index),
        props.unloadDelay * 2000
    );


    const render = () => {
        return (
            <motion.span
                    className={`relative inline-block rounded w-full px-2.5 py-2 mt-1 ${props.color} z-50`}
                    animate={{ left: isVisible ? "0" : "200%", }}
                    transition={{ type: "spring", stiffness: 400, damping: 10 }}
                >
                {props.children}
            </motion.span>
        );
    }

    return render();
}