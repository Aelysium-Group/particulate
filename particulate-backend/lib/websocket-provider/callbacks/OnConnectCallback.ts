import WebSocket from "ws";

export type OnConnectCallback = (connection: WebSocket) => void;