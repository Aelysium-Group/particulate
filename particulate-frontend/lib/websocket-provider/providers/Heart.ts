const defaultHeartbeatRate = 30; // The default number of seconds to set the heartbeat
const oneSecond = 1000; // 1 second is equal to 1000ms

export class Heart {
    #_isAlive: boolean = true;
    #_rate: number;
    #_timer?: NodeJS.Timer;
    #_onDeath: Function = () => {};

    constructor(rate: number) {
        this.#_rate = oneSecond * (rate ?? defaultHeartbeatRate);
    }
    
    /**
     * Starts the heart
     * @throws An error if you try to start the heart while it's already beating
     */
    start = (callback: Function): void => {
        this.#_isAlive = true;
        if(this.#_timer != undefined) throw new Error("This heart has already started beating! You must kill it before you can start it again!");

        this.#_timer = setInterval(() => {
            if(this.#_isAlive === false) this.kill();
            callback();
        }, this.#_rate);
    }

    /**
     * Kills the heart immediately
     */
    kill = (): void => {
        clearInterval(this.#_timer);
        this.#_isAlive = false;
        this.#_timer = undefined;
        
        this.#_onDeath();
    }

    /**
     * Sets the heart to die on the next beat
     */
    killLater = (): void => { this.#_isAlive = false; }

    /**
     * Sets the heart to keep beating if it was meant to die on the next beat
     */
    keepBeating = (): void => { this.#_isAlive = true; }

    on = (eventName: string, callback: Function): void => {
        switch(eventName) {
            case "death":
                this.#_onDeath = callback;
                break;
        }
    }
}