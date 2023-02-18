import chalk from "chalk";
import { DefaultConfig } from "./config/DefaultConfig";
import { WebsocketProvider } from "./websocket-provider/providers/WebsocketProvider";
import { Lang } from "./lang/Lang";
import { ServerInitializationError } from "./errors/ServerInitializationError";

export class ParticulateServer {
    private static _instance?: ParticulateServer;
    public static get instance() {
        if(this._instance == undefined) throw new Error("No bot is defined but it was still called!");
        return this._instance;
    }

    private _websocketProvider: WebsocketProvider | undefined;

    constructor() {}

    public static start = async (): Promise<boolean> => {
        if(!(ParticulateServer._instance == undefined)) {
            console.log("A server is already running! Use .stop() to kill it!");
            return false;
        }
    
        ParticulateServer._instance = new ParticulateServer();
        await ParticulateServer._instance.init();
    
        return true;
    }

    /**
     * Stops the current instance of the bot. Whatever is sitting in Bot.instance.
     * After this is run, you can start a new instance of the bot.
     */
    public static stop = async () => {
        if(ParticulateServer._instance == undefined) return;
        await ParticulateServer.instance._uninit();
    }

    public init = async () => {
        console.log(chalk.yellow("Initializing Server..."));
        this._initConfigs();

        await this._initServer();

        Lang.print(Lang.MAIN_WORDMARK, (row) => {
            console.log(row);
        })
        console.log(chalk.green("Loaded successfully!"));
        console.log(Lang.SPACING);
    }

    private _uninit = async () => {
    };

    private _initConfigs = () => {
        console.log(chalk.cyan("Initializing config.yml"));
        DefaultConfig.config = new DefaultConfig();
        DefaultConfig.config.generate();
        DefaultConfig.config.prepare();
        console.log(chalk.blue("Finished!"));
    }

    private _initServer = async () => {
        try {
            this._websocketProvider = new WebsocketProvider(DefaultConfig.config.port,"particulate-director-websocket");
        
            this._websocketProvider.on("message",(message: string) => {
                console.log(message);
            });
            
            this._websocketProvider.on("close",() => {
                console.log("A connection has closed.");
            });
            
            this._websocketProvider.on("connect",() => {
                console.log("A connection has been made.");
            });
        
            if(await this._websocketProvider.connect()) return true;

            throw new ServerInitializationError("Unable to start the websocket server.");
        } catch(error) {
            console.error(error);
        }
    
        throw new ServerInitializationError("Unable to start the websocket server.");
    }
}