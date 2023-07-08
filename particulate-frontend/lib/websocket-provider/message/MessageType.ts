export const MessageType = {
    DEMAND_PING: 200,
    DEMAND_TOGGLE_ON: 201,
    DEMAND_TOGGLE_OFF: 202,
    DEMAND_KILL_ALL: 203,
    
} as const;
export type MessageType = typeof MessageType[keyof typeof MessageType];