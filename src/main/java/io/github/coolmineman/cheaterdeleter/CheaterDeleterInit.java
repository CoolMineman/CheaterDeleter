package io.github.coolmineman.cheaterdeleter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.coolmineman.cheaterdeleter.checks.CheckManager;
import io.github.coolmineman.cheaterdeleter.events.EventManager;
import net.fabricmc.api.ModInitializer;

public class CheaterDeleterInit implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger("CheaterDeleter-Init");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Events");
		EventManager.init();
		LOGGER.info("Initializing Checks...");
		long checkInitStart = System.currentTimeMillis();
		CheckManager.init();
		LOGGER.info("Loaded {} checks in {}ms", CheckManager.getCheckCount(), System.currentTimeMillis() - checkInitStart);
	}
}
