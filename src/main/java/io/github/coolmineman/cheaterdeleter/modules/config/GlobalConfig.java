package io.github.coolmineman.cheaterdeleter.modules.config;

import net.fabricmc.loader.api.FabricLoader;

public class GlobalConfig {
    /**
     * 2 - Dev - Don't kick just notify
     * 1 - Testing - Show players their own flags (but still kick)
     * 0 - Production
     */
    public static int debugMode = FabricLoader.getInstance().isDevelopmentEnvironment() ? 2 : 1;
}
