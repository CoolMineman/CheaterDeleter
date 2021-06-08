package io.github.coolmineman.cheaterdeleter.modules.combat;

import io.github.coolmineman.cheaterdeleter.events.PacketCallback;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;

public class SpaceRockAlways extends CDModule implements PacketCallback {
    public SpaceRockAlways() {
        super("space_rock_always");
        PacketCallback.EVENT.register(this);
    }

    class B {
        double a = 0;
        int state = 0;
        long time = 0;
    }

    @Override
    public ActionResult onPacket(CDPlayer player, Packet<ServerPlayPacketListener> packet) {
        B b = player.getOrCreateData(B.class, B::new);
        if (b.state == 0) {
            if (packet instanceof PlayerMoveC2SPacketView && ((PlayerMoveC2SPacketView)packet).isChangeLook()) {
                b.state = 1;
            }
        } else if (b.state == 1) {
            if (packet instanceof PlayerInteractEntityC2SPacket) {
                b.state = 2;
            } else {
                b.state = 0;
            }
        } else if (b.state == 2) {
            if (packet instanceof HandSwingC2SPacket) {
                b.state = 3;
            } else {
                b.state = 0;
            }
        } else if (b.state == 3) {
            if (packet instanceof PlayerMoveC2SPacket.LookAndOnGround) {
                b.state = 4;
            } else if (packet instanceof PlayerInteractEntityC2SPacket) {
                b.state = 5;
            } else {
                b.state = 0;
            }
        } else if (b.state == 4) {
            if (packet instanceof PlayerInteractEntityC2SPacket) {
                b.state = 5;
            } else {
                b.state = 0;
            }
        } else if (b.state == 5) {
            if (packet instanceof HandSwingC2SPacket) {
                if (System.currentTimeMillis() - b.time > 120000) {
                    b.a = 1;
                    b.time = System.currentTimeMillis();
                } else {
                    ++b.a;
                    if (b.a > 10) {
                        flag(player, FlagSeverity.MAJOR, "Space Rock Always");
                    }
                }
            }
            b.state = 0;
        }
        return null;
    }
}
