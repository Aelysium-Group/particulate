export class NoPermissionError extends Error {
    constructor(message: string = "You don't have permission to do this!") {
        super(message);
        this.name = "NoPermissionError";
    }
}