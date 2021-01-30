package io.github.coolmineman.cheaterdeleter.events;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public interface InteractItemListener {
    Event<InteractItemListener> EVENT = EventFactory.createArrayBacked(InteractItemListener.class,
        listeners -> (player, hand, stackInHand) -> {
            for (InteractItemListener listener : listeners) {
                listener.onInteractItem(player, hand, stackInHand);
            }
    });

    public static void init() {
        PacketCallback.EVENT.register((player, packet) -> {
                if (packet instanceof PlayerInteractItemC2SPacket) EVENT.invoker().onInteractItem(player,  (PlayerInteractItemC2SPacket)packet);
                return ActionResult.PASS;
            }
        );
    }

    void onInteractItem(CDPlayer player, Hand hand, ItemStack stackInHand);

    default void onInteractItem(CDPlayer player, PlayerInteractItemC2SPacket packet) {
        onInteractItem(player, packet.getHand(), player.asMcPlayer().getStackInHand(packet.getHand()));
    }
}
