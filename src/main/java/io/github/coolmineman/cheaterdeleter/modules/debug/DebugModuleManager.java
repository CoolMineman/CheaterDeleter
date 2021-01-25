package io.github.coolmineman.cheaterdeleter.modules.debug;

import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;

public class DebugModuleManager {
    private DebugModuleManager() { }

    public static void init() {
        if (GlobalConfig.getDebugMode() >= 3) {
            ModuleManager.registerModule(new PacketClassSpammerModule());
        }
    }
}
