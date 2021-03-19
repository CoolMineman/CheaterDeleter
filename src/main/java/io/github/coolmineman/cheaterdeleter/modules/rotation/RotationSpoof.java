package io.github.coolmineman.cheaterdeleter.modules.rotation;

import io.github.coolmineman.cheaterdeleter.events.PlayerRotationListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;

public class RotationSpoof extends CDModule implements PlayerRotationListener {

    public RotationSpoof() {
        super("rotation_spoof");
        PlayerRotationListener.EVENT.register(this);
    }

    @Override
    public void onRotate(CDPlayer player, float yawDelta, float pitchDelta, double move, PlayerMoveC2SPacketView packet) {
        RotationSpoofData data = player.getOrCreateData(RotationSpoofData.class, RotationSpoofData::new);
        if (move > 5 && MathUtil.getDistanceSquared(yawDelta, pitchDelta, data.oldYawDelta * -1, data.oldPitchDelta * -1) < 0.1) {
            flag(player, FlagSeverity.MAJOR, "Rotation Spoof");
        }
        data.oldYawDelta = yawDelta;
        data.oldPitchDelta = pitchDelta;
    }

    public static class RotationSpoofData {
        float oldYawDelta;
        float oldPitchDelta;
    }
    
}
