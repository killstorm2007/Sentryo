package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecuter {
    private static final ExecutorService executor = Executors.newFixedThreadPool(3);
    
    public static void execute(Runnable task) {
        executor.submit(task);
    }
    
    public static void shutdown() {
        executor.shutdown();
    }
}