import { useScreen, useView, QueryMode, useHookOntoScreen } from 'react-ui-breakpoints';
import { Pixels } from '../../resources/Pixels';
import { motion } from 'framer-motion';
import { Icon, IconName } from '../Icon';

interface PopUp {
    isVisible: boolean;
    close: Function;
    children: JSX.Element | JSX.Element[];
    className?: string;
    width?: Pixels;
    unCloseable?: boolean;
}
export const PopUp = (props: PopUp) => {
    const auto = useHookOntoScreen();

    const view_desktop = () => (
        <motion.div className={`fixed left-0 w-screen h-screen z-50`}
            initial={{top: window.innerHeight}}
            animate={{top: props.isVisible ? 0 : window.innerHeight}}
            transition={{ type: "spring", stiffness: 400, damping: 20 }}
            >
            <div className='relative w-screen h-screen pb-100px'>
                <div 
                    className={`absolute left-0 top-0 w-screen h-screen duration-200 opacity-80 ${ props.unCloseable ? "bg-neutral-900/80 cursor-default" : "bg-transparent cursor-pointer"}`}
                    onClick={() => props.close()}
                    ></div>
                <div className='mx-auto' style={{width: props.width ?? "700px"}}>
                    <motion.div
                        className='frosted-glass block relative top-25px min-h-[300px] rounded-lg bg-zinc-900/75 overflow-auto overflow-x-hidden shadow-2xl'
                        drag
                        dragConstraints={{left: 0, right: 0, top: 0, bottom: 200}}
                        transition={{ type: "spring", stiffness: 100, damping: 10 }}
                        >
                        {
                            props.unCloseable ? <></> :
                            <motion.div
                                className={`absolute left-25px top-25px cursor-pointer leading-none z-50`}
                                onTapStart={() => props.close()}
                                whileHover={{ scale: 1.2 }}
                                whileTap={{ scale: 0.8 }}
                                transition={{ type: "spring", stiffness: 400, damping: 12 }}
                                >
                                <Icon iconName={IconName.CLOSE} className='w-40px aspect-square invert'/>
                            </motion.div>
                        }
                        <div className='p-10 z-20 isolate'>
                            { props.isVisible ? props.children : <></> }
                        </div>
                    </motion.div>
                </div>
            </div>
        </motion.div>
    )
    
    const view_default = () => (
        <div className={`fixed left-0 w-screen h-screen z-50 duration-500
                       ${props.isVisible ? 'top-0 opacity-100' : 'top-full opacity-0'}`}>
            <div className='relative w-screen h-screen pb-100px'>
                <div className={`absolute left-0 top-0 w-screen h-screen bg-black duration-200 cursor-pointer opacity-80`} />
                <div className='w-full'>
                    <div className='frosted-glass block relative h-screen w-screen overflow-auto overflow-x-hidden duration-300 ease-[cubic-bezier(.34,.98,.7,1)]'>
                        <span
                            className={`fixed right-15px text-3xl text-neutral-400 cursor-pointer leading-none duration-200 hover:text-neutral-300 z-50
                                        ${props.isVisible ? 'top-15px' : '-top-25px'}`}
                            onClick={() => props.close()}
                            >✖</span>
                        <div className='p-10 z-20 isolate'>
                            { props.isVisible ? props.children : <></> }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )

    return useScreen(
        QueryMode.MOBILE_FIRST,
        useView("800px", view_desktop()),
        useView("default", view_default())
    );
}