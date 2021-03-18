package io.github.coolmineman.cheaterdeleter.modules.rotation;

import io.github.coolmineman.cheaterdeleter.events.PlayerRotationListener;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.objects.PlayerMoveC2SPacketView;
import io.github.coolmineman.cheaterdeleter.objects.entity.CDPlayer;

//                              ____
//                      __,-~~/~    `---.
//                    _/_,---(      ,    )
//                __ /        <    /   )  \___
// - ------===;;;'====------------------===;;;===----- -  -
//                   \/  ~"~"~"~"~"~\~"~)~"/
//                   (_ (   \  (     >    \)
//                    \_( _ <         >_>'
//                       ~ `-i' ::>|--"
//                           I;|.|.|
//                          <|i::|i|`.
//                         (` ^'"`-' ")
public class CyberNuke extends CDModule implements PlayerRotationListener {

    public CyberNuke() {
        super("cyber_nuke");
        PlayerRotationListener.EVENT.register(this);
    }

    @Override
    public void onRotate(CDPlayer player, float yawDelta, float pitchDelta, double move, PlayerMoveC2SPacketView packet) {
        if (enabledFor(player) && move > 1000 && packet.getYaw() < 360 && packet.getYaw() > 0) {
            player.ban(7 * 24, "Cyber Nuked");
        }
    }
    
}
