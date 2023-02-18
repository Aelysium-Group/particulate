type WebsocketPayload = {
    auth_key: string;
    payload: Object;
}
export class Payload {
    private _payload: WebsocketPayload;

    constructor(publicKey: string) {
        this._payload = { auth_key: publicKey, payload: {} }
    }

    public set = (key: string, value: any) => {this._payload.payload[key] = value;}
    public get = (key: string) => this._payload.payload[key];
    public remove = (key: string) => { delete this._payload.payload[key];}
    public toString = () => JSON.stringify(this._payload);
}