import { Payload } from "./Payload";

export class MessageQueue {
    private messages: Payload[] = [];
    private max: number = 50;

    constructor(max?: number) {
        this.max = max;
    }

    public queue = (message: Payload): void => {
        this.messages.push(message);

        this.flush();
    }

    public dump = (): Payload[] => {
        const queue = [...this.messages];
        this.messages = [];
        return queue;
    }

    private flush = () => {
        if(this.messages.length <= this.max) return;

        this.messages = this.messages.slice(Math.floor(this.max * 0.5), this.messages.length);
    }
}