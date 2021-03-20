package io.github.coolmineman.cheaterdeleter.events;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractionType;
import net.minecraft.util.ActionResult;

public interface PlayerAttackListener {
    Event<PlayerAttackListener> EVENT = EventFactory.createArrayBacked(PlayerAttackListener.class,
        listeners -> (player, target, attackPacket) -> {
            for (PlayerAttackListener listener : listeners) {
                listener.onAttack(player, target, attackPacket);
            }
    });

    public static void init() {
        PacketCallback.EVENT.register((player, packet) -> {
            if (packet instanceof PlayerInteractEntityC2SPacket) {
                PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = (PlayerInteractEntityC2SPacket)packet;
                if (playerInteractEntityC2SPacket.getType() == InteractionType.ATTACK) {
                    EVENT.invoker().onAttack(player, CDEntity.of(playerInteractEntityC2SPacket.getEntity(player.getWorld())), playerInteractEntityC2SPacket);
                }
            }
            return ActionResult.PASS;
        });
    }

    void onAttack(CDPlayer player, @Nullable CDEntity target, PlayerInteractEntityC2SPacket attackPacket);
}
