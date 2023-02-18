import WebSocket from "ws";

export type OnReconnectCallback = (connection: WebSocket) => void;