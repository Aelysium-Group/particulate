export class ConfigParsingError extends Error {
    constructor(message: string = "There was an issue parsing a bot config!") {
        super(message);
        this.name = "ConfigParsingError";
    }
}