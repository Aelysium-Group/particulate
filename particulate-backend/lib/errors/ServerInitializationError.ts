export class ServerInitializationError extends Error {
    constructor(message: string = "There was an issue initializing the server!") {
        super(message);
        this.name = "ServerInitializationError";
    }
}