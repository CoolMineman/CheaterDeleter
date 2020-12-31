package io.github.coolmineman.cheaterdeleter.events;

public class EventManager {
    private EventManager() { }

    public static void init() {
        MovementPacketCallback.init();
        PlayerEndTickCallback.init();
    }
}
