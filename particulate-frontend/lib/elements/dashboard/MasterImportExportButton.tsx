import { motion } from "framer-motion";
import { Icon, IconName } from "../Icon";
import { throw_closeContextMenuEvent } from "../../events/events";

type MasterEditButton = {
    onClick: Function;
}
export const MasterImportExportButton = (props: MasterEditButton) => {
    return (
        <motion.div 
        className="fixed bottom-10 right-100px w-50px h-50px rounded-full bg-neutral-200 cursor-pointer"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            transition={{ type: "spring", stiffness: 400, damping: 17 }}
            onTap={() => {
                props.onClick();
                throw_closeContextMenuEvent();
            }}
            >
            <Icon className="w-30px m-10px aspect-square opacity-90" iconName={IconName.TRANSFER} />
        </motion.div>
    );
}