package io.github.coolmineman.cheaterdeleter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.coolmineman.cheaterdeleter.compat.CompatManager;
import io.github.coolmineman.cheaterdeleter.config.GlobalConfig;
import io.github.coolmineman.cheaterdeleter.events.EventManager;
import io.github.coolmineman.cheaterdeleter.modules.ModuleManager;
import io.github.coolmineman.cheaterdeleter.trackers.Trackers;
import net.fabricmc.api.ModInitializer;

public class CheaterDeleterInit implements ModInitializer {
	public static final Logger GLOBAL_LOGGER = LogManager.getLogger("CheaterDeleter");
	private static final Logger LOGGER = LogManager.getLogger("CheaterDeleter-Init");
	private static final String EQUALS_LINE = "========================================";

	@Override
	public void onInitialize() {
		LOGGER.info(EQUALS_LINE);
		LOGGER.info("CheaterDeleter Anti-Cheat is Loading");
		LOGGER.info(EQUALS_LINE);
		LOGGER.info("Initializing Events");
		EventManager.init();
		LOGGER.info("Finished Initializing Events");
		LOGGER.info("Initializing Trackers...");
		long trackerInitStart = System.currentTimeMillis();
		Trackers.init();
		LOGGER.info("Loaded {} Trackers in {}ms", Trackers.getTrackerCount(), System.currentTimeMillis() - trackerInitStart);
		LOGGER.info("Initializing Checks...");
		long checkInitStart = System.currentTimeMillis();
		ModuleManager.init();
		LOGGER.info("Loaded {} Modules in {}ms", ModuleManager.getModuleCount(), System.currentTimeMillis() - checkInitStart);
		LOGGER.info("Initializing Compatability Manager");
		long compatInitStart = System.currentTimeMillis();
		CompatManager.init();
		LOGGER.info("Loaded Compatability For {} Mods in {}ms", CompatManager.getCompatCount(), System.currentTimeMillis() - compatInitStart);
		GlobalConfig.loadConfig();
		GlobalConfig.saveConfig();
		LOGGER.info(EQUALS_LINE);
		LOGGER.info("CheaterDeleter Anti-Cheat has Loaded Succesfully :)");
		LOGGER.info(EQUALS_LINE);
	}
}
