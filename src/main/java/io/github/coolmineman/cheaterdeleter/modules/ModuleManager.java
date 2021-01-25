package io.github.coolmineman.cheaterdeleter.modules;

import java.util.ArrayList;
import java.util.List;

import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.modules.debug.DebugModuleManager;
import io.github.coolmineman.cheaterdeleter.modules.exploit.ExploitModuleMangaer;
import io.github.coolmineman.cheaterdeleter.modules.movement.MovementModuleManager;

public class ModuleManager {
    private ModuleManager() { }

    private static final ArrayList<CDModule> MODULES = new ArrayList<>();

    public static void init() {
        registerModule(new PacketLimiterCheck());
        if (GlobalConfig.getDebugMode() >= 2) {
            DebugModuleManager.init();
        }
        registerModule(new TimerCheck());
        ExploitModuleMangaer.init();
        MovementModuleManager.init();
    }

    public static void registerModule(CDModule check) {
        MODULES.add(check);
    }

    public static List<CDModule> getModules() {
        return MODULES;
    }

    public static int getModuleCount() {
        return MODULES.size();
    }

}
