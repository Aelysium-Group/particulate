import * as fs from 'fs';
import { parse } from 'yaml';

export class Config {
    protected _targetPath: string;
    protected _templatePath: string;
    protected _contents: any;

    protected static get = (node: any, path: string) => {
        const steps: string[] = path.split(".");

        let currentNode: any = node;
        for (const step of steps)
            currentNode = currentNode[step];

        return currentNode;
    }

    protected get = (path: string) => {
        const steps: string[] = path.split(".");

        let currentNode: any = this._contents;
        for (const step of steps)
            currentNode = currentNode[step];

        return currentNode;
    }

    /**
     * Generate the configuration file.
     * If it already exists, load it.
     */
    generate = (): boolean => {
        try {
            if(fs.existsSync(this._targetPath)) {
                const file = fs.readFileSync(this._targetPath, "utf8");
            
                this._contents = parse(file);
            } else {
                const file = fs.readFileSync(this._templatePath, "utf8");
                if(!fs.existsSync("./configs")) fs.mkdirSync("./configs");
                fs.writeFileSync(this._targetPath, file);
                
                this.generate();
            }
            
            return true;
        } catch(e) {
            console.log(e);
            return false;
        }
    }

    /**
     * Prepare all contents to be used in the bot.
     */
    prepare = () => {}
}