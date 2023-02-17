export const EventName = {
    ContextMenuOpen: "react-contextMenu-open",
    ContextMenuClose: "react-contextMenu-close",

    OpenCreateElementPopUp: "OpenCreateElementPopUp",
    CloseCreateElementPopUp: "CloseCreateElementPopUp",

    EnterControlEditMode: "EnterControlEditMode",
    ExitControlEditMode: "ExitControlEditMode",

    RegisterNewControl: "register-control",
    DeleteControl: "delete-control",

    ContextMenuOption_StopEffects: "contextMenu-option-stopEffects",

    TransportLogMessages: "transport-logMessages",

    /**
     * Event contains the catchID of the Lightbar to target
     * and a TailwindCSS class name for the color to add
     */
    LightBar_ColorUpdate: "lightbar-colorUpdate",
} as const;
export type EventName = typeof EventName[keyof typeof EventName];