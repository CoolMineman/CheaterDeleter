package io.github.coolmineman.cheaterdeleter.modules;

import java.util.ArrayList;
import java.util.List;

import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.modules.block.BlockModuleManager;
import io.github.coolmineman.cheaterdeleter.modules.combat.CombatModuleManager;
import io.github.coolmineman.cheaterdeleter.modules.debug.DebugModuleManager;
import io.github.coolmineman.cheaterdeleter.modules.exploit.ExploitModuleMangaer;
import io.github.coolmineman.cheaterdeleter.modules.movement.MovementModuleManager;
import io.github.coolmineman.cheaterdeleter.modules.packetcount.PacketCountModuleManager;
import io.github.coolmineman.cheaterdeleter.modules.rotation.RotationModuleManager;

public class ModuleManager {
    private ModuleManager() { }

    private static final ArrayList<CDModule> MODULES = new ArrayList<>();

    public static void init() {
        PacketCountModuleManager.init();
        if (GlobalConfig.getDebugMode() >= 2) {
            DebugModuleManager.init();
        }
        BlockModuleManager.init();
        ExploitModuleMangaer.init();
        MovementModuleManager.init();
        RotationModuleManager.init();
        CombatModuleManager.init();
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
