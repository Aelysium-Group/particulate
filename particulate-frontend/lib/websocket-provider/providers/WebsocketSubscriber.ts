import { OnCloseCallback } from '../interfaces/callbacks/OnCloseCallback';
import { OnConnectCallback } from '../interfaces/callbacks/OnConnectCallback';
import { OnErrorCallback } from '../interfaces/callbacks/OnErrorCallback';
import { OnMessageCallback } from '../interfaces/callbacks/OnMessageCallback';
import { OnReconnectCallback } from '../interfaces/callbacks/OnReconnectCallback';
import { WebSocketHandler } from '../interfaces/WebSocketHandler';
import { Heart } from './Heart';

/**
 * A websocket subscriber.
 * Used to setup a connection to a websocket server.
 */
export class WebsocketSubscriber implements WebSocketHandler {
    #_uri: string;
    #_reconnectHeart?: Heart;
    #_heart: Heart;
    #_connection?: WebSocket = undefined;
    #_onConnectCallback:    OnConnectCallback = () => {};
    #_onReconnectCallback:  OnReconnectCallback = () => {};
    #_onMessageCallback:    OnMessageCallback = () => {};
    #_onCloseCallback:      OnCloseCallback   = () => {};
    #_onErrorCallback:      OnErrorCallback   = () => {};

    constructor(uri: string, timeout?: number) {
        this.#_uri = uri;

        this.#_heart = new Heart(timeout ?? 30);
    }

    on = (eventName: string, callback: Function): void => {
        if(this.#_connection) throw new Error("Tried to assign a new event listener while the connection is active!");

        switch (eventName) {
            case "connect":
                this.#_onConnect(callback as OnConnectCallback);
                return;
            case "reconnect":
                this.#_onReconnect(callback as OnReconnectCallback);
                return;
            case "message":
                this.#_onMessage(callback as OnMessageCallback);
                return;
            case "close":
                this.#_onClose(callback as OnCloseCallback);
                return;
            case "error":
                this.#_onError(callback as OnErrorCallback);
                return;
        }

        console.warn(`WebsocketProvider was asked to listen for event: ${eventName}. But no such event exists!`);
    }

    /**
     * Set the callback to fire when the WebSocket first connects.
     * @param callback The callback to fire when the WebSocket connection opens.
     */
    #_onConnect = (callback: OnConnectCallback): OnConnectCallback => this.#_onConnectCallback = callback;

    /**
     * Set the callback to fire when the WebSocket receives a message.
     * @param callback The callback to fire when the WebSocket receives a message.
     */
    #_onMessage = (callback: OnMessageCallback): OnMessageCallback => this.#_onMessageCallback = callback;

    /**
     * Set the callback to fire when the WebSocket connection closes.
     * @param callback The callback to fire when the WebSocket connection closes.
     */
    #_onClose = (callback: OnCloseCallback): OnCloseCallback => this.#_onCloseCallback = callback;

    /**
     * Set the callback to fire when the WebSocket connects again after closing.
     * @param callback The callback to fire when the WebSocket connection closes.
     */
    #_onReconnect = (callback: OnReconnectCallback): OnReconnectCallback => this.#_onReconnectCallback = callback;

    /**
     * Set the callback to fire when the WebSocket connection encounters an error.
     * @param callback The callback to fire when the WebSocket encounters an error.
     */
    #_onError = (callback: OnErrorCallback): OnErrorCallback => this.#_onErrorCallback = callback;

    /**
     * Open the WebSocket connection.
     */
    connect = (): Promise<boolean> => new Promise((success) => {
        console.log(`--| Initializing websocket subscriber '${this.#_uri}'...`);

        const connection = new WebSocket(this.#_uri);
        this.#_connection = connection;

        let connectionOpened = false;
        
        this.#_connection.addEventListener("open",() => {
            this.#_onConnectCallback(this.#_connection);
            connectionOpened = true;
        });

        /**
         * If the heart dies, terminate the connection.
         */
        this.#_heart.on("death",() => {
            this.#_connection?.close();
        });
        /**
         * Start the heart.
         */
        this.#_heart.start(() => {
            this.#_heart.killLater();
            this.ping();
        });

        /**
         * When the connection is ponged.
         */
        this.#_connection.addEventListener('pong', () => {
            this.#_heart.keepBeating();
        });

        /**
         * When message is received.
         */
        this.#_connection.addEventListener("message",(event: Event) => {
            this.#_onMessageCallback(event.toString());
        });

        /**
         * When connection dies.
         */
         this.#_connection.addEventListener("close", async () => {
            this.#_heart.kill();
            this.#_onCloseCallback();
            
            if(this.#_reconnectHeart != undefined) {
                const attemptReconnection = () => {
                    console.log('--| Attempting to reconnect...');
                    try {
                        this.#_reconnectHeart?.start(async () => {
                            await this.connect();

                            // If the connection closed without even opening. It means the server is dead.
                            if(!connectionOpened) {
                                console.warn('--| Server either isn\'t alive or was unreachable!');
                            }
            
                            this.#_onReconnectCallback(connection);
            
                            this.#_reconnectHeart?.kill();
                        });
                    } catch(error) {
                        console.log('--| Attempt failed!');
                        attemptReconnection();
                    }
                }
                attemptReconnection();
            }
        });

        /**
         * When there's an error on the connection, call the specified callback.
         */
        this.#_connection.addEventListener("error", (event) => this.#_onErrorCallback(event));

        console.log(`--| Websocket subscriber '${this.#_uri}' is active.`);

        return success(true);
    });


    /**
     * Send a message over the connection.
     * @param message The message to be sent.
     * @throws An error if you try to send a message when the connection is closed.
     */
    send = (message: string): void => {
        if(!this.#_connection) throw new Error("Tried to send a message over a WebSocket connection that wasn't open!");

        this.#_connection.send(message);
    }

    /**
     * Ping the connection.
     * @throws An error if you try to ping the connection while it's closed.
     */
    ping = (): void => {
        if(!this.#_connection) throw new Error("Tried to send a message over a WebSocket connection that wasn't open!");

        //this.#_connection.;
    }

    /**
     * Close the connection.
     * If you have `.shouldReconnect()` set to `true` this connection will reopen immediately after closing. If you want to permanently close the connection, use `.kill()` instead.
     */
    close = (): void => {
        if(!this.#_connection) return;

        this.#_connection.close();
    }

    /**
     * Kill the connection.
     * The only difference between this and `.close()` is that this will not allow the connection to be restarted; even if `.reconnect()` has been set.
     */
    kill = (): void => {
        if(!this.#_connection) return;

        this.#_reconnectHeart = undefined;
        this.#_connection.close();

        console.warn("Connection has been killed.");
    }

    /**
     * Set this Websocket to attempt to reconnect.
     * @param delay Default `null` - Time after failure (in seconds) to wait before attempting to reconnect. Set to `null` to disable auto-reconnection.
     */
    reconnect = (delay: number) => {
        if(this.#_connection) throw new Error("Tried to set the webhook to auto-reconnect after it's already connected!");

        this.#_reconnectHeart = new Heart(delay);
    }
}
