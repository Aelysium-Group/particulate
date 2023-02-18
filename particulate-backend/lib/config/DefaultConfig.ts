import { Config } from './Config.js';

export class DefaultConfig extends Config {
    protected _targetPath: string = "configs/config.yml";
    protected _templatePath: string = "assets/config_template.yml";

    public static config: DefaultConfig;

    private _port: number;

    public get port() { return this._port; }

    prepare = () => {
        this._port = this.get("port");

        this._contents = null;
    }
}