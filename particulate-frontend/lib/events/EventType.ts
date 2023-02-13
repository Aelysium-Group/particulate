export const EventType = {
    ContextMenuOpen: "react-contextMenu-open",
    ContextMenuClose: "react-contextMenu-close",

    ContextMenuOption_NewElement: "contextMenu-option-newElement",
    ContextMenuOption_DeleteElement: "contextMenu-option-deleteElement",

    RegisterNewControl: "register-control",
    DeleteControl: "delete-control",

    ContextMenuOption_StopEffects: "contextMenu-option-stopEffects",

    /**
     * Event contains the catchID of the Lightbar to target
     * and a TailwindCSS class name for the color to add
     */
    LightBar_ColorUpdate: "lightbar-colorUpdate",
} as const;
export type EventType = typeof EventType[keyof typeof EventType];