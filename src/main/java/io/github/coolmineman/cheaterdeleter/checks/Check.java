package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class Check {
    public void flag(CDPlayer player, String message) {
        player.mcPlayer.sendMessage(new LiteralText("Flagged: " + message), true);
    }
}
