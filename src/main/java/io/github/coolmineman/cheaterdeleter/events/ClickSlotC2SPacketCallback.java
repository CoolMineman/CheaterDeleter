package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.util.ActionResult;

public interface ClickSlotC2SPacketCallback {
    Event<ClickSlotC2SPacketCallback> EVENT = EventFactory.createArrayBacked(ClickSlotC2SPacketCallback.class,
        listeners -> (player, packet) -> {
            for (ClickSlotC2SPacketCallback listener : listeners) {
                ActionResult result = listener.onClickSlotC2SPacket(player, packet);

                if(result != ActionResult.PASS) {
                    return result;
                }
            }
        return ActionResult.PASS;
    });

    public static void init() {
        PacketCallback.EVENT.register((player, packet) -> 
            packet instanceof ClickSlotC2SPacket ? ClickSlotC2SPacketCallback.EVENT.invoker().onClickSlotC2SPacket(player, (ClickSlotC2SPacket)packet) : ActionResult.PASS
        );
    }

    ActionResult onClickSlotC2SPacket(CDPlayer player, ClickSlotC2SPacket packet);
}
