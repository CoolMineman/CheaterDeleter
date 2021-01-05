package io.github.coolmineman.cheaterdeleter.compat;

import io.github.coolmineman.cheaterdeleter.objects.CDPlayer;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.util.TriState;

public class LuckoPermissionsCompat implements Compat {
    public TriState get(CDPlayer player, String permission) {
        return Permissions.getPermissionValue(player.mcPlayer, permission);
    }
}
