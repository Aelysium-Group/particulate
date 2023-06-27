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
    private _publicKey: string;
    private _username: string;
    private _password: string;

    constructor(uri: URIBuilder, publicKey: string, username: string, password: string) {
        this._uri = uri;
        this._publicKey = publicKey;
        this._username = username;
        this._password = password;
    }

    private authenticate = () => {
        if(!this._socket) throw new WebsocketLaunchError("There was an issue connecting to the remote server.");

        const payload = new Payload(this._publicKey, this._username, MessageType.LOGIN);
              payload.set("password", this._password);

        this._socket.send(payload);
    }

    public resolve = async () => {
        const socket = new WebsocketSubscriber(this._uri.build(),5);
        socket.reconnect(2);

        socket.on("message",(message: string) => {
            console.log(message);
        });
        socket.on("close",() => {
            console.log("Connection closed...");
            throw_event(EventName.LogAppMessage, "Connection dropped. Attempting to reconnect...");
        });
        socket.on("connect",() => {
            this._socket = socket;
            this.authenticate();
        });
        socket.on("error",(event) => {
            throw_event(EventName.LogAppError, event);
            throw_event(EventName.OpenLoginPopup);
        });

        if(!(await socket.connect())) throw new Error('Unable to start the connection!');

        return socket;
    }
}