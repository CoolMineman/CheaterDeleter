package io.github.coolmineman.cheaterdeleter.modules;

import io.github.coolmineman.cheaterdeleter.CheaterDeleterInit;
import io.github.coolmineman.cheaterdeleter.modules.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.text.LiteralText;

public class CDModule {
    public long getFlagCoolDownMs() {
        return 1000;
    }

    public boolean flag(CDPlayer player, CDModule.FlagSeverity severity, String message) {
        if (GlobalConfig.debugMode >= 1) {
            player.asMcPlayer().sendMessage(new LiteralText("Flagged: " + message), true);
        }
        if (severity == FlagSeverity.MAJOR) {
            CheaterDeleterInit.GLOBAL_LOGGER.warn("{} Was Major Flagged: {}", player.asString(), message);
            player.asMcPlayer().getServer().getPlayerManager().getPlayerList().forEach(player1 -> {
                CDPlayer player2 = CDPlayer.of(player1);
                if (player2.shouldSendMajorFlags()) player2.asMcPlayer().sendMessage(new LiteralText(player.asMcPlayer().getGameProfile().getName() + " was major flagged: " + message), false);
            });
        } else {
            CheaterDeleterInit.GLOBAL_LOGGER.info("{} Was Minor Flagged: {}", player.asString(), message);
            player.asMcPlayer().getServer().getPlayerManager().getPlayerList().forEach(player1 -> {
                CDPlayer player2 = CDPlayer.of(player1);
                if (player2.shouldSendMinorFlags()) player2.asMcPlayer().sendMessage(new LiteralText(player.asMcPlayer().getGameProfile().getName() + " was minor flagged: " + message), false);
            });
        }
        return player.flag(this, severity);
    }

    public boolean assertOrFlag(boolean condition, CDPlayer player, CDModule.FlagSeverity severity, String message) {
        if (!condition) return flag(player, severity, message);
        return false;
    }

    public enum FlagSeverity {
        MINOR, //Fixable/Likely False Positive
        MAJOR //Admins Should Investigate
    }
}
