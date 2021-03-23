package io.github.coolmineman.cheaterdeleter.modules.combat;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.cheaterdeleter.events.PlayerAttackListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDEntity;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class VerifyAttackAngle extends CDModule implements PlayerAttackListener {

    public VerifyAttackAngle() {
        super("verify_attack_angle");
        PlayerAttackListener.EVENT.register(this);
    }

    @Override
    public void onAttack(CDPlayer player, @Nullable CDEntity target, PlayerInteractEntityC2SPacket attackPacket) {
        if (!enabledFor(player) || target == null) return;
        double x = player.getPacketX();
        double y = player.getPacketY();
        double z = player.getPacketZ();
        float yaw = player.getPacketYaw();
        float pitch = player.getPacketPitch();
        Vec3d packetRot = MathUtil.getRotationVector(MathHelper.wrapDegrees(pitch), MathHelper.wrapDegrees(yaw));
        Vec3d headPos = new Vec3d(x, y + player.asMcPlayer().getStandingEyeHeight(), z); //TODO better eye height
        Vec3d endPos = MathUtil.offsetInDirection(headPos.x, headPos.y, headPos.z, packetRot, 60);
        Box targetBox = target.getBox().expand(0.5); // Network Delay n stuff
        if (!MathUtil.intersects(targetBox, headPos, endPos)) flag(player, FlagSeverity.MAJOR, "Bad Attack Angle");
    }
    
}
