import { URIBuilder } from "../websocket-provider/providers/URIBuilder";
import { WebsocketSubscriber } from "../websocket-provider/providers/WebsocketSubscriber";
import { throw_event } from '../events/events';
import { EventName } from "../events/EventName";
import { WebsocketLaunchError } from "../errors/WebsocketLaunchError";
import { Payload } from "../websocket-provider/providers/Payload";
import { MessageType } from "../websocket-provider/message/MessageType";

export class ConnectionResolvable {
    private _socket?: WebsocketSubscriber;
    private _uri: URIBuilder;
    private _authKey: string;

    constructor(uri: URIBuilder, authKey: string) {
        this._uri = uri;
        this._authKey = authKey;
    }

    public resolve = async () => {
        const socket = new WebsocketSubscriber(this._uri.build(),30);
        socket.reconnect(1);

        socket.on("message",(message: string) => {
            console.log(message);
        });
        socket.on("close",() => {
            console.log("Connection closed...");
            throw_event(EventName.ShowLoader);
        });
        socket.on("connect",() => {
            this._socket = socket;
            throw_event(EventName.CloseLoginPopup);
            throw_event(EventName.HideLoader);
        });
        socket.on("error",(event) => {
            throw_event(EventName.LogAppError, event);
            throw_event(EventName.OpenLoginPopup);
        });

        if(!(await socket.connect())) throw new Error('Unable to start the connection!');

        return socket;
    }
}