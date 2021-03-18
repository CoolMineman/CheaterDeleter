package io.github.coolmineman.cheaterdeleter.modules.debug;

import io.github.coolmineman.cheaterdeleter.LoggerThread;
import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.events.PlayerRotationListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;

public class RotationSpammerModule extends CDModule implements PlayerRotationListener {
    public RotationSpammerModule() {
        super("rotation_spammer_module");
        PlayerRotationListener.EVENT.register(this);
    }

    @Override
    public void onRotate(CDPlayer player, float yawDelta, float pitchDelta, double move, PlayerMoveC2SPacketView packet) {
        if (!enabledFor(player) || GlobalConfig.getDebugMode() < 3 || move < 10) return;
        LoggerThread.info("ΔYaw: " + yawDelta + " ΔPitch: " + pitchDelta);
    }
}
