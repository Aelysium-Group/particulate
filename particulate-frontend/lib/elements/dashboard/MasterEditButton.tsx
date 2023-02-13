import { motion } from "framer-motion";
import { Icon, IconName } from "../Icon";
import { throw_closeContextMenuEvent } from "../../events/ContextMenuEvents";

type MasterEditButton = {
    editMode: boolean;
    onClick: Function;
}
export const MasterEditButton = (props: MasterEditButton) => {
    const render_unactive = () => (
        <motion.div 
        className="fixed bottom-10 right-10 w-50px h-50px rounded-full bg-neutral-200 cursor-pointer"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            transition={{ type: "spring", stiffness: 400, damping: 17 }}
            onTap={() => {props.onClick();throw_closeContextMenuEvent();}}
            >
            <Icon className="w-30px m-10px aspect-square opacity-90" iconName={IconName.EDIT} />
        </motion.div>
    );

    const render_active = () => (
        <motion.div 
            className="fixed bottom-10 right-10 w-50px h-50px rounded-full bg-gradient-to-br from-orange-600 to-rose-600 cursor-pointer shadow-white-md"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            transition={{ type: "spring", stiffness: 400, damping: 17 }}
            onTap={() => {props.onClick();throw_closeContextMenuEvent();}}>
            <Icon className="w-30px m-10px aspect-square opacity-90 invert" iconName={IconName.EDIT} />
        </motion.div>
    );
    return props.editMode ? render_active() : render_unactive();
}