export const EventName = {
    ToggleEditMode: "ToggleEditMode",

    ContextMenuOpen: "react-contextMenu-open",
    ContextMenuClose: "react-contextMenu-close",

    OpenCreateElementPopUp: "OpenCreateElementPopUp",
    CloseCreateElementPopUp: "CloseCreateElementPopUp",

    OpenLoginPopup: "OpenLoginPopup",
    CloseLoginPopup: "CloseLoginPopup",

    LogAppError: "LogAppError",
    LogAppSuccess: "LogAppSuccess",
    LogAppMessage: "LogAppMessage",

    RegisterNewControl: "register-control",
    DeleteControl: "delete-control",

    ContextMenuOption_StopEffects: "contextMenu-option-stopEffects",

    TransportLogMessages: "transport-logMessages",

    ShowLoader: "show-loader",
    HideLoader: "hide-loader",

    
    DispatchDemandMessage: "dispatch-demandMessage",
    DispatchDemandToggleOnMessage: "dispatch-demandToggleOnMessage",
    DispatchDemandToggleOffMessage: "dispatch-demandToggleOffMessage",

    /**
     * Event contains the catchID of the Lightbar to target
     * and a TailwindCSS class name for the color to add
     */
    LightBar_ColorUpdate: "lightbar-colorUpdate",
} as const;
export type EventName = typeof EventName[keyof typeof EventName];