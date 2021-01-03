package io.github.coolmineman.cheaterdeleter.checks.config;

import net.fabricmc.loader.api.FabricLoader;

public class GlobalConfig {
    public static boolean debugMode = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static int maxMajorFlagsPerMinute = 5;
}
