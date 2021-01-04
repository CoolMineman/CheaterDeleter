package io.github.coolmineman.cheaterdeleter.checks;

import io.github.coolmineman.cheaterdeleter.CheaterDeleterInit;
import io.github.coolmineman.cheaterdeleter.checks.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import net.minecraft.text.LiteralText;

public class Check {
    public long getFlagCoolDownMs() {
        return 1000;
    }

    public boolean flag(CDPlayer player, Check.FlagSeverity severity, String message) {
        if (GlobalConfig.debugMode) {
            player.mcPlayer.sendMessage(new LiteralText("Flagged: " + message), true);
        }
        if (severity == FlagSeverity.MAJOR) {
            CheaterDeleterInit.GLOBAL_LOGGER.warn("{} Was Flagged: {}", player, message);
        } else {
            CheaterDeleterInit.GLOBAL_LOGGER.info("{} Was Flagged: {}", player, message);
        }
        return player.flag(this, severity);
    }

    public void assertOrFlag(boolean condition, CDPlayer player, Check.FlagSeverity severity, String message) {
        if (!condition) flag(player, severity, message);
    }

    public enum FlagSeverity {
        MINOR, //Fixable/Likely False Positive
        MAJOR //Admins Should Investigate
    }
}
