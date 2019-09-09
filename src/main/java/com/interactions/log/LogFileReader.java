package com.interactions.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Log File Reader thread.
 * Reads commit log file and distributes log lines to reader threads.
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
public class LogFileReader implements Runnable {
    public static final String POISON_MESSAGE = "End of Log Lines";

    private BlockingQueue<String> blockingQueueA;
    private BlockingQueue<String> blockingQueueB;

    /**
     * Constructs and instance
     * @param blockingQueueA
     * @param blockingQueueB
     */
    LogFileReader(BlockingQueue<String> blockingQueueA, BlockingQueue<String> blockingQueueB) {
        this.blockingQueueA = blockingQueueA;
        this.blockingQueueB = blockingQueueB;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        try {
            String logFile = AppConfig.getInstance().getCommitLogFileName();
            bufferedReader = new BufferedReader(new FileReader(logFile));
            String logLine;
            while( (logLine = bufferedReader.readLine()) != null) {
                if (logLine.startsWith("A")) {
                    blockingQueueA.put(logLine);
                } else if (logLine.startsWith("B")) {
                    blockingQueueB.put(logLine);
                }
            }

            putPoisionMessageIntoQueues();

            bufferedReader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            try {
                putPoisionMessageIntoQueues();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private void putPoisionMessageIntoQueues() throws InterruptedException {
        blockingQueueA.put(POISON_MESSAGE);
        blockingQueueB.put(POISON_MESSAGE);
    }
}
