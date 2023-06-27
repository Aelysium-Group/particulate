import { MessageType } from "../message/MessageType";

type WebsocketPayload = {
    auth_key: string;
    client_id: string;
    type: MessageType;
    payload: Object;
}
export class Payload {
    private _payload: WebsocketPayload;

    constructor(publicKey: string, clientId: string, type: MessageType) {
        this._payload = { auth_key: publicKey, type: type, client_id: clientId, payload: {} }
    }

    public set = (key: string, value: any) => {this._payload.payload[key] = value;}
    public get = (key: string) => this._payload.payload[key];
    public remove = (key: string) => { delete this._payload.payload[key];}
    public toString = () => JSON.stringify(this._payload);
}