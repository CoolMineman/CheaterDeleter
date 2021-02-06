package io.github.coolmineman.cheaterdeleter.modules.movement.elytra;

import io.github.coolmineman.cheaterdeleter.events.PlayerMovementListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import io.github.coolmineman.cheaterdeleter.util.MathUtil;
import io.github.coolmineman.cheaterdeleter.util.PunishUtil;

public class ElytraMaxHorizontalSpeedCheck extends CDModule implements PlayerMovementListener {
    public ElytraMaxHorizontalSpeedCheck() {
        super("elytra_max_horizontal_check");
        PlayerMovementListener.EVENT.register(this);
    }

    private class ElytraMaxHorizontalSpeedCheckData {
        public double move = 0.0;
        public long starttime = System.currentTimeMillis();
    }

	@Override
	public void onMovement(CDPlayer player, PlayerMoveC2SPacketView packet, MoveCause cause) {
        if (!enabledFor(player) || !player.isFallFlying() || cause.isTeleport() || MathUtil.getDistanceSquared(0, 0, player.getVelocity().getX(), player.getVelocity().getZ()) > 4) {
            ElytraMaxHorizontalSpeedCheckData data = player.getData(ElytraMaxHorizontalSpeedCheckData.class);
            if (data != null) {
                data.move = 0;
            }
            return;
        }
        if (!packet.isChangePosition()) return;
        double distance = MathUtil.getDistanceSquared(player.getPacketX(), player.getPacketZ(), packet.getX(), packet.getZ());
        ElytraMaxHorizontalSpeedCheckData data = player.getOrCreateData(ElytraMaxHorizontalSpeedCheckData.class, ElytraMaxHorizontalSpeedCheckData::new);
        data.move += distance;
        if (System.currentTimeMillis() - data.starttime > 2500) {
            if (data.move >= 150 && flag(player, FlagSeverity.MINOR, "Elytra Move Too High: " + data.move)) {
                player.rollback();
                PunishUtil.groundPlayer(player);
            }
            data.starttime = System.currentTimeMillis();
            data.move = 0.0;
        }
	}
}
