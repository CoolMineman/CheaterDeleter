package io.github.coolmineman.cheaterdeleter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerThread implements Runnable {
    public static final Thread INSTANCE = new Thread(new LoggerThread(), "CheaterDeleterLogger");
    private static final Logger LOGGER = LogManager.getLogger("CheaterDeleter");
    private static final Object LOCK = new Object[0];
    private static final Queue<String> INFO_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<String> WARN_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        INSTANCE.setDaemon(true);
    }

    private LoggerThread() { }

    @Override
    public void run() {
        while (true) {
            boolean empty = true;
            if(!INFO_QUEUE.isEmpty()) {
                empty = false;
                LOGGER.info(INFO_QUEUE.poll());
            }
            if(!WARN_QUEUE.isEmpty()) {
                empty = false;
                LOGGER.warn(WARN_QUEUE.poll());
            }
            if(empty) {
                synchronized(LOCK) {
                    try {
                        LOCK.wait();
                    } catch(InterruptedException ignored) {}
                }
            }
        }
    }

    public static void info(String string) {
        boolean empty = INFO_QUEUE.isEmpty();
        INFO_QUEUE.add(string);
        if(empty) {
            synchronized(LOCK) {
                LOCK.notify();
            }
        }
    }

    public static void warn(String string) {
        boolean empty = WARN_QUEUE.isEmpty();
        WARN_QUEUE.add(string);
        if(empty) {
            synchronized(LOCK) {
                LOCK.notify();
            }
        }
    }
}
