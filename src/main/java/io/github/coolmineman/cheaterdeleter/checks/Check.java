package io.github.coolmineman.cheaterdeleter.checks;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class Check {
    public void flag(ServerPlayerEntity player, String message) {
        player.sendMessage(new LiteralText("Flagged: " + message), true);
    }
}
