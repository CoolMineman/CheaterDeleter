package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.events.MovementPacketEvent;
import net.minecraft.util.ActionResult;

public class TimerCheck extends Check {
    public TimerCheck() {
        MovementPacketEvent.EVENT.register((player, packet) -> {
            System.out.println(packet.isOnGround());
            return ActionResult.PASS;
        });
    }
}
