package group.aelysium.particulatebridge.lib.websocket;

import java.net.ServerSocket;
import java.util.List;
import java.util.Map;

public class WebsocketServer {
    protected String name;
    protected int port;
    protected ServerSocket client;
    protected Map<EventType, Runnable> callbacks;

    public enum EventType {
        CONNECT,
        MESSAGE,
        CLOSE,
        ERROR,
    }
}

/**
 * A websocket provider.
 * Used to setup a websocket server.
 */
        export class WebsocketProvider implements WebSocketHandler {
    #_name?: string;
    #_port: number;
    #_server?: any;
    #_onConnectCallback: OnConnectCallback = () => {};
    #_onMessageCallback: OnMessageCallback = () => {};
    #_onCloseCallback:   OnCloseCallback   = () => {};
    #_onErrorCallback:   OnErrorCallback   = () => {};

    constructor(port: number, name?: string) {
        this.#_port = port;
        this.#_name = name ?? ("localhost:" + this.#_port.toString());
    }

    /**
     * Listen for an event.
     * @param eventName The name of the event to listen for.
     * @param callback The callback to fire once an event is thrown.
     * @throws An error if you attempt to assign a new event listener while the websocket is connected.
     */
    on = (eventName: string, callback: Function): void => {
        if(this.#_server) throw new Error("Tried to assign a new event listener while the connection is active!");

        if(eventName == "connect") return this.#_onConnect(callback as OnConnectCallback);
        if(eventName == "message") return this.#_onMessage(callback as OnMessageCallback);
        if(eventName == "close")   return this.#_onClose(callback as OnCloseCallback);
        if(eventName == "error")   return this.#_onError(callback as OnErrorCallback);

        console.warn(`WebsocketProvider was asked to listen for event: ${eventName}. But no such event exists!`);
    }

    /**
     * Set the callback to fire when the WebSocket first connects.
     * @param callback The callback to fire when the WebSocket connection opens.
     */
    #_onConnect = (callback: OnConnectCallback): void => this.#_onConnectCallback = callback;

    /**
     * Set the callback to fire when the WebSocket receives a message.
     * @param callback The callback to fire when the WebSocket receives a message.
     */
    #_onMessage = (callback: OnMessageCallback): void => this.#_onMessageCallback = callback;

    /**
     * Set the callback to fire when the WebSocket connection closes.
     * @param callback The callback to fire when the WebSocket connection closes.
     */
    #_onClose = (callback: OnCloseCallback): void => this.#_onCloseCallback = callback;

    /**
     * Set the callback to fire when the WebSocket connection encounters an error.
     * @param callback The callback to fire when the WebSocket encounters an error.
     */
    #_onError = (callback: OnErrorCallback): void => this.#_onErrorCallback = callback;

    /**
     * Open the WebSocket for connections to be made.
     */
    connect = (): Promise<boolean> => new Promise((success, failure) => {
        try {
            console.log(`--| Starting new websocket server '${this.#_name}' on port: ${this.#_port}...`);

            const port = this.#_port;
            this.#_server = new WebSocket.Server({ port });

            console.log(`>>> Websocket server '${this.#_name}' started on port: ${this.#_port}...`);

            console.log(`>>> Listening for websocket connections to '${this.#_name}' on port: ${this.#_port}...`);

            const onNewConnection = (connection: WebSocket) => {
                this.#_onConnectCallback(connection);

                /**
                 * When a connection dies.
                 */
                connection.on("close", () => this.#_onCloseCallback());

                /**
                 * When a message is received.
                 */
                connection.on("message", (message: Buffer) => this.#_onMessageCallback(message.toString()));

                /**
                 * When there's an error on a connection.
                 */
                connection.on("error", (error: ErrorEvent) => this.#_onErrorCallback(error));
            }

            /**
             * When a new connection is made.
             */
            this.#_server.on("connection", (connection: WebSocket) => onNewConnection(connection));

            return success(true);
        } catch(error) {
            return failure(false);
        }
    });

    /**
     * Send a message over the connection.
     * @param message The message to be sent.
     * @throws An error if you try to send a message when the connection is closed.
     */
    send = (message: string): void => {
        if(!this.#_server) throw new Error("Tried to send a message over a WebSocket connection that wasn't open!");

        this.#_server.send(message);
    }

    /**
     * Ping the connection.
     * @throws An error if you try to ping the connection while it's closed.
     */
    ping = (): void => {
        if(!this.#_server) throw new Error("Tried to send a message over a WebSocket connection that wasn't open!");

        this.#_server.ping();
    }

    /**
     * Close the connection.
     * If you have `.shouldReconnect()` set to `true` this connection will reopen immediately after closing. If you want to permanently close the connection, use `.kill()` instead.
     */
    close = (): void => {
        if(!this.#_server) return;

        this.#_server.close();
    }
}
