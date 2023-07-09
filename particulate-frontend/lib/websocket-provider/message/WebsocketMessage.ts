import { MessageType } from "./MessageType";

export class WebsocketMessage {
    private _authToken: string;
    private _type: MessageType;
    private _payload: Map<string, any> = new Map();

    constructor(auth_token: string, type: MessageType) {
        this._authToken = auth_token;
        this._type = type;
    }

    set = (key: string, value: any) => this._payload.set(key, value);

    toSendable = () => {
        return JSON.stringify({
            auth_token: this._authToken,
            type: this._type,
            payload: Object.fromEntries(this._payload)
        });
    }
}