package io.github.coolmineman.cheaterdeleter.modules;

import java.util.ArrayList;

import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.modules.debug.DebugModuleManager;
import io.github.coolmineman.cheaterdeleter.modules.exploit.ExploitModuleMangaer;
import io.github.coolmineman.cheaterdeleter.modules.movement.MovementModuleManager;

public class ModuleManager {
    private ModuleManager() { }

    private static final ArrayList<CDModule> CHECKS = new ArrayList<>();

    public static void init() {
        registerCheck(new PacketLimiterCheck());
        if (GlobalConfig.debugMode >= 2) {
            DebugModuleManager.init();
        }
        registerCheck(new TimerCheck());
        ExploitModuleMangaer.init();
        MovementModuleManager.init();
    }

    public static void registerCheck(CDModule check) {
        CHECKS.add(check);
    }

    public static int getCheckCount() {
        return CHECKS.size();
    }

}
