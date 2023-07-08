import { MessageType } from "../message/MessageType";

type WebsocketPayload = {
    k: string;
    t: MessageType;
    p: Object;
}
export class Payload {
    private _payload: WebsocketPayload;

    constructor(publicKey: string, type: MessageType, p: object) {
        this._payload = { k: publicKey, t: type, p: p }
    }

    public set = (key: string, value: any) => {this._payload.p[key] = value;}
    public get = (key: string) => this._payload.p[key];
    public remove = (key: string) => { delete this._payload.p[key];}
    public toString = () => JSON.stringify(this._payload);
}