export interface WebSocketHandler {
    /**
     * Open the WebSocket connection.
     */
    connect(): Promise<boolean>;
    
    /**
     * Listen for an event.
     * @param eventName The name of the event to listen for.
     * @param callback The callback to fire once an event is thrown.
     * @throws An error if you attempt to assign a new event listener while the websocket is connected.
     */
    on(eventName: string, callback: Function): void;

    /**
     * Ping the connection.
     * @throws An error if you try to ping the connection while it's closed.
     */
    ping(): void;

    /**
     * Close the connection.
     * If you have `.shouldReconnect()` set to `true` this connection will reopen immediately after closing. If you want to permanently close the connection, use `.kill()` instead.
     */
    close(): void;
}