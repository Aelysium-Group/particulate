
import { WebsocketSubscriber } from "../websocket-provider/providers/WebsocketSubscriber";

export class Client {
    protected static _instance: Client = new Client();
    public static remapInstance(publicKey: string) { this._instance = new Client(publicKey); }
    public static get instance() { return this._instance; }

    private _publicKey: string;
    private _websocketSubscriber: WebsocketSubscriber;
    
    constructor(publicKey?: string) {
        this._publicKey = publicKey ?? "";
    }

    public get publicKey() { return this._publicKey; }

    public set socket(websocketSubscriber: WebsocketSubscriber) {this._websocketSubscriber = websocketSubscriber;}
    public get socket() { return this._websocketSubscriber; }
}