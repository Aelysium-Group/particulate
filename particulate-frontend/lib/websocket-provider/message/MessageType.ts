export const MessageType = {
    LOGIN: "LOGIN",
} as const;
export type MessageType = typeof MessageType[keyof typeof MessageType];