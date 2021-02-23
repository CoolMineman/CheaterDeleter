package io.github.coolmineman.cheaterdeleter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerThread extends Thread {
    public static final LoggerThread INSTANCE = new LoggerThread();
    private static final Logger LOGGER = LogManager.getLogger("CheaterDeleter");
    private static final Queue<String> INFO_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<String> WARN_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        INSTANCE.setName("CheaterDeleterLogger");
        INSTANCE.setDaemon(true);
    }

    private LoggerThread() { }

    @Override
    public void run() {
        while (true) {
            String info = INFO_QUEUE.poll();
            if (info != null) LOGGER.info(info);
            String warn = WARN_QUEUE.poll();
            if (warn != null) LOGGER.warn(warn);
        }
    }

    public static void info(String string) {
        INFO_QUEUE.add(string);
    }

    public static void warn(String string) {
        WARN_QUEUE.add(string);
    }
}
