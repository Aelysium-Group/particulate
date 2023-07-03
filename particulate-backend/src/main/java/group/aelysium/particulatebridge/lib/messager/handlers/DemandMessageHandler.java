package group.aelysium.particulatebridge.lib.messager.websocket.handlers;

import group.aelysium.particulatebridge.ParticulateBridge;
import group.aelysium.particulatebridge.central.API;
import group.aelysium.particulatebridge.lib.messager.websocket.WebsocketService;
import group.aelysium.particulatebridge.lib.messager.messages.GenericMessage;

public class DemandMessageHandler implements Runnable {
    private final GenericMessage message;

    public DemandMessageHandler(GenericMessage message) {
        this.message = message;
    }

    @Override
    public void run() {
        API api = ParticulateBridge.getAPI();


        api.getService(WebsocketService.class).publish();
    }
}
