
import { WebsocketSubscriber } from "../websocket-provider/providers/WebsocketSubscriber";

export class Client {
    private static _instance = new Client();
    public static get instance() { return this._instance; }

    private _csrfToken: string;
    private _publicKey: string;
    private _websocketSubscriber: WebsocketSubscriber;

    public set socket(websocketSubscriber: WebsocketSubscriber) {this._websocketSubscriber = websocketSubscriber;}
    public get socket() { return this._websocketSubscriber; }

    constructor() {}
}