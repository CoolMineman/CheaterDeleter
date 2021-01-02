package io.github.coolmineman.cheaterdeleter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.coolmineman.cheaterdeleter.checks.CheckManager;
import io.github.coolmineman.cheaterdeleter.compat.CompatManager;
import io.github.coolmineman.cheaterdeleter.events.EventManager;
import io.github.coolmineman.cheaterdeleter.trackers.TrackerManager;
import net.fabricmc.api.ModInitializer;

public class CheaterDeleterInit implements ModInitializer {
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
		TrackerManager.init();
		LOGGER.info("Loaded {} trackers in {}ms", TrackerManager.getTrackerCount(), System.currentTimeMillis() - trackerInitStart);
		LOGGER.info("Initializing Checks...");
		long checkInitStart = System.currentTimeMillis();
		CheckManager.init();
		LOGGER.info("Loaded {} checks in {}ms", CheckManager.getCheckCount(), System.currentTimeMillis() - checkInitStart);
		LOGGER.info("Initializing Compatability Manager");
		long compatInitStart = System.currentTimeMillis();
		CompatManager.init();
		LOGGER.info("Loaded Compatability For {} Mods in {}ms", CompatManager.getCompatCount(), System.currentTimeMillis() - compatInitStart);
		LOGGER.info(EQUALS_LINE);
		LOGGER.info("CheaterDeleter Anti-Cheat has Loaded Succesfully :)");
		LOGGER.info(EQUALS_LINE);
	}
}
