package io.github.coolmineman.cheaterdeleter.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.coolmineman.cheaterdeleter.CheaterDeleterInit;
import io.github.coolmineman.cheaterdeleter.config.BooleanConfigValue;
import io.github.coolmineman.cheaterdeleter.config.ConfigValue;
import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.text.LiteralText;

public class CDModule {
    public final List<ConfigValue> configValues = new ArrayList<>();
    protected BooleanConfigValue enabled = booleanConfig("enabled", true);
    public final String moduleName;

    public CDModule(String moduleName) {
        this.moduleName = moduleName;
    }

    public long getFlagCoolDownMs() {
        return 1000;
    }

    public boolean flag(CDPlayer player, CDModule.FlagSeverity severity, String message) {
        if (GlobalConfig.getDebugMode() >= 1) {
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

    public boolean enabledFor(CDPlayer player) {
        return enabled.get() && !player.shouldBypassAnticheat();
    }

    public boolean assertOrFlag(boolean condition, CDPlayer player, CDModule.FlagSeverity severity, String message) {
        if (!condition) return flag(player, severity, message);
        return false;
    }

    public final BooleanConfigValue booleanConfig(String key, boolean defaultValue) {
        BooleanConfigValue result = new BooleanConfigValue(key, defaultValue);
        configValues.add(result);
        return result;
    }

    public enum FlagSeverity {
        MINOR, //Fixable/Likely False Positive
        MAJOR //Admins Should Investigate
    }
}
