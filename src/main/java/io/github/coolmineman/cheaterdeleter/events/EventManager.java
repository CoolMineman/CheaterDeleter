package io.github.coolmineman.cheaterdeleter.events;

public class EventManager {
    private EventManager() { }

    public static void init() {
        PlayerEndTickCallback.init();
        OutgoingTeleportListener.init();
        ClickSlotC2SPacketCallback.init();
        ClientCommandC2SPacketListener.init();
        InteractItemListener.init();
        PlayerRotationListener.init();
    }
}
