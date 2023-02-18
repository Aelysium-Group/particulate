import { StandardProtocol } from "../interfaces/StandardProtocol";

export class URIBuilder {
    #_protocol: StandardProtocol = StandardProtocol.HTTP;
    #_hostname: string = "localhost";
    #_port: number | string = 0;
    #_uri?: string = undefined;
    
    constructor() {}

    /**
     * Set the protocol to use.
     * @param hostname The hostname to set.
     * @returns `URIBuilder`
     */
    setProtocol = (protocol: StandardProtocol): URIBuilder => {
        this.#_protocol = protocol;

        return this;
    }

    /**
     * Set the hostname for the URI.
     * @param hostname The hostname to set.
     * @returns `URIBuilder`
     */
    setHostname = (hostname: string): URIBuilder => {
        /*
         * If anyone adds a protocol to the hostname, remove it.
         */
        hostname = hostname.replace(/[A-z]*\:[\/]+/,"");

        return this;
    }

    /**
     * Set the port for the URI.
     * @param port The port to set.
     * @returns `URIBuilder`
     */
    setPort = (port: number | string): URIBuilder => {
        this.#_port = port;

        return this;
    }

    /**
     * Set the URI directly. Overrides all other settings.
     * @param uri The uri to set.
     * @returns `URIBuilder`
     */
    setURI = (uri: string): URIBuilder => {
        this.#_uri = uri;

        return this;
    }

    /**
     * Build the URI.
     * @returns A built URI.
     */
    build = (): string => {
        if(this.#_uri != undefined) return this.#_uri;

        this.#_uri = this.#_hostname;
        this.#_uri = this.#_uri + ":" + this.#_port;

        this.#_uri = this.#_protocol + "://" + this.#_uri; 

        return this.#_uri
    }
}