import { EventName } from "../../events/EventName";
import { Client } from "../../session/Client";
import { MessageType } from "../message/MessageType";
import { MessageQueue } from "./MessageQueue";
import { Payload } from "./Payload";

export const MessageDispatcher = () => {
    const messageQueue = new MessageQueue();
    let isConnected = false;

    const dispatchControlMessage = (channelID: string, effectID: number, type: MessageType) => {
        const message: Payload = new Payload(Client.instance.publicKey, type, { cid: channelID, eid: effectID });

        if(!isConnected) {
            messageQueue.queue(message);
            return;
        }

        Client.instance.socket.send(message);
    }
    const dispatchKillMessage = () => {
        const message: Payload = new Payload(Client.instance.publicKey, MessageType.DEMAND_KILL_ALL, {});

        if(!isConnected) {
            messageQueue.queue(message);
            return;
        }

        Client.instance.socket.send(message);
    }

    const dispatchQueue = () => {
        messageQueue.dump().forEach(message => {
            Client.instance.socket.send(message);
        });
    }

    document.addEventListener(EventName.DispatchDemandMessage,(e: any) => dispatchControlMessage(e.detail.channelID, e.detail.effectID, MessageType.DEMAND_PING));
    document.addEventListener(EventName.DispatchDemandToggleOnMessage,(e: any) => dispatchControlMessage(e.detail.channelID, e.detail.effectID, MessageType.DEMAND_TOGGLE_ON));
    document.addEventListener(EventName.DispatchDemandToggleOffMessage,(e: any) => dispatchControlMessage(e.detail.channelID, e.detail.effectID, MessageType.DEMAND_TOGGLE_OFF));
    document.addEventListener(EventName.ContextMenuOption_StopEffects, dispatchKillMessage);
    document.addEventListener(EventName.ShowLoader,() => {
        isConnected = false;
    });
    document.addEventListener(EventName.HideLoader,() => {
        dispatchQueue();
        isConnected = true;
    });
}