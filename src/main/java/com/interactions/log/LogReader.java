package com.interactions.log;

import java.util.concurrent.BlockingQueue;

/**
 * Log Reader.
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
public class LogReader implements Runnable {
    private String categoryName;
    private BlockingQueue<String> blockingQueue;

    /**
     * Constructor, creates and initializes an instance of {@link LogReader}
     * @param blockingQueue
     */
    LogReader(String categoryName, BlockingQueue blockingQueue) {
        this.categoryName = categoryName;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        synchronized (blockingQueue) {
            try {
                while (true) {
                    String logMessage = blockingQueue.take();
                    if (logMessage.equals(LogFileReader.POISON_MESSAGE)) {
                        // notify other readers to finish by putting POISON_PILL in the queue
                        blockingQueue.put(LogFileReader.POISON_MESSAGE);
                        break;
                    }
                    System.out.println(logMessage + " :" + Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
