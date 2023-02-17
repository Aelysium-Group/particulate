import { motion } from "framer-motion";
import { InterfaceColor } from "../../resources/InterfaceColor";

type ButtonInput = {
    onClick: Function;
    title: string;
    color: InterfaceColor;
}
export const ButtonInput = (props: ButtonInput) => (
    <motion.div
        className='relative top-20px p-9px pt-8px mt-10px text-center text-2xl rounded-xl block text-neutral-300 font-bold cursor-pointer select-none'
        style={{ background: props.color }}
        whileHover={{ scale: 1.05 }}
        whileTap={{ scale: 0.9 }}
        onTapStart={() => props.onClick()}
        transition={{ type: "spring", stiffness: 400, damping: 10 }}
        >
        {props.title}
    </motion.div>
)