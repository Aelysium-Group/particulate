export class WebsocketLaunchError extends Error {
    constructor(message: string = "There was an error connecting to the websocket!") {
        super(message);
        this.name = "WebsocketLaunchError";
    }
}