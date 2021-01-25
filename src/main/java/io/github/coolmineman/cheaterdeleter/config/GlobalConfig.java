package io.github.coolmineman.cheaterdeleter.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.coolmineman.cheaterdeleter.CheaterDeleterInit;
import io.github.coolmineman.cheaterdeleter.modules.CDModule;
import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;
import io.github.coolmineman.nestedtext.api.NestedTextReader;
import io.github.coolmineman.nestedtext.api.NestedTextWriter;
import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;
import net.fabricmc.loader.api.FabricLoader;

public class GlobalConfig {
    private GlobalConfig() { }

    /**
     * 3 - Ultra Dev - Spam logs with debug info 2 - Dev - Don't kick just notify 1
     * - Testing - Show players their own flags (but still kick) 0 - Production
     */
    private static IntConfigValue debugMode = new IntConfigValue(
        "debug_mode",
        FabricLoader.getInstance().isDevelopmentEnvironment() ? 2 : 1,
        "0 - Production\n1 - Notify Players When They Flag\n2 - Don't Kick/Ban\n3 - Spams Log With Debug Info"
    );

    public static int getDebugMode() {
        return debugMode.get();
    }

    private static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("CheaterDeleter.nt");
    }

    public static void loadConfig() {
        Path config = getConfigPath();
        if (Files.isRegularFile(config)) {
            CheaterDeleterInit.GLOBAL_LOGGER.info("Found Config File, Loading...");
            long loadConfigStart = System.currentTimeMillis();
            try {
                try (BufferedReader reader = Files.newBufferedReader(config)) {
                    Map<String, NestedTextNode> root = NestedTextReader.read(reader).asMap();
                    debugMode.read(root);
                    Map<String, NestedTextNode> modules = root.get("modules").asMap();
                    for (CDModule module : ModuleManager.getModules()) {
                        NestedTextNode moduleNode = modules.get(module.moduleName);
                        if (moduleNode != null) {
                            Map<String, NestedTextNode> moduleMap = moduleNode.asMap();
                            for (ConfigValue configValue : module.configValues) {
                                configValue.read(moduleMap);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            CheaterDeleterInit.GLOBAL_LOGGER.info("Loaded Config File in {}ms", System.currentTimeMillis() - loadConfigStart);
        }
    }

    public static void saveConfig() {
        CheaterDeleterInit.GLOBAL_LOGGER.info("Saving Config File...");
        long saveConfigStart = System.currentTimeMillis();
        Path config = getConfigPath();
        Map<String, NestedTextNode> root = new LinkedHashMap<>();
        debugMode.write(root);
        Map<String, NestedTextNode> modules = new LinkedHashMap<>();
        root.put("modules", NestedTextNode.of(modules));
        for (CDModule module : ModuleManager.getModules()) {
            Map<String, NestedTextNode> moduleMap = new LinkedHashMap<>();
            modules.put(module.moduleName, NestedTextNode.of(moduleMap));
            for (ConfigValue configValue : module.configValues) {
                configValue.write(moduleMap);
            }
        }
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(config)) {
                NestedTextWriter.write(NestedTextNode.of(root), writer);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        CheaterDeleterInit.GLOBAL_LOGGER.info("Saved Config File in {}ms", System.currentTimeMillis() - saveConfigStart);
    }
}
